package com.javablog.adapter.rest;

import com.javablog.api.TokenApi;
import com.javablog.api.model.TokenResponseDto;
import com.javablog.application.AuthenticationTokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TokenResource implements TokenApi {

    private final AuthenticationTokenService authenticationTokenService;

    @Inject
    public TokenResource(AuthenticationTokenService authenticationTokenService) {
        this.authenticationTokenService = authenticationTokenService;
    }

    @Override
    public TokenResponseDto generateToken() {
        return new TokenResponseDto()
                .token(authenticationTokenService.generateToken())
                .expiresIn(authenticationTokenService.getExpiresIn());
    }
}
