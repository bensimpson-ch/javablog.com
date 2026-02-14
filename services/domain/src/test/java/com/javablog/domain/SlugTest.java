package com.javablog.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SlugTest {

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource
    void validateConstraints(String value) {
        assertThatThrownBy(() -> new Slug(value))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<String> validateConstraints() {
        return Stream.of(
                "   ",
                "a".repeat(256)
        );
    }
}
