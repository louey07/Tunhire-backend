package com.example.tunhire.applications.repository;

import com.example.tunhire.applications.entity.Application;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository
    extends JpaRepository<Application, Long>
{
    List<Application> findByJobId(Long jobId);
    List<Application> findByUserId(Long userId);
    List<Application> findByJobIdIn(List<Long> jobIds);
}
