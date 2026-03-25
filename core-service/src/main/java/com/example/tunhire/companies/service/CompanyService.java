package com.example.tunhire.companies.service;

import com.example.tunhire.common.exception.ResourceNotFoundException;
import com.example.tunhire.companies.dto.CompanyCreateRequest;
import com.example.tunhire.companies.dto.CompanyResponse;
import com.example.tunhire.companies.dto.CompanyUpdateRequest;
import com.example.tunhire.companies.entity.Company;
import com.example.tunhire.companies.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
	private final CompanyRepository companyRepository;

	public CompanyService(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	public CompanyResponse create(CompanyCreateRequest request) {
		Company company = new Company();
		company.setName(request.name());
		company.setSlug(request.slug());
		company.setDescription(request.description());
		company.setLogoUrl(request.logoUrl());
		company.setWebsite(request.website());
		company.setLocation(request.location());
		return toResponse(companyRepository.save(company));
	}

	public CompanyResponse getById(Long id) {
		return toResponse(getEntityById(id));
	}

	public CompanyResponse getBySlug(String slug) {
		Company company = companyRepository.findBySlug(slug)
				.orElseThrow(() -> new ResourceNotFoundException("Company not found"));
		return toResponse(company);
	}

	public CompanyResponse update(Long id, CompanyUpdateRequest request) {
		Company company = getEntityById(id);
		if (request.name() != null) {
			company.setName(request.name());
		}
		if (request.description() != null) {
			company.setDescription(request.description());
		}
		if (request.logoUrl() != null) {
			company.setLogoUrl(request.logoUrl());
		}
		if (request.website() != null) {
			company.setWebsite(request.website());
		}
		if (request.location() != null) {
			company.setLocation(request.location());
		}
		return toResponse(companyRepository.save(company));
	}

	public Company getEntityById(Long id) {
		return companyRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Company not found"));
	}

	private CompanyResponse toResponse(Company company) {
		return new CompanyResponse(
				company.getId(),
				company.getName(),
				company.getSlug(),
				company.getDescription(),
				company.getLogoUrl(),
				company.getWebsite(),
				company.getLocation()
		);
	}
}
