package com.javablog.adapter.persistence.blog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "articles")
@NamedQuery(name = ArticleEntity.FIND_ALL, query = "SELECT a FROM ArticleEntity a")
@NamedQuery(name = ArticleEntity.FIND_BY_SLUG, query = "SELECT a FROM ArticleEntity a WHERE a.slug = :slug")
public class ArticleEntity {

	public static final String FIND_ALL = "ArticleEntity.findAll";
	public static final String FIND_BY_SLUG = "ArticleEntity.findBySlug";

	@Id
	@Column(name = "article_id")
	private UUID articleId;

	@Column(name = "slug", length = 255, unique = true)
	private String slug;

	@Column(name = "title", length = 255)
	private String title;

	@Column(name = "summary", length = 500, nullable = false)
	private String summary;

	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "language_code", length = 2, nullable = false)
	private String languageCode;

	protected ArticleEntity() {
	}

	public ArticleEntity(UUID articleId, String slug, String title, String summary, String content, LocalDateTime createdAt, String languageCode) {
		this.articleId = articleId;
		this.slug = slug;
		this.title = title;
		this.summary = summary;
		this.content = content;
		this.createdAt = createdAt;
		this.languageCode = languageCode;
	}

	public UUID getArticleId() {
		return articleId;
	}

	public String getSlug() {
		return slug;
	}

	public String getTitle() {
		return title;
	}

	public String getSummary() {
		return summary;
	}

	public String getContent() {
		return content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
