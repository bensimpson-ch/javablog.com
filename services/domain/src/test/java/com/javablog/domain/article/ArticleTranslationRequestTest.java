package com.javablog.domain.article;

import com.javablog.domain.ConstraintViolationException;
import com.javablog.domain.Author;
import com.javablog.domain.Languages;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.javablog.domain.Fixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleTranslationRequestTest {

    @ParameterizedTest
    @MethodSource
    void validateConstraints(ArticleId articleId, Author author, Languages languages) {
        assertThatThrownBy(() -> new ArticleTranslationRequest(articleId, author, languages))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<Arguments> validateConstraints() {
        return Stream.of(
                Arguments.of(null, author(), languages()),
                Arguments.of(articleId(), null, languages()),
                Arguments.of(articleId(), author(), null)
        );
    }
}
