package com.tunhire.tunhire.companies.service;

import com.tunhire.tunhire.applications.dto.ApplicationSummary;
import com.tunhire.tunhire.applications.service.ApplicationService;
import com.tunhire.tunhire.companies.dto.CompanyDashboardResponse;
import com.tunhire.tunhire.companies.dto.CompanyResponse;
import com.tunhire.tunhire.companies.dto.JobSummaryDto;
import com.tunhire.tunhire.recruiter.service.MembershipService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompanyDashboardService {

    private final CompanyService companyService;
    private final JobSummaryProvider jobSummaryProvider;
    private final ApplicationService applicationService;
    private final MembershipService membershipService;

    public CompanyDashboardService(
        CompanyService companyService,
        JobSummaryProvider jobSummaryProvider,
        ApplicationService applicationService,
        MembershipService membershipService
    ) {
        this.companyService = companyService;
        this.jobSummaryProvider = jobSummaryProvider;
        this.applicationService = applicationService;
        this.membershipService = membershipService;
    }

    public CompanyDashboardResponse getDashboard(Long companyId, Long userId) {
        if (!membershipService.isMember(companyId, userId)) {
            throw new IllegalArgumentException("You are not a member of this company");
        }
        CompanyResponse company = companyService.getById(companyId);
        List<JobSummaryDto> jobs = jobSummaryProvider.getJobsByCompanyId(
            companyId
        );
        List<ApplicationSummary> applications =
            applicationService.getApplicationsForCompany(companyId);
        return new CompanyDashboardResponse(company, jobs, applications);
    }

    public List<JobSummaryDto> getJobs(Long companyId, Long userId) {
        if (!membershipService.isMember(companyId, userId)) {
            throw new IllegalArgumentException("You are not a member of this company");
        }
        return jobSummaryProvider.getJobsByCompanyId(companyId);
    }

    public List<ApplicationSummary> getApplications(Long companyId, Long userId) {
        if (!membershipService.isMember(companyId, userId)) {
            throw new IllegalArgumentException("You are not a member of this company");
        }
        return applicationService.getApplicationsForCompany(companyId);
    }
}

