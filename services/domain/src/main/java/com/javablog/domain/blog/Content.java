package com.javablog.domain.blog;

import com.javablog.domain.Guard;

public record Content(String value) {

    public Content {
        Guard.againstEmpty(value, "Content.value");
    }
}
