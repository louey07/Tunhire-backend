package com.tunhire.tunhire.recruiter;

import com.tunhire.tunhire.recruiter.MemberRole;

public record MembershipRequest(
    Long userId,
    MemberRole role
) {}

