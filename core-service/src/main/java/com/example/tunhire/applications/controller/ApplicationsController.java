package com.example.tunhire.applications.controller;

import com.example.tunhire.applications.dto.ApplicationCreateRequest;
import com.example.tunhire.applications.dto.ApplicationResponse;
import com.example.tunhire.applications.dto.ApplicationSummary;
import com.example.tunhire.applications.service.ApplicationService;
import com.example.tunhire.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationsController {
	private final ApplicationService applicationService;

	public ApplicationsController(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	@PostMapping
	public ApiResponse<ApplicationResponse> create(@RequestBody ApplicationCreateRequest request) {
		return ApiResponse.ok("Application created", applicationService.create(request));
	}

	@GetMapping("/{id}")
	public ApiResponse<ApplicationResponse> getById(@PathVariable Long id) {
		return ApiResponse.ok("Application fetched", applicationService.getById(id));
	}

	@GetMapping
	public ApiResponse<List<ApplicationSummary>> list(
			@RequestParam(required = false) Long jobId,
			@RequestParam(required = false) Long userId
	) {
		if (jobId != null) {
			return ApiResponse.ok("Applications fetched", applicationService.getByJobId(jobId));
		}
		if (userId != null) {
			return ApiResponse.ok("Applications fetched", applicationService.getByUserId(userId));
		}
		return ApiResponse.ok("Applications fetched", List.of());
	}
}
