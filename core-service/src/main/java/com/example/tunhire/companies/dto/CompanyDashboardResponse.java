package com.example.tunhire.companies.dto;

import com.example.tunhire.applications.dto.ApplicationSummary;

import java.util.List;

public record CompanyDashboardResponse(
		CompanyResponse company,
		List<JobSummaryDto> jobs,
		List<ApplicationSummary> applications
) {
}
