package com.example.tunhire.companies.service;

import com.example.tunhire.companies.dto.JobSummaryDto;

import java.util.List;

public interface JobSummaryProvider {
	List<JobSummaryDto> getJobsByCompanyId(Long companyId);
}
