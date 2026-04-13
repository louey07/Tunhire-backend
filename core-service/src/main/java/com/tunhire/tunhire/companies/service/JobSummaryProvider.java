package com.tunhire.tunhire.companies.service;

import com.tunhire.tunhire.companies.dto.JobSummaryDto;

import java.util.List;

public interface JobSummaryProvider {
	List<JobSummaryDto> getJobsByCompanyId(Long companyId);
}

