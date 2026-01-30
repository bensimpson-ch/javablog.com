package com.javablog.domain.blog;

import com.javablog.domain.Guard;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public record Posts(Set<Post> values) {

    public Posts {
        Guard.againstNull(values, "Posts.values");
    }

    public List<Post> sorted() {
        return values.stream()
                .sorted(Comparator.comparing((Post p) -> p.createdAt().value()).reversed())
                .toList();
    }
}
