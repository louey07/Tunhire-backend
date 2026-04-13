package com.tunhire.tunhire.applications.service;

import java.util.List;

public interface JobLookupService {
	List<Long> getJobIdsByCompanyId(Long companyId);
}

