package com.javablog.domain.blog;

import com.javablog.domain.Guard;

public record Title(String value) {

    public Title {
        Guard.againstBlank(value, "Title.value");
    }
}
