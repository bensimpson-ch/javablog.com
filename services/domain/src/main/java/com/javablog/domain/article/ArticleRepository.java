package com.javablog.domain.article;

import com.javablog.domain.Language;
import com.javablog.domain.Slug;

import java.util.Optional;

public interface ArticleRepository {

    Article create(Article article);

    Article update(ArticleUpdate article);

    Optional<Article> findById(ArticleId id);

    Optional<Article> findBySlug(Slug slug);

    Articles list(Language language);
}
