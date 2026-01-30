package com.javablog.domain.blog;

import com.javablog.domain.Guard;

public record Author(String value) {

    public Author {
        Guard.againstBlank(value, "Author.value");
    }
}
