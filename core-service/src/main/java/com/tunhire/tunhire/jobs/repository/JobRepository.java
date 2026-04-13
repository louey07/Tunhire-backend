package com.tunhire.tunhire.jobs.repository;

import com.tunhire.tunhire.jobs.entity.Job;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByStatus(String status);
    Page<Job> findByStatus(String status, Pageable pageable);

    List<Job> findByCompanyId(Long companyId);
}

