package com.example.tunhire.recruiter.service;

import com.example.tunhire.recruiter.dto.MembershipRequest;
import com.example.tunhire.recruiter.dto.MembershipResponse;
import com.example.tunhire.recruiter.entity.MemberRole;
import java.util.List;

public interface MembershipService {
    boolean isMember(Long companyId, Long userId);
    boolean isOwnerOrAdmin(Long companyId, Long userId);
    Long getCompanyIdByUserId(Long userId);
    MembershipResponse addMember(Long companyId, MembershipRequest request, Long currentUserId);
    void removeMember(Long companyId, Long userIdToRemove, Long currentUserId);
    MembershipResponse updateMemberRole(Long companyId, Long userIdToUpdate, MemberRole newRole, Long currentUserId);
    List<MembershipResponse> getMembers(Long companyId, Long currentUserId);
}
