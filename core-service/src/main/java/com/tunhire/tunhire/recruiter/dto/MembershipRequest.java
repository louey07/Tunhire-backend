package com.tunhire.tunhire.recruiter.dto;

import com.tunhire.tunhire.recruiter.entity.MemberRole;

public record MembershipRequest(
    Long userId,
    MemberRole role
) {}

