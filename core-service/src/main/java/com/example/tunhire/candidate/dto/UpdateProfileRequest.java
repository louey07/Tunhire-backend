package com.example.tunhire.candidate.dto;

import java.time.LocalDate;

public record UpdateProfileRequest(
    String bio,
    String resumeUrl,
    String location,
    LocalDate availableFrom,
    Integer yearsOfExperience
) {}
