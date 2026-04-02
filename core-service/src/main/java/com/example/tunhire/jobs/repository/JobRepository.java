package com.example.tunhire.jobs.repository;

import com.example.tunhire.jobs.entity.Job;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByStatus(String status);

    List<Job> findByCompanyId(Long companyId);
}
