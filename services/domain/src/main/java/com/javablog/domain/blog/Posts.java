package com.javablog.domain.blog;

import com.javablog.domain.Guard;

import java.util.Set;

public record Posts(Set<Post> values) {

    public Posts {
        Guard.againstNull(values, "Posts.values");
    }
}
