package com.tunhire.tunhire.jobs;

import java.math.BigDecimal;
import java.time.Instant;

public record JobResponse(
    Long id,
    String title,
    Long companyId,
    String companyName,
    String location,
    String description,
    String contractType,
    String experienceLevel,
    BigDecimal salaryMin,
    BigDecimal salaryMax,
    String status,
    Instant createdAt,
    Instant updatedAt
) {}

