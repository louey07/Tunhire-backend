package com.example.tunhire.companies.dto;

public record CompanyUpdateRequest(
		String name,
		String description,
		String logoUrl,
		String website,
		String location
) {
}
