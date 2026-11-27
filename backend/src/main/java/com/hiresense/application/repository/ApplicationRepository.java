package com.hiresense.application.repository;

import com.hiresense.application.domain.Application;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access for applications.
 * No custom methods yet: the lookups ("my applications", "applicants of a job", duplicate check)
 * arrive with the tasks that use them (1.8 to 1.10).
 */
public interface ApplicationRepository extends JpaRepository<Application, UUID> {
}
