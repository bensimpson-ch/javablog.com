package com.javablog.adapter.rest;

import com.javablog.api.NewsletterApi;
import com.javablog.api.model.NewsletterRegistrationRequestDto;
import com.javablog.api.model.NewsletterRegistrationResponseDto;
import com.javablog.application.NewsletterService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NewsletterResource implements NewsletterApi {

    private final NewsletterService newsletterService;

    @Inject
    public NewsletterResource(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @Override
    public NewsletterRegistrationResponseDto registerNewsletter(NewsletterRegistrationRequestDto request) {
        String message = newsletterService.register(request.getEmail(), request.getToken());
        return new NewsletterRegistrationResponseDto()
                .message(message);
    }
}
