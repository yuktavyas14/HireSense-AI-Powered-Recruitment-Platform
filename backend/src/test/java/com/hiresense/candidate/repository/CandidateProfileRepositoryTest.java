package com.hiresense.candidate.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.hiresense.candidate.domain.CandidateProfile;
import com.hiresense.config.JpaAuditingConfig;
import com.hiresense.user.domain.Role;
import com.hiresense.user.domain.User;
import com.hiresense.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Repository tests against the real PostgreSQL test database (hiresense_test), no H2.
 * Each test runs in a transaction that is rolled back, so no rows stay behind.
 * The profile needs a real user row (foreign key), so tests save a User first.
 * A failed statement aborts the PostgreSQL transaction, so in the constraint tests the
 * failing statement is always the last database call.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfig.class)
class CandidateProfileRepositoryTest {

    private static final String UNIQUE_VIOLATION = "23505";
    private static final String FOREIGN_KEY_VIOLATION = "23503";
    private static final String VALUE_TOO_LONG = "22001";

    @Autowired
    private CandidateProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /** Safety guard, runs before every test: test database, application role, rollback transaction. */
    @BeforeEach
    void guardTestEnvironment() {
        assertThat(jdbcTemplate.queryForObject("SELECT current_database()", String.class))
                .isEqualTo("hiresense_test");
        assertThat(jdbcTemplate.queryForObject("SELECT current_user", String.class))
                .isEqualTo("hiresense_app");
        assertThat(TestTransaction.isActive()).isTrue();
        assertThat(TestTransaction.isFlaggedForRollback()).isTrue();
    }

    private UUID savedUserId() {
        User user = new User("test-" + UUID.randomUUID() + "@example.com",
                "not-a-real-hash", "Test User", Role.CANDIDATE);
        return userRepository.saveAndFlush(user).getId();
    }

    private long versionInDatabase(UUID id) {
        return jdbcTemplate.queryForObject(
                "SELECT version FROM candidate_profiles WHERE id = ?", Long.class, id);
    }

    /** Checks the PostgreSQL error under Spring's wrapper: exact SQLState and the constraint name. */
    private static void assertViolation(Throwable thrown, String sqlState, String constraintName) {
        Throwable root = NestedExceptionUtils.getMostSpecificCause(thrown);
        assertThat(root).isInstanceOf(SQLException.class);
        assertThat(((SQLException) root).getSQLState()).isEqualTo(sqlState);
        assertThat(root).hasMessageContaining(constraintName);
    }

    @Test
    void emptyProfileCanBeSavedAndFoundByUserId() {
        UUID userId = savedUserId();
        UUID profileId = profileRepository.saveAndFlush(new CandidateProfile(userId)).getId();
        entityManager.clear();

        CandidateProfile found = profileRepository.findByUserId(userId).orElseThrow();

        assertThat(found.getId()).isEqualTo(profileId);
        assertThat(found.getUserId()).isEqualTo(userId);
        assertThat(found.getPhone()).isNull();
        assertThat(found.getLocation()).isNull();
        assertThat(found.getHeadline()).isNull();
        assertThat(found.getSummary()).isNull();
        assertThat(found.getExperienceSummary()).isNull();
        assertThat(found.getEducationSummary()).isNull();
    }

    @Test
    void findByUserIdReturnsEmptyWhenUserHasNoProfile() {
        UUID userWithoutProfile = savedUserId();

        assertThat(profileRepository.findByUserId(userWithoutProfile)).isEmpty();
    }

    @Test
    void optionalDetailsSurviveARoundTrip() {
        UUID userId = savedUserId();
        CandidateProfile profile = new CandidateProfile(userId);
        String longText = "x".repeat(5000);   // far beyond any VARCHAR limit: proves the columns are TEXT
        ReflectionTestUtils.setField(profile, "phone", "+91 98765 43210");
        ReflectionTestUtils.setField(profile, "location", "Pune, India");
        ReflectionTestUtils.setField(profile, "headline", "Java developer");
        ReflectionTestUtils.setField(profile, "summary", longText);
        ReflectionTestUtils.setField(profile, "experienceSummary", longText);
        ReflectionTestUtils.setField(profile, "educationSummary", longText);
        profileRepository.saveAndFlush(profile);
        entityManager.clear();

        CandidateProfile found = profileRepository.findByUserId(userId).orElseThrow();

        assertThat(found.getPhone()).isEqualTo("+91 98765 43210");
        assertThat(found.getLocation()).isEqualTo("Pune, India");
        assertThat(found.getHeadline()).isEqualTo("Java developer");
        assertThat(found.getSummary()).isEqualTo(longText);
        assertThat(found.getExperienceSummary()).isEqualTo(longText);
        assertThat(found.getEducationSummary()).isEqualTo(longText);
    }

    @Test
    void auditFieldsAndVersionAreFilledOnInsert() {
        UUID userId = savedUserId();
        Instant before = Instant.now();
        CandidateProfile saved = profileRepository.saveAndFlush(new CandidateProfile(userId));
        Instant after = Instant.now();

        assertThat(saved.getCreatedAt()).isBetween(before.minusSeconds(5), after.plusSeconds(5));
        assertThat(saved.getUpdatedAt()).isBetween(before.minusSeconds(5), after.plusSeconds(5));
        assertThat(saved.getVersion()).isZero();
        assertThat(versionInDatabase(saved.getId())).isZero();
    }

    @Test
    void secondProfileForTheSameUserIsRejectedByUniqueConstraint() {
        UUID userId = savedUserId();
        profileRepository.saveAndFlush(new CandidateProfile(userId));   // control: the first one works

        Throwable thrown = catchThrowable(
                () -> profileRepository.saveAndFlush(new CandidateProfile(userId)));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, UNIQUE_VIOLATION, "uq_candidate_profiles_user");
    }

    @Test
    void profileForANonexistentUserIsRejectedByForeignKey() {
        profileRepository.saveAndFlush(new CandidateProfile(savedUserId()));   // control: a real user works

        Throwable thrown = catchThrowable(
                () -> profileRepository.saveAndFlush(new CandidateProfile(UUID.randomUUID())));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, FOREIGN_KEY_VIOLATION, "fk_candidate_profiles_user");
    }

    @Test
    void userWithAProfileCannotBeDeleted() {
        UUID userId = savedUserId();
        profileRepository.saveAndFlush(new CandidateProfile(userId));
        UUID otherUserId = savedUserId();
        // Control: a user without a profile can be deleted, so the failure below is caused by the profile only.
        assertThat(jdbcTemplate.update("DELETE FROM users WHERE id = ?", otherUserId)).isEqualTo(1);

        Throwable thrown = catchThrowable(
                () -> jdbcTemplate.update("DELETE FROM users WHERE id = ?", userId));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, FOREIGN_KEY_VIOLATION, "fk_candidate_profiles_user");
    }

    @Test
    void tooLongHeadlineIsRejectedByColumnLength() {
        UUID okUserId = savedUserId();
        CandidateProfile ok = new CandidateProfile(okUserId);
        ReflectionTestUtils.setField(ok, "headline", "h".repeat(200));   // control: exactly the limit works
        profileRepository.saveAndFlush(ok);

        CandidateProfile tooLong = new CandidateProfile(savedUserId());
        ReflectionTestUtils.setField(tooLong, "headline", "h".repeat(201));
        Throwable thrown = catchThrowable(() -> profileRepository.saveAndFlush(tooLong));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        Throwable root = NestedExceptionUtils.getMostSpecificCause(thrown);
        assertThat(root).isInstanceOf(SQLException.class);
        assertThat(((SQLException) root).getSQLState()).isEqualTo(VALUE_TOO_LONG);
    }

    @Test
    void staleUpdateIsRejectedByOptimisticLocking() {
        UUID id = profileRepository.saveAndFlush(new CandidateProfile(savedUserId())).getId();
        entityManager.clear();

        CandidateProfile stale = profileRepository.findById(id).orElseThrow();    // version 0
        entityManager.detach(stale);
        CandidateProfile current = profileRepository.findById(id).orElseThrow();  // another copy, version 0

        ReflectionTestUtils.setField(current, "headline", "Changed by the first editor");
        profileRepository.saveAndFlush(current);                                   // database row is now version 1

        ReflectionTestUtils.setField(stale, "headline", "Changed by the second editor");
        assertThatThrownBy(() -> profileRepository.saveAndFlush(stale))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

    @Test
    void versionIncrementsOnRealUpdate() {
        UUID id = profileRepository.saveAndFlush(new CandidateProfile(savedUserId())).getId();
        entityManager.clear();
        CandidateProfile profile = profileRepository.findById(id).orElseThrow();
        assertThat(versionInDatabase(id)).isZero();

        ReflectionTestUtils.setField(profile, "headline", "A new headline");
        profileRepository.flush();

        assertThat(versionInDatabase(id)).isEqualTo(1L);
    }
}
