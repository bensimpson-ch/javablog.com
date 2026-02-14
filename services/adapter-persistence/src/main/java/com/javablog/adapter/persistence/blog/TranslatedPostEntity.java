package com.javablog.adapter.persistence.blog;

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
@Table(name = "translated_posts", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"post_id", "language_code"})
})
@NamedQuery(name = TranslatedPostEntity.FIND_BY_LANGUAGE,
		query = "SELECT t FROM TranslatedPostEntity t WHERE t.languageCode = :languageCode")
@NamedQuery(name = TranslatedPostEntity.FIND_BY_SLUG_AND_LANGUAGE,
		query = "SELECT t FROM TranslatedPostEntity t WHERE t.post.slug = :slug AND t.languageCode = :languageCode")
public class TranslatedPostEntity {

	public static final String FIND_BY_LANGUAGE = "TranslatedPostEntity.findByLanguage";
	public static final String FIND_BY_SLUG_AND_LANGUAGE = "TranslatedPostEntity.findBySlugAndLanguage";

	@Id
	@Column(name = "translated_post_id")
	private UUID translatedPostId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private PostEntity post;

	@Column(name = "language_code", length = 2, nullable = false)
	private String languageCode;

	@Column(name = "title", length = 255, nullable = false)
	private String title;

	@Column(name = "summary", length = 500, nullable = false)
	private String summary;

	@Column(name = "content", columnDefinition = "TEXT", nullable = false)
	private String content;

	protected TranslatedPostEntity() {
	}

	public TranslatedPostEntity(UUID translatedPostId, PostEntity post, String languageCode, String title, String summary, String content) {
		this.translatedPostId = translatedPostId;
		this.post = post;
		this.languageCode = languageCode;
		this.title = title;
		this.summary = summary;
		this.content = content;
	}

	public UUID getTranslatedPostId() {
		return translatedPostId;
	}

	public PostEntity getPost() {
		return post;
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

	public String getContent() {
		return content;
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