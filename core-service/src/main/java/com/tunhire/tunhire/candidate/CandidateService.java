package com.tunhire.tunhire.candidate;

import com.tunhire.tunhire.candidate.CandidateProfileResponse;
import com.tunhire.tunhire.candidate.CandidateSkillResponse;
import com.tunhire.tunhire.candidate.SkillRequest;
import com.tunhire.tunhire.candidate.UpdateProfileRequest;

public interface CandidateService {
    CandidateProfileResponse getMyProfile(Long userId);
    CandidateProfileResponse updateProfile(Long userId, UpdateProfileRequest request);
    CandidateSkillResponse addSkill(Long userId, SkillRequest request);
    void removeSkill(Long userId, Long skillId);
    CandidateProfileResponse getPublicProfile(Long userId);
}

