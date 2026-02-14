package com.javablog.adapter.persistence.blog;

import com.javablog.domain.*;
import com.javablog.domain.article.*;
import com.javablog.domain.blog.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Transactional
public class JpaArticleTranslationRepository implements ArticleTranslationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean translationJobExists(ArticleId articleId, Language language) {
        Long count = entityManager
                .createNamedQuery(ArticleTranslationJobEntity.EXISTS_BY_ORIGINAL_ARTICLE_ID_AND_LANGUAGE, Long.class)
                .setParameter("originalArticleId", articleId.value())
                .setParameter("languageCode", language.code())
                .getSingleResult();
        return count > 0;
    }

    @Override
    public void saveTranslationJob(TranslationJobId jobId, ArticleId articleId, Language language) {
        ArticleTranslationJobEntity entity = new ArticleTranslationJobEntity(
                jobId.value(),
                articleId.value(),
                language.code(),
                LocalDateTime.now()
        );
        entityManager.persist(entity);
    }

    @Override
    public Optional<ArticleTranslationJob> findTranslationJob(TranslationJobId jobId) {
        ArticleTranslationJobEntity entity = entityManager.find(ArticleTranslationJobEntity.class, jobId.value());
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(new ArticleTranslationJob(
                new TranslationJobId(entity.getArticleTranslationJobId()),
                new ArticleId(entity.getOriginalArticleId()),
                Language.fromCode(entity.getLanguageCode())
        ));
    }

    @Override
    public void saveTranslatedArticle(ArticleId originalArticleId, Language language, Title title, Summary summary, Slug slug, Content content) {
        ArticleEntity articleEntity = entityManager.getReference(ArticleEntity.class, originalArticleId.value());
        TranslatedArticleEntity entity = new TranslatedArticleEntity(
                UUID.randomUUID(),
                articleEntity,
                language.code(),
                title.value(),
                summary.value(),
                content.value()
        );
        entityManager.persist(entity);
    }

    @Override
    public void completeTranslationJob(TranslationJobId jobId) {
        ArticleTranslationJobEntity entity = entityManager.find(ArticleTranslationJobEntity.class, jobId.value());
        if (entity != null) {
            entity.setCompletedAt(LocalDateTime.now());
        }
    }

    @Override
    public TranslatedArticles findTranslations(ArticleId originalArticleId) {
        List<ArticleTranslationJobEntity> entities = entityManager
                .createNamedQuery(ArticleTranslationJobEntity.FIND_BY_ORIGINAL_ARTICLE_ID, ArticleTranslationJobEntity.class)
                .setParameter("originalArticleId", originalArticleId.value())
                .getResultList();

        Set<TranslatedArticle> articles = entities.stream()
                .map(e -> new TranslatedArticle(
                        new ArticleId(e.getArticleTranslationJobId()),
                        originalArticleId,
                        Language.fromCode(e.getLanguageCode()),
                        null,
                        e.getSlug() != null ? new Slug(e.getSlug()) : null,
                        new CreatedAt(e.getCreatedAt()),
                        null
                ))
                .collect(Collectors.toSet());

        return new TranslatedArticles(articles);
    }
}
