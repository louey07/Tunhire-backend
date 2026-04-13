package com.tunhire.tunhire.jobs.service;

import com.tunhire.tunhire.common.exception.ResourceNotFoundException;
import com.tunhire.tunhire.companies.entity.Company;
import com.tunhire.tunhire.companies.repository.CompanyRepository;
import com.tunhire.tunhire.jobs.dto.JobRequest;
import com.tunhire.tunhire.jobs.dto.JobResponse;
import com.tunhire.tunhire.jobs.entity.Job;
import com.tunhire.tunhire.jobs.repository.JobRepository;
import com.tunhire.tunhire.recruiter.service.MembershipService;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final MembershipService membershipService;

    public JobServiceImpl(
        JobRepository jobRepository,
        CompanyRepository companyRepository,
        MembershipService membershipService
    ) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.membershipService = membershipService;
    }

    @Override
    public JobResponse create(JobRequest request, Long recruiterId) {
        if (!membershipService.isMember(request.companyId(), recruiterId)) {
            throw new IllegalArgumentException("You are not a member of this company");
        }

        Company company = companyRepository
            .findById(request.companyId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Company not found")
            );

        Job job = new Job();
        job.setTitle(request.title());
        job.setCompany(company);
        job.setLocation(request.location());
        job.setDescription(request.description());
        job.setContractType(request.contractType());
        job.setExperienceLevel(request.experienceLevel());
        job.setSalaryMin(request.salaryMin());
        job.setSalaryMax(request.salaryMax());
        job.setStatus("active");

        return toResponse(jobRepository.save(job));
    }

    @Override
    public Page<JobResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jobRepository
            .findByStatus("active", pageable)
            .map(this::toResponse);
    }

    @Override
    public JobResponse getById(Long id) {
        Job job = jobRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        return toResponse(job);
    }

    @Override
    public JobResponse update(Long id, JobRequest request, Long recruiterId) {
        Job job = jobRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!membershipService.isMember(job.getCompany().getId(), recruiterId)) {
            throw new IllegalArgumentException(
                "You do not have permission to update this job"
            );
        }

        Company company = companyRepository
            .findById(request.companyId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Company not found")
            );

        job.setTitle(request.title());
        job.setCompany(company);
        job.setLocation(request.location());
        job.setDescription(request.description());
        job.setContractType(request.contractType());
        job.setExperienceLevel(request.experienceLevel());
        job.setSalaryMin(request.salaryMin());
        job.setSalaryMax(request.salaryMax());

        return toResponse(jobRepository.save(job));
    }

    @Override
    public void delete(Long id, Long recruiterId) {
        Job job = jobRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!membershipService.isMember(job.getCompany().getId(), recruiterId)) {
            throw new IllegalArgumentException(
                "You do not have permission to delete this job"
            );
        }

        jobRepository.delete(job);
    }

    private JobResponse toResponse(Job job) {
        return new JobResponse(
            job.getId(),
            job.getTitle(),
            job.getCompany().getId(),
            job.getCompany().getName(),
            job.getLocation(),
            job.getDescription(),
            job.getContractType(),
            job.getExperienceLevel(),
            job.getSalaryMin(),
            job.getSalaryMax(),
            job.getStatus(),
            job.getCreatedAt(),
            job.getUpdatedAt()
        );
    }
}

