package com.javablog.domain.blog;

import com.javablog.domain.Guard;

public record TranslationJob(
        TranslationJobId id,
        PostId originalPostId,
        Language language
) {

    public TranslationJob {
        Guard.againstNull(id, "TranslationJob.id");
        Guard.againstNull(originalPostId, "TranslationJob.originalPostId");
        Guard.againstNull(language, "TranslationJob.language");
    }
}
