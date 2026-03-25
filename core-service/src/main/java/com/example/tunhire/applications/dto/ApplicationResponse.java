package com.example.tunhire.applications.dto;

import com.example.tunhire.applications.entity.ApplicationStatus;

import java.time.Instant;

public record ApplicationResponse(
		Long id,
		Long jobId,
		Long userId,
		ApplicationStatus status,
		Instant createdAt
) {
}
