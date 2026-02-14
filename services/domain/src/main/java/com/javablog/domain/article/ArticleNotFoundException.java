package com.javablog.domain.article;

public class ArticleNotFoundException extends RuntimeException {

    public ArticleNotFoundException(ArticleId articleId) {
        super("Article not found: " + articleId.value());
    }
}
