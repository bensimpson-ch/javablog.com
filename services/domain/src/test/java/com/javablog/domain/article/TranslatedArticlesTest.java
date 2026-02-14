package com.javablog.domain.article;

import com.javablog.domain.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TranslatedArticlesTest {

    @Test
    void validateConstraints() {
        assertThatThrownBy(() -> new TranslatedArticles(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("TranslatedArticles.values must not be null");
    }
}
