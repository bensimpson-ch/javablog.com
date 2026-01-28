package com.javablog.adapter.persistence.blog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "posts")
@NamedQuery(name = PostEntity.FIND_ALL, query = "SELECT p FROM PostEntity p")
public class PostEntity {

	public static final String FIND_ALL = "PostEntity.findAll";

	@Id
	@Column(name = "post_id")
	private UUID postId;

	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	protected PostEntity() {
	}

	public PostEntity(UUID postId, String content) {
		this.postId = postId;
		this.content = content;
	}

	public UUID getPostId() {
		return postId;
	}

	public String getContent() {
		return content;
	}
}
