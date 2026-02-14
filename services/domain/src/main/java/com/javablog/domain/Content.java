package com.javablog.domain;

public record Content(String value) {

    public Content {
        Guard.againstEmpty(value, "Content.value");
    }
}
