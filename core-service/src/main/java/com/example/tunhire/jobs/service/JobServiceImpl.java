package com.example.tunhire.jobs.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.tunhire.auth.entity.User;
import com.example.tunhire.auth.repository.UserRepository;
import com.example.tunhire.common.exception.ResourceNotFoundException;
import com.example.tunhire.jobs.dto.JobRequest;
import com.example.tunhire.jobs.dto.JobResponse;
import com.example.tunhire.jobs.entity.Job;
import com.example.tunhire.jobs.repository.JobRepository;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobServiceImpl(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @Override
    public JobResponse create(JobRequest request, Long recruiterId) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        Job job = new Job();
        job.setRecruiter(recruiter);
        job.setTitle(request.title());
        job.setCompany(request.company());
        job.setLocation(request.location());
        job.setDescription(request.description());
        job.setContractType(request.contractType());
        job.setExperienceLevel(request.experienceLevel());
        job.setSalaryMin(request.salaryMin());
        job.setSalaryMax(request.salaryMax());
        job.setStatus("active");
        job.setCreatedAt(Instant.now());
        job.setUpdatedAt(Instant.now());

        return toResponse(jobRepository.save(job));
    }

    @Override
    public List<JobResponse> getAll() {
        return jobRepository.findByStatus("active")
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public JobResponse getById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        return toResponse(job);
    }

    @Override
    public JobResponse update(Long id, JobRequest request, Long recruiterId) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiterId)) {
            throw new IllegalArgumentException("You are not the owner of this job");
        }

        job.setTitle(request.title());
        job.setCompany(request.company());
        job.setLocation(request.location());
        job.setDescription(request.description());
        job.setContractType(request.contractType());
        job.setExperienceLevel(request.experienceLevel());
        job.setSalaryMin(request.salaryMin());
        job.setSalaryMax(request.salaryMax());
        job.setUpdatedAt(Instant.now());

        return toResponse(jobRepository.save(job));
    }

    @Override
    public void delete(Long id, Long recruiterId) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiterId)) {
            throw new IllegalArgumentException("You are not the owner of this job");
        }

        jobRepository.delete(job);
    }

    private JobResponse toResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getRecruiter().getId(),
                job.getRecruiter().getFirstName() + " " + job.getRecruiter().getLastName(),
                job.getTitle(),
                job.getCompany(),
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