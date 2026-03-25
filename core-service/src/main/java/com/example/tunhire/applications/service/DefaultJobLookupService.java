package com.example.tunhire.applications.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DefaultJobLookupService implements JobLookupService {
	@Override
	public List<Long> getJobIdsByCompanyId(Long companyId) {
		return Collections.emptyList();
	}
}
