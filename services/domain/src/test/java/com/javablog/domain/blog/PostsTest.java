package com.javablog.domain.blog;

import com.javablog.domain.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostsTest {

    @Test
    void validateConstraints() {
        assertThatThrownBy(() -> new Posts(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("Posts.values must not be null");
    }
}
