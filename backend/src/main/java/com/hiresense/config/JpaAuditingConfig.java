package com.hiresense.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Switches on Spring Data JPA auditing, so that AuditableEntity's
 * createdAt and updatedAt are filled automatically.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
