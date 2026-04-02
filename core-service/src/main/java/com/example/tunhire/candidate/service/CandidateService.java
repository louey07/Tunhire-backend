package com.example.tunhire.candidate.service;

import com.example.tunhire.candidate.dto.CandidateProfileResponse;
import com.example.tunhire.candidate.dto.CandidateSkillResponse;
import com.example.tunhire.candidate.dto.SkillRequest;
import com.example.tunhire.candidate.dto.UpdateProfileRequest;

public interface CandidateService {
    CandidateProfileResponse getMyProfile(Long userId);
    CandidateProfileResponse updateProfile(Long userId, UpdateProfileRequest request);
    CandidateSkillResponse addSkill(Long userId, SkillRequest request);
    void removeSkill(Long userId, Long skillId);
    CandidateProfileResponse getPublicProfile(Long userId);
}
