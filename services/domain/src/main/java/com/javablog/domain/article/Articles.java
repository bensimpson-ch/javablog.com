package com.javablog.domain.article;

import com.javablog.domain.Guard;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public record Articles(Set<Article> values) {

    public Articles {
        Guard.againstNull(values, "Articles.values");
    }

    public List<Article> sorted() {
        return values.stream()
                .sorted(Comparator.comparing((Article a) -> a.createdAt().value()).reversed())
                .toList();
    }
}
