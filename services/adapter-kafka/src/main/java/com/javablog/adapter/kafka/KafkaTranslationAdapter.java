package com.javablog.adapter.kafka;

import com.javablog.domain.article.Article;
import com.javablog.domain.article.ArticleTranslationPort;
import com.javablog.domain.Language;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.TranslationJobId;
import com.javablog.domain.blog.TranslationPort;
import services.simpson.translation.TranslationRequestMessage;
import services.simpson.translation.TranslationRequestMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaTranslationAdapter implements TranslationPort, ArticleTranslationPort {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String TOPIC_TRANSLATION_REQUESTS = "translation.requests";

    private final KafkaTemplate<String, TranslationRequestMessage> kafkaTemplate;

    public KafkaTranslationAdapter(KafkaTemplate<String, TranslationRequestMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public TranslationJobId translate(Post post, Language targetLanguage) {
        TranslationJobId jobId = TranslationJobId.generate();

        TranslationRequestMessage message = new TranslationRequestMessage(
                jobId.value().toString(),
                post.title().value(),
                post.summary().value(),
                post.slug().value(),
                post.content().value(),
                post.language().code(),
                targetLanguage.code()
        );

        LOGGER.info("Sending translation request: jobId={} postId={} targetLang={}",
                jobId.value(), post.id().value(), targetLanguage.code());

        kafkaTemplate.send(TOPIC_TRANSLATION_REQUESTS, jobId.value().toString(), message);

        return jobId;
    }

    @Override
    public TranslationJobId translate(Article article, Language targetLanguage) {
        TranslationJobId jobId = TranslationJobId.generate();

        TranslationRequestMessage message = new TranslationRequestMessage(
                jobId.value().toString(),
                article.title().value(),
                article.summary().value(),
                article.slug().value(),
                article.content().value(),
                article.language().code(),
                targetLanguage.code()
        );

        LOGGER.info("Sending article translation request: jobId={} articleId={} targetLang={}",
                jobId.value(), article.id().value(), targetLanguage.code());

        kafkaTemplate.send(TOPIC_TRANSLATION_REQUESTS, jobId.value().toString(), message);

        return jobId;
    }
}
