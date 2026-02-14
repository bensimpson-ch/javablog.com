package com.javablog.domain.article;

import com.javablog.domain.*;

public record Article(
        ArticleId id,
        Slug slug,
        Title title,
        Summary summary,
        Content content,
        Language language,
        CreatedAt createdAt
) {

    public Article {
        Guard.againstNull(id, "Article.id");
        Guard.againstNull(slug, "Article.slug");
        Guard.againstNull(title, "Article.title");
        Guard.againstNull(summary, "Article.summary");
        Guard.againstNull(content, "Article.content");
        Guard.againstNull(language, "Article.language");
        Guard.againstNull(createdAt, "Article.createdAt");
    }
}
