package com.javablog.adapter.rest;

import com.javablog.api.model.TokenResponseDto;
import com.javablog.application.AuthenticationTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenResourceTest {

    @Mock
    AuthenticationTokenService authenticationTokenService;

    @InjectMocks
    TokenResource tokenResource;

    @Test
    void generateTokenReturnsTokenFromService() {
        when(authenticationTokenService.generateToken()).thenReturn("test-token-123");
        when(authenticationTokenService.getExpiresIn()).thenReturn(300);

        TokenResponseDto response = tokenResource.generateToken();

        assertThat(response.getToken()).isEqualTo("test-token-123");
        assertThat(response.getExpiresIn()).isEqualTo(300);
    }
}
