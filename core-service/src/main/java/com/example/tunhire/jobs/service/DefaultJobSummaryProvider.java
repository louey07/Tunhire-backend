package com.example.tunhire.jobs.service;

import com.example.tunhire.companies.dto.JobSummaryDto;
import com.example.tunhire.companies.service.JobSummaryProvider;
import com.example.tunhire.jobs.entity.Job;
import com.example.tunhire.jobs.repository.JobRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DefaultJobSummaryProvider implements JobSummaryProvider {

    private final JobRepository jobRepository;

    public DefaultJobSummaryProvider(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<JobSummaryDto> getJobsByCompanyId(Long companyId) {
        return jobRepository
            .findByCompanyId(companyId)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    private JobSummaryDto toDto(Job job) {
        return new JobSummaryDto(
            job.getId(),
            job.getTitle(),
            job.getLocation(),
            job.getStatus()
        );
    }
}
