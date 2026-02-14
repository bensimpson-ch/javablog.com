package com.javablog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentsTest {

    @Test
    void validateConstraints() {
        assertThatThrownBy(() -> new Comments(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("Comments.values must not be null");
    }
}
