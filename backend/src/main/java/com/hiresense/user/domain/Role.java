package com.hiresense.user.domain;

/**
 * Account roles (one per user in the MVP).
 * The names must match the CHECK constraint ck_users_role in the users table.
 */
public enum Role {
    CANDIDATE,
    RECRUITER,
    ADMIN
}
