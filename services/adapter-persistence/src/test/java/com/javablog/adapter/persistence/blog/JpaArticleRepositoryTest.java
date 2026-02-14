package com.javablog.adapter.persistence.blog;

import com.javablog.domain.*;
import com.javablog.domain.article.Article;
import com.javablog.domain.article.Articles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaArticleRepository.class)
@ActiveProfiles("test")
class JpaArticleRepositoryTest {

    @Autowired
    private JpaArticleRepository repository;

    @Test
    void articleRoundTrip() {
        // Create
        Article article = Fixture.article("test-article-slug", "Test Article Title");
        repository.create(article);

        // Read by ID
        Article found = repository.findById(article.id()).orElseThrow();
        assertThat(found.id()).isEqualTo(article.id());
        assertThat(found.slug()).isEqualTo(article.slug());
        assertThat(found.title()).isEqualTo(article.title());
        assertThat(found.summary()).isEqualTo(article.summary());
        assertThat(found.content()).isEqualTo(article.content());

        // Read by slug
        Article foundBySlug = repository.findBySlug(article.slug()).orElseThrow();
        assertThat(foundBySlug.id()).isEqualTo(article.id());

        // List articles includes new article
        Articles articles = repository.list(Language.EN);
        assertThat(articles.values()).hasSize(1);
        assertThat(articles.values()).anyMatch(a -> a.id().equals(article.id()));

        // Update
        Article updated = new Article(
                article.id(),
                new Slug("updated-article-slug"),
                new Title("Updated Article Title"),
                new Summary("Updated summary"),
                new Content("Updated content"),
                Language.DE,
                article.createdAt()
        );
        repository.update(updated);

        Article afterUpdate = repository.findById(article.id()).orElseThrow();
        assertThat(afterUpdate.slug().value()).isEqualTo("updated-article-slug");
        assertThat(afterUpdate.title().value()).isEqualTo("Updated Article Title");
    }
}
