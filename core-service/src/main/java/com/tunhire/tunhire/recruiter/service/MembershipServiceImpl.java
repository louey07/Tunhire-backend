package com.tunhire.tunhire.recruiter.service;

import com.tunhire.tunhire.common.exception.ResourceNotFoundException;
import com.tunhire.tunhire.companies.entity.Company;
import com.tunhire.tunhire.companies.repository.CompanyRepository;
import com.tunhire.tunhire.recruiter.dto.MembershipRequest;
import com.tunhire.tunhire.recruiter.dto.MembershipResponse;
import com.tunhire.tunhire.recruiter.entity.CompanyMembership;
import com.tunhire.tunhire.recruiter.entity.MemberRole;
import com.tunhire.tunhire.recruiter.repository.CompanyMembershipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MembershipServiceImpl implements MembershipService {

    private final CompanyMembershipRepository membershipRepository;
    private final CompanyRepository companyRepository;

    public MembershipServiceImpl(CompanyMembershipRepository membershipRepository, CompanyRepository companyRepository) {
        this.membershipRepository = membershipRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public boolean isMember(Long companyId, Long userId) {
        return membershipRepository.existsByCompanyIdAndUserId(companyId, userId);
    }

    @Override
    public boolean isOwnerOrAdmin(Long companyId, Long userId) {
        return membershipRepository.existsByCompanyIdAndUserIdAndRoleIn(companyId, userId, List.of(MemberRole.OWNER, MemberRole.ADMIN));
    }

    @Override
    public Long getCompanyIdByUserId(Long userId) {
        return membershipRepository.findByUserId(userId)
            .stream().findFirst()
            .map(m -> m.getCompany().getId())
            .orElseThrow(() -> new IllegalArgumentException("User does not belong to any company"));
    }

    @Override
    public MembershipResponse addMember(Long companyId, MembershipRequest request, Long currentUserId) {
        boolean hasMembers = !membershipRepository.findByCompanyId(companyId).isEmpty();
        if (hasMembers && !isOwnerOrAdmin(companyId, currentUserId)) {
            throw new IllegalArgumentException("You do not have permission to add members");
        }
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        
        CompanyMembership newMember = CompanyMembership.builder()
            .company(company)
            .userId(request.userId())
            .role(request.role())
            .build();
        newMember = membershipRepository.save(newMember);
        return toResponse(newMember);
    }

    @Override
    public void removeMember(Long companyId, Long userIdToRemove, Long currentUserId) {
        if (!isOwnerOrAdmin(companyId, currentUserId)) {
            throw new IllegalArgumentException("You do not have permission to remove members");
        }
        CompanyMembership member = membershipRepository.findByCompanyIdAndUserId(companyId, userIdToRemove)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        if (member.getRole() == MemberRole.OWNER && userIdToRemove.equals(currentUserId)) {
            throw new IllegalArgumentException("Owners cannot remove themselves");
        }
        membershipRepository.delete(member);
    }

    @Override
    public MembershipResponse updateMemberRole(Long companyId, Long userIdToUpdate, MemberRole newRole, Long currentUserId) {
        if (!isOwnerOrAdmin(companyId, currentUserId)) {
            throw new IllegalArgumentException("You do not have permission to update members");
        }
        CompanyMembership member = membershipRepository.findByCompanyIdAndUserId(companyId, userIdToUpdate)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        member.setRole(newRole);
        return toResponse(membershipRepository.save(member));
    }

    @Override
    public List<MembershipResponse> getMembers(Long companyId, Long currentUserId) {
        if (!isMember(companyId, currentUserId)) {
            throw new IllegalArgumentException("You do not have permission to view members");
        }
        return membershipRepository.findByCompanyId(companyId).stream()
            .map(this::toResponse).collect(Collectors.toList());
    }

    private MembershipResponse toResponse(CompanyMembership membership) {
        return new MembershipResponse(
            membership.getId(),
            membership.getCompany().getId(),
            membership.getUserId(),
            membership.getRole(),
            membership.getJoinedAt()
        );
    }
}

