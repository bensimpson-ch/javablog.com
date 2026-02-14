package com.javablog.domain.article;

import com.javablog.domain.CreatedAt;
import com.javablog.domain.Guard;
import com.javablog.domain.Language;
import com.javablog.domain.Slug;
import com.javablog.domain.Title;

import java.time.LocalDateTime;

public record TranslatedArticle(
        ArticleId articleId,
        ArticleId originalArticleId,
        Language language,
        Title title,
        Slug slug,
        CreatedAt createdDate,
        LocalDateTime completedDate
) {

    public TranslatedArticle {
        Guard.againstNull(articleId, "TranslatedArticle.articleId");
        Guard.againstNull(originalArticleId, "TranslatedArticle.originalArticleId");
        Guard.againstNull(language, "TranslatedArticle.language");
        Guard.againstNull(createdDate, "TranslatedArticle.createdDate");
    }
}
