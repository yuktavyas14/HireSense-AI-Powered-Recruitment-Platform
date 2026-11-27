package com.hiresense.recruiter.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.hiresense.config.JpaAuditingConfig;
import com.hiresense.recruiter.domain.RecruiterProfile;
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

/**
 * Repository tests against the real PostgreSQL test database (hiresense_test), no H2.
 * Each test runs in a transaction that is rolled back, so no rows stay behind.
 * There is no Company entity yet (owner decision), so company rows are inserted with SQL.
 * A failed statement aborts the PostgreSQL transaction, so in the constraint tests the
 * failing statement is always the last database call.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfig.class)
class RecruiterProfileRepositoryTest {

    private static final String UNIQUE_VIOLATION = "23505";
    private static final String FOREIGN_KEY_VIOLATION = "23503";

    @Autowired
    private RecruiterProfileRepository profileRepository;

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
                "not-a-real-hash", "Test Recruiter", Role.RECRUITER);
        return userRepository.saveAndFlush(user).getId();
    }

    private UUID insertCompany() {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update(
                "INSERT INTO companies (id, name, created_at, updated_at) VALUES (?, 'Test Company', now(), now())",
                id);
        return id;
    }

    private long versionInDatabase(UUID id) {
        return jdbcTemplate.queryForObject(
                "SELECT version FROM recruiter_profiles WHERE id = ?", Long.class, id);
    }

    /** Checks the PostgreSQL error under Spring's wrapper: exact SQLState and the constraint name. */
    private static void assertViolation(Throwable thrown, String sqlState, String constraintName) {
        Throwable root = NestedExceptionUtils.getMostSpecificCause(thrown);
        assertThat(root).isInstanceOf(SQLException.class);
        assertThat(((SQLException) root).getSQLState()).isEqualTo(sqlState);
        assertThat(root).hasMessageContaining(constraintName);
    }

    @Test
    void profileWithoutCompanyCanBeSavedAndFoundByUserId() {
        UUID userId = savedUserId();
        UUID profileId = profileRepository.saveAndFlush(new RecruiterProfile(userId)).getId();
        entityManager.clear();

        RecruiterProfile found = profileRepository.findByUserId(userId).orElseThrow();

        assertThat(found.getId()).isEqualTo(profileId);
        assertThat(found.getUserId()).isEqualTo(userId);
        assertThat(found.getCompanyId()).isNull();
    }

    @Test
    void findByUserIdReturnsEmptyWhenUserHasNoProfile() {
        assertThat(profileRepository.findByUserId(savedUserId())).isEmpty();
    }

    @Test
    void assignedCompanyIsSavedAndReadBack() {
        UUID userId = savedUserId();
        UUID companyId = insertCompany();
        RecruiterProfile profile = new RecruiterProfile(userId);
        profile.assignCompany(companyId);
        profileRepository.saveAndFlush(profile);
        entityManager.clear();

        assertThat(profileRepository.findByUserId(userId).orElseThrow().getCompanyId())
                .isEqualTo(companyId);
    }

    @Test
    void companyCanBeAssignedLaterToAnExistingProfile() {
        UUID userId = savedUserId();
        UUID id = profileRepository.saveAndFlush(new RecruiterProfile(userId)).getId();
        entityManager.clear();
        UUID companyId = insertCompany();

        RecruiterProfile profile = profileRepository.findById(id).orElseThrow();
        profile.assignCompany(companyId);
        profileRepository.flush();
        entityManager.clear();

        assertThat(profileRepository.findById(id).orElseThrow().getCompanyId()).isEqualTo(companyId);
        assertThat(versionInDatabase(id)).isEqualTo(1L);
    }

    @Test
    void entityRejectsNullUserIdAndNullCompanyId() {
        assertThatNullPointerException().isThrownBy(() -> new RecruiterProfile(null));
        RecruiterProfile profile = new RecruiterProfile(UUID.randomUUID());
        assertThatNullPointerException().isThrownBy(() -> profile.assignCompany(null));
    }

    @Test
    void auditFieldsAndVersionAreFilledOnInsert() {
        UUID userId = savedUserId();
        Instant before = Instant.now();
        RecruiterProfile saved = profileRepository.saveAndFlush(new RecruiterProfile(userId));
        Instant after = Instant.now();

        assertThat(saved.getCreatedAt()).isBetween(before.minusSeconds(5), after.plusSeconds(5));
        assertThat(saved.getUpdatedAt()).isBetween(before.minusSeconds(5), after.plusSeconds(5));
        assertThat(saved.getVersion()).isZero();
        assertThat(versionInDatabase(saved.getId())).isZero();
    }

    @Test
    void twoRecruitersCanShareOneCompanyBecauseTheSchemaAllowsIt() {
        UUID companyId = insertCompany();
        RecruiterProfile first = new RecruiterProfile(savedUserId());
        first.assignCompany(companyId);
        RecruiterProfile second = new RecruiterProfile(savedUserId());
        second.assignCompany(companyId);

        profileRepository.saveAndFlush(first);
        profileRepository.saveAndFlush(second);   // no exception: "one recruiter per company" is a service rule

        assertThat(jdbcTemplate.queryForObject(
                "SELECT count(*) FROM recruiter_profiles WHERE company_id = ?", Long.class, companyId))
                .isEqualTo(2L);
    }

    @Test
    void secondProfileForTheSameUserIsRejectedByUniqueConstraint() {
        UUID userId = savedUserId();
        profileRepository.saveAndFlush(new RecruiterProfile(userId));   // control: the first one works

        Throwable thrown = catchThrowable(
                () -> profileRepository.saveAndFlush(new RecruiterProfile(userId)));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, UNIQUE_VIOLATION, "uq_recruiter_profiles_user");
    }

    @Test
    void profileForANonexistentUserIsRejectedByForeignKey() {
        profileRepository.saveAndFlush(new RecruiterProfile(savedUserId()));   // control: a real user works

        Throwable thrown = catchThrowable(
                () -> profileRepository.saveAndFlush(new RecruiterProfile(UUID.randomUUID())));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, FOREIGN_KEY_VIOLATION, "fk_recruiter_profiles_user");
    }

    @Test
    void profileForANonexistentCompanyIsRejectedByForeignKey() {
        RecruiterProfile ok = new RecruiterProfile(savedUserId());
        ok.assignCompany(insertCompany());                       // control: a real company works
        profileRepository.saveAndFlush(ok);

        RecruiterProfile bad = new RecruiterProfile(savedUserId());
        bad.assignCompany(UUID.randomUUID());
        Throwable thrown = catchThrowable(() -> profileRepository.saveAndFlush(bad));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, FOREIGN_KEY_VIOLATION, "fk_recruiter_profiles_company");
    }

    @Test
    void userWithAProfileCannotBeDeleted() {
        UUID userId = savedUserId();
        profileRepository.saveAndFlush(new RecruiterProfile(userId));
        UUID otherUserId = savedUserId();
        // Control: a user without a profile can be deleted, so the failure below is caused by the profile only.
        assertThat(jdbcTemplate.update("DELETE FROM users WHERE id = ?", otherUserId)).isEqualTo(1);

        Throwable thrown = catchThrowable(
                () -> jdbcTemplate.update("DELETE FROM users WHERE id = ?", userId));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, FOREIGN_KEY_VIOLATION, "fk_recruiter_profiles_user");
    }

    @Test
    void companyWithARecruiterCannotBeDeleted() {
        UUID companyId = insertCompany();
        RecruiterProfile profile = new RecruiterProfile(savedUserId());
        profile.assignCompany(companyId);
        profileRepository.saveAndFlush(profile);
        UUID otherCompanyId = insertCompany();
        // Control: a company without a recruiter can be deleted.
        assertThat(jdbcTemplate.update("DELETE FROM companies WHERE id = ?", otherCompanyId)).isEqualTo(1);

        Throwable thrown = catchThrowable(
                () -> jdbcTemplate.update("DELETE FROM companies WHERE id = ?", companyId));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, FOREIGN_KEY_VIOLATION, "fk_recruiter_profiles_company");
    }

    @Test
    void staleUpdateIsRejectedByOptimisticLocking() {
        UUID id = profileRepository.saveAndFlush(new RecruiterProfile(savedUserId())).getId();
        UUID companyA = insertCompany();
        UUID companyB = insertCompany();
        entityManager.clear();

        RecruiterProfile stale = profileRepository.findById(id).orElseThrow();    // version 0
        entityManager.detach(stale);
        RecruiterProfile current = profileRepository.findById(id).orElseThrow();  // another copy, version 0

        current.assignCompany(companyA);
        profileRepository.saveAndFlush(current);                                   // database row is now version 1

        stale.assignCompany(companyB);
        assertThatThrownBy(() -> profileRepository.saveAndFlush(stale))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}
