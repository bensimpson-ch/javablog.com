package com.javablog.adapter.rest;

import com.javablog.api.v1.model.ArticleTranslationRequestBodyDto;
import com.javablog.api.v1.model.LanguageCodeDto;
import com.javablog.application.service.ArticleTranslationService;
import com.javablog.domain.Fixture;
import com.javablog.domain.article.ArticleNotFoundException;
import com.javablog.domain.article.ArticleTranslationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ArticleTranslationResourceTest {

    private ArticleTranslationService articleTranslationService;
    private ArticleTranslationResource resource;

    @BeforeEach
    void setUp() {
        articleTranslationService = mock(ArticleTranslationService.class);
        resource = new ArticleTranslationResource(articleTranslationService);
    }

    @Test
    void requestTranslationDelegatesToService() {
        var articleId = Fixture.articleId();
        var body = new ArticleTranslationRequestBodyDto().languages(List.of(LanguageCodeDto.DE, LanguageCodeDto.FR));

        resource.requestArticleTranslation(articleId.value(), body);

        ArgumentCaptor<ArticleTranslationRequest> captor = ArgumentCaptor.forClass(ArticleTranslationRequest.class);
        verify(articleTranslationService).requestTranslation(captor.capture());

        ArticleTranslationRequest request = captor.getValue();
        assertThat(request.articleId()).isEqualTo(articleId);
        assertThat(request.languages().values()).hasSize(2);
    }

    @Test
    void requestTranslationThrowsNotFoundWhenArticleDoesNotExist() {
        var articleId = Fixture.articleId();
        var body = new ArticleTranslationRequestBodyDto().languages(List.of(LanguageCodeDto.DE));

        doThrow(new ArticleNotFoundException(articleId)).when(articleTranslationService).requestTranslation(any());

        assertThatThrownBy(() -> resource.requestArticleTranslation(articleId.value(), body))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }
}
