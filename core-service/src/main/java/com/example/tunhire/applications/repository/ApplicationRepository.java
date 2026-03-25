package com.example.tunhire.applications.repository;

import com.example.tunhire.applications.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
	List<Application> findByJobId(Long jobId);
	List<Application> findByUserId(Long userId);
	List<Application> findByJobIdIn(List<Long> jobIds);
}
