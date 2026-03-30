package com.example.tunhire.jobs.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record JobRequest(

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Company is required")
        String company,

        @NotBlank(message = "Location is required")
        String location,

        @NotBlank(message = "Description is required")
        String description,

        String contractType,

        String experienceLevel,

        BigDecimal salaryMin,

        BigDecimal salaryMax
) {}