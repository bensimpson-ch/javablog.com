package com.javablog.domain.article;

import com.javablog.domain.Guard;

import java.util.UUID;

public record ArticleId(UUID value) {

    public ArticleId {
        Guard.againstNull(value, "ArticleId.value");
    }

    public static ArticleId generate() {
        return new ArticleId(UUID.randomUUID());
    }
}
