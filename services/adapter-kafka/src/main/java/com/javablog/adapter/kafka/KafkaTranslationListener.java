package com.javablog.adapter.kafka;

import com.javablog.domain.blog.Content;
import com.javablog.domain.blog.Slug;
import com.javablog.domain.blog.Summary;
import com.javablog.domain.blog.Title;
import com.javablog.domain.blog.TranslationCompletedEvent;
import com.javablog.domain.blog.TranslationJobId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KafkaTranslationListener {

    private static final Logger LOGGER = LogManager.getLogger();

    private final ApplicationEventPublisher eventPublisher;

    public KafkaTranslationListener(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @KafkaListener(topics = "translation.responses", groupId = "javablog-translation")
    public void onTranslationResponse(TranslationResponseMessage message) {
        LOGGER.info("Received translation response: jobId={}", message.jobId());

        TranslationCompletedEvent event = new TranslationCompletedEvent(
                new TranslationJobId(UUID.fromString(message.jobId())),
                new Title(message.title()),
                new Summary(message.summary()),
                new Slug(message.slug()),
                new Content(message.content())
        );

        eventPublisher.publishEvent(event);
    }
}
