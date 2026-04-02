package com.example.tunhire.recruiter.dto;

import com.example.tunhire.recruiter.entity.MemberRole;

public record MembershipRequest(
    Long userId,
    MemberRole role
) {}
