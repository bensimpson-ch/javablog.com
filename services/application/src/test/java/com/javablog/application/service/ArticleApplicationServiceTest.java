package com.javablog.application.service;

import com.javablog.domain.Fixture;
import com.javablog.domain.article.Article;
import com.javablog.domain.article.ArticleId;
import com.javablog.domain.article.ArticleRepository;
import com.javablog.domain.article.ArticleUpdate;
import com.javablog.domain.article.Articles;
import com.javablog.domain.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ArticleApplicationServiceTest {

	private ArticleRepository articleRepository;
	private ArticleApplicationService service;

	@BeforeEach
	void setUp() {
		articleRepository = mock(ArticleRepository.class);
		service = new ArticleApplicationService(articleRepository);
	}

	@Test
	void createArticleInvokesRepository() {
		Article article = Fixture.article();
		when(articleRepository.create(article)).thenReturn(article);

		service.createArticle(article);

		verify(articleRepository).create(article);
	}

	@Test
	void updateArticleInvokesRepository() {
		ArticleUpdate update = Fixture.articleUpdate();
		Article article = Fixture.article();
		when(articleRepository.update(update)).thenReturn(article);

		service.updateArticle(update);

		verify(articleRepository).update(update);
	}

	@Test
	void findArticleByIdInvokesRepository() {
		ArticleId articleId = Fixture.articleId();
		when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

		service.findArticleById(articleId);

		verify(articleRepository).findById(articleId);
	}

	@Test
	void listArticlesInvokesRepository() {
		when(articleRepository.list(Language.EN)).thenReturn(new Articles(Set.of()));

		service.listArticles(Language.EN);

		verify(articleRepository).list(Language.EN);
	}
}
