package com.javablog.application.service;

import com.javablog.domain.blog.BlogRepository;
import com.javablog.domain.Comment;
import com.javablog.domain.CommentId;
import com.javablog.domain.Comments;
import com.javablog.domain.Language;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.PostId;
import com.javablog.domain.blog.Posts;
import com.javablog.domain.Slug;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlogApplicationService {

	private final BlogRepository blogRepository;

	public BlogApplicationService(BlogRepository blogRepository) {
		this.blogRepository = blogRepository;
	}

	public Post createPost(Post post) {
		return blogRepository.create(post);
	}

	public Post updatePost(Post post) {
		return blogRepository.update(post);
	}

	public void deletePost(PostId id) {
		blogRepository.delete(id);
	}

	public Comment createComment(Comment comment) {
		return blogRepository.create(comment);
	}

	public void deleteComment(CommentId id) {
		blogRepository.delete(id);
	}

	public Optional<Post> findPostById(PostId id) {
		return blogRepository.findPostById(id);
	}

	public Optional<Post> findPostBySlug(Slug slug) {
		return blogRepository.findPostBySlug(slug);
	}

	public Posts listPosts(Language language) {
		return blogRepository.listPosts(language);
	}

	public Comments listComments(Post post) {
		return blogRepository.listComments(post);
	}
}
