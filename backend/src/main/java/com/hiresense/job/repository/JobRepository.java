package com.hiresense.job.repository;

import com.hiresense.job.domain.Job;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Data access for job postings.
 * Besides the inherited methods, it can run Specifications (see JobSpecifications)
 * for the public job search.
 */
public interface JobRepository extends JpaRepository<Job, UUID>, JpaSpecificationExecutor<Job> {
}
