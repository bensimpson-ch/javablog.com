package com.javablog.adapter.rest;

import com.javablog.api.v1.model.LanguageCode;
import com.javablog.api.v1.model.TranslationRequestBody;
import com.javablog.application.service.TranslationService;
import com.javablog.domain.Fixture;
import com.javablog.domain.blog.PostNotFoundException;
import com.javablog.domain.blog.TranslationRequest;
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

class TranslationResourceTest {

    private TranslationService translationService;
    private TranslationResource resource;

    @BeforeEach
    void setUp() {
        translationService = mock(TranslationService.class);
        resource = new TranslationResource(translationService);
    }

    @Test
    void requestTranslationDelegatesToService() {
        var postId = Fixture.postId();
        var body = new TranslationRequestBody().languages(List.of(LanguageCode.DE, LanguageCode.FR));

        resource.requestTranslation(postId.value(), body);

        ArgumentCaptor<TranslationRequest> captor = ArgumentCaptor.forClass(TranslationRequest.class);
        verify(translationService).requestTranslation(captor.capture());

        TranslationRequest request = captor.getValue();
        assertThat(request.postId()).isEqualTo(postId);
        assertThat(request.languages().values()).hasSize(2);
    }

    @Test
    void requestTranslationThrowsNotFoundWhenPostDoesNotExist() {
        var postId = Fixture.postId();
        var body = new TranslationRequestBody().languages(List.of(LanguageCode.DE));

        doThrow(new PostNotFoundException(postId)).when(translationService).requestTranslation(any());

        assertThatThrownBy(() -> resource.requestTranslation(postId.value(), body))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }
}
