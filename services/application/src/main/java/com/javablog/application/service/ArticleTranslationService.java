package com.javablog.application.service;

import com.javablog.domain.Language;
import com.javablog.domain.article.*;
import com.javablog.domain.blog.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
public class ArticleTranslationService {

    private static final Logger LOGGER = LogManager.getLogger();

    private final ArticleRepository articleRepository;
    private final ArticleTranslationPort articleTranslationPort;
    private final ArticleTranslationRepository articleTranslationRepository;

    public ArticleTranslationService(ArticleRepository articleRepository,
                                      ArticleTranslationPort articleTranslationPort,
                                      ArticleTranslationRepository articleTranslationRepository) {
        this.articleRepository = articleRepository;
        this.articleTranslationPort = articleTranslationPort;
        this.articleTranslationRepository = articleTranslationRepository;
    }

    public TranslatedArticles listTranslations(ArticleId articleId) {
        return articleTranslationRepository.findTranslations(articleId);
    }

    public void requestTranslation(ArticleTranslationRequest request) {
        LOGGER.info("requestTranslation articleId={} languages={}", request.articleId().value(), request.languages().values());

        Article article = articleRepository.findById(request.articleId())
                .orElseThrow(() -> new ArticleNotFoundException(request.articleId()));

        for (Language language : request.languages().values()) {
            TranslationJobId translationJobId = articleTranslationPort.translate(article, language);
            LOGGER.info("Translation sent, saving job jobId={} articleId={} language={}", translationJobId.value(), article.id().value(), language.code());
            articleTranslationRepository.saveTranslationJob(translationJobId, article.id(), language);
            LOGGER.info("Translation job saved jobId={}", translationJobId.value());
        }
    }

    @EventListener
    public void onTranslationCompleted(TranslationCompletedEvent event) {
        LOGGER.info("Article translation check: jobId={}", event.jobId().value());

        var job = articleTranslationRepository.findTranslationJob(event.jobId());
        if (job.isEmpty()) {
            return;
        }

        ArticleTranslationJob translationJob = job.get();

        articleTranslationRepository.saveTranslatedArticle(
                translationJob.originalArticleId(),
                translationJob.language(),
                event.title(),
                event.summary(),
                event.slug(),
                event.content()
        );

        articleTranslationRepository.completeTranslationJob(event.jobId());

        LOGGER.info("Article translation saved and job completed: jobId={} articleId={} language={}",
                event.jobId().value(), translationJob.originalArticleId().value(), translationJob.language().code());
    }
}
