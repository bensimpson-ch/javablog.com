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
public class PostEntity {

	public static final String FIND_ALL = "PostEntity.findAll";

	@Id
	@Column(name = "post_id")
	private UUID postId;

	@Column(name = "slug", length = 255, unique = true)
	private String slug;

	@Column(name = "title", length = 255)
	private String title;

	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	protected PostEntity() {
	}

	public PostEntity(UUID postId, String slug, String title, String content, LocalDateTime createdAt) {
		this.postId = postId;
		this.slug = slug;
		this.title = title;
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

	public String getContent() {
		return content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
