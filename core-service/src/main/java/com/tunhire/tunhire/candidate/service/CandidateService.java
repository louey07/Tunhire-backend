package com.tunhire.tunhire.candidate.service;

import com.tunhire.tunhire.candidate.dto.CandidateProfileResponse;
import com.tunhire.tunhire.candidate.dto.CandidateSkillResponse;
import com.tunhire.tunhire.candidate.dto.SkillRequest;
import com.tunhire.tunhire.candidate.dto.UpdateProfileRequest;

public interface CandidateService {
    CandidateProfileResponse getMyProfile(Long userId);
    CandidateProfileResponse updateProfile(Long userId, UpdateProfileRequest request);
    CandidateSkillResponse addSkill(Long userId, SkillRequest request);
    void removeSkill(Long userId, Long skillId);
    CandidateProfileResponse getPublicProfile(Long userId);
}

