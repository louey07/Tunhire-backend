package com.tunhire.tunhire.auth.dto;

public record AuthResponse(
        String token,
        UserDto user
) {}
