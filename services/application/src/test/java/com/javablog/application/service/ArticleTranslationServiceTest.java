package com.javablog.application.service;

import com.javablog.domain.*;
import com.javablog.domain.article.*;
import com.javablog.domain.blog.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ArticleTranslationServiceTest {

    private ArticleRepository articleRepository;
    private ArticleTranslationPort articleTranslationPort;
    private ArticleTranslationRepository articleTranslationRepository;
    private ArticleTranslationService service;

    @BeforeEach
    void setUp() {
        articleRepository = mock(ArticleRepository.class);
        articleTranslationPort = mock(ArticleTranslationPort.class);
        articleTranslationRepository = mock(ArticleTranslationRepository.class);
        service = new ArticleTranslationService(articleRepository, articleTranslationPort, articleTranslationRepository);
    }

    @Test
    void requestTranslationTranslatesAndSavesEachLanguage() {
        Article article = Fixture.article();
        TranslationJobId jobIdDe = Fixture.translationJobId();
        TranslationJobId jobIdFr = Fixture.translationJobId();
        ArticleTranslationRequest request = Fixture.articleTranslationRequest(article.id(), Set.of(Language.DE, Language.FR));

        when(articleRepository.findById(article.id())).thenReturn(Optional.of(article));
        when(articleTranslationPort.translate(article, Language.DE)).thenReturn(jobIdDe);
        when(articleTranslationPort.translate(article, Language.FR)).thenReturn(jobIdFr);

        service.requestTranslation(request);

        verify(articleTranslationPort).translate(article, Language.DE);
        verify(articleTranslationPort).translate(article, Language.FR);
        verify(articleTranslationRepository).saveTranslationJob(jobIdDe, article.id(), Language.DE);
        verify(articleTranslationRepository).saveTranslationJob(jobIdFr, article.id(), Language.FR);
    }

    @Test
    void requestTranslationThrowsWhenArticleNotFound() {
        ArticleTranslationRequest request = Fixture.articleTranslationRequest();
        when(articleRepository.findById(request.articleId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.requestTranslation(request))
                .isInstanceOf(ArticleNotFoundException.class);

        verify(articleTranslationPort, never()).translate(any(Article.class), any());
        verify(articleTranslationRepository, never()).saveTranslationJob(any(), any(), any());
    }

    @Test
    void onTranslationCompletedSavesTranslatedArticleAndCompletesJob() {
        TranslationJobId jobId = Fixture.translationJobId();
        ArticleId articleId = Fixture.articleId();
        ArticleTranslationJob job = new ArticleTranslationJob(jobId, articleId, Language.DE);

        Title title = new Title("Translated Title");
        Summary summary = new Summary("Translated Summary");
        Slug slug = new Slug("translated-slug");
        Content content = new Content("Translated content");

        TranslationCompletedEvent event = new TranslationCompletedEvent(
                jobId, title, summary, slug, content
        );

        when(articleTranslationRepository.findTranslationJob(jobId)).thenReturn(Optional.of(job));

        service.onTranslationCompleted(event);

        verify(articleTranslationRepository).saveTranslatedArticle(articleId, Language.DE, title, summary, slug, content);
        verify(articleTranslationRepository).completeTranslationJob(jobId);
    }

    @Test
    void onTranslationCompletedIgnoresUnknownJob() {
        TranslationJobId jobId = Fixture.translationJobId();

        TranslationCompletedEvent event = new TranslationCompletedEvent(
                jobId,
                new Title("Translated Title"),
                new Summary("Translated Summary"),
                new Slug("translated-slug"),
                new Content("Translated content")
        );

        when(articleTranslationRepository.findTranslationJob(jobId)).thenReturn(Optional.empty());

        service.onTranslationCompleted(event);

        verify(articleTranslationRepository, never()).saveTranslatedArticle(any(), any(), any(), any(), any(), any());
        verify(articleTranslationRepository, never()).completeTranslationJob(any());
    }
}
