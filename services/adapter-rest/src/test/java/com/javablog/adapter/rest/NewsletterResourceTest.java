package com.javablog.adapter.rest;

import com.javablog.api.model.NewsletterRegistrationRequestDto;
import com.javablog.api.model.NewsletterRegistrationResponseDto;
import com.javablog.application.NewsletterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsletterResourceTest {

    @Mock
    NewsletterService newsletterService;

    @InjectMocks
    NewsletterResource newsletterResource;

    @Test
    void registerNewsletterDelegatesToService() {
        NewsletterRegistrationRequestDto request = new NewsletterRegistrationRequestDto()
                .email("user@example.com")
                .token("valid-token");
        when(newsletterService.register("user@example.com", "valid-token"))
                .thenReturn("Successfully registered");

        NewsletterRegistrationResponseDto response = newsletterResource.registerNewsletter(request);

        assertThat(response.getMessage()).isEqualTo("Successfully registered");
    }
}
