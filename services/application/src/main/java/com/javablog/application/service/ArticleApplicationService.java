package com.javablog.application.service;

import com.javablog.domain.article.*;
import com.javablog.domain.Language;
import com.javablog.domain.Slug;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleApplicationService {

	private final ArticleRepository articleRepository;

	public ArticleApplicationService(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}

	public Article createArticle(Article article) {
		return articleRepository.create(article);
	}

	public Article updateArticle(ArticleUpdate articleUpdate) {
		return articleRepository.update(articleUpdate);
	}

	public Optional<Article> findArticleById(ArticleId id) {
		return articleRepository.findById(id);
	}

	public Optional<Article> findArticleBySlug(Slug slug) {
		return articleRepository.findBySlug(slug);
	}

	public Articles listArticles(Language language) {
		return articleRepository.list(language);
	}
}
