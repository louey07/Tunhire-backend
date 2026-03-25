package com.example.tunhire.companies.service;

import com.example.tunhire.companies.dto.CompanyCreateRequest;
import com.example.tunhire.companies.dto.CompanyResponse;
import com.example.tunhire.companies.entity.Company;
import com.example.tunhire.companies.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {
	@Mock
	private CompanyRepository companyRepository;

	@InjectMocks
	private CompanyService companyService;

	@Test
	void createAndFetchBySlug() {
		Company saved = new Company();
		saved.setId(1L);
		saved.setName("Acme");
		saved.setSlug("acme");

		when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> {
			Company company = invocation.getArgument(0);
			company.setId(1L);
			return company;
		});
		when(companyRepository.findBySlug("acme")).thenReturn(Optional.of(saved));

		CompanyResponse created = companyService.create(new CompanyCreateRequest(
				"Acme",
				"acme",
				null,
				null,
				null,
				null
		));

		CompanyResponse fetched = companyService.getBySlug("acme");
		assertThat(fetched.id()).isEqualTo(created.id());
	}
}
