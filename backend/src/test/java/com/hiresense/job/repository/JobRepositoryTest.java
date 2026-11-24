package com.hiresense.job.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.hiresense.config.JpaAuditingConfig;
import com.hiresense.job.domain.EmploymentType;
import com.hiresense.job.domain.Job;
import com.hiresense.job.domain.JobStatus;
import jakarta.persistence.EntityManager;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import org.springframework.test.context.transaction.TestTransaction;

/**
 * Repository tests against the real PostgreSQL test database (hiresense_test), no H2.
 * Everything runs as the application role hiresense_app inside a transaction that is rolled
 * back, so no rows stay behind. There is no Company entity yet, so companies are inserted with
 * SQL. Invalid rows cannot be built through Job, so constraint tests use raw SQL, each with a
 * valid control statement first. A failed statement aborts the PostgreSQL transaction, so the
 * failing statement is always the last database call of a test.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfig.class)
class JobRepositoryTest {

    private static final String CHECK_VIOLATION = "23514";
    private static final String FOREIGN_KEY_VIOLATION = "23503";

    private static final Instant CLOSED_AT = Instant.parse("2026-03-01T09:30:00Z");
    private static final Instant LATER_CLOSED_AT = Instant.parse("2026-04-02T10:45:00Z");

    private static final String INSERT_COMPANY = """
            INSERT INTO companies (id, name, created_at, updated_at)
            VALUES (?, 'Test Company', now(), now())
            """;

    /** The closed_at value is one of two constant SQL fragments, never user input. */
    private static String insertRawJobSql(boolean withClosedAt) {
        return """
                INSERT INTO jobs (id, company_id, title, description, employment_type, status,
                                  closed_at, created_at, updated_at)
                VALUES (?, ?, 'Raw Job', 'Raw description', ?, ?, %s, now(), now())
                """.formatted(withClosedAt ? "now()" : "NULL");
    }

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Safety guard, runs before every test: the connection must be the test database as the
     * application role, and the test transaction must be active and flagged for rollback.
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

    private UUID insertCompany() {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update(INSERT_COMPANY, id);
        return id;
    }

    private static Job newJob(UUID companyId) {
        return new Job(companyId, "Java Developer", "Build the backend.", "Pune", EmploymentType.FULL_TIME);
    }

    private UUID insertRawJob(UUID companyId, String employmentType, String status, boolean withClosedAt) {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update(insertRawJobSql(withClosedAt), id, companyId, employmentType, status);
        return id;
    }

    private long versionInDatabase(UUID id) {
        return jdbcTemplate.queryForObject("SELECT version FROM jobs WHERE id = ?", Long.class, id);
    }

    private String statusInDatabase(UUID id) {
        return jdbcTemplate.queryForObject("SELECT status FROM jobs WHERE id = ?", String.class, id);
    }

    private Instant closedAtInDatabase(UUID id) {
        Timestamp value = jdbcTemplate.queryForObject("SELECT closed_at FROM jobs WHERE id = ?", Timestamp.class, id);
        return value == null ? null : value.toInstant();
    }

    /** Checks the PostgreSQL error under Spring's wrapper: exact SQLState and the constraint name. */
    private static void assertViolation(Throwable thrown, String sqlState, String constraintName) {
        Throwable root = NestedExceptionUtils.getMostSpecificCause(thrown);
        assertThat(root).isInstanceOf(SQLException.class);
        assertThat(((SQLException) root).getSQLState()).isEqualTo(sqlState);
        assertThat(root).hasMessageContaining(constraintName);
    }

    // ---- 1. save and retrieve ----

    @Test
    void savedJobCanBeReadBack() {
        UUID companyId = insertCompany();
        UUID id = jobRepository.saveAndFlush(newJob(companyId)).getId();
        entityManager.clear();

        Job found = jobRepository.findById(id).orElseThrow();

        assertThat(found.getCompanyId()).isEqualTo(companyId);
        assertThat(found.getTitle()).isEqualTo("Java Developer");
        assertThat(found.getDescription()).isEqualTo("Build the backend.");
        assertThat(found.getLocation()).isEqualTo("Pune");
        assertThat(found.getEmploymentType()).isEqualTo(EmploymentType.FULL_TIME);
        assertThat(found.getStatus()).isEqualTo(JobStatus.OPEN);
        assertThat(found.getClosedAt()).isNull();
    }

    @Test
    void jobWithoutLocationCanBeSavedAndReadBack() {
        UUID id = jobRepository.saveAndFlush(
                new Job(insertCompany(), "Remote Tester", "Test things.", null, EmploymentType.CONTRACT)).getId();
        entityManager.clear();

        assertThat(jobRepository.findById(id).orElseThrow().getLocation()).isNull();
    }

    // ---- 2. audit fields and version ----

    @Test
    void auditFieldsAndVersionAreFilledOnInsert() {
        Instant before = Instant.now();
        Job saved = jobRepository.saveAndFlush(newJob(insertCompany()));
        Instant after = Instant.now();

        assertThat(saved.getCreatedAt()).isBetween(before.minusSeconds(5), after.plusSeconds(5));
        assertThat(saved.getUpdatedAt()).isBetween(before.minusSeconds(5), after.plusSeconds(5));
        assertThat(saved.getUpdatedAt()).isAfterOrEqualTo(saved.getCreatedAt());
        assertThat(saved.getVersion()).isZero();
        assertThat(versionInDatabase(saved.getId())).isZero();
    }

    // ---- 3. valid employment types and statuses ----

    @ParameterizedTest
    @EnumSource(EmploymentType.class)
    void everyEmploymentTypeIsPersistedAsItsName(EmploymentType type) {
        UUID id = jobRepository.saveAndFlush(new Job(insertCompany(), "Any Job", "Any description.", null, type))
                .getId();
        entityManager.clear();

        String stored = jdbcTemplate.queryForObject("SELECT employment_type FROM jobs WHERE id = ?", String.class, id);
        assertThat(stored).isEqualTo(type.name());
        assertThat(jobRepository.findById(id).orElseThrow().getEmploymentType()).isEqualTo(type);
    }

    @Test
    void newJobIsStoredAsOpenWithoutClosedAt() {
        UUID id = jobRepository.saveAndFlush(newJob(insertCompany())).getId();

        assertThat(statusInDatabase(id)).isEqualTo("OPEN");
        assertThat(closedAtInDatabase(id)).isNull();
    }

    @Test
    void closedJobIsStoredAsClosedWithClosedAt() {
        Job job = newJob(insertCompany());
        job.close(CLOSED_AT);
        UUID id = jobRepository.saveAndFlush(job).getId();
        entityManager.clear();

        assertThat(statusInDatabase(id)).isEqualTo("CLOSED");
        assertThat(closedAtInDatabase(id)).isEqualTo(CLOSED_AT);
        assertThat(jobRepository.findById(id).orElseThrow().getStatus()).isEqualTo(JobStatus.CLOSED);
    }

    // ---- 4. invalid employment type and status ----

    @Test
    void databaseRejectsInvalidEmploymentTypeThroughCheckConstraint() {
        UUID companyId = insertCompany();
        insertRawJob(companyId, "FULL_TIME", "OPEN", false); // control: valid values work

        Throwable thrown = catchThrowable(() -> insertRawJob(companyId, "FREELANCE", "OPEN", false));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, CHECK_VIOLATION, "ck_jobs_employment_type");
    }

    @Test
    void databaseRejectsInvalidStatusThroughCheckConstraint() {
        UUID companyId = insertCompany();
        insertRawJob(companyId, "FULL_TIME", "OPEN", false); // control: valid values work

        // closed_at stays NULL, so ck_jobs_closed_at is satisfied and only ck_jobs_status can fail.
        Throwable thrown = catchThrowable(() -> insertRawJob(companyId, "FULL_TIME", "ARCHIVED", false));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, CHECK_VIOLATION, "ck_jobs_status");
    }

    // ---- 5. both invalid closed_at combinations ----

    @Test
    void databaseRejectsOpenJobWithClosedAt() {
        UUID companyId = insertCompany();
        insertRawJob(companyId, "FULL_TIME", "OPEN", false); // control: OPEN without closed_at works

        Throwable thrown = catchThrowable(() -> insertRawJob(companyId, "FULL_TIME", "OPEN", true));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, CHECK_VIOLATION, "ck_jobs_closed_at");
    }

    @Test
    void databaseRejectsClosedJobWithoutClosedAt() {
        UUID companyId = insertCompany();
        insertRawJob(companyId, "FULL_TIME", "CLOSED", true); // control: CLOSED with closed_at works

        Throwable thrown = catchThrowable(() -> insertRawJob(companyId, "FULL_TIME", "CLOSED", false));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, CHECK_VIOLATION, "ck_jobs_closed_at");
    }

    // ---- 6. foreign key to companies ----

    @Test
    void jobWithNonexistentCompanyIsRejectedByForeignKey() {
        jobRepository.saveAndFlush(newJob(insertCompany())); // control: a job with a real company works

        Throwable thrown = catchThrowable(() -> jobRepository.saveAndFlush(newJob(UUID.randomUUID())));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, FOREIGN_KEY_VIOLATION, "fk_jobs_company");
    }

    // ---- 7. company deletion restricted while jobs exist ----

    @Test
    void companyWithJobsCannotBeDeleted() {
        UUID companyWithJob = insertCompany();
        jobRepository.saveAndFlush(newJob(companyWithJob));
        UUID companyWithoutJob = insertCompany();
        // control: a company without jobs can be deleted
        assertThat(jdbcTemplate.update("DELETE FROM companies WHERE id = ?", companyWithoutJob)).isEqualTo(1);

        Throwable thrown = catchThrowable(
                () -> jdbcTemplate.update("DELETE FROM companies WHERE id = ?", companyWithJob));

        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertViolation(thrown, FOREIGN_KEY_VIOLATION, "fk_jobs_company");
    }

    // ---- 8. close() and reopen() ----

    @Test
    void closeSetsStatusAndClosedAtTogether() {
        Job job = newJob(UUID.randomUUID());

        job.close(CLOSED_AT);

        assertThat(job.getStatus()).isEqualTo(JobStatus.CLOSED);
        assertThat(job.getClosedAt()).isEqualTo(CLOSED_AT);
    }

    @Test
    void closeWithNullTimestampThrowsAndLeavesJobOpen() {
        Job job = newJob(UUID.randomUUID());

        assertThatThrownBy(() -> job.close(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Close timestamp must not be null");

        assertThat(job.getStatus()).isEqualTo(JobStatus.OPEN);
        assertThat(job.getClosedAt()).isNull();
    }

    @Test
    void closeWithNullTimestampAlsoThrowsOnAnAlreadyClosedJobAndKeepsItUnchanged() {
        Job job = newJob(UUID.randomUUID());
        job.close(CLOSED_AT);

        assertThatThrownBy(() -> job.close(null)).isInstanceOf(NullPointerException.class);

        assertThat(job.getStatus()).isEqualTo(JobStatus.CLOSED);
        assertThat(job.getClosedAt()).isEqualTo(CLOSED_AT);
    }

    @Test
    void closingTwiceKeepsTheOriginalClosedAt() {
        Job job = newJob(UUID.randomUUID());
        job.close(CLOSED_AT);

        job.close(LATER_CLOSED_AT);

        assertThat(job.getStatus()).isEqualTo(JobStatus.CLOSED);
        assertThat(job.getClosedAt()).isEqualTo(CLOSED_AT);
    }

    @Test
    void reopenOnAnOpenJobChangesNothing() {
        Job job = newJob(UUID.randomUUID());

        job.reopen();

        assertThat(job.getStatus()).isEqualTo(JobStatus.OPEN);
        assertThat(job.getClosedAt()).isNull();
    }

    @Test
    void reopenClearsStatusAndClosedAtTogether() {
        Job job = newJob(UUID.randomUUID());
        job.close(CLOSED_AT);

        job.reopen();

        assertThat(job.getStatus()).isEqualTo(JobStatus.OPEN);
        assertThat(job.getClosedAt()).isNull();
    }

    @Test
    void closeAfterReopenStoresTheNewTimestamp() {
        Job job = newJob(UUID.randomUUID());
        job.close(CLOSED_AT);
        job.reopen();

        job.close(LATER_CLOSED_AT);

        assertThat(job.getClosedAt()).isEqualTo(LATER_CLOSED_AT);
    }

    @Test
    void closeIsPersistedAndAcceptedByTheDatabaseCheckAndVersionIncrements() {
        UUID id = jobRepository.saveAndFlush(newJob(insertCompany())).getId();
        entityManager.clear();
        Job job = jobRepository.findById(id).orElseThrow();

        job.close(CLOSED_AT);
        jobRepository.flush();

        assertThat(statusInDatabase(id)).isEqualTo("CLOSED");
        assertThat(closedAtInDatabase(id)).isEqualTo(CLOSED_AT);
        assertThat(versionInDatabase(id)).isEqualTo(1L);
    }

    @Test
    void reopenIsPersistedAndAcceptedByTheDatabaseCheck() {
        Job closed = newJob(insertCompany());
        closed.close(CLOSED_AT);
        UUID id = jobRepository.saveAndFlush(closed).getId();
        entityManager.clear();
        Job job = jobRepository.findById(id).orElseThrow();

        job.reopen();
        jobRepository.flush();

        assertThat(statusInDatabase(id)).isEqualTo("OPEN");
        assertThat(closedAtInDatabase(id)).isNull();
        assertThat(versionInDatabase(id)).isEqualTo(1L);
    }

    @Test
    void callsThatChangeNothingDoNotIncrementTheVersion() {
        UUID id = jobRepository.saveAndFlush(newJob(insertCompany())).getId();
        entityManager.clear();
        Job job = jobRepository.findById(id).orElseThrow();

        job.reopen(); // already OPEN: no-op
        jobRepository.flush();

        assertThat(versionInDatabase(id)).isZero();
    }

    // ---- 9. real INSERT / SELECT / UPDATE as hiresense_app (the guard proves the role) ----

    @Test
    void appRoleCanInsertIntoJobs() {
        UUID companyId = insertCompany();
        UUID id = UUID.randomUUID();

        int inserted = jdbcTemplate.update(insertRawJobSql(false), id, companyId, "PART_TIME", "OPEN");

        assertThat(inserted).isEqualTo(1);
    }

    @Test
    void appRoleCanSelectFromJobs() {
        UUID id = insertRawJob(insertCompany(), "INTERNSHIP", "OPEN", false);

        String employmentType = jdbcTemplate.queryForObject(
                "SELECT employment_type FROM jobs WHERE id = ?", String.class, id);

        assertThat(employmentType).isEqualTo("INTERNSHIP");
    }

    @Test
    void appRoleCanUpdateJobs() {
        UUID id = insertRawJob(insertCompany(), "FULL_TIME", "OPEN", false);

        int updated = jdbcTemplate.update("UPDATE jobs SET title = 'Updated Title' WHERE id = ?", id);

        assertThat(updated).isEqualTo(1);
        assertThat(jdbcTemplate.queryForObject("SELECT title FROM jobs WHERE id = ?", String.class, id))
                .isEqualTo("Updated Title");
    }
}
