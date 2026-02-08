package com.javablog.application.service;

import com.javablog.domain.blog.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
            TranslationJobId translationJobId = translationPort.translate(post, language);
            LOGGER.info("Translation sent, saving job jobId={} postId={} language={}", translationJobId.value(), post.id().value(), language.code());
            translationRepository.saveTranslationJob(translationJobId, post.id(), language);
            LOGGER.info("Translation job saved jobId={}", translationJobId.value());
        }
    }
}
