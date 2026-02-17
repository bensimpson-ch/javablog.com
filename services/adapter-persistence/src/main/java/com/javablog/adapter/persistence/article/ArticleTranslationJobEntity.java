package com.javablog.adapter.persistence.article;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "article_translation_jobs", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"original_article_id", "language_code"})
})
@NamedQuery(
        name = ArticleTranslationJobEntity.FIND_BY_ORIGINAL_ARTICLE_ID,
        query = "SELECT t FROM ArticleTranslationJobEntity t WHERE t.originalArticleId = :originalArticleId"
)
@NamedQuery(
        name = ArticleTranslationJobEntity.EXISTS_BY_ORIGINAL_ARTICLE_ID_AND_LANGUAGE,
        query = "SELECT COUNT(t) FROM ArticleTranslationJobEntity t WHERE t.originalArticleId = :originalArticleId AND t.languageCode = :languageCode"
)
public class ArticleTranslationJobEntity {

    public static final String FIND_BY_ORIGINAL_ARTICLE_ID = "ArticleTranslationJobEntity.findByOriginalArticleId";
    public static final String EXISTS_BY_ORIGINAL_ARTICLE_ID_AND_LANGUAGE = "ArticleTranslationJobEntity.existsByOriginalArticleIdAndLanguage";

    @Id
    @Column(name = "article_translation_job_id")
    private UUID articleTranslationJobId;

    @Column(name = "original_article_id", nullable = false)
    private UUID originalArticleId;

    @Column(name = "language_code", length = 2, nullable = false)
    private String languageCode;

    @Column(name = "slug")
    private String slug;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    protected ArticleTranslationJobEntity() {
    }

    public ArticleTranslationJobEntity(UUID articleTranslationJobId, UUID originalArticleId, String languageCode, LocalDateTime createdAt) {
        this.articleTranslationJobId = articleTranslationJobId;
        this.originalArticleId = originalArticleId;
        this.languageCode = languageCode;
        this.createdAt = createdAt;
    }

    public UUID getArticleTranslationJobId() {
        return articleTranslationJobId;
    }

    public UUID getOriginalArticleId() {
        return originalArticleId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getSlug() {
        return slug;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
