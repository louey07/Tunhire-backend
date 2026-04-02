package com.example.tunhire.recruiter.repository;

import com.example.tunhire.recruiter.entity.CompanyMembership;
import com.example.tunhire.recruiter.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyMembershipRepository extends JpaRepository<CompanyMembership, Long> {
    Optional<CompanyMembership> findByCompanyIdAndUserId(Long companyId, Long userId);
    List<CompanyMembership> findByCompanyId(Long companyId);
    List<CompanyMembership> findByUserId(Long userId);
    boolean existsByCompanyIdAndUserId(Long companyId, Long userId);
    boolean existsByCompanyIdAndUserIdAndRoleIn(Long companyId, Long userId, List<MemberRole> roles);
}
