package com.javablog.adapter.rest;

import com.javablog.api.v1.CommentsApi;
import com.javablog.api.v1.PostsApi;
import com.javablog.api.v1.model.*;
import com.javablog.application.service.BlogApplicationService;
import com.javablog.domain.blog.Author;
import com.javablog.domain.blog.Comment;
import com.javablog.domain.blog.CommentId;
import com.javablog.domain.blog.Content;
import com.javablog.domain.blog.CreatedAt;
import com.javablog.domain.blog.Language;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.PostId;
import com.javablog.domain.blog.Slug;
import com.javablog.domain.blog.Summary;
import com.javablog.domain.blog.Title;
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

	private final BlogApplicationService blogApplicationService;

	public BlogResource(BlogApplicationService blogApplicationService) {
		this.blogApplicationService = blogApplicationService;
	}

	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PostResponse createPost(@RequestBody CreatePostRequest request) {
		Post post = new Post(
				PostId.generate(),
				new Slug(request.getSlug()),
				new Title(request.getTitle()),
				new Summary(request.getSummary()),
				new Content(request.getContent()),
				CreatedAt.now()
		);
		return toResponse(blogApplicationService.createPost(post));
	}

	@Override
	@GetMapping
	public List<PostResponse> listPosts(@RequestParam("language") LanguageCode language) {
		return blogApplicationService.listPosts(Language.fromCode(language.toString())).sorted().stream()
				.map(this::toResponse)
				.toList();
	}

	@Override
	@GetMapping("/{postId}")
	public PostResponse getPost(@PathVariable("postId") UUID postId) {
		return blogApplicationService.findPostById(new PostId(postId))
				.map(this::toResponse)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	@GetMapping("/by-slug/{slug}")
	public PostResponse getPostBySlug(@PathVariable("slug") String slug) {
		return blogApplicationService.findPostBySlug(new Slug(slug))
				.map(this::toResponse)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	@PutMapping("/{postId}")
	public PostResponse updatePost(
			@PathVariable("postId") UUID postId,
			@RequestBody UpdatePostRequest request) {
		return blogApplicationService.findPostById(new PostId(postId))
				.map(existing -> {
					Post updated = new Post(
							existing.id(),
							new Slug(request.getSlug()),
							new Title(request.getTitle()),
							new Summary(request.getSummary()),
							new Content(request.getContent()),
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
		blogApplicationService.findPostById(new PostId(postId))
				.ifPresentOrElse(
						post -> blogApplicationService.deletePost(post.id()),
						() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND); }
				);
	}

	@Override
	@PostMapping("/{postId}/comments")
	@ResponseStatus(HttpStatus.CREATED)
	public CommentResponse createComment(
			@PathVariable("postId") UUID postId,
			@RequestBody CreateCommentRequest request) {
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
	public List<CommentResponse> listComments(@PathVariable("postId") UUID postId) {
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
		blogApplicationService.deleteComment(new CommentId(commentId));
	}

	private PostResponse toResponse(Post post) {
		return new PostResponse()
				.id(post.id().value())
				.slug(post.slug().value())
				.title(post.title().value())
				.summary(post.summary().value())
				.content(post.content().value())
				.createdAt(post.createdAt().value().atOffset(ZoneOffset.UTC));
	}

	private CommentResponse toResponse(Comment comment) {
		return new CommentResponse()
				.id(comment.id().value())
				.postId(comment.postId().value())
				.author(comment.author().value())
				.content(comment.content().value())
				.createdAt(comment.createdAt().value().atOffset(ZoneOffset.UTC));
	}
}
