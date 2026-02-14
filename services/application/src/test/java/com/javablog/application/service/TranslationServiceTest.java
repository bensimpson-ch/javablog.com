package com.javablog.application.service;

import com.javablog.domain.*;
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

class TranslationServiceTest {

    private BlogRepository blogRepository;
    private TranslationPort translationPort;
    private TranslationRepository translationRepository;
    private TranslationService service;

    @BeforeEach
    void setUp() {
        blogRepository = mock(BlogRepository.class);
        translationPort = mock(TranslationPort.class);
        translationRepository = mock(TranslationRepository.class);
        service = new TranslationService(blogRepository, translationPort, translationRepository);
    }

    @Test
    void requestTranslationTranslatesAndSavesEachLanguage() {
        Post post = Fixture.post();
        TranslationJobId jobIdDe = Fixture.translationJobId();
        TranslationJobId jobIdFr = Fixture.translationJobId();
        TranslationRequest request = Fixture.translationRequest(post.id(), Set.of(Language.DE, Language.FR));

        when(blogRepository.findPostById(post.id())).thenReturn(Optional.of(post));
        when(translationPort.translate(post, Language.DE)).thenReturn(jobIdDe);
        when(translationPort.translate(post, Language.FR)).thenReturn(jobIdFr);

        service.requestTranslation(request);

        verify(translationPort).translate(post, Language.DE);
        verify(translationPort).translate(post, Language.FR);
        verify(translationRepository).saveTranslationJob(jobIdDe, post.id(), Language.DE);
        verify(translationRepository).saveTranslationJob(jobIdFr, post.id(), Language.FR);
    }

    @Test
    void requestTranslationThrowsWhenPostNotFound() {
        TranslationRequest request = Fixture.translationRequest();
        when(blogRepository.findPostById(request.postId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.requestTranslation(request))
                .isInstanceOf(PostNotFoundException.class);

        verify(translationPort, never()).translate(any(Post.class), any());
        verify(translationRepository, never()).saveTranslationJob(any(), any(), any());
    }

    @Test
    void onTranslationCompletedSavesTranslatedPostAndDeletesJob() {
        TranslationJobId jobId = Fixture.translationJobId();
        PostId postId = Fixture.postId();
        TranslationJob job = new TranslationJob(jobId, postId, Language.DE);

        Title title = new Title("Translated Title");
        Summary summary = new Summary("Translated Summary");
        Slug slug = new Slug("translated-slug");
        Content content = new Content("Translated content");

        TranslationCompletedEvent event = new TranslationCompletedEvent(
                jobId, title, summary, slug, content
        );

        when(translationRepository.findTranslationJob(jobId)).thenReturn(Optional.of(job));

        service.onTranslationCompleted(event);

        verify(translationRepository).saveTranslatedPost(postId, Language.DE, title, summary, slug, content);
        verify(translationRepository).deleteTranslationJob(jobId);
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

        when(translationRepository.findTranslationJob(jobId)).thenReturn(Optional.empty());

        service.onTranslationCompleted(event);

        verify(translationRepository, never()).saveTranslatedPost(any(), any(), any(), any(), any(), any());
        verify(translationRepository, never()).deleteTranslationJob(any());
    }
}
