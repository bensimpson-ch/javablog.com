package com.javablog.adapter.persistence.article;

import com.javablog.domain.*;
import com.javablog.domain.article.Article;
import com.javablog.domain.article.ArticleId;

class ArticleMapper {



    Article map(ArticleEntity entity) {
        return new Article(
                new ArticleId(entity.getArticleId()),
                new Slug(entity.getSlug()),
                new Title(entity.getTitle()),
                new Summary(entity.getSummary()),
                new Content(entity.getContent()),
                Language.fromCode(entity.getLanguageCode()),
                new CreatedAt(entity.getCreatedAt())
        );
    }

    Article map(TranslatedArticleEntity entity) {
        ArticleEntity article = entity.getArticle();
        return new Article(
                new ArticleId(article.getArticleId()),
                new Slug(article.getSlug()),
                new Title(entity.getTitle()),
                new Summary(entity.getSummary()),
                new Content(entity.getContent()),
                Language.fromCode(entity.getLanguageCode()),
                new CreatedAt(article.getCreatedAt())
        );
    }

    ArticleEntity map(Article article) {
        return new ArticleEntity(
                article.id().value(),
                article.slug().value(),
                article.title().value(),
                article.summary().value(),
                article.content().value(),
                article.createdAt().value(),
                article.language().code()
        );
    }
}
