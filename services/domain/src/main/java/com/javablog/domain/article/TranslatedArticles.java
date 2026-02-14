package com.javablog.domain.article;

import com.javablog.domain.Guard;

import java.util.Set;

public record TranslatedArticles(Set<TranslatedArticle> values) {

    public TranslatedArticles {
        Guard.againstNull(values, "TranslatedArticles.values");
    }
}
