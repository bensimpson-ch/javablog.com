package com.javablog.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NewsletterService {

    private final AuthenticationTokenService authenticationTokenService;

    @Inject
    public NewsletterService(AuthenticationTokenService authenticationTokenService) {
        this.authenticationTokenService = authenticationTokenService;
    }

    public String register(String email, String token) {
        return "";
    }
}
