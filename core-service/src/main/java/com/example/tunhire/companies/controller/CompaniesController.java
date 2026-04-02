package com.example.tunhire.companies.controller;

import com.example.tunhire.applications.dto.ApplicationSummary;
import com.example.tunhire.auth.service.AuthService;
import com.example.tunhire.common.dto.ApiResponse;
import com.example.tunhire.companies.dto.CompanyCreateRequest;
import com.example.tunhire.companies.dto.CompanyDashboardResponse;
import com.example.tunhire.companies.dto.CompanyResponse;
import com.example.tunhire.companies.dto.CompanyUpdateRequest;
import com.example.tunhire.companies.dto.JobSummaryDto;
import com.example.tunhire.companies.service.CompanyDashboardService;
import com.example.tunhire.companies.service.CompanyService;
import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
public class CompaniesController {

    private final CompanyService companyService;
    private final CompanyDashboardService dashboardService;
    private final AuthService authService;

    public CompaniesController(
        CompanyService companyService,
        CompanyDashboardService dashboardService,
        AuthService authService
    ) {
        this.companyService = companyService;
        this.dashboardService = dashboardService;
        this.authService = authService;
    }

    @PostMapping
    public ApiResponse<CompanyResponse> create(
        @RequestBody CompanyCreateRequest request,
        Principal principal
    ) {
        Long userId = authService.getUserIdByEmail(principal.getName());
        return ApiResponse.ok(
            "Company created",
            companyService.create(request, userId)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<CompanyResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok("Company fetched", companyService.getById(id));
    }

    @GetMapping("/slug/{slug}")
    public ApiResponse<CompanyResponse> getBySlug(@PathVariable String slug) {
        return ApiResponse.ok(
            "Company fetched",
            companyService.getBySlug(slug)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<CompanyResponse> update(
        @PathVariable Long id,
        @RequestBody CompanyUpdateRequest request,
        Principal principal
    ) {
        Long userId = authService.getUserIdByEmail(principal.getName());
        return ApiResponse.ok(
            "Company updated",
            companyService.update(id, request, userId)
        );
    }

    @GetMapping("/{id}/jobs")
    public ApiResponse<List<JobSummaryDto>> getJobs(
        @PathVariable Long id,
        Principal principal
    ) {
        Long userId = authService.getUserIdByEmail(principal.getName());
        return ApiResponse.ok(
            "Company jobs fetched",
            dashboardService.getJobs(id, userId)
        );
    }

    @GetMapping("/{id}/applications")
    public ApiResponse<List<ApplicationSummary>> getApplications(
        @PathVariable Long id,
        Principal principal
    ) {
        Long userId = authService.getUserIdByEmail(principal.getName());
        return ApiResponse.ok(
            "Company applications fetched",
            dashboardService.getApplications(id, userId)
        );
    }

    @GetMapping("/{id}/dashboard")
    public ApiResponse<CompanyDashboardResponse> getDashboard(
        @PathVariable Long id,
        Principal principal
    ) {
        Long userId = authService.getUserIdByEmail(principal.getName());
        return ApiResponse.ok(
            "Company dashboard fetched",
            dashboardService.getDashboard(id, userId)
        );
    }
}
