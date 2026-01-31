package com.javablog.adapter.persistence.blog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "posts")
@NamedQuery(name = PostEntity.FIND_ALL, query = "SELECT p FROM PostEntity p")
@NamedQuery(name = PostEntity.FIND_BY_SLUG, query = "SELECT p FROM PostEntity p WHERE p.slug = :slug")
public class PostEntity {

	public static final String FIND_ALL = "PostEntity.findAll";
	public static final String FIND_BY_SLUG = "PostEntity.findBySlug";

	@Id
	@Column(name = "post_id")
	private UUID postId;

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

	protected PostEntity() {
	}

	public PostEntity(UUID postId, String slug, String title, String summary, String content, LocalDateTime createdAt) {
		this.postId = postId;
		this.slug = slug;
		this.title = title;
		this.summary = summary;
		this.content = content;
		this.createdAt = createdAt;
	}

	public UUID getPostId() {
		return postId;
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
