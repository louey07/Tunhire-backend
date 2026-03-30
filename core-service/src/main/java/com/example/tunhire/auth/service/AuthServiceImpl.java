package com.example.tunhire.auth.service;

import com.example.tunhire.auth.dto.AuthResponse;
import com.example.tunhire.auth.dto.LoginRequest;
import com.example.tunhire.auth.dto.RegisterRequest;
import com.example.tunhire.auth.dto.UserDto;
import com.example.tunhire.auth.entity.User;
import com.example.tunhire.auth.repository.UserRepository;
import com.example.tunhire.auth.security.JwtUtil;
import com.example.tunhire.common.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Register a new user:
     * 1. Check email not already taken
     * 2. Hash the password
     * 3. Save user to database
     * 4. Generate and return JWT token
     */
    @Override
    public AuthResponse register(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered: " + request.email());
        }

        // Build the new user entity
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhone(request.phone());
        user.setRole(request.role());
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        // Save to database
        User saved = userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRole().name());

        return new AuthResponse(token, toDto(saved));
    }

    /**
     * Login:
     * 1. Find user by email
     * 2. Check password matches
     * 3. Generate and return JWT token
     */
    @Override
    public AuthResponse login(LoginRequest request) {

        // Find user by email
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check password
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(token, toDto(user));
    }

    /**
     * Get current user by email extracted from JWT token
     */
    @Override
    public UserDto getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toDto(user);
    }

    /**
     * Convert User entity to UserDto (never expose password)
     */
    private UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}