package com.javablog.adapter.rest;

import com.javablog.api.v1.ArticleTranslationsApi;
import com.javablog.api.v1.model.ArticleTranslationRequestBodyDto;
import com.javablog.api.v1.model.LanguageCodeDto;
import com.javablog.api.v1.model.TranslatedArticleDto;
import com.javablog.application.service.ArticleTranslationService;
import com.javablog.domain.article.ArticleId;
import com.javablog.domain.article.ArticleNotFoundException;
import com.javablog.domain.article.ArticleTranslationRequest;
import com.javablog.domain.article.TranslatedArticle;
import com.javablog.domain.Author;
import com.javablog.domain.Language;
import com.javablog.domain.Languages;
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
@RequestMapping("/v1/articles")
public class ArticleTranslationResource implements ArticleTranslationsApi {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ArticleTranslationService articleTranslationService;

    public ArticleTranslationResource(ArticleTranslationService articleTranslationService) {
        this.articleTranslationService = articleTranslationService;
    }

    @Override
    @PostMapping("/{articleId}/translations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestArticleTranslation(@PathVariable("articleId") UUID articleId, @RequestBody ArticleTranslationRequestBodyDto body) {
        LOGGER.info("requestArticleTranslation articleId={} languages={}", articleId, body.getLanguages());
        try {
            articleTranslationService.requestTranslation(toTranslationRequest(articleId, body));
        } catch (ArticleNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @GetMapping("/{articleId}/translations")
    public List<TranslatedArticleDto> listArticleTranslations(@PathVariable("articleId") UUID articleId) {
        return articleTranslationService.listTranslations(new ArticleId(articleId)).values().stream()
                .map(this::toTranslatedArticleDto)
                .toList();
    }

    private TranslatedArticleDto toTranslatedArticleDto(TranslatedArticle domain) {
        TranslatedArticleDto dto = new TranslatedArticleDto()
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

    private ArticleTranslationRequest toTranslationRequest(UUID articleId, ArticleTranslationRequestBodyDto body) {
        Set<Language> languages = body.getLanguages().stream()
                .map(this::toLanguage)
                .collect(Collectors.toSet());
        return new ArticleTranslationRequest(
                new ArticleId(articleId),
                new Author("system"),
                new Languages(languages)
        );
    }

    private Language toLanguage(LanguageCodeDto code) {
        return Language.fromCode(code.toString());
    }
}
