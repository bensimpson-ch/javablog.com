package com.javablog.adapter.rest;

import com.javablog.api.v1.TranslationsApi;
import com.javablog.api.v1.model.LanguageCodeDto;
import com.javablog.api.v1.model.TranslatedPostDto;
import com.javablog.api.v1.model.TranslationRequestBodyDto;
import com.javablog.application.service.TranslationService;
import com.javablog.domain.Author;
import com.javablog.domain.Language;
import com.javablog.domain.Languages;
import com.javablog.domain.blog.PostId;
import com.javablog.domain.blog.PostNotFoundException;
import com.javablog.domain.blog.TranslationRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
    public void requestTranslation(@PathVariable("postId") UUID postId, @RequestBody TranslationRequestBodyDto body) {
        LOGGER.info("requestTranslation postId={} languages={}", postId, body.getLanguages());
        try {
            translationService.requestTranslation(toTranslationRequest(postId, body));
        } catch (PostNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @GetMapping("/{postId}/translations")
    public List<TranslatedPostDto> listTranslations(@PathVariable("postId") UUID postId) {
        return translationService.listTranslations(new PostId(postId)).values().stream()
                .map(this::toTranslatedPostDto)
                .toList();
    }

    private TranslatedPostDto toTranslatedPostDto(com.javablog.domain.blog.TranslatedPost domain) {
        TranslatedPostDto dto = new TranslatedPostDto()
                .orderedDatetime(domain.createdDate().value().atOffset(java.time.ZoneOffset.UTC))
                .language(LanguageCodeDto.fromValue(domain.language().code()));
        if (domain.completedDate() != null) {
            dto.completedDatetime(domain.completedDate().atOffset(java.time.ZoneOffset.UTC));
        }
        if (domain.title() != null) {
            dto.title(domain.title().value());
        }
        if (domain.slug() != null) {
            dto.slug(domain.slug().value());
        }
        return dto;
    }

    private TranslationRequest toTranslationRequest(UUID postId, TranslationRequestBodyDto body) {
        Set<Language> languages = body.getLanguages().stream()
                .map(this::toLanguage)
                .collect(Collectors.toSet());
        return new TranslationRequest(
                new PostId(postId),
                new Author("system"),
                new Languages(languages)
        );
    }

    private Language toLanguage(LanguageCodeDto code) {
        return Language.fromCode(code.toString());
    }
}
