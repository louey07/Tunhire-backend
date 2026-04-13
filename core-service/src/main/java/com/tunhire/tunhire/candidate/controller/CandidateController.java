package com.tunhire.tunhire.candidate.controller;

import com.tunhire.tunhire.auth.service.AuthService;
import com.tunhire.tunhire.candidate.dto.CandidateProfileResponse;
import com.tunhire.tunhire.candidate.dto.CandidateSkillResponse;
import com.tunhire.tunhire.candidate.dto.SkillRequest;
import com.tunhire.tunhire.candidate.dto.UpdateProfileRequest;
import com.tunhire.tunhire.candidate.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;
    private final AuthService authService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileResponse> getMyProfile(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return ResponseEntity.ok(candidateService.getMyProfile(userId));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileResponse> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request) {
        Long userId = extractUserId(authentication);
        return ResponseEntity.ok(candidateService.updateProfile(userId, request));
    }

    @PostMapping("/me/skills")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateSkillResponse> addSkill(
            Authentication authentication,
            @RequestBody SkillRequest request) {
        Long userId = extractUserId(authentication);
        return ResponseEntity.ok(candidateService.addSkill(userId, request));
    }

    @DeleteMapping("/me/skills/{id}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> removeSkill(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = extractUserId(authentication);
        candidateService.removeSkill(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<CandidateProfileResponse> getPublicProfile(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.getPublicProfile(id));
    }

    private Long extractUserId(Authentication authentication) {
        return authService.getUserIdByEmail(authentication.getName());
    }
}

