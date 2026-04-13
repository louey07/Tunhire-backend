package com.tunhire.tunhire.companies;

import com.tunhire.tunhire.companies.JobSummaryDto;

import java.util.List;

public interface JobSummaryProvider {
	List<JobSummaryDto> getJobsByCompanyId(Long companyId);
}

