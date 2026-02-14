package com.javablog.domain.blog;

import com.javablog.domain.*;

public record TranslationCompletedEvent(
        TranslationJobId jobId,
        Title title,
        Summary summary,
        Slug slug,
        Content content
) {

    public TranslationCompletedEvent {
        Guard.againstNull(jobId, "TranslationCompletedEvent.jobId");
        Guard.againstNull(title, "TranslationCompletedEvent.title");
        Guard.againstNull(summary, "TranslationCompletedEvent.summary");
        Guard.againstNull(slug, "TranslationCompletedEvent.slug");
        Guard.againstNull(content, "TranslationCompletedEvent.content");
    }
}
