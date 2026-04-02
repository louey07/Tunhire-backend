package com.example.tunhire.common.candidate;

public record CandidateSummaryDto(
        Long userId,
        String firstName,
        String lastName,
        String resumeUrl
) {
}
