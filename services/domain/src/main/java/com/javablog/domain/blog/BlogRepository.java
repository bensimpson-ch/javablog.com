package com.javablog.domain.blog;

import java.util.Optional;

public interface BlogRepository {

	Post create(Post post);

	Post update(Post post);

	Comment create(Comment comment);

	Optional<Post> findPostById(PostId id);

	Posts listPosts();

	Comments listComments(Post post);
}
