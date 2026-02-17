package com.javablog.adapter.persistence.article;

import com.javablog.domain.Fixture;
import com.javablog.domain.article.Article;
import com.javablog.domain.Language;
import com.javablog.domain.blog.TranslationJobId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaArticleTranslationRepository.class, JpaArticleRepository.class})
@ActiveProfiles("test")
class JpaArticleTranslationRepositoryTest {

    @Autowired
    private JpaArticleTranslationRepository translationRepository;

    @Autowired
    private JpaArticleRepository articleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void translationJobRoundTrip() {
        // Create an article first
        Article article = Fixture.article("translation-test-slug", "Translation Test Article");
        articleRepository.create(article);

        // Flush to ensure article is persisted
        entityManager.flush();
        entityManager.clear();

        // Create translation job
        TranslationJobId jobId = Fixture.translationJobId();
        translationRepository.saveTranslationJob(jobId, article.id(), Language.DE);

        // Flush and clear to force database read
        entityManager.flush();
        entityManager.clear();

        // Verify job was saved
        ArticleTranslationJobEntity found = entityManager.find(ArticleTranslationJobEntity.class, jobId.value());
        assertThat(found).isNotNull();
        assertThat(found.getArticleTranslationJobId()).isEqualTo(jobId.value());
        assertThat(found.getOriginalArticleId()).isEqualTo(article.id().value());
        assertThat(found.getLanguageCode()).isEqualTo("de");
        assertThat(found.getCreatedAt()).isNotNull();
    }
}
