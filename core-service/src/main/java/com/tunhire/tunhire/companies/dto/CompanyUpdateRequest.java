package com.tunhire.tunhire.companies.dto;

public record CompanyUpdateRequest(
		String name,
		String description,
		String logoUrl,
		String website,
		String location
) {
}

