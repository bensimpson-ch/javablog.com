package com.javablog.adapter.persistence.article;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.UUID;

@Entity
@Table(name = "translated_articles", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"article_id", "language_code"})
})
@NamedQuery(name = TranslatedArticleEntity.FIND_BY_LANGUAGE,
		query = "SELECT t FROM TranslatedArticleEntity t WHERE t.languageCode = :languageCode")
public class TranslatedArticleEntity {

	public static final String FIND_BY_LANGUAGE = "TranslatedArticleEntity.findByLanguage";

	@Id
	@Column(name = "translated_article_id")
	private UUID translatedArticleId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "article_id", nullable = false)
	private ArticleEntity article;

	@Column(name = "language_code", length = 2, nullable = false)
	private String languageCode;

	@Column(name = "title", length = 255, nullable = false)
	private String title;

	@Column(name = "summary", length = 500, nullable = false)
	private String summary;

	@Column(name = "slug", length = 255, nullable = false)
	private String slug;

	@Column(name = "content", columnDefinition = "TEXT", nullable = false)
	private String content;

	protected TranslatedArticleEntity() {
	}

	public TranslatedArticleEntity(UUID translatedArticleId, ArticleEntity article, String languageCode, String title, String summary, String slug, String content) {
		this.translatedArticleId = translatedArticleId;
		this.article = article;
		this.languageCode = languageCode;
		this.title = title;
		this.summary = summary;
		this.slug = slug;
		this.content = content;
	}

	public UUID getTranslatedArticleId() {
		return translatedArticleId;
	}

	public ArticleEntity getArticle() {
		return article;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public String getTitle() {
		return title;
	}

	public String getSummary() {
		return summary;
	}

	public String getSlug() {
		return slug;
	}

	public String getContent() {
		return content;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
