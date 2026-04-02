package com.example.tunhire.jobs.service;

import com.example.tunhire.applications.service.JobLookupService;
import com.example.tunhire.jobs.entity.Job;
import com.example.tunhire.jobs.repository.JobRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DefaultJobLookupService implements JobLookupService {

    private final JobRepository jobRepository;

    public DefaultJobLookupService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<Long> getJobIdsByCompanyId(Long companyId) {
        return jobRepository
            .findByCompanyId(companyId)
            .stream()
            .map(Job::getId)
            .collect(Collectors.toList());
    }
}
