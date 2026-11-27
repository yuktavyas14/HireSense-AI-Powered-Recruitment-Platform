package com.hiresense.job.web;

import com.hiresense.job.domain.EmploymentType;
import com.hiresense.job.domain.Job;
import com.hiresense.job.domain.JobStatus;
import java.time.Instant;
import java.util.UUID;

/**
 * What the API returns for a job. The Job entity itself never leaves the service layer.
 * Deliberately left out: version and updatedAt (internal). Company details come in a later
 * slice, so only the company id is returned for now.
 */
public record JobResponse(
        UUID id,
        UUID companyId,
        String title,
        String description,
        String location,
        EmploymentType employmentType,
        JobStatus status,
        Instant createdAt,
        Instant closedAt) {

    public static JobResponse from(Job job) {
        return new JobResponse(
                job.getId(),
                job.getCompanyId(),
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),
                job.getEmploymentType(),
                job.getStatus(),
                job.getCreatedAt(),
                job.getClosedAt());
    }
}
