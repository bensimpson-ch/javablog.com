package com.javablog.application.service;

import com.javablog.domain.Language;
import com.javablog.domain.blog.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
public class TranslationService {

    private static final Logger LOGGER = LogManager.getLogger();

    private final BlogRepository blogRepository;
    private final TranslationPort translationPort;
    private final TranslationRepository translationRepository;

    public TranslationService(BlogRepository blogRepository,
                               TranslationPort translationPort,
                               TranslationRepository translationRepository) {
        this.blogRepository = blogRepository;
        this.translationPort = translationPort;
        this.translationRepository = translationRepository;
    }

    public TranslatedPosts listTranslations(PostId postId) {
        return translationRepository.findTranslations(postId);
    }

    public void requestTranslation(TranslationRequest request) {
        LOGGER.info("requestTranslation postId={} languages={}", request.postId().value(), request.languages().values());

        Post post = blogRepository.findPostById(request.postId())
                .orElseThrow(() -> new PostNotFoundException(request.postId()));

        for (Language language : request.languages().values()) {
            if (translationRepository.translationJobExists(post.id(), language)) {
                LOGGER.info("Translation job already exists postId={} language={}, skipping", post.id().value(), language.code());
                continue;
            }
            TranslationJobId translationJobId = translationPort.translate(post, language);
            LOGGER.info("Translation sent, saving job jobId={} postId={} language={}", translationJobId.value(), post.id().value(), language.code());
            translationRepository.saveTranslationJob(translationJobId, post.id(), language);
            LOGGER.info("Translation job saved jobId={}", translationJobId.value());
        }
    }

    @EventListener
    public void onTranslationCompleted(TranslationCompletedEvent event) {
        LOGGER.info("Post translation check: jobId={}", event.jobId().value());

        var job = translationRepository.findTranslationJob(event.jobId());
        if (job.isEmpty()) {
            return;
        }

        TranslationJob translationJob = job.get();

        translationRepository.saveTranslatedPost(
                translationJob.originalPostId(),
                translationJob.language(),
                event.title(),
                event.summary(),
                event.slug(),
                event.content()
        );

        translationRepository.completeTranslationJob(event.jobId());

        LOGGER.info("Translation saved and job completed: jobId={} postId={} language={}",
                event.jobId().value(), translationJob.originalPostId().value(), translationJob.language().code());
    }
}
