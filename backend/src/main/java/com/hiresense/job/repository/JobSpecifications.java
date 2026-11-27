package com.hiresense.job.repository;

import com.hiresense.job.domain.EmploymentType;
import com.hiresense.job.domain.Job;
import com.hiresense.job.domain.JobStatus;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;

/**
 * Building blocks for searching jobs. A filter that is not requested adds no condition at all,
 * so no null is ever sent to PostgreSQL. Attribute names are plain strings (no generated metamodel);
 * a typo would be caught by the repository tests.
 */
public final class JobSpecifications {

    private static final char LIKE_ESCAPE = '\\';

    private JobSpecifications() {
    }

    /** The public job list: only OPEN jobs, narrowed by whichever filters are given. */
    public static Specification<Job> openJobs(String keyword, String location, EmploymentType employmentType) {
        Specification<Job> spec = isOpen();
        if (hasText(keyword)) {
            spec = spec.and(containsIgnoringCase("title", keyword));
        }
        if (hasText(location)) {
            spec = spec.and(containsIgnoringCase("location", location));
        }
        if (employmentType != null) {
            spec = spec.and(hasEmploymentType(employmentType));
        }
        return spec;
    }

    static Specification<Job> isOpen() {
        return (root, query, cb) -> cb.equal(root.get("status"), JobStatus.OPEN);
    }

    static Specification<Job> hasEmploymentType(EmploymentType employmentType) {
        return (root, query, cb) -> cb.equal(root.get("employmentType"), employmentType);
    }

    static Specification<Job> containsIgnoringCase(String attribute, String text) {
        String pattern = "%" + escapeLike(text.trim().toLowerCase(Locale.ROOT)) + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get(attribute)), pattern, LIKE_ESCAPE);
    }

    /** Makes user text literal inside LIKE: the escape character itself, then % and _. */
    static String escapeLike(String text) {
        return text.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
