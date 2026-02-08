package com.javablog.domain.blog;

import com.javablog.domain.Guard;

import java.util.Set;

public record TranslatedPosts(Set<TranslatedPost> values) {

    public TranslatedPosts {
        Guard.againstNull(values, "TranslatedPosts.values");
    }
}
