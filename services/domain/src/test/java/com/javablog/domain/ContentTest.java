package com.javablog.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContentTest {

    @ParameterizedTest
    @NullAndEmptySource
    void validateConstraints(String value) {
        assertThatThrownBy(() -> new Content(value))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
