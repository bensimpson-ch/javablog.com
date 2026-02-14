package com.javablog.domain.article;

import com.javablog.domain.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticlesTest {

    @Test
    void validateConstraints() {
        assertThatThrownBy(() -> new Articles(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("Articles.values must not be null");
    }
}
