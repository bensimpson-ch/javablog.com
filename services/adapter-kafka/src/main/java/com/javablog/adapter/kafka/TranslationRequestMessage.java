package com.javablog.adapter.kafka;

public record TranslationRequestMessage(
        String jobId,
        String content,
        String sourceLang,
        String targetLang,
        String callbackId
) {
}
