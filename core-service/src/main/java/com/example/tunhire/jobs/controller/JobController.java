package com.example.tunhire.jobs.controller;

import com.example.tunhire.auth.repository.UserRepository;
import com.example.tunhire.common.dto.ApiResponse;
import com.example.tunhire.common.exception.ResourceNotFoundException;
import com.example.tunhire.jobs.dto.JobRequest;
import com.example.tunhire.jobs.dto.JobResponse;
import com.example.tunhire.jobs.service.JobService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
    private final UserRepository userRepository;

    public JobController(JobService jobService, UserRepository userRepository) {
        this.jobService = jobService;
        this.userRepository = userRepository;
    }

    /** POST /jobs — create a job (RECRUITER only) */
    @PostMapping
    public ApiResponse<JobResponse> create(
            @Valid @RequestBody JobRequest request,
            Authentication authentication
    ) {
        Long recruiterId = getAuthenticatedUserId(authentication);
        return ApiResponse.ok("Job created", jobService.create(request, recruiterId));
    }

    /** GET /jobs — list all active jobs */
    @GetMapping
    public ApiResponse<List<JobResponse>> getAll() {
        return ApiResponse.ok("Jobs fetched", jobService.getAll());
    }

    /** GET /jobs/{id} — get single job */
    @GetMapping("/{id}")
    public ApiResponse<JobResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok("Job fetched", jobService.getById(id));
    }

    /** PUT /jobs/{id} — update job (owner only) */
    @PutMapping("/{id}")
    public ApiResponse<JobResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody JobRequest request,
            Authentication authentication
    ) {
        Long recruiterId = getAuthenticatedUserId(authentication);
        return ApiResponse.ok("Job updated", jobService.update(id, request, recruiterId));
    }

    /** DELETE /jobs/{id} — delete job (owner only) */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Long recruiterId = getAuthenticatedUserId(authentication);
        jobService.delete(id, recruiterId);
        return ApiResponse.ok("Job deleted", null);
    }

    private Long getAuthenticatedUserId(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getId();
    }
}