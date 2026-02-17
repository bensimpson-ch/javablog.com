package com.javablog.adapter.persistence.article;

import com.javablog.domain.article.*;
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

	private final ArticleMapper mapper = new ArticleMapper();

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Article create(Article article) {
		ArticleEntity entity = mapper.map(article);
		entityManager.persist(entity);
		return article;
	}

	@Override
	public Article update(ArticleUpdate article) {
		ArticleEntity entity = entityManager.find(ArticleEntity.class, article.id().value());
		entity.setSlug(article.slug().value());
		entity.setTitle(article.title().value());
		entity.setSummary(article.summary().value());
		entity.setContent(article.content().value());
		entityManager.merge(entity);
		return mapper.map(entity);
	}

	@Override
	public Optional<Article> findById(ArticleId id) {
		ArticleEntity entity = entityManager.find(ArticleEntity.class, id.value());
		return Optional.ofNullable(entity).map(mapper::map);
	}

	@Override
	public Optional<Article> findBySlug(Slug slug) {
		return entityManager.createNamedQuery(ArticleEntity.FIND_BY_SLUG, ArticleEntity.class)
				.setParameter("slug", slug.value())
				.getResultList()
				.stream()
				.findFirst()
				.map(mapper::map);
	}

	@Override
	public Articles list(Language language) {
		if (language == Language.EN) {
			return new Articles(entityManager.createNamedQuery(ArticleEntity.FIND_ALL, ArticleEntity.class)
					.getResultList()
					.stream()
					.map(mapper::map)
					.collect(Collectors.toSet()));
		}
		return new Articles(entityManager.createNamedQuery(TranslatedArticleEntity.FIND_BY_LANGUAGE, TranslatedArticleEntity.class)
				.setParameter("languageCode", language.code())
				.getResultList()
				.stream()
				.map(mapper::map)
				.collect(Collectors.toSet()));
	}
}
