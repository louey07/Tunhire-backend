package com.tunhire.tunhire.companies.dto;

import com.tunhire.tunhire.applications.dto.ApplicationSummary;

import java.util.List;

public record CompanyDashboardResponse(
		CompanyResponse company,
		List<JobSummaryDto> jobs,
		List<ApplicationSummary> applications
) {
}

