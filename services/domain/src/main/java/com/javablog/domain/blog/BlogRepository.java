package com.javablog.domain.blog;

import java.util.List;

public interface BlogRepository {

	List<Post> listPosts();

	List<Comment> listComments(Post post);
}
