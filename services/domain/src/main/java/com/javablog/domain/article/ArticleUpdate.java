package com.javablog.domain.article;

import com.javablog.domain.*;

public record ArticleUpdate(
        ArticleId id,
        Slug slug,
        Title title,
        Summary summary,
        Content content,
        Language language
) {

    public ArticleUpdate {
        Guard.againstNull(id, "ArticleUpdate.id");
        Guard.againstNull(slug, "ArticleUpdate.slug");
        Guard.againstNull(title, "ArticleUpdate.title");
        Guard.againstNull(summary, "ArticleUpdate.summary");
        Guard.againstNull(content, "ArticleUpdate.content");
        Guard.againstNull(language, "ArticleUpdate.language");
    }
}