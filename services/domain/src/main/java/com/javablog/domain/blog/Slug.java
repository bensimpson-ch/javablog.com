package com.javablog.domain.blog;

import com.javablog.domain.Guard;

public record Slug(String value) {

    public Slug {
        Guard.againstBlank(value, "Slug.value");
        Guard.againstInvalidMaxLength(value, "Slug.value", 255);
    }
}
