package com.javablog.domain.blog;

import com.javablog.domain.ConstraintViolationException;
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
