package com.javablog.domain.article;

import com.javablog.domain.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleIdTest {

    @Test
    void validateConstraints() {
        assertThatThrownBy(() -> new ArticleId(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("ArticleId.value must not be null");
    }

    @Test
    void generateCreatesUniqueIds() {
        ArticleId id1 = ArticleId.generate();
        ArticleId id2 = ArticleId.generate();

        assertThat(id1).isNotEqualTo(id2);
    }
}
