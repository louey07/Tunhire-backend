package com.tunhire.tunhire.companies.dto;

public record CompanyCreateRequest(
		String name,
		String slug,
		String description,
		String logoUrl,
		String website,
		String location
) {
}

