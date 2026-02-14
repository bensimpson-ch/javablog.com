package com.javablog.domain;

public record Summary(String value) {

    public Summary {
        Guard.againstBlank(value, "Summary.value");
        Guard.againstInvalidMaxLength(value, "Summary.value", 500);
    }
}
