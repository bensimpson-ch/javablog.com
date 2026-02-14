package com.javablog.adapter.rest;

import com.javablog.api.v1.model.CreateArticleRequestDto;
import com.javablog.api.v1.model.LanguageCodeDto;
import com.javablog.application.service.ArticleApplicationService;
import com.javablog.domain.Fixture;
import com.javablog.domain.article.Article;
import com.javablog.domain.article.Articles;
import com.javablog.domain.Language;
import org.springframework.web.server.ResponseStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ArticleResourceTest {

	private ArticleApplicationService articleApplicationService;
	private ArticleResource resource;

	@BeforeEach
	void setUp() {
		articleApplicationService = mock(ArticleApplicationService.class);
		resource = new ArticleResource(articleApplicationService);
	}

	@Test
	void createArticleReturnsCreatedStatus() {
		when(articleApplicationService.createArticle(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));
		var request = new CreateArticleRequestDto()
				.slug("test-slug")
				.title("Test Title")
				.summary("Test summary")
				.content("Test content")
				.language(LanguageCodeDto.EN);

		resource.createArticle(request);

		verify(articleApplicationService).createArticle(any(Article.class));
	}

	@Test
	void listArticlesReturnsArticlesSortedByNewest() {
		Article article = Fixture.article();
		when(articleApplicationService.listArticles(Language.EN)).thenReturn(new Articles(Set.of(article)));

		var result = resource.listArticles(LanguageCodeDto.EN);

		assertEquals(1, result.size());
		verify(articleApplicationService).listArticles(Language.EN);
	}

	@Test
	void getArticleReturnsArticleWhenFound() {
		Article article = Fixture.article();
		when(articleApplicationService.findArticleById(article.id())).thenReturn(Optional.of(article));

		var result = resource.getArticle(article.id().value());

		assertNotNull(result);
		assertEquals(article.id().value(), result.getId());
	}

	@Test
	void getArticleThrowsNotFoundWhenArticleDoesNotExist() {
		var articleId = Fixture.articleId();
		when(articleApplicationService.findArticleById(articleId)).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> resource.getArticle(articleId.value()));
	}

	@Test
	void getArticleBySlugReturnsArticleWhenFound() {
		Article article = Fixture.article();
		when(articleApplicationService.findArticleBySlug(article.slug())).thenReturn(Optional.of(article));

		var result = resource.getArticleBySlug(article.slug().value());

		assertNotNull(result);
	}

	@Test
	void getArticleBySlugThrowsNotFoundWhenArticleDoesNotExist() {
		var slug = Fixture.slug();
		when(articleApplicationService.findArticleBySlug(slug)).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> resource.getArticleBySlug(slug.value()));
	}
}
