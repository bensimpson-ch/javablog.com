package com.javablog.domain.blog;

import com.javablog.domain.Guard;

import java.util.UUID;

public record PostId(UUID value) {

    public PostId {
        Guard.againstNull(value, "PostId.value");
    }

    public static PostId generate() {
        return new PostId(UUID.randomUUID());
    }
}
