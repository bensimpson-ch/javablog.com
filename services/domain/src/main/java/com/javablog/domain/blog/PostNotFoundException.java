package com.javablog.domain.blog;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(PostId postId) {
        super("Post not found: " + postId.value());
    }
}
