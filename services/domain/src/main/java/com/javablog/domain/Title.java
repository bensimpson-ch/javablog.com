package com.javablog.domain;

public record Title(String value) {

    public Title {
        Guard.againstBlank(value, "Title.value");
    }
}
