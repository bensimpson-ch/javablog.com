package com.javablog.adapter.rest;

import com.javablog.api.v1.CommentsApi;
import com.javablog.api.v1.PostsApi;
import com.javablog.api.v1.model.*;
import com.javablog.application.service.BlogApplicationService;
import com.javablog.domain.Author;
import com.javablog.domain.Comment;
import com.javablog.domain.CommentId;
import com.javablog.domain.Content;
import com.javablog.domain.CreatedAt;
import com.javablog.domain.Language;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.PostId;
import com.javablog.domain.Slug;
import com.javablog.domain.Summary;
import com.javablog.domain.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/v1/posts")
public class BlogResource implements PostsApi, CommentsApi {

	private static final Logger LOGGER = LogManager.getLogger();
	private final BlogApplicationService blogApplicationService;

	public BlogResource(BlogApplicationService blogApplicationService) {
		this.blogApplicationService = blogApplicationService;
	}

	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PostResponseDto createPost(@RequestBody CreatePostRequestDto request) {
		LOGGER.info("createPost slug={}", request::getSlug);
		Post post = new Post(
				PostId.generate(),
				new Slug(request.getSlug()),
				new Title(request.getTitle()),
				new Summary(request.getSummary()),
				new Content(request.getContent()),
				Language.fromCode(request.getLanguage().toString()),
				CreatedAt.now()
		);
		return toResponse(blogApplicationService.createPost(post));
	}

	@Override
	@GetMapping
	public List<PostResponseDto> listPosts(@RequestParam("language") LanguageCodeDto language) {

		List<PostResponseDto> responses =  blogApplicationService.listPosts(Language.fromCode(language.toString())).sorted().stream()
				.map(this::toResponse)
				.toList();
		LOGGER.info("list posts size {} language {}", responses::size, language::name);
		return responses;
	}

	@Override
	@GetMapping("/{postId}")
	public PostResponseDto getPost(@PathVariable("postId") UUID postId) {
		LOGGER.info("getPost postId={}", postId);
		return blogApplicationService.findPostById(new PostId(postId))
				.map(this::toResponse)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	@GetMapping("/by-slug/{slug}")
	public PostResponseDto getPostBySlug(@PathVariable("slug") String slug) {
		LOGGER.info("getPostBySlug slug={}", slug);
		return blogApplicationService.findPostBySlug(new Slug(slug))
				.map(this::toResponse)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	@PutMapping("/{postId}")
	public PostResponseDto updatePost(
			@PathVariable("postId") UUID postId,
			@RequestBody UpdatePostRequestDto request) {
		LOGGER.info("updatePost postId={}", postId);
		return blogApplicationService.findPostById(new PostId(postId))
				.map(existing -> {
					Post updated = new Post(
							existing.id(),
							new Slug(request.getSlug()),
							new Title(request.getTitle()),
							new Summary(request.getSummary()),
							new Content(request.getContent()),
							Language.fromCode(request.getLanguage().toString()),
							existing.createdAt()
					);
					return toResponse(blogApplicationService.updatePost(updated));
				})
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	@DeleteMapping("/{postId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePost(@PathVariable("postId") UUID postId) {
		LOGGER.info("deletePost postId={}", postId);
		blogApplicationService.findPostById(new PostId(postId))
				.ifPresentOrElse(
						post -> blogApplicationService.deletePost(post.id()),
						() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND); }
				);
	}

	@Override
	@PostMapping("/{postId}/comments")
	@ResponseStatus(HttpStatus.CREATED)
	public CommentResponseDto createComment(
			@PathVariable("postId") UUID postId,
			@RequestBody CreateCommentRequestDto request) {
		LOGGER.info("createComment postId={} author={}", postId::toString, request::getAuthor);
		return blogApplicationService.findPostById(new PostId(postId))
				.map(post -> {
					Comment comment = new Comment(
							CommentId.generate(),
							post.id(),
							new Author(request.getAuthor()),
							new Content(request.getContent()),
							CreatedAt.now()
					);
					return toResponse(blogApplicationService.createComment(comment));
				})
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	@GetMapping("/{postId}/comments")
	public List<CommentResponseDto> listComments(@PathVariable("postId") UUID postId) {
		LOGGER.info("listComments postId={}", postId);
		return blogApplicationService.findPostById(new PostId(postId))
				.map(post -> blogApplicationService.listComments(post)
						.sorted().stream()
						.map(this::toResponse)
						.toList())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	@DeleteMapping("/{postId}/comments/{commentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteComment(
			@PathVariable("postId") UUID postId,
			@PathVariable("commentId") UUID commentId) {
		LOGGER.info("deleteComment postId={} commentId={}", postId, commentId);
		blogApplicationService.deleteComment(new CommentId(commentId));
	}

	private PostResponseDto toResponse(Post post) {
		return new PostResponseDto()
				.id(post.id().value())
				.slug(post.slug().value())
				.title(post.title().value())
				.summary(post.summary().value())
				.content(post.content().value())
				.language(LanguageCodeDto.fromValue(post.language().code()))
				.createdAt(post.createdAt().value().atOffset(ZoneOffset.UTC));
	}

	private CommentResponseDto toResponse(Comment comment) {
		return new CommentResponseDto()
				.id(comment.id().value())
				.postId(comment.postId().value())
				.author(comment.author().value())
				.content(comment.content().value())
				.createdAt(comment.createdAt().value().atOffset(ZoneOffset.UTC));
	}
}
