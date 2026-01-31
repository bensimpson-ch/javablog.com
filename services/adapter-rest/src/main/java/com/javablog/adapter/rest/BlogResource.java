package com.javablog.adapter.rest;

import com.javablog.api.v1.model.CommentResponse;
import com.javablog.api.v1.model.CreateCommentRequest;
import com.javablog.api.v1.model.CreatePostRequest;
import com.javablog.api.v1.model.PostResponse;
import com.javablog.api.v1.model.UpdatePostRequest;
import com.javablog.application.service.BlogApplicationService;
import com.javablog.domain.blog.Author;
import com.javablog.domain.blog.Comment;
import com.javablog.domain.blog.CommentId;
import com.javablog.domain.blog.Content;
import com.javablog.domain.blog.CreatedAt;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.PostId;
import com.javablog.domain.blog.Slug;
import com.javablog.domain.blog.Summary;
import com.javablog.domain.blog.Title;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/posts")
public class BlogResource {

	private final BlogApplicationService blogApplicationService;

	public BlogResource(BlogApplicationService blogApplicationService) {
		this.blogApplicationService = blogApplicationService;
	}

	@PostMapping
	public ResponseEntity<PostResponse> createPost(@RequestBody CreatePostRequest request) {
		Post post = new Post(
				PostId.generate(),
				new Slug(request.getSlug()),
				new Title(request.getTitle()),
				new Summary(request.getSummary()),
				new Content(request.getContent()),
				CreatedAt.now()
		);
		Post created = blogApplicationService.createPost(post);
		return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
	}

	@GetMapping
	public List<PostResponse> listPosts() {
		return blogApplicationService.listPosts().sorted().stream()
				.map(this::toResponse)
				.toList();
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostResponse> getPost(@PathVariable("postId") UUID postId) {
		return blogApplicationService.findPostById(new PostId(postId))
				.map(post -> ResponseEntity.ok(toResponse(post)))
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/by-slug/{slug}")
	public ResponseEntity<PostResponse> getPostBySlug(@PathVariable("slug") String slug) {
		return blogApplicationService.findPostBySlug(new Slug(slug))
				.map(post -> ResponseEntity.ok(toResponse(post)))
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{postId}")
	public ResponseEntity<PostResponse> updatePost(
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
					Post saved = blogApplicationService.updatePost(updated);
					return ResponseEntity.ok(toResponse(saved));
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> deletePost(@PathVariable("postId") UUID postId) {
		return blogApplicationService.findPostById(new PostId(postId))
				.map(post -> {
					blogApplicationService.deletePost(post.id());
					return ResponseEntity.noContent().<Void>build();
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/{postId}/comments")
	public ResponseEntity<CommentResponse> createComment(
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
					Comment created = blogApplicationService.createComment(comment);
					return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/{postId}/comments")
	public ResponseEntity<List<CommentResponse>> listComments(@PathVariable("postId") UUID postId) {
		return blogApplicationService.findPostById(new PostId(postId))
				.map(post -> {
					List<CommentResponse> comments = blogApplicationService.listComments(post)
							.sorted().stream()
							.map(this::toResponse)
							.toList();
					return ResponseEntity.ok(comments);
				})
				.orElse(ResponseEntity.notFound().build());
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
