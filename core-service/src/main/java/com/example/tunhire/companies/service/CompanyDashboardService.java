package com.example.tunhire.companies.service;

import com.example.tunhire.applications.dto.ApplicationSummary;
import com.example.tunhire.applications.service.ApplicationService;
import com.example.tunhire.companies.dto.CompanyDashboardResponse;
import com.example.tunhire.companies.dto.CompanyResponse;
import com.example.tunhire.companies.dto.JobSummaryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyDashboardService {
	private final CompanyService companyService;
	private final JobSummaryProvider jobSummaryProvider;
	private final ApplicationService applicationService;

	public CompanyDashboardService(
			CompanyService companyService,
			JobSummaryProvider jobSummaryProvider,
			ApplicationService applicationService
	) {
		this.companyService = companyService;
		this.jobSummaryProvider = jobSummaryProvider;
		this.applicationService = applicationService;
	}

	public CompanyDashboardResponse getDashboard(Long companyId) {
		CompanyResponse company = companyService.getById(companyId);
		List<JobSummaryDto> jobs = jobSummaryProvider.getJobsByCompanyId(companyId);
		List<ApplicationSummary> applications = applicationService.getApplicationsForCompany(companyId);
		return new CompanyDashboardResponse(company, jobs, applications);
	}
}
