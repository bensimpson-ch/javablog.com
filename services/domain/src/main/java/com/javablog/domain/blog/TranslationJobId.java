package com.javablog.domain.blog;

import com.javablog.domain.Guard;

import java.util.UUID;

public record TranslationJobId(UUID value) {

    public TranslationJobId {
        Guard.againstNull(value, "TranslationJobId.value");
    }

    public static TranslationJobId generate() {
        return new TranslationJobId(UUID.randomUUID());
    }
}
