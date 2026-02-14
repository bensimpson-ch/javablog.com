package com.javablog.domain;

import java.util.Set;

public record Languages(Set<Language> values) {

    public Languages {
        Guard.againstEmpty(values, "Languages.values");
    }
}
