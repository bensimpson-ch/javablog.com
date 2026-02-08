package com.javablog.adapter.kafka;

import com.javablog.domain.blog.Language;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.TranslationJobId;
import com.javablog.domain.blog.TranslationJobStatus;
import com.javablog.domain.blog.TranslationPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaTranslationAdapter implements TranslationPort {

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
                post.content().value(),
                post.language().code(),
                targetLanguage.code(),
                post.id().value().toString()
        );

        LOGGER.info("Sending translation request: jobId={} postId={} targetLang={}",
                jobId.value(), post.id().value(), targetLanguage.code());

        kafkaTemplate.send(TOPIC_TRANSLATION_REQUESTS, jobId.value().toString(), message);

        return jobId;
    }

    @Override
    public TranslationJobStatus translationJobStatus(TranslationJobId jobId) {
        LOGGER.info("Checking translation job status: jobId={}", jobId.value());
        // TODO: Query status from database or external service
        return TranslationJobStatus.NOT_STARTED;
    }
}
