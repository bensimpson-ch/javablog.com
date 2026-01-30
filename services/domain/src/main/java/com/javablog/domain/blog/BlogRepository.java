package com.javablog.domain.blog;

public interface BlogRepository {

	Posts listPosts();

	Comments listComments(Post post);
}
