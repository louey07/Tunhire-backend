package com.tunhire.tunhire.applications.service;

import com.tunhire.tunhire.applications.dto.ApplicationCreateRequest;
import com.tunhire.tunhire.applications.dto.ApplicationResponse;
import com.tunhire.tunhire.applications.dto.ApplicationSummary;
import com.tunhire.tunhire.applications.entity.Application;
import com.tunhire.tunhire.applications.entity.ApplicationStatus;
import com.tunhire.tunhire.applications.repository.ApplicationRepository;
import com.tunhire.tunhire.common.candidate.CandidateProfileProvider;
import com.tunhire.tunhire.common.candidate.CandidateSummaryDto;
import com.tunhire.tunhire.common.exception.ResourceNotFoundException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobLookupService jobLookupService;
    private final CandidateProfileProvider candidateProfileProvider;

    public ApplicationService(
        ApplicationRepository applicationRepository,
        JobLookupService jobLookupService,
        CandidateProfileProvider candidateProfileProvider
    ) {
        this.applicationRepository = applicationRepository;
        this.jobLookupService = jobLookupService;
        this.candidateProfileProvider = candidateProfileProvider;
    }

    public ApplicationResponse create(
        ApplicationCreateRequest request,
        Long userId
    ) {
        Application application = new Application();
        application.setJobId(request.jobId());
        application.setUserId(userId);
        application.setStatus(ApplicationStatus.SUBMITTED);
        return toResponse(applicationRepository.save(application));
    }

    public ApplicationResponse getById(Long id) {
        Application application = applicationRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Application not found")
            );
        return toResponse(application);
    }

    public List<ApplicationSummary> getByJobId(Long jobId) {
        return toSummaryList(applicationRepository.findByJobId(jobId));
    }

    public List<ApplicationSummary> getByUserId(Long userId) {
        return toSummaryList(applicationRepository.findByUserId(userId));
    }

    public List<ApplicationSummary> getApplicationsForCompany(Long companyId) {
        List<Long> jobIds = jobLookupService.getJobIdsByCompanyId(companyId);
        if (jobIds == null || jobIds.isEmpty()) {
            return Collections.emptyList();
        }
        return toSummaryList(applicationRepository.findByJobIdIn(jobIds));
    }

    private ApplicationResponse toResponse(Application application) {
        CandidateSummaryDto summary =
            candidateProfileProvider.getCandidateSummary(
                application.getUserId()
            );
        return new ApplicationResponse(
            application.getId(),
            application.getJobId(),
            application.getUserId(),
            summary != null ? summary.firstName() : "Unknown",
            summary != null ? summary.lastName() : "Unknown",
            summary != null ? summary.resumeUrl() : null,
            application.getStatus(),
            application.getCreatedAt()
        );
    }

    private List<ApplicationSummary> toSummaryList(
        List<Application> applications
    ) {
        return applications
            .stream()
            .map(app ->
                new ApplicationSummary(
                    app.getId(),
                    app.getJobId(),
                    app.getUserId(),
                    app.getStatus(),
                    app.getCreatedAt()
                )
            )
            .collect(Collectors.toList());
    }
}

