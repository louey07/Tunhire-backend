package com.example.tunhire.jobs.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record JobResponse(
        Long id,
        Long recruiterId,
        String recruiterName,
        String title,
        String company,
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