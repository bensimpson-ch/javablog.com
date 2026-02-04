package com.javablog.adapter.rest;

import com.javablog.api.v1.model.CreateCommentRequest;
import com.javablog.api.v1.model.CreatePostRequest;
import com.javablog.api.v1.model.LanguageCode;
import com.javablog.application.service.BlogApplicationService;
import com.javablog.domain.Fixture;
import com.javablog.domain.blog.Comment;
import com.javablog.domain.blog.Comments;
import com.javablog.domain.blog.Language;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.Posts;
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

class BlogResourceTest {

	private BlogApplicationService blogApplicationService;
	private BlogResource resource;

	@BeforeEach
	void setUp() {
		blogApplicationService = mock(BlogApplicationService.class);
		resource = new BlogResource(blogApplicationService);
	}

	@Test
	void createPostReturnsCreatedStatus() {
		when(blogApplicationService.createPost(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));
		var request = new CreatePostRequest()
				.slug("test-slug")
				.title("Test Title")
				.summary("Test summary")
				.content("Test content");

		resource.createPost(request);

		verify(blogApplicationService).createPost(any(Post.class));
	}

	@Test
	void listPostsReturnsPostsSortedByNewest() {
		Post post = Fixture.post();
		when(blogApplicationService.listPosts(Language.EN)).thenReturn(new Posts(Set.of(post)));

		var result = resource.listPosts(LanguageCode.EN);

		assertEquals(1, result.size());
		verify(blogApplicationService).listPosts(Language.EN);
	}

	@Test
	void createCommentReturnsResponseWhenPostExists() {
		Post post = Fixture.post();
		when(blogApplicationService.findPostById(post.id())).thenReturn(Optional.of(post));
		when(blogApplicationService.createComment(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));
		var request = new CreateCommentRequest()
				.author("Ben")
				.content("Great post!");

		var response = resource.createComment(post.id().value(), request);

		assertNotNull(response);
		assertEquals("Ben", response.getAuthor());
		verify(blogApplicationService).createComment(any(Comment.class));
	}

	@Test
	void createCommentThrowsNotFoundWhenPostDoesNotExist() {
		var postId = Fixture.postId();
		when(blogApplicationService.findPostById(postId)).thenReturn(Optional.empty());
		var request = new CreateCommentRequest()
				.author("Ben")
				.content("Great post!");

		assertThrows(ResponseStatusException.class, () -> resource.createComment(postId.value(), request));
	}

	@Test
	void listCommentsReturnsCommentsWhenPostExists() {
		Post post = Fixture.post();
		Comment comment = Fixture.comment(post.id());
		when(blogApplicationService.findPostById(post.id())).thenReturn(Optional.of(post));
		when(blogApplicationService.listComments(post)).thenReturn(new Comments(Set.of(comment)));

		var result = resource.listComments(post.id().value());

		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	void listCommentsThrowsNotFoundWhenPostDoesNotExist() {
		var postId = Fixture.postId();
		when(blogApplicationService.findPostById(postId)).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> resource.listComments(postId.value()));
	}

	@Test
	void deletePostSucceedsWhenPostExists() {
		Post post = Fixture.post();
		when(blogApplicationService.findPostById(post.id())).thenReturn(Optional.of(post));

		resource.deletePost(post.id().value());

		verify(blogApplicationService).deletePost(post.id());
	}

	@Test
	void deletePostThrowsNotFoundWhenPostDoesNotExist() {
		var postId = Fixture.postId();
		when(blogApplicationService.findPostById(postId)).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> resource.deletePost(postId.value()));
	}

	@Test
	void deleteCommentSucceeds() {
		var postId = Fixture.postId();
		var commentId = Fixture.commentId();

		resource.deleteComment(postId.value(), commentId.value());

		verify(blogApplicationService).deleteComment(commentId);
	}
}
