package com.javablog.domain.blog;

import com.javablog.domain.Guard;

import java.util.Set;

public record Languages(Set<Language> values) {

    public Languages {
        Guard.againstEmpty(values, "Languages.values");
    }
}
