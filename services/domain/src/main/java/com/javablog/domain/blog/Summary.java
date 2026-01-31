package com.javablog.domain.blog;

import com.javablog.domain.Guard;

public record Summary(String value) {

    public Summary {
        Guard.againstBlank(value, "Summary.value");
        Guard.againstInvalidMaxLength(value, "Summary.value", 500);
    }
}
