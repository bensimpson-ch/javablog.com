package com.javablog.adapter.kafka;

public record TranslationResponseMessage(
        String jobId,
        String translatedContent,
        String sourceLang,
        String targetLang,
        String callbackId,
        String status,
        String error
) {
}
