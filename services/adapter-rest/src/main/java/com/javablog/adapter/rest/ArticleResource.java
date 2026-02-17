package com.javablog.adapter.rest;

import com.javablog.api.v1.ArticlesApi;
import com.javablog.api.v1.model.*;
import com.javablog.application.service.ArticleApplicationService;
import com.javablog.domain.article.Article;
import com.javablog.domain.article.ArticleId;
import com.javablog.domain.article.ArticleUpdate;
import com.javablog.domain.Content;
import com.javablog.domain.CreatedAt;
import com.javablog.domain.Language;
import com.javablog.domain.Slug;
import com.javablog.domain.Summary;
import com.javablog.domain.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/articles")
public class ArticleResource implements ArticlesApi {

	private static final Logger LOGGER = LogManager.getLogger();
	private final ArticleApplicationService articleApplicationService;

	public ArticleResource(ArticleApplicationService articleApplicationService) {
		this.articleApplicationService = articleApplicationService;
	}

	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ArticleResponseDto createArticle(@RequestBody CreateArticleRequestDto request) {
		LOGGER.info("createArticle slug={}", request::getSlug);
		Article article = new Article(
				ArticleId.generate(),
				new Slug(request.getSlug()),
				new Title(request.getTitle()),
				new Summary(request.getSummary()),
				new Content(request.getContent()),
				Language.fromCode(request.getLanguage().toString()),
				CreatedAt.now()
		);
		return toResponse(articleApplicationService.createArticle(article));
	}

	@Override
	@GetMapping
	public List<ArticleResponseDto> listArticles(@RequestParam("language") LanguageCodeDto language) {
		List<ArticleResponseDto> responses = articleApplicationService.listArticles(Language.fromCode(language.toString())).sorted().stream()
				.map(this::toResponse)
				.toList();
		LOGGER.info("list articles size {} language {}", responses::size, language::name);
		return responses;
	}

	@Override
	@GetMapping("/{articleId}")
	public ArticleResponseDto getArticle(@PathVariable("articleId") UUID articleId) {
		LOGGER.info("getArticle articleId={}", articleId);
		return articleApplicationService.findArticleById(new ArticleId(articleId))
				.map(this::toResponse)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	@GetMapping("/by-slug/{slug}")
	public ArticleResponseDto getArticleBySlug(@PathVariable("slug") String slug) {
		LOGGER.info("getArticleBySlug slug={}", slug);
		return articleApplicationService.findArticleBySlug(new Slug(slug))
				.map(this::toResponse)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	@PutMapping("/{articleId}")
	public ArticleResponseDto updateArticle(
			@PathVariable("articleId") UUID articleId,
			@RequestBody UpdateArticleRequestDto request) {
		LOGGER.info("updateArticle articleId={}", articleId);
		ArticleUpdate articleUpdate = new ArticleUpdate(
				new ArticleId(articleId),
				new Slug(request.getSlug()),
				new Title(request.getTitle()),
				new Summary(request.getSummary()),
				new Content(request.getContent()),
				Language.fromCode(request.getLanguage().toString())
		);
		return toResponse(articleApplicationService.updateArticle(articleUpdate));
	}

	private ArticleResponseDto toResponse(Article article) {
		return new ArticleResponseDto()
				.id(article.id().value())
				.slug(article.slug().value())
				.title(article.title().value())
				.summary(article.summary().value())
				.content(article.content().value())
				.language(LanguageCodeDto.fromValue(article.language().code()))
				.createdAt(article.createdAt().value().atOffset(ZoneOffset.UTC));
	}
}
