package com.javablog.adapter.persistence.blog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

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

	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	protected CommentEntity() {
	}

	public CommentEntity(UUID commentId, PostEntity post, String content) {
		this.commentId = commentId;
		this.post = post;
		this.content = content;
	}

	public UUID getCommentId() {
		return commentId;
	}

	public PostEntity getPost() {
		return post;
	}

	public String getContent() {
		return content;
	}
}
