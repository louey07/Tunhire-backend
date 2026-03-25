package com.example.tunhire.applications.service;

import com.example.tunhire.applications.dto.ApplicationCreateRequest;
import com.example.tunhire.applications.dto.ApplicationSummary;
import com.example.tunhire.applications.entity.Application;
import com.example.tunhire.applications.entity.ApplicationStatus;
import com.example.tunhire.applications.repository.ApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
	@Mock
	private ApplicationRepository applicationRepository;

	@Mock
	private JobLookupService jobLookupService;

	@InjectMocks
	private ApplicationService applicationService;

	@Test
	void createSetsDefaults() {
		when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> {
			Application application = invocation.getArgument(0);
			application.setId(1L);
			return application;
		});

		var response = applicationService.create(new ApplicationCreateRequest(10L, 20L));
		assertThat(response.status()).isEqualTo(ApplicationStatus.SUBMITTED);
		assertThat(response.createdAt()).isNotNull();
	}

	@Test
	void getApplicationsForCompanyFiltersByJobs() {
		when(jobLookupService.getJobIdsByCompanyId(99L)).thenReturn(List.of(10L));
		when(applicationRepository.findByJobIdIn(eq(List.of(10L)))).thenReturn(List.of(
				buildApplication(1L, 10L, 1L),
				buildApplication(2L, 10L, 2L)
		));

		List<ApplicationSummary> results = applicationService.getApplicationsForCompany(99L);
		assertThat(results).hasSize(2);
		assertThat(results.get(0).jobId()).isEqualTo(10L);
	}

	private Application buildApplication(Long id, Long jobId, Long userId) {
		Application application = new Application();
		application.setId(id);
		application.setJobId(jobId);
		application.setUserId(userId);
		application.setStatus(ApplicationStatus.SUBMITTED);
		application.setCreatedAt(Instant.now());
		return application;
	}
}
