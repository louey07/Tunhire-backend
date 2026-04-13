package com.tunhire.tunhire.jobs;

import com.tunhire.tunhire.jobs.JobRequest;
import com.tunhire.tunhire.jobs.JobResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobService {

    /** Create a new job — only RECRUITER can do this */
    JobResponse create(JobRequest request, Long recruiterId);

    /** Get all active jobs */
    Page<JobResponse> getAll(int page, int size);

    /** Get a single job by id */
    JobResponse getById(Long id);

    /** Update a job — only the owner recruiter can do this */
    JobResponse update(Long id, JobRequest request, Long recruiterId);

    /** Delete a job — only the owner recruiter can do this */
    void delete(Long id, Long recruiterId);
}
