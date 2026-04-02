package com.example.tunhire.recruiter.dto;

import com.example.tunhire.recruiter.entity.MemberRole;
import java.time.LocalDateTime;

public record MembershipResponse(
    Long id,
    Long companyId,
    Long userId,
    MemberRole role,
    LocalDateTime joinedAt
) {}
