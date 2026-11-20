package com.hiresense.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.hiresense.config.JpaAuditingConfig;
import com.hiresense.user.domain.Role;
import com.hiresense.user.domain.User;
import jakarta.persistence.EntityManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
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
 * User has no setters yet, so tests that need a change use ReflectionTestUtils.
 * A failed statement aborts the PostgreSQL transaction, so in the constraint tests the
 * failing statement is always the last database call.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfig.class)
class UserRepositoryTest {

    private static final String UNIQUE_VIOLATION = "23505";
    private static final String CHECK_VIOLATION = "23514";

    private static final String INSERT_RAW_USER = """
            INSERT INTO users (id, email, password_hash, full_name, role, created_at, updated_at)
            VALUES (?, ?, 'not-a-real-hash', 'Raw User', ?, now(), now())
            """;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Safety guard, runs before every test: the connection must be the test database as the
     * application role, and the test transaction must be active and flagged for rollback.
     * Without an active test transaction every repository call would commit its own data.
     */
    @BeforeEach
    void guardTestEnvironment() {
        assertThat(jdbcTemplate.queryForObject("SELECT current_database()", String.class))
                .isEqualTo("hiresense_test");
        assertThat(jdbcTemplate.queryForObject("SELECT current_user", String.class))
                .isEqualTo("hiresense_app");
        assertThat(TestTransaction.isActive()).isTrue();
        assertThat(TestTransaction.isFlaggedForRollback()).isTrue();
    }

    private static String uniqueEmail() {
        return "test-" + UUID.randomUUID() + "@example.com";
    }

    private static User newUser(String email) {
        return new User(email, "not-a-real-hash", "Test User", Role.CANDIDATE);
    }

    private long versionInDatabase(UUID id) {
        return jdbcTemplate.queryForObject("SELECT version FROM users WHERE id = ?", Long.class, id);
    }

    private Instant createdAtInDatabase(UUID id) {
        return jdbcTemplate.queryForObject("SELECT created_at FROM users WHERE id = ?", Timestamp.class, id)
                .toInstant();
    }

    private Instant updatedAtInDatabase(UUID id) {
        return jdbcTemplate.queryForObject("SELECT updated_at FROM users WHERE id = ?", Timestamp.class, id)
                .toInstant();
    }

    /** Checks the PostgreSQL error under Spring's wrapper: exact SQLState and the constraint name. */
    private static void assertViolation(Throwable thrown, String sqlState, String constraintName) {
        Throwable root = NestedExceptionUtils.getMostSpecificCause(thrown);
        assertThat(root).isInstanceOf(SQLException.class);
        assertThat(((SQLException) root).getSQLState()).isEqualTo(sqlState);
        assertThat(root).hasMessageContaining(constraintName);
    }

    @Test
    void savedUserCanBeReadBack() {
        String email = uniqueEmail();
        UUID id = userRepository.saveAndFlush(newUser(email)).getId();
        entityManager.clear();

        User found = userRepository.findById(id).orElseThrow();

        assertThat(found.getEmail()).isEqualTo(email);
        assertThat(found.getPasswordHash()).isEqualTo("not-a-real-hash");
        assertThat(found.getFullName()).isEqualTo("Test User");
        assertThat(found.getRole()).isEqualTo(Role.CANDIDATE);
        assertThat(found.isEnabled()).isTrue();
    }

    @Test
    void emailIsTrimmedAndLowercasedAndCanBeFoundByEmail() {
        String lower = uniqueEmail();
        userRepository.saveAndFlush(newUser("  " + lower.toUpperCase(Locale.ROOT) + " "));
        entityManager.clear();

        assertThat(userRepository.findByEmail(lower).orElseThrow().getEmail()).isEqualTo(lower);
        assertThat(userRepository.existsByEmail(lower)).isTrue();
        assertThat(userRepository.existsByEmail(uniqueEmail())).isFalse();
    }

    @Test
    void auditFieldsAndVersionAreFilledOnInsert() {
        Instant before = Instant.now();
        User saved = userRepository.saveAndFlush(newUser(uniqueEmail()));
        Instant after = Instant.now();

        assertThat(saved.getCreatedAt()).isBetween(before.minusSeconds(5), after.plusSeconds(5));
        assertThat(saved.getUpdatedAt()).isBetween(before.minusSeconds(5), after.plusSeconds(5));
        assertThat(saved.getUpdatedAt()).isAfterOrEqualTo(saved.getCreatedAt());
        assertThat(saved.getVersion()).isZero();
        assertThat(versionInDatabase(saved.getId())).isZero();
    }

    @Test
    void duplicateEmailIsRejectedByUniqueConstraint() {
        String email = uniqueEmail();
        userRepository.saveAndFlush(newUser(email));

        Throwable thrown = catchThrowable(() -> userRepository.saveAndFlush(newUser(email)));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, UNIQUE_VIOLATION, "uq_users_email");
    }

    @Test
    void databaseRejectsInvalidRoleThroughCheckConstraint() {
        // Control: the same statement with a valid role works, so the failure below is caused by the role only.
        jdbcTemplate.update(INSERT_RAW_USER, UUID.randomUUID(), uniqueEmail(), "ADMIN");

        Throwable thrown = catchThrowable(
                () -> jdbcTemplate.update(INSERT_RAW_USER, UUID.randomUUID(), uniqueEmail(), "HACKER"));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, CHECK_VIOLATION, "ck_users_role");
    }

    @Test
    void databaseRejectsUppercaseEmailThroughCheckConstraint() {
        // Control: the same statement with a lowercase email works, so the failure below is caused by the email only.
        jdbcTemplate.update(INSERT_RAW_USER, UUID.randomUUID(), uniqueEmail(), "CANDIDATE");

        String uppercaseEmail = "Upper-" + UUID.randomUUID() + "@Example.com";
        Throwable thrown = catchThrowable(
                () -> jdbcTemplate.update(INSERT_RAW_USER, UUID.randomUUID(), uppercaseEmail, "CANDIDATE"));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, CHECK_VIOLATION, "ck_users_email_lowercase");
    }

    @Test
    void staleUpdateIsRejectedByOptimisticLocking() {
        UUID id = userRepository.saveAndFlush(newUser(uniqueEmail())).getId();
        entityManager.clear();

        User stale = userRepository.findById(id).orElseThrow();   // version 0
        entityManager.detach(stale);
        User current = userRepository.findById(id).orElseThrow(); // another copy, version 0

        ReflectionTestUtils.setField(current, "fullName", "Changed by the first editor");
        userRepository.saveAndFlush(current);                      // database row is now version 1

        ReflectionTestUtils.setField(stale, "fullName", "Changed by the second editor");
        assertThatThrownBy(() -> userRepository.saveAndFlush(stale))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

    @Test
    void concurrentChangeInDatabaseIsDetectedAtFlush() {
        UUID id = userRepository.saveAndFlush(newUser(uniqueEmail())).getId();
        entityManager.clear();
        User user = userRepository.findById(id).orElseThrow();     // managed copy, version 0

        // Simulates another transaction that changed the row behind Hibernate's back.
        jdbcTemplate.update("UPDATE users SET full_name = 'Changed elsewhere', version = version + 1 WHERE id = ?", id);

        ReflectionTestUtils.setField(user, "fullName", "Changed here");
        // Hibernate now sends UPDATE ... WHERE id = ? AND version = 0, which matches no row.
        assertThatThrownBy(() -> userRepository.flush())
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

    @Test
    void versionIncrementsOnRealUpdate() {
        UUID id = userRepository.saveAndFlush(newUser(uniqueEmail())).getId();
        entityManager.clear();
        User user = userRepository.findById(id).orElseThrow();
        assertThat(versionInDatabase(id)).isZero();

        ReflectionTestUtils.setField(user, "fullName", "A New Name");
        userRepository.flush();

        assertThat(versionInDatabase(id)).isEqualTo(1L);
    }

    @Test
    void versionAndUpdatedAtDoNotChangeWhenNothingChanged() {
        UUID id = userRepository.saveAndFlush(newUser(uniqueEmail())).getId();
        entityManager.clear();
        Instant updatedBefore = updatedAtInDatabase(id);
        userRepository.findById(id).orElseThrow();

        userRepository.flush();

        assertThat(versionInDatabase(id)).isZero();
        assertThat(updatedAtInDatabase(id)).isEqualTo(updatedBefore);
    }

    @Test
    void updatedAtMovesForwardAndCreatedAtStaysOnUpdate() {
        UUID id = userRepository.saveAndFlush(newUser(uniqueEmail())).getId();
        entityManager.clear();
        // Plant an old updated_at, so "later than before" cannot depend on clock resolution or sleeping.
        jdbcTemplate.update("UPDATE users SET updated_at = now() - interval '1 day' WHERE id = ?", id);
        Instant createdBefore = createdAtInDatabase(id);
        Instant updatedBefore = updatedAtInDatabase(id);
        assertThat(updatedBefore).isBefore(Instant.now().minus(Duration.ofHours(1)));

        User user = userRepository.findById(id).orElseThrow();
        ReflectionTestUtils.setField(user, "fullName", "A New Name");
        userRepository.flush();

        Instant updatedAfter = updatedAtInDatabase(id);
        assertThat(updatedAfter).isAfter(updatedBefore);
        assertThat(updatedAfter).isBetween(
                Instant.now().minus(Duration.ofMinutes(1)), Instant.now().plus(Duration.ofMinutes(1)));
        assertThat(createdAtInDatabase(id)).isEqualTo(createdBefore);
    }
}
