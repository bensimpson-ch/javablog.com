package com.javablog.application;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuthenticationTokenService {

    public String generateToken() {
        return "";
    }

    public int getExpiresIn() {
        return 0;
    }

    public boolean validateToken(String token) {
        return false;
    }
}
