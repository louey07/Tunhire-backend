package com.example.tunhire.companies.repository;

import com.example.tunhire.companies.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
	Optional<Company> findBySlug(String slug);
	boolean existsBySlug(String slug);
}
