package com.hiresense.job.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.hiresense.job.domain.EmploymentType;
import com.hiresense.job.domain.Job;
import com.hiresense.job.domain.JobStatus;
import java.lang.reflect.RecordComponent;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JobResponseTest {

    private static final Instant CREATED_AT = Instant.parse("2026-02-01T08:00:00Z");
    private static final Instant CLOSED_AT = Instant.parse("2026-03-01T09:30:00Z");

    @Test
    void openJobIsCopiedFieldByField() {
        UUID companyId = UUID.randomUUID();
        Job job = new Job(companyId, "Java Developer", "Build the backend.", "Pune", EmploymentType.FULL_TIME);
        ReflectionTestUtils.setField(job, "createdAt", CREATED_AT);   // normally set by auditing on save

        JobResponse response = JobResponse.from(job);

        assertThat(response.id()).isEqualTo(job.getId());
        assertThat(response.companyId()).isEqualTo(companyId);
        assertThat(response.title()).isEqualTo("Java Developer");
        assertThat(response.description()).isEqualTo("Build the backend.");
        assertThat(response.location()).isEqualTo("Pune");
        assertThat(response.employmentType()).isEqualTo(EmploymentType.FULL_TIME);
        assertThat(response.status()).isEqualTo(JobStatus.OPEN);
        assertThat(response.createdAt()).isEqualTo(CREATED_AT);
        assertThat(response.closedAt()).isNull();
    }

    @Test
    void closedJobCarriesStatusAndClosedAt() {
        Job job = new Job(UUID.randomUUID(), "Tester", "Test things.", null, EmploymentType.CONTRACT);
        job.close(CLOSED_AT);

        JobResponse response = JobResponse.from(job);

        assertThat(response.status()).isEqualTo(JobStatus.CLOSED);
        assertThat(response.closedAt()).isEqualTo(CLOSED_AT);
        assertThat(response.location()).isNull();
    }

    @Test
    void responseHasNoInternalFields() {
        assertThat(Arrays.stream(JobResponse.class.getRecordComponents()).map(RecordComponent::getName))
                .doesNotContain("version", "updatedAt")
                .containsExactly("id", "companyId", "title", "description", "location",
                        "employmentType", "status", "createdAt", "closedAt");
    }
}
