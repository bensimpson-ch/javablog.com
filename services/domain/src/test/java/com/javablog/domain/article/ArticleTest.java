package com.javablog.domain.article;

import com.javablog.domain.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.javablog.domain.Fixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleTest {

    @ParameterizedTest
    @MethodSource
    void validateConstraints(ArticleId id, Slug slug, Title title, Summary summary, Content content, Language language, CreatedAt createdAt) {
        assertThatThrownBy(() -> new Article(id, slug, title, summary, content, language, createdAt))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<Arguments> validateConstraints() {
        return Stream.of(
                Arguments.of(null, slug(), title(), summary(), content(), Language.EN, createdAt()),
                Arguments.of(articleId(), null, title(), summary(), content(), Language.EN, createdAt()),
                Arguments.of(articleId(), slug(), null, summary(), content(), Language.EN, createdAt()),
                Arguments.of(articleId(), slug(), title(), null, content(), Language.EN, createdAt()),
                Arguments.of(articleId(), slug(), title(), summary(), null, Language.EN, createdAt()),
                Arguments.of(articleId(), slug(), title(), summary(), content(), null, createdAt()),
                Arguments.of(articleId(), slug(), title(), summary(), content(), Language.EN, null)
        );
    }
}
