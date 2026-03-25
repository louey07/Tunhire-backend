package com.example.tunhire.companies.service;

import com.example.tunhire.companies.dto.JobSummaryDto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DefaultJobSummaryProvider implements JobSummaryProvider {
	@Override
	public List<JobSummaryDto> getJobsByCompanyId(Long companyId) {
		return Collections.emptyList();
	}
}
