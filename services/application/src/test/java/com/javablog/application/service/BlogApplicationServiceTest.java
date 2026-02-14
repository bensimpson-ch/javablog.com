package com.javablog.application.service;

import com.javablog.domain.Fixture;
import com.javablog.domain.blog.BlogRepository;
import com.javablog.domain.Comment;
import com.javablog.domain.CommentId;
import com.javablog.domain.Comments;
import com.javablog.domain.Language;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.PostId;
import com.javablog.domain.blog.Posts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BlogApplicationServiceTest {

	private BlogRepository blogRepository;
	private BlogApplicationService service;

	@BeforeEach
	void setUp() {
		blogRepository = mock(BlogRepository.class);
		service = new BlogApplicationService(blogRepository);
	}

	@Test
	void createPostInvokesRepository() {
		Post post = Fixture.post();
		when(blogRepository.create(post)).thenReturn(post);

		service.createPost(post);

		verify(blogRepository).create(post);
	}

	@Test
	void createCommentInvokesRepository() {
		Comment comment = Fixture.comment();
		when(blogRepository.create(comment)).thenReturn(comment);

		service.createComment(comment);

		verify(blogRepository).create(comment);
	}

	@Test
	void findPostByIdInvokesRepository() {
		PostId postId = Fixture.postId();
		when(blogRepository.findPostById(postId)).thenReturn(Optional.empty());

		service.findPostById(postId);

		verify(blogRepository).findPostById(postId);
	}

	@Test
	void listPostsInvokesRepository() {
		when(blogRepository.listPosts(Language.EN)).thenReturn(new Posts(Set.of()));

		service.listPosts(Language.EN);

		verify(blogRepository).listPosts(Language.EN);
	}

	@Test
	void listCommentsInvokesRepository() {
		Post post = Fixture.post();
		when(blogRepository.listComments(post)).thenReturn(new Comments(Set.of()));

		service.listComments(post);

		verify(blogRepository).listComments(post);
	}

	@Test
	void deletePostInvokesRepository() {
		PostId postId = Fixture.postId();

		service.deletePost(postId);

		verify(blogRepository).delete(postId);
	}

	@Test
	void deleteCommentInvokesRepository() {
		CommentId commentId = Fixture.commentId();

		service.deleteComment(commentId);

		verify(blogRepository).delete(commentId);
	}
}
