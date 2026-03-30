package com.example.tunhire.jobs.service;

import com.example.tunhire.jobs.dto.JobRequest;
import com.example.tunhire.jobs.dto.JobResponse;

import java.util.List;

public interface JobService {

    /** Create a new job — only RECRUITER can do this */
    JobResponse create(JobRequest request, Long recruiterId);

    /** Get all active jobs */
    List<JobResponse> getAll();

    /** Get a single job by id */
    JobResponse getById(Long id);

    /** Update a job — only the owner recruiter can do this */
    JobResponse update(Long id, JobRequest request, Long recruiterId);

    /** Delete a job — only the owner recruiter can do this */
    void delete(Long id, Long recruiterId);
}