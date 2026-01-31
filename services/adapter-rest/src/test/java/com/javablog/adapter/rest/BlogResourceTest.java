package com.javablog.adapter.rest;

import com.javablog.api.v1.model.CreateCommentRequest;
import com.javablog.api.v1.model.CreatePostRequest;
import com.javablog.application.service.BlogApplicationService;
import com.javablog.domain.Fixture;
import com.javablog.domain.blog.Comment;
import com.javablog.domain.blog.Comments;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.Posts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

		var response = resource.createPost(request);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("test-slug", response.getBody().getSlug());
		verify(blogApplicationService).createPost(any(Post.class));
	}

	@Test
	void listPostsReturnsPostsSortedByNewest() {
		Post post = Fixture.post();
		when(blogApplicationService.listPosts()).thenReturn(new Posts(Set.of(post)));

		var result = resource.listPosts();

		assertEquals(1, result.size());
		verify(blogApplicationService).listPosts();
	}

	@Test
	void createCommentReturnsCreatedStatusWhenPostExists() {
		Post post = Fixture.post();
		when(blogApplicationService.findPostById(post.id())).thenReturn(Optional.of(post));
		when(blogApplicationService.createComment(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));
		var request = new CreateCommentRequest()
				.author("Ben")
				.content("Great post!");

		var response = resource.createComment(post.id().value(), request);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Ben", response.getBody().getAuthor());
		verify(blogApplicationService).createComment(any(Comment.class));
	}

	@Test
	void createCommentReturnsNotFoundWhenPostDoesNotExist() {
		var postId = Fixture.postId();
		when(blogApplicationService.findPostById(postId)).thenReturn(Optional.empty());
		var request = new CreateCommentRequest()
				.author("Ben")
				.content("Great post!");

		var response = resource.createComment(postId.value(), request);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void listCommentsReturnsCommentsWhenPostExists() {
		Post post = Fixture.post();
		Comment comment = Fixture.comment(post.id());
		when(blogApplicationService.findPostById(post.id())).thenReturn(Optional.of(post));
		when(blogApplicationService.listComments(post)).thenReturn(new Comments(Set.of(comment)));

		var response = resource.listComments(post.id().value());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1, response.getBody().size());
	}

	@Test
	void listCommentsReturnsNotFoundWhenPostDoesNotExist() {
		var postId = Fixture.postId();
		when(blogApplicationService.findPostById(postId)).thenReturn(Optional.empty());

		var response = resource.listComments(postId.value());

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void deletePostReturnsNoContentWhenPostExists() {
		Post post = Fixture.post();
		when(blogApplicationService.findPostById(post.id())).thenReturn(Optional.of(post));

		var response = resource.deletePost(post.id().value());

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(blogApplicationService).deletePost(post.id());
	}

	@Test
	void deletePostReturnsNotFoundWhenPostDoesNotExist() {
		var postId = Fixture.postId();
		when(blogApplicationService.findPostById(postId)).thenReturn(Optional.empty());

		var response = resource.deletePost(postId.value());

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}
