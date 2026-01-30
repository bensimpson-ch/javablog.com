package com.javablog.application.service;

import com.javablog.domain.blog.BlogRepository;
import com.javablog.domain.blog.Comment;
import com.javablog.domain.blog.Comments;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.PostId;
import com.javablog.domain.blog.Posts;
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

	public Comment createComment(Comment comment) {
		return blogRepository.create(comment);
	}

	public Optional<Post> findPostById(PostId id) {
		return blogRepository.findPostById(id);
	}

	public Posts listPosts() {
		return blogRepository.listPosts();
	}

	public Comments listComments(Post post) {
		return blogRepository.listComments(post);
	}
}
