package com.tunhire.tunhire.candidate;

public interface CandidateService {
    CandidateProfileResponse getMyProfile(Long userId);
    CandidateProfileResponse updateProfile(Long userId, UpdateProfileRequest request);
    CandidateSkillResponse addSkill(Long userId, SkillRequest request);
    void removeSkill(Long userId, Long skillId);
    CandidateProfileResponse getPublicProfile(Long userId);
}

