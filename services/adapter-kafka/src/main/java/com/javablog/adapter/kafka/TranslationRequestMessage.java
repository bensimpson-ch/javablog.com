package com.javablog.adapter.kafka;

public record TranslationRequestMessage(
        String jobId,
        String title,
        String summary,
        String slug,
        String content,
        String sourceLang,
        String targetLang
) {
}
