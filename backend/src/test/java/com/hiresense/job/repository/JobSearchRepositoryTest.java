package com.hiresense.job.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.hiresense.config.JpaAuditingConfig;
import com.hiresense.job.domain.EmploymentType;
import com.hiresense.job.domain.Job;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.transaction.TestTransaction;

/**
 * Tests of the public job search (JobSpecifications) against the real PostgreSQL test database,
 * no H2. Each test runs in a rolled-back transaction as hiresense_app. Every test scopes its data
 * with a unique random token in the title or location, so other rows can never change a result.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfig.class)
class JobSearchRepositoryTest {

    private static final Pageable FIRST_PAGE = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "createdAt"));

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID companyId;

    /** Safety guard: test database, application role, rollback transaction. Then one company for the test. */
    @BeforeEach
    void guardTestEnvironmentAndInsertCompany() {
        assertThat(jdbcTemplate.queryForObject("SELECT current_database()", String.class))
                .isEqualTo("hiresense_test");
        assertThat(jdbcTemplate.queryForObject("SELECT current_user", String.class))
                .isEqualTo("hiresense_app");
        assertThat(TestTransaction.isActive()).isTrue();
        assertThat(TestTransaction.isFlaggedForRollback()).isTrue();

        companyId = UUID.randomUUID();
        jdbcTemplate.update(
                "INSERT INTO companies (id, name, created_at, updated_at) VALUES (?, 'Test Company', now(), now())",
                companyId);
    }

    private static String token() {
        return "t" + UUID.randomUUID().toString().replace("-", "");
    }

    private UUID save(String title, String location, EmploymentType type) {
        return jobRepository.saveAndFlush(new Job(companyId, title, "Some description.", location, type)).getId();
    }

    private UUID saveClosed(String title, String location, EmploymentType type) {
        Job job = new Job(companyId, title, "Some description.", location, type);
        job.close(Instant.parse("2026-03-01T09:30:00Z"));
        return jobRepository.saveAndFlush(job).getId();
    }

    private List<UUID> search(String keyword, String location, EmploymentType type) {
        return jobRepository.findAll(JobSpecifications.openJobs(keyword, location, type), FIRST_PAGE)
                .map(Job::getId).getContent();
    }

    @Test
    void closedJobsAreNeverReturned() {
        String token = token();
        UUID open = save("Open " + token, null, EmploymentType.FULL_TIME);
        saveClosed("Closed " + token, null, EmploymentType.FULL_TIME);

        assertThat(search(token, null, null)).containsExactly(open);
    }

    @Test
    void keywordMatchesTheTitlePartiallyAndIgnoringCase() {
        String token = token();
        UUID id = save("Senior Java Developer " + token, null, EmploymentType.FULL_TIME);
        save("Accountant " + token, null, EmploymentType.FULL_TIME);

        assertThat(search("  jAvA dEVeloper " + token.toUpperCase(), null, null)).containsExactly(id);
    }

    @Test
    void keywordDoesNotLookIntoTheDescription() {
        String token = token();
        jobRepository.saveAndFlush(
                new Job(companyId, "Plain title", "Description mentions " + token, null, EmploymentType.FULL_TIME));

        assertThat(search(token, null, null)).isEmpty();
    }

    @Test
    void locationMatchesPartiallyAndIgnoringCase() {
        String token = token();
        UUID pune = save("Job A", "Pune, " + token, EmploymentType.FULL_TIME);
        save("Job B", "Mumbai, " + token, EmploymentType.FULL_TIME);

        assertThat(search(null, "PUNE, " + token, null)).containsExactly(pune);
    }

    @Test
    void jobWithoutLocationIsNotMatchedByALocationFilter() {
        String token = token();
        save("No location " + token, null, EmploymentType.FULL_TIME);
        UUID withLocation = save("With location " + token, "Remote " + token, EmploymentType.FULL_TIME);

        assertThat(search(token, "remote " + token, null)).containsExactly(withLocation);
    }

    @Test
    void employmentTypeFilterKeepsOnlyThatType() {
        String token = token();
        UUID contract = save("Contract " + token, null, EmploymentType.CONTRACT);
        save("Full time " + token, null, EmploymentType.FULL_TIME);
        save("Intern " + token, null, EmploymentType.INTERNSHIP);

        assertThat(search(token, null, EmploymentType.CONTRACT)).containsExactly(contract);
    }

    @Test
    void filtersAreCombinedWithAnd() {
        String token = token();
        UUID match = save("Java " + token, "Pune", EmploymentType.FULL_TIME);
        save("Java " + token, "Mumbai", EmploymentType.FULL_TIME);    // wrong location
        save("Java " + token, "Pune", EmploymentType.CONTRACT);       // wrong type
        save("Python " + token, "Pune", EmploymentType.FULL_TIME);    // wrong keyword

        assertThat(search("java " + token, "pune", EmploymentType.FULL_TIME)).containsExactly(match);
    }

    @Test
    void nullAndBlankFiltersAreIgnored() {
        String token = token();
        UUID a = save("A " + token, token, EmploymentType.FULL_TIME);
        UUID b = save("B " + token, token, EmploymentType.PART_TIME);

        assertThat(search(null, null, null)).contains(a, b);
        assertThat(search("   ", "", null)).contains(a, b);
    }

    @Test
    void percentSignIsTreatedAsALiteral() {
        String token = token();
        UUID literal = save("Rate 100% remote " + token, null, EmploymentType.FULL_TIME);
        save("Rate 1000 remote " + token, null, EmploymentType.FULL_TIME);   // matched if % were a wildcard

        assertThat(search("100% remote " + token, null, null)).containsExactly(literal);
    }

    @Test
    void underscoreIsTreatedAsALiteral() {
        String token = token();
        UUID literal = save("a_b " + token, null, EmploymentType.FULL_TIME);
        save("axb " + token, null, EmploymentType.FULL_TIME);                // matched if _ were a wildcard

        assertThat(search("a_b " + token, null, null)).containsExactly(literal);
    }

    @Test
    void backslashIsTreatedAsALiteral() {
        String token = token();
        UUID literal = save("path\\dir " + token, null, EmploymentType.FULL_TIME);
        save("pathdir " + token, null, EmploymentType.FULL_TIME);

        assertThat(search("path\\dir " + token, null, null)).containsExactly(literal);
    }

    @Test
    void searchWithoutMatchesGivesAnEmptyPage() {
        Page<Job> page = jobRepository.findAll(
                JobSpecifications.openJobs("no-such-job-" + token(), null, null), FIRST_PAGE);

        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isZero();
    }

    @Test
    void pagingReturnsTheRequestedSliceNewestFirstWithTotals() {
        String token = token();
        UUID oldest = save("Job 1 " + token, null, EmploymentType.FULL_TIME);
        UUID middle = save("Job 2 " + token, null, EmploymentType.FULL_TIME);
        UUID newest = save("Job 3 " + token, null, EmploymentType.FULL_TIME);
        // Plant known creation times, so the order does not depend on the clock.
        jdbcTemplate.update("UPDATE jobs SET created_at = '2026-01-01T00:00:00Z' WHERE id = ?", oldest);
        jdbcTemplate.update("UPDATE jobs SET created_at = '2026-01-02T00:00:00Z' WHERE id = ?", middle);
        jdbcTemplate.update("UPDATE jobs SET created_at = '2026-01-03T00:00:00Z' WHERE id = ?", newest);
        entityManager.clear();
        Sort newestFirst = Sort.by(Sort.Direction.DESC, "createdAt");

        Page<Job> first = jobRepository.findAll(
                JobSpecifications.openJobs(token, null, null), PageRequest.of(0, 2, newestFirst));
        Page<Job> second = jobRepository.findAll(
                JobSpecifications.openJobs(token, null, null), PageRequest.of(1, 2, newestFirst));

        assertThat(first.map(Job::getId).getContent()).containsExactly(newest, middle);
        assertThat(second.map(Job::getId).getContent()).containsExactly(oldest);
        assertThat(first.getTotalElements()).isEqualTo(3L);
        assertThat(first.getTotalPages()).isEqualTo(2);
    }

    @Test
    void sortByTitleAscendingOrdersAlphabetically() {
        String token = token();
        UUID b = save("B " + token, null, EmploymentType.FULL_TIME);
        UUID c = save("C " + token, null, EmploymentType.FULL_TIME);
        UUID a = save("A " + token, null, EmploymentType.FULL_TIME);

        Page<Job> page = jobRepository.findAll(JobSpecifications.openJobs(token, null, null),
                PageRequest.of(0, 50, Sort.by(Sort.Direction.ASC, "title")));

        assertThat(page.map(Job::getId).getContent()).containsExactly(a, b, c);
    }
}
