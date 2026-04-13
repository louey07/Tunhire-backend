package com.tunhire.tunhire.auth.service;

import com.tunhire.tunhire.auth.dto.AuthResponse;
import com.tunhire.tunhire.auth.dto.LoginRequest;
import com.tunhire.tunhire.auth.dto.RegisterRequest;
import com.tunhire.tunhire.auth.dto.UserDto;

public interface AuthService {
    /**
     * Register a new user and return a JWT token
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Login with email and password, return a JWT token
     */
    AuthResponse login(LoginRequest request);

    /**
     * Get the currently authenticated user by email
     */
    UserDto getCurrentUser(String email);

    /**
     * Get the user ID by email
     */
    Long getUserIdByEmail(String email);

    /**
     * Get the user by ID
     */
    UserDto getUserById(Long id);
}

