package com.tunhire.tunhire.auth;

import org.springframework.context.ApplicationEvent;

public record CandidateRegisteredEvent(Long userId) {}
