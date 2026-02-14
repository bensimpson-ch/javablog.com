package com.javablog.domain;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public record Comments(Set<Comment> values) {

    public Comments {
        Guard.againstNull(values, "Comments.values");
    }

    public List<Comment> sorted() {
        return values.stream()
                .sorted(Comparator.comparing((Comment c) -> c.createdAt().value()).reversed())
                .toList();
    }
}
