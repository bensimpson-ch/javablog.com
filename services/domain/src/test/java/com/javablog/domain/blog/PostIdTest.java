package com.javablog.domain.blog;

import com.javablog.domain.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostIdTest {

    @Test
    void validateConstraints() {
        assertThatThrownBy(() -> new PostId(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("PostId.value must not be null");
    }
}
