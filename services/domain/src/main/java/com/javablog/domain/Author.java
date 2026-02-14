package com.javablog.domain;

public record Author(String value) {

    public Author {
        Guard.againstBlank(value, "Author.value");
    }
}
