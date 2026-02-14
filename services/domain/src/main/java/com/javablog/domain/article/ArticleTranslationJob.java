package com.javablog.domain.article;

import com.javablog.domain.Guard;
import com.javablog.domain.Language;
import com.javablog.domain.blog.TranslationJobId;

public record ArticleTranslationJob(
        TranslationJobId id,
        ArticleId originalArticleId,
        Language language
) {

    public ArticleTranslationJob {
        Guard.againstNull(id, "ArticleTranslationJob.id");
        Guard.againstNull(originalArticleId, "ArticleTranslationJob.originalArticleId");
        Guard.againstNull(language, "ArticleTranslationJob.language");
    }
}
