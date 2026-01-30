package com.javablog.domain.blog;

import com.javablog.domain.ConstraintViolationException;
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
