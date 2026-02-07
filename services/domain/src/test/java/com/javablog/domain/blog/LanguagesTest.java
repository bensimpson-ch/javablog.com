package com.javablog.domain.blog;

import com.javablog.domain.ConstraintViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LanguagesTest {

    @ParameterizedTest
    @NullAndEmptySource
    void validateConstraints(Set<Language> values) {
        assertThatThrownBy(() -> new Languages(values))
                .isInstanceOf(ConstraintViolationException.class);
    }
}