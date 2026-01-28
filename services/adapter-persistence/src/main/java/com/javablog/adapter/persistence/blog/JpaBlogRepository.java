package com.javablog.adapter.persistence.blog;

import com.javablog.domain.blog.BlogRepository;
import com.javablog.domain.blog.Comment;
import com.javablog.domain.blog.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaBlogRepository implements BlogRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Post> listPosts() {
		return entityManager.createNamedQuery(PostEntity.FIND_ALL, PostEntity.class)
				.getResultList()
				.stream()
				.map(this::toDomain)
				.toList();
	}

	@Override
	public List<Comment> listComments(Post post) {
		return entityManager.createNamedQuery(CommentEntity.FIND_BY_POST_ID, CommentEntity.class)
				.setParameter("postId", post.id())
				.getResultList()
				.stream()
				.map(this::toDomain)
				.toList();
	}

	private Post toDomain(PostEntity entity) {
		return new Post(entity.getPostId(), entity.getContent());
	}

	private Comment toDomain(CommentEntity entity) {
		return new Comment(entity.getCommentId(), entity.getPost().getPostId(), entity.getContent());
	}
}
