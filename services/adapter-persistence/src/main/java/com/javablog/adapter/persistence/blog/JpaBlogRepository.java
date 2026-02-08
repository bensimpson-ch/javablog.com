package com.javablog.adapter.persistence.blog;

import com.javablog.domain.blog.Author;
import com.javablog.domain.blog.BlogRepository;
import com.javablog.domain.blog.Comment;
import com.javablog.domain.blog.CommentId;
import com.javablog.domain.blog.Comments;
import com.javablog.domain.blog.Content;
import com.javablog.domain.blog.CreatedAt;
import com.javablog.domain.blog.Language;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.PostId;
import com.javablog.domain.blog.Posts;
import com.javablog.domain.blog.Slug;
import com.javablog.domain.blog.Summary;
import com.javablog.domain.blog.Title;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional
public class JpaBlogRepository implements BlogRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Post create(Post post) {
		PostEntity entity = toEntity(post);
		entityManager.persist(entity);
		return post;
	}

	@Override
	public Post update(Post post) {
		PostEntity entity = entityManager.find(PostEntity.class, post.id().value());
		entity.setSlug(post.slug().value());
		entity.setTitle(post.title().value());
		entity.setSummary(post.summary().value());
		entity.setContent(post.content().value());
		return post;
	}

	@Override
	public Optional<Post> findPostBySlug(Slug slug) {
		return entityManager.createNamedQuery(PostEntity.FIND_BY_SLUG, PostEntity.class)
				.setParameter("slug", slug.value())
				.getResultList()
				.stream()
				.findFirst()
				.map(this::toDomain);
	}

	@Override
	public void delete(PostId id) {
		PostEntity entity = entityManager.find(PostEntity.class, id.value());
		if (entity != null) {
			entityManager.remove(entity);
		}
	}

	@Override
	public Comment create(Comment comment) {
		PostEntity postEntity = entityManager.find(PostEntity.class, comment.postId().value());
		CommentEntity entity = toEntity(comment, postEntity);
		entityManager.persist(entity);
		return comment;
	}

	@Override
	public void delete(CommentId id) {
		CommentEntity entity = entityManager.find(CommentEntity.class, id.value());
		if (entity != null) {
			entityManager.remove(entity);
		}
	}

	@Override
	public Optional<Post> findPostById(PostId id) {
		PostEntity entity = entityManager.find(PostEntity.class, id.value());
		return Optional.ofNullable(entity).map(this::toDomain);
	}

	@Override
	public Posts listPosts(Language language) {
		if (language == Language.EN) {
			return new Posts(entityManager.createNamedQuery(PostEntity.FIND_ALL, PostEntity.class)
					.getResultList()
					.stream()
					.map(this::toDomain)
					.collect(Collectors.toSet()));
		}
		return new Posts(entityManager.createNamedQuery(TranslatedPostEntity.FIND_BY_LANGUAGE, TranslatedPostEntity.class)
				.setParameter("languageCode", language.code())
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
				new Summary(entity.getSummary()),
				new Content(entity.getContent()),
				Language.fromCode(entity.getLanguageCode()),
				new CreatedAt(entity.getCreatedAt())
		);
	}

	private Post toDomain(TranslatedPostEntity entity) {
		PostEntity post = entity.getPost();
		return new Post(
				new PostId(post.getPostId()),
				new Slug(post.getSlug()),
				new Title(entity.getTitle()),
				new Summary(entity.getSummary()),
				new Content(entity.getContent()),
				Language.fromCode(entity.getLanguageCode()),
				new CreatedAt(post.getCreatedAt())
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

	private PostEntity toEntity(Post post) {
		return new PostEntity(
				post.id().value(),
				post.slug().value(),
				post.title().value(),
				post.summary().value(),
				post.content().value(),
				post.createdAt().value(),
				post.language().code()
		);
	}

	private CommentEntity toEntity(Comment comment, PostEntity postEntity) {
		return new CommentEntity(
				comment.id().value(),
				postEntity,
				comment.author().value(),
				comment.content().value(),
				comment.createdAt().value()
		);
	}
}
