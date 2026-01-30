package com.javablog.adapter.persistence.blog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comments")
@NamedQuery(name = CommentEntity.FIND_BY_POST_ID, query = "SELECT c FROM CommentEntity c WHERE c.post.postId = :postId")
public class CommentEntity {

	public static final String FIND_BY_POST_ID = "CommentEntity.findByPostId";

	@Id
	@Column(name = "comment_id")
	private UUID commentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private PostEntity post;

	@Column(name = "author", length = 100)
	private String author;

	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	protected CommentEntity() {
	}

	public CommentEntity(UUID commentId, PostEntity post, String author, String content, LocalDateTime createdAt) {
		this.commentId = commentId;
		this.post = post;
		this.author = author;
		this.content = content;
		this.createdAt = createdAt;
	}

	public UUID getCommentId() {
		return commentId;
	}

	public PostEntity getPost() {
		return post;
	}

	public String getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
