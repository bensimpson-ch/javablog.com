package com.javablog.domain.blog;

import com.javablog.domain.Guard;

import java.util.UUID;

public record CommentId(UUID value) {

    public CommentId {
        Guard.againstNull(value, "CommentId.value");
    }

    public static CommentId generate() {
        return new CommentId(UUID.randomUUID());
    }
}
