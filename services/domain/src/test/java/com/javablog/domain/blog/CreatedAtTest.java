package com.javablog.domain.blog;

import com.javablog.domain.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreatedAtTest {

    @Test
    void validateConstraints() {
        assertThatThrownBy(() -> new CreatedAt(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("CreatedAt.value must not be null");
    }
}
