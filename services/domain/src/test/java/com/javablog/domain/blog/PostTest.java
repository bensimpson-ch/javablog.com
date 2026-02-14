package com.javablog.domain.blog;

import com.javablog.domain.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.javablog.domain.Fixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostTest {

    @ParameterizedTest
    @MethodSource
    void validateConstraints(PostId id, Slug slug, Title title, Summary summary, Content content, Language language, CreatedAt createdAt) {
        assertThatThrownBy(() -> new Post(id, slug, title, summary, content, language, createdAt))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<Arguments> validateConstraints() {
        return Stream.of(
                Arguments.of(null, slug(), title(), summary(), content(), Language.EN, createdAt()),
                Arguments.of(postId(), null, title(), summary(), content(), Language.EN, createdAt()),
                Arguments.of(postId(), slug(), null, summary(), content(), Language.EN, createdAt()),
                Arguments.of(postId(), slug(), title(), null, content(), Language.EN, createdAt()),
                Arguments.of(postId(), slug(), title(), summary(), null, Language.EN, createdAt()),
                Arguments.of(postId(), slug(), title(), summary(), content(), null, createdAt()),
                Arguments.of(postId(), slug(), title(), summary(), content(), Language.EN, null)
        );
    }
}
