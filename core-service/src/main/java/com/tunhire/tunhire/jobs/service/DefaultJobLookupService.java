package com.tunhire.tunhire.jobs.service;

import com.tunhire.tunhire.applications.JobLookupService;
import com.tunhire.tunhire.jobs.entity.Job;
import com.tunhire.tunhire.jobs.repository.JobRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

