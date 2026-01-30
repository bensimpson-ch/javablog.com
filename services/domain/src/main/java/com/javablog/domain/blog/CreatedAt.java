package com.javablog.domain.blog;

import com.javablog.domain.Guard;

import java.time.LocalDateTime;

public record CreatedAt(LocalDateTime value) {

    public CreatedAt {
        Guard.againstNull(value, "CreatedAt.value");
    }

    public static CreatedAt now() {
        return new CreatedAt(LocalDateTime.now());
    }
}
