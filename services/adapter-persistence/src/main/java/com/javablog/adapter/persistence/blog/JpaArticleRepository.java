package com.javablog.adapter.persistence.blog;

import com.javablog.domain.article.Article;
import com.javablog.domain.article.ArticleId;
import com.javablog.domain.article.ArticleRepository;
import com.javablog.domain.article.Articles;
import com.javablog.domain.Content;
import com.javablog.domain.CreatedAt;
import com.javablog.domain.Language;
import com.javablog.domain.Slug;
import com.javablog.domain.Summary;
import com.javablog.domain.Title;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional
public class JpaArticleRepository implements ArticleRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Article create(Article article) {
		ArticleEntity entity = toEntity(article);
		entityManager.persist(entity);
		return article;
	}

	@Override
	public Article update(Article article) {
		ArticleEntity entity = entityManager.find(ArticleEntity.class, article.id().value());
		entity.setSlug(article.slug().value());
		entity.setTitle(article.title().value());
		entity.setSummary(article.summary().value());
		entity.setContent(article.content().value());
		return article;
	}

	@Override
	public Optional<Article> findById(ArticleId id) {
		ArticleEntity entity = entityManager.find(ArticleEntity.class, id.value());
		return Optional.ofNullable(entity).map(this::toDomain);
	}

	@Override
	public Optional<Article> findBySlug(Slug slug) {
		return entityManager.createNamedQuery(ArticleEntity.FIND_BY_SLUG, ArticleEntity.class)
				.setParameter("slug", slug.value())
				.getResultList()
				.stream()
				.findFirst()
				.map(this::toDomain);
	}

	@Override
	public Articles list(Language language) {
		if (language == Language.EN) {
			return new Articles(entityManager.createNamedQuery(ArticleEntity.FIND_ALL, ArticleEntity.class)
					.getResultList()
					.stream()
					.map(this::toDomain)
					.collect(Collectors.toSet()));
		}
		return new Articles(entityManager.createNamedQuery(TranslatedArticleEntity.FIND_BY_LANGUAGE, TranslatedArticleEntity.class)
				.setParameter("languageCode", language.code())
				.getResultList()
				.stream()
				.map(this::toDomain)
				.collect(Collectors.toSet()));
	}

	private Article toDomain(ArticleEntity entity) {
		return new Article(
				new ArticleId(entity.getArticleId()),
				new Slug(entity.getSlug()),
				new Title(entity.getTitle()),
				new Summary(entity.getSummary()),
				new Content(entity.getContent()),
				Language.fromCode(entity.getLanguageCode()),
				new CreatedAt(entity.getCreatedAt())
		);
	}

	private Article toDomain(TranslatedArticleEntity entity) {
		ArticleEntity article = entity.getArticle();
		return new Article(
				new ArticleId(article.getArticleId()),
				new Slug(article.getSlug()),
				new Title(entity.getTitle()),
				new Summary(entity.getSummary()),
				new Content(entity.getContent()),
				Language.fromCode(entity.getLanguageCode()),
				new CreatedAt(article.getCreatedAt())
		);
	}

	private ArticleEntity toEntity(Article article) {
		return new ArticleEntity(
				article.id().value(),
				article.slug().value(),
				article.title().value(),
				article.summary().value(),
				article.content().value(),
				article.createdAt().value(),
				article.language().code()
		);
	}
}
