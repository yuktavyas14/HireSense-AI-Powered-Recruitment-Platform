package com.hiresense.job.repository;

import com.hiresense.job.domain.Job;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access for job postings.
 * Only the methods inherited from JpaRepository for now; queries are added
 * together with the first service that needs them (Task 1.8).
 */
public interface JobRepository extends JpaRepository<Job, UUID> {
}
