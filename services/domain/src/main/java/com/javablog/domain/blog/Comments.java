package com.javablog.domain.blog;

import com.javablog.domain.Guard;

import java.util.Set;

public record Comments(Set<Comment> values) {

    public Comments {
        Guard.againstNull(values, "Comments.values");
    }
}
