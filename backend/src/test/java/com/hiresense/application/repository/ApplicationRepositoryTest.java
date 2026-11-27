package com.hiresense.application.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.hiresense.application.domain.Application;
import com.hiresense.application.domain.ApplicationStatus;
import com.hiresense.candidate.domain.CandidateProfile;
import com.hiresense.candidate.repository.CandidateProfileRepository;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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
 * Users and candidate profiles are saved through their repositories; jobs, companies and resumes
 * have no repository or entity yet, so they are inserted with SQL.
 * A failed statement aborts the PostgreSQL transaction, so in the constraint tests the
 * failing statement is always the last database call.
 * Violation tests use a different job for the failing application than for the control one,
 * otherwise the unique constraint would fail first and hide the constraint under test.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfig.class)
class ApplicationRepositoryTest {

    private static final String UNIQUE_VIOLATION = "23505";
    private static final String CHECK_VIOLATION = "23514";
    private static final String FOREIGN_KEY_VIOLATION = "23503";

    private static final String INSERT_RAW_APPLICATION = """
            INSERT INTO applications (id, job_id, candidate_id, resume_id, status, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, now(), now())
            """;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateProfileRepository candidateProfileRepository;

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

    private UUID savedCandidateId() {
        User user = new User("test-" + UUID.randomUUID() + "@example.com",
                "not-a-real-hash", "Test Candidate", Role.CANDIDATE);
        UUID userId = userRepository.saveAndFlush(user).getId();
        return candidateProfileRepository.saveAndFlush(new CandidateProfile(userId)).getId();
    }

    /** Inserts a company and an open job of that company; returns the job id. */
    private UUID insertJob() {
        UUID companyId = UUID.randomUUID();
        jdbcTemplate.update(
                "INSERT INTO companies (id, name, created_at, updated_at) VALUES (?, 'Test Company', now(), now())",
                companyId);
        UUID jobId = UUID.randomUUID();
        jdbcTemplate.update("""
                INSERT INTO jobs (id, company_id, title, description, employment_type, created_at, updated_at)
                VALUES (?, ?, 'Test Job', 'Test description', 'FULL_TIME', now(), now())
                """, jobId, companyId);
        return jobId;
    }

    private UUID insertResume(UUID candidateId) {
        UUID resumeId = UUID.randomUUID();
        jdbcTemplate.update("""
                INSERT INTO resumes (id, candidate_id, original_filename, size_bytes, storage_key, created_at)
                VALUES (?, ?, 'cv.pdf', 1000, ?, now())
                """, resumeId, candidateId, "test-" + UUID.randomUUID());
        return resumeId;
    }

    private long versionInDatabase(UUID id) {
        return jdbcTemplate.queryForObject(
                "SELECT version FROM applications WHERE id = ?", Long.class, id);
    }

    private String statusInDatabase(UUID id) {
        return jdbcTemplate.queryForObject(
                "SELECT status FROM applications WHERE id = ?", String.class, id);
    }

    /** Checks the PostgreSQL error under Spring's wrapper: exact SQLState and the constraint name. */
    private static void assertViolation(Throwable thrown, String sqlState, String constraintName) {
        Throwable root = NestedExceptionUtils.getMostSpecificCause(thrown);
        assertThat(root).isInstanceOf(SQLException.class);
        assertThat(((SQLException) root).getSQLState()).isEqualTo(sqlState);
        assertThat(root).hasMessageContaining(constraintName);
    }

    @Test
    void savedApplicationCanBeReadBackAndStartsAsApplied() {
        UUID candidateId = savedCandidateId();
        UUID jobId = insertJob();
        UUID resumeId = insertResume(candidateId);
        UUID id = applicationRepository.saveAndFlush(new Application(jobId, candidateId, resumeId)).getId();
        entityManager.clear();

        Application found = applicationRepository.findById(id).orElseThrow();

        assertThat(found.getJobId()).isEqualTo(jobId);
        assertThat(found.getCandidateId()).isEqualTo(candidateId);
        assertThat(found.getResumeId()).isEqualTo(resumeId);
        assertThat(found.getStatus()).isEqualTo(ApplicationStatus.APPLIED);
        assertThat(statusInDatabase(id)).isEqualTo("APPLIED");
    }

    @Test
    void auditFieldsAndVersionAreFilledOnInsert() {
        UUID candidateId = savedCandidateId();
        UUID jobId = insertJob();
        UUID resumeId = insertResume(candidateId);
        Instant before = Instant.now();
        Application saved = applicationRepository.saveAndFlush(new Application(jobId, candidateId, resumeId));
        Instant after = Instant.now();

        assertThat(saved.getCreatedAt()).isBetween(before.minusSeconds(5), after.plusSeconds(5));
        assertThat(saved.getUpdatedAt()).isBetween(before.minusSeconds(5), after.plusSeconds(5));
        assertThat(saved.getVersion()).isZero();
        assertThat(versionInDatabase(saved.getId())).isZero();
    }

    @ParameterizedTest
    @EnumSource(ApplicationStatus.class)
    void everyStatusCanBeStoredAndReadBack(ApplicationStatus status) {
        UUID candidateId = savedCandidateId();
        Application application = new Application(insertJob(), candidateId, insertResume(candidateId));
        ReflectionTestUtils.setField(application, "status", status);   // no setter by design (Task 1.10)
        UUID id = applicationRepository.saveAndFlush(application).getId();
        entityManager.clear();

        assertThat(applicationRepository.findById(id).orElseThrow().getStatus()).isEqualTo(status);
        assertThat(statusInDatabase(id)).isEqualTo(status.name());
    }

    @Test
    void databaseRejectsAnUnknownStatusThroughCheckConstraint() {
        UUID candidateId = savedCandidateId();
        UUID resumeId = insertResume(candidateId);
        // Control: the same statement with a valid status works, so the failure below is caused by the status only.
        jdbcTemplate.update(INSERT_RAW_APPLICATION, UUID.randomUUID(), insertJob(), candidateId, resumeId, "APPLIED");

        UUID otherJobId = insertJob();
        Throwable thrown = catchThrowable(() -> jdbcTemplate.update(
                INSERT_RAW_APPLICATION, UUID.randomUUID(), otherJobId, candidateId, resumeId, "HIRED"));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, CHECK_VIOLATION, "ck_applications_status");
    }

    @Test
    void secondApplicationOfTheSameCandidateToTheSameJobIsRejected() {
        UUID candidateId = savedCandidateId();
        UUID jobId = insertJob();
        UUID resumeId = insertResume(candidateId);
        applicationRepository.saveAndFlush(new Application(jobId, candidateId, resumeId));   // control

        Throwable thrown = catchThrowable(
                () -> applicationRepository.saveAndFlush(new Application(jobId, candidateId, resumeId)));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, UNIQUE_VIOLATION, "uq_applications_job_candidate");
    }

    @Test
    void sameCandidateMayApplyToAnotherJobAndAnotherCandidateToTheSameJob() {
        UUID candidateA = savedCandidateId();
        UUID candidateB = savedCandidateId();
        UUID resumeA = insertResume(candidateA);
        UUID resumeB = insertResume(candidateB);
        UUID job1 = insertJob();
        UUID job2 = insertJob();

        applicationRepository.saveAndFlush(new Application(job1, candidateA, resumeA));
        applicationRepository.saveAndFlush(new Application(job2, candidateA, resumeA));   // same candidate, other job
        applicationRepository.saveAndFlush(new Application(job1, candidateB, resumeB));   // other candidate, same job

        assertThat(applicationRepository.count()).isEqualTo(3L);
    }

    @Test
    void applicationForANonexistentJobIsRejectedByForeignKey() {
        UUID candidateId = savedCandidateId();
        UUID resumeId = insertResume(candidateId);
        applicationRepository.saveAndFlush(new Application(insertJob(), candidateId, resumeId));   // control

        Throwable thrown = catchThrowable(() -> applicationRepository.saveAndFlush(
                new Application(UUID.randomUUID(), candidateId, resumeId)));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, FOREIGN_KEY_VIOLATION, "fk_applications_job");
    }

    @Test
    void applicationForANonexistentCandidateIsRejectedByForeignKey() {
        UUID candidateId = savedCandidateId();
        UUID resumeId = insertResume(candidateId);
        applicationRepository.saveAndFlush(new Application(insertJob(), candidateId, resumeId));   // control

        UUID otherJobId = insertJob();
        Throwable thrown = catchThrowable(() -> applicationRepository.saveAndFlush(
                new Application(otherJobId, UUID.randomUUID(), resumeId)));

        // Two foreign keys fail at once here (the candidate does not exist, and neither does the
        // pair resume + candidate), and PostgreSQL does not promise which one it reports first.
        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        Throwable root = NestedExceptionUtils.getMostSpecificCause(thrown);
        assertThat(root).isInstanceOf(SQLException.class);
        assertThat(((SQLException) root).getSQLState()).isEqualTo(FOREIGN_KEY_VIOLATION);
        assertThat(root.getMessage()).containsPattern("fk_applications_(candidate|resume)");
    }

    @Test
    void anotherCandidatesResumeIsRejectedByTheCompositeForeignKey() {
        UUID candidateA = savedCandidateId();
        UUID candidateB = savedCandidateId();
        UUID resumeA = insertResume(candidateA);
        UUID resumeB = insertResume(candidateB);
        // Control: candidate A applies with their own resume.
        applicationRepository.saveAndFlush(new Application(insertJob(), candidateA, resumeA));

        UUID otherJobId = insertJob();
        // The resume exists and so does the candidate, but the resume belongs to B, not to A.
        Throwable thrown = catchThrowable(() -> applicationRepository.saveAndFlush(
                new Application(otherJobId, candidateA, resumeB)));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, FOREIGN_KEY_VIOLATION, "fk_applications_resume");
    }

    @Test
    void applicationWithANonexistentResumeIsRejectedByForeignKey() {
        UUID candidateId = savedCandidateId();
        applicationRepository.saveAndFlush(new Application(insertJob(), candidateId, insertResume(candidateId)));   // control

        UUID otherJobId = insertJob();
        Throwable thrown = catchThrowable(() -> applicationRepository.saveAndFlush(
                new Application(otherJobId, candidateId, UUID.randomUUID())));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, FOREIGN_KEY_VIOLATION, "fk_applications_resume");
    }

    @Test
    void jobWithAnApplicationCannotBeDeleted() {
        UUID candidateId = savedCandidateId();
        UUID jobId = insertJob();
        applicationRepository.saveAndFlush(new Application(jobId, candidateId, insertResume(candidateId)));
        UUID jobWithoutApplications = insertJob();
        // Control: a job without applications can be deleted, so the failure below is caused by the application only.
        assertThat(jdbcTemplate.update("DELETE FROM jobs WHERE id = ?", jobWithoutApplications)).isEqualTo(1);

        Throwable thrown = catchThrowable(() -> jdbcTemplate.update("DELETE FROM jobs WHERE id = ?", jobId));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, FOREIGN_KEY_VIOLATION, "fk_applications_job");
    }

    @Test
    void resumeUsedByAnApplicationCannotBeDeleted() {
        UUID candidateId = savedCandidateId();
        UUID usedResume = insertResume(candidateId);
        applicationRepository.saveAndFlush(new Application(insertJob(), candidateId, usedResume));
        UUID unusedResume = insertResume(candidateId);
        // Control: an unused resume can be deleted.
        assertThat(jdbcTemplate.update("DELETE FROM resumes WHERE id = ?", unusedResume)).isEqualTo(1);

        Throwable thrown = catchThrowable(() -> jdbcTemplate.update("DELETE FROM resumes WHERE id = ?", usedResume));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, FOREIGN_KEY_VIOLATION, "fk_applications_resume");
    }

    @Test
    void staleUpdateIsRejectedByOptimisticLocking() {
        UUID candidateId = savedCandidateId();
        UUID id = applicationRepository.saveAndFlush(
                new Application(insertJob(), candidateId, insertResume(candidateId))).getId();
        entityManager.clear();

        Application stale = applicationRepository.findById(id).orElseThrow();    // version 0
        entityManager.detach(stale);
        Application current = applicationRepository.findById(id).orElseThrow();  // another copy, version 0

        ReflectionTestUtils.setField(current, "status", ApplicationStatus.SHORTLISTED);
        applicationRepository.saveAndFlush(current);                              // database row is now version 1

        ReflectionTestUtils.setField(stale, "status", ApplicationStatus.REJECTED);
        assertThatThrownBy(() -> applicationRepository.saveAndFlush(stale))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

    @Test
    void versionIncrementsOnRealUpdate() {
        UUID candidateId = savedCandidateId();
        UUID id = applicationRepository.saveAndFlush(
                new Application(insertJob(), candidateId, insertResume(candidateId))).getId();
        entityManager.clear();
        Application application = applicationRepository.findById(id).orElseThrow();
        assertThat(versionInDatabase(id)).isZero();

        ReflectionTestUtils.setField(application, "status", ApplicationStatus.SHORTLISTED);
        applicationRepository.flush();

        assertThat(versionInDatabase(id)).isEqualTo(1L);
        assertThat(statusInDatabase(id)).isEqualTo("SHORTLISTED");
    }

    @Test
    void entityRejectsNullIds() {
        UUID id = UUID.randomUUID();
        assertThatNullPointerException().isThrownBy(() -> new Application(null, id, id));
        assertThatNullPointerException().isThrownBy(() -> new Application(id, null, id));
        assertThatNullPointerException().isThrownBy(() -> new Application(id, id, null));
    }
}
