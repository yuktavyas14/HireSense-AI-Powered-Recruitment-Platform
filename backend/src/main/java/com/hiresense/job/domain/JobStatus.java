package com.hiresense.job.domain;

/**
 * Whether a job accepts new applications.
 * The names must match the CHECK constraint ck_jobs_status in the jobs table.
 */
public enum JobStatus {
    OPEN,
    CLOSED
}
