package com.javablog.adapter.persistence.blog;

import com.javablog.domain.blog.Author;
import com.javablog.domain.blog.BlogRepository;
import com.javablog.domain.blog.Comment;
import com.javablog.domain.blog.CommentId;
import com.javablog.domain.blog.Comments;
import com.javablog.domain.blog.Content;
import com.javablog.domain.blog.CreatedAt;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.PostId;
import com.javablog.domain.blog.Posts;
import com.javablog.domain.blog.Slug;
import com.javablog.domain.blog.Title;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

@Repository
public class JpaBlogRepository implements BlogRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Posts listPosts() {
		return new Posts(entityManager.createNamedQuery(PostEntity.FIND_ALL, PostEntity.class)
				.getResultList()
				.stream()
				.map(this::toDomain)
				.collect(Collectors.toSet()));
	}

	@Override
	public Comments listComments(Post post) {
		return new Comments(entityManager.createNamedQuery(CommentEntity.FIND_BY_POST_ID, CommentEntity.class)
				.setParameter("postId", post.id().value())
				.getResultList()
				.stream()
				.map(this::toDomain)
				.collect(Collectors.toSet()));
	}

	private Post toDomain(PostEntity entity) {
		return new Post(
				new PostId(entity.getPostId()),
				new Slug(entity.getSlug()),
				new Title(entity.getTitle()),
				new Content(entity.getContent()),
				new CreatedAt(entity.getCreatedAt())
		);
	}

	private Comment toDomain(CommentEntity entity) {
		return new Comment(
				new CommentId(entity.getCommentId()),
				new PostId(entity.getPost().getPostId()),
				new Author(entity.getAuthor()),
				new Content(entity.getContent()),
				new CreatedAt(entity.getCreatedAt())
		);
	}
}
