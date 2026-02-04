package com.javablog.domain.blog;

import java.util.Optional;

public interface BlogRepository {

	Post create(Post post);

	Post update(Post post);

	void delete(PostId id);

	Comment create(Comment comment);

	void delete(CommentId id);

	Optional<Post> findPostById(PostId id);

	Optional<Post> findPostBySlug(Slug slug);

	Posts listPosts(Language language);

	Comments listComments(Post post);
}
