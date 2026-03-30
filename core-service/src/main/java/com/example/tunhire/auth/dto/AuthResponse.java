package com.example.tunhire.auth.dto;

public record AuthResponse(
        String token,
        UserDto user
) {}