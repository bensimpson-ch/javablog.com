package com.javablog;

import com.javablog.api.v1.model.CommentResponse;
import com.javablog.api.v1.model.PostResponse;
import com.javablog.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BlogIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	@Test
	void createAndReadPostsAndComments() throws Exception {
		// Create first blog post (requires authentication)
		String firstPostJson = """
				{"slug": "first-post", "title": "First Post", "summary": "Summary of first post", "content": "Content of the first post"}
				""";
		MvcResult firstPostResult = mockMvc.perform(post("/v1/posts")
						.with(jwt())
						.contentType(MediaType.APPLICATION_JSON)
						.content(firstPostJson))
				.andExpect(status().isCreated())
				.andReturn();

		PostResponse firstPost = objectMapper.readValue(
				firstPostResult.getResponse().getContentAsString(),
				PostResponse.class);

		// Add a comment to the first post (public endpoint)
		String firstPostCommentJson = """
				{"author": "Alice", "content": "Nice first post!"}
				""";
		mockMvc.perform(post("/v1/posts/" + firstPost.getId() + "/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(firstPostCommentJson))
				.andExpect(status().isCreated());

		// Small delay to ensure different createdAt timestamps
		Thread.sleep(10);

		// Create second blog post (requires authentication)
		String secondPostJson = """
				{"slug": "second-post", "title": "Second Post", "summary": "Summary of second post", "content": "Content of the second post"}
				""";
		MvcResult secondPostResult = mockMvc.perform(post("/v1/posts")
						.with(jwt())
						.contentType(MediaType.APPLICATION_JSON)
						.content(secondPostJson))
				.andExpect(status().isCreated())
				.andReturn();

		PostResponse secondPost = objectMapper.readValue(
				secondPostResult.getResponse().getContentAsString(),
				PostResponse.class);

		// Add first comment to the second post (public endpoint)
		String secondPostComment1Json = """
				{"author": "Bob", "content": "Great second post!"}
				""";
		mockMvc.perform(post("/v1/posts/" + secondPost.getId() + "/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(secondPostComment1Json))
				.andExpect(status().isCreated());

		// Small delay to ensure different createdAt timestamps
		Thread.sleep(10);

		// Add second comment to the second post (public endpoint)
		String secondPostComment2Json = """
				{"author": "Charlie", "content": "I agree with Bob!"}
				""";
		mockMvc.perform(post("/v1/posts/" + secondPost.getId() + "/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(secondPostComment2Json))
				.andExpect(status().isCreated());

		// Read all posts and verify they are sorted by newest first (public endpoint)
		MvcResult postsResult = mockMvc.perform(get("/v1/posts").param("language", "en"))
				.andExpect(status().isOk())
				.andReturn();

		PostResponse[] posts = objectMapper.readValue(
				postsResult.getResponse().getContentAsString(),
				PostResponse[].class);

		assertThat(posts).hasSize(2);
		assertThat(posts[0].getSlug()).isEqualTo("second-post"); // newest first
		assertThat(posts[1].getSlug()).isEqualTo("first-post");

		// Read comments from the second post and verify they are sorted by newest first (public endpoint)
		MvcResult commentsResult = mockMvc.perform(get("/v1/posts/" + secondPost.getId() + "/comments"))
				.andExpect(status().isOk())
				.andReturn();

		CommentResponse[] comments = objectMapper.readValue(
				commentsResult.getResponse().getContentAsString(),
				CommentResponse[].class);

		assertThat(comments).hasSize(2);
		assertThat(comments[0].getAuthor()).isEqualTo("Charlie"); // newest first
		assertThat(comments[1].getAuthor()).isEqualTo("Bob");
	}

	@Test
	void deletePost() throws Exception {
		// Create a blog post (requires authentication)
		String postJson = """
				{"slug": "post-to-delete", "title": "Post To Delete", "summary": "Summary of post to delete", "content": "This post will be deleted"}
				""";
		MvcResult createResult = mockMvc.perform(post("/v1/posts")
						.with(jwt())
						.contentType(MediaType.APPLICATION_JSON)
						.content(postJson))
				.andExpect(status().isCreated())
				.andReturn();

		PostResponse createdPost = objectMapper.readValue(
				createResult.getResponse().getContentAsString(),
				PostResponse.class);

		// Verify post exists
		mockMvc.perform(get("/v1/posts/" + createdPost.getId()))
				.andExpect(status().isOk());

		// Delete the post (requires authentication)
		mockMvc.perform(delete("/v1/posts/" + createdPost.getId())
						.with(jwt()))
				.andExpect(status().isNoContent());

		// Verify post no longer exists
		mockMvc.perform(get("/v1/posts/" + createdPost.getId()))
				.andExpect(status().isNotFound());
	}

	@Test
	void deletePostRequiresAuthentication() throws Exception {
		// Create a blog post first
		String postJson = """
				{"slug": "auth-test-post", "title": "Auth Test Post", "summary": "Summary for auth test", "content": "Testing auth"}
				""";
		MvcResult createResult = mockMvc.perform(post("/v1/posts")
						.with(jwt())
						.contentType(MediaType.APPLICATION_JSON)
						.content(postJson))
				.andExpect(status().isCreated())
				.andReturn();

		PostResponse createdPost = objectMapper.readValue(
				createResult.getResponse().getContentAsString(),
				PostResponse.class);

		// Attempt to delete without authentication
		mockMvc.perform(delete("/v1/posts/" + createdPost.getId()))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void deleteNonExistentPostReturnsNotFound() throws Exception {
		mockMvc.perform(delete("/v1/posts/00000000-0000-0000-0000-000000000000")
						.with(jwt()))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteComment() throws Exception {
		// Create a blog post
		String postJson = """
				{"slug": "post-with-comment", "title": "Post With Comment", "summary": "Summary", "content": "Content"}
				""";
		MvcResult postResult = mockMvc.perform(post("/v1/posts")
						.with(jwt())
						.contentType(MediaType.APPLICATION_JSON)
						.content(postJson))
				.andExpect(status().isCreated())
				.andReturn();

		PostResponse createdPost = objectMapper.readValue(
				postResult.getResponse().getContentAsString(),
				PostResponse.class);

		// Add a comment
		String commentJson = """
				{"author": "Test Author", "content": "Test comment to delete"}
				""";
		MvcResult commentResult = mockMvc.perform(post("/v1/posts/" + createdPost.getId() + "/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(commentJson))
				.andExpect(status().isCreated())
				.andReturn();

		CommentResponse createdComment = objectMapper.readValue(
				commentResult.getResponse().getContentAsString(),
				CommentResponse.class);

		// Verify comment exists
		MvcResult commentsBeforeDelete = mockMvc.perform(get("/v1/posts/" + createdPost.getId() + "/comments"))
				.andExpect(status().isOk())
				.andReturn();

		CommentResponse[] commentsBefore = objectMapper.readValue(
				commentsBeforeDelete.getResponse().getContentAsString(),
				CommentResponse[].class);
		assertThat(commentsBefore).hasSize(1);

		// Delete the comment (requires authentication)
		mockMvc.perform(delete("/v1/posts/" + createdPost.getId() + "/comments/" + createdComment.getId())
						.with(jwt()))
				.andExpect(status().isNoContent());

		// Verify comment no longer exists
		MvcResult commentsAfterDelete = mockMvc.perform(get("/v1/posts/" + createdPost.getId() + "/comments"))
				.andExpect(status().isOk())
				.andReturn();

		CommentResponse[] commentsAfter = objectMapper.readValue(
				commentsAfterDelete.getResponse().getContentAsString(),
				CommentResponse[].class);
		assertThat(commentsAfter).isEmpty();
	}

	@Test
	void deleteCommentRequiresAuthentication() throws Exception {
		// Create a blog post
		String postJson = """
				{"slug": "post-for-auth-comment", "title": "Post For Auth Comment", "summary": "Summary", "content": "Content"}
				""";
		MvcResult postResult = mockMvc.perform(post("/v1/posts")
						.with(jwt())
						.contentType(MediaType.APPLICATION_JSON)
						.content(postJson))
				.andExpect(status().isCreated())
				.andReturn();

		PostResponse createdPost = objectMapper.readValue(
				postResult.getResponse().getContentAsString(),
				PostResponse.class);

		// Add a comment
		String commentJson = """
				{"author": "Test Author", "content": "Test comment"}
				""";
		MvcResult commentResult = mockMvc.perform(post("/v1/posts/" + createdPost.getId() + "/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(commentJson))
				.andExpect(status().isCreated())
				.andReturn();

		CommentResponse createdComment = objectMapper.readValue(
				commentResult.getResponse().getContentAsString(),
				CommentResponse.class);

		// Attempt to delete without authentication
		mockMvc.perform(delete("/v1/posts/" + createdPost.getId() + "/comments/" + createdComment.getId()))
				.andExpect(status().isUnauthorized());
	}
}
