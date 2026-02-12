package com.javablog.adapter.kafka;

public record TranslationResponseMessage(
        String jobId,
        String title,
        String summary,
        String slug,
        String content
) {
}
