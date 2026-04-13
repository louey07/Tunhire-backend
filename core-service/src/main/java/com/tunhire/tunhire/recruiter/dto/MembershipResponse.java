package com.tunhire.tunhire.recruiter.dto;

import com.tunhire.tunhire.recruiter.entity.MemberRole;
import java.time.LocalDateTime;

public record MembershipResponse(
    Long id,
    Long companyId,
    Long userId,
    MemberRole role,
    LocalDateTime joinedAt
) {}

