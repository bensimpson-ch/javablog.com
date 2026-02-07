package com.javablog.adapter.rest;

import com.javablog.api.v1.TranslationsApi;
import com.javablog.api.v1.model.LanguageCode;
import com.javablog.api.v1.model.TranslationRequestBody;
import com.javablog.application.service.TranslationService;
import com.javablog.domain.blog.Author;
import com.javablog.domain.blog.Language;
import com.javablog.domain.blog.Languages;
import com.javablog.domain.blog.PostId;
import com.javablog.domain.blog.PostNotFoundException;
import com.javablog.domain.blog.TranslationRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/posts")
public class TranslationResource implements TranslationsApi {

    private static final Logger LOGGER = LogManager.getLogger();
    private final TranslationService translationService;

    public TranslationResource(TranslationService translationService) {
        this.translationService = translationService;
    }

    @Override
    @PostMapping("/{postId}/translations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestTranslation(@PathVariable("postId") UUID postId, @RequestBody TranslationRequestBody body) {
        LOGGER.info("requestTranslation postId={} languages={}", postId, body.getLanguages());
        try {
            translationService.requestTranslation(toTranslationRequest(postId, body));
        } catch (PostNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private TranslationRequest toTranslationRequest(UUID postId, TranslationRequestBody body) {
        Set<Language> languages = body.getLanguages().stream()
                .map(this::toLanguage)
                .collect(Collectors.toSet());
        return new TranslationRequest(
                new PostId(postId),
                new Author("system"),
                new Languages(languages)
        );
    }

    private Language toLanguage(LanguageCode code) {
        return Language.fromCode(code.toString());
    }
}
