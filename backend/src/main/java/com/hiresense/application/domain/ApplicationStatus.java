package com.hiresense.application.domain;

/**
 * Where an application currently stands (D-10, D-14).
 * The names must match the CHECK constraint ck_applications_status in the applications table.
 * The rules for moving between statuses live in the service layer (Task 1.10), not here.
 */
public enum ApplicationStatus {
    APPLIED,
    SHORTLISTED,
    INTERVIEW,
    OFFER,
    ACCEPTED,
    DECLINED,
    REJECTED
}
