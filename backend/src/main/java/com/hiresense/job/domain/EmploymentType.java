package com.hiresense.job.domain;

/**
 * How a job is offered.
 * The names must match the CHECK constraint ck_jobs_employment_type in the jobs table.
 */
public enum EmploymentType {
    FULL_TIME,
    PART_TIME,
    CONTRACT,
    INTERNSHIP
}
