package com.example.tunhire.auth.dto;

import com.example.tunhire.auth.entity.Role;

import java.time.Instant;

public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phone,
        Role role,
        Instant createdAt
) {}