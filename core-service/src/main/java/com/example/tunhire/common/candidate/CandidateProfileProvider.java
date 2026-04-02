package com.example.tunhire.common.candidate;

public interface CandidateProfileProvider {
    CandidateSummaryDto getCandidateSummary(Long userId);
}
