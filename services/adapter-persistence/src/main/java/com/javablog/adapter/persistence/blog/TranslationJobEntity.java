package com.javablog.adapter.persistence.blog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "translation_jobs", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "language_code"})
})
public class TranslationJobEntity {

    @Id
    @Column(name = "translation_job_id")
    private UUID translationJobId;

    @Column(name = "post_id", nullable = false)
    private UUID postId;

    @Column(name = "language_code", length = 2, nullable = false)
    private String languageCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected TranslationJobEntity() {
    }

    public TranslationJobEntity(UUID translationJobId, UUID postId, String languageCode, LocalDateTime createdAt) {
        this.translationJobId = translationJobId;
        this.postId = postId;
        this.languageCode = languageCode;
        this.createdAt = createdAt;
    }

    public UUID getTranslationJobId() {
        return translationJobId;
    }

    public UUID getPostId() {
        return postId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
