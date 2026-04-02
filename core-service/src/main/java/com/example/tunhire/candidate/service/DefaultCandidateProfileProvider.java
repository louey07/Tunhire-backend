package com.example.tunhire.candidate.service;

import com.example.tunhire.auth.dto.UserDto;
import com.example.tunhire.auth.service.AuthService;
import com.example.tunhire.candidate.entity.CandidateProfile;
import com.example.tunhire.candidate.repository.CandidateProfileRepository;
import com.example.tunhire.common.candidate.CandidateProfileProvider;
import com.example.tunhire.common.candidate.CandidateSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultCandidateProfileProvider implements CandidateProfileProvider {

    private final CandidateProfileRepository profileRepository;
    private final AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public CandidateSummaryDto getCandidateSummary(Long userId) {
        UserDto user = authService.getUserById(userId);
        Optional<CandidateProfile> profile = profileRepository.findByUserId(userId);
        return new CandidateSummaryDto(
                userId,
                user.firstName(),
                user.lastName(),
                profile.map(CandidateProfile::getResumeUrl).orElse(null)
        );
    }
}
