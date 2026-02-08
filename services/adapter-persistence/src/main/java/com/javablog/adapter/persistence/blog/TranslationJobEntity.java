package com.javablog.adapter.persistence.blog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "translation_jobs", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"original_post_id", "language_code"})
})
@NamedQuery(
        name = TranslationJobEntity.FIND_BY_ORIGINAL_POST_ID,
        query = "SELECT t FROM TranslationJobEntity t WHERE t.originalPostId = :originalPostId"
)
public class TranslationJobEntity {

    public static final String FIND_BY_ORIGINAL_POST_ID = "TranslationJobEntity.findByOriginalPostId";

    @Id
    @Column(name = "translation_job_id")
    private UUID translationJobId;

    @Column(name = "original_post_id", nullable = false)
    private UUID originalPostId;

    @Column(name = "language_code", length = 2, nullable = false)
    private String languageCode;

    @Column(name = "slug")
    private String slug;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected TranslationJobEntity() {
    }

    public TranslationJobEntity(UUID translationJobId, UUID originalPostId, String languageCode, LocalDateTime createdAt) {
        this.translationJobId = translationJobId;
        this.originalPostId = originalPostId;
        this.languageCode = languageCode;
        this.createdAt = createdAt;
    }

    public UUID getTranslationJobId() {
        return translationJobId;
    }

    public UUID getOriginalPostId() {
        return originalPostId;
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
}
