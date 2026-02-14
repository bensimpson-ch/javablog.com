package com.javablog.domain.blog;

import com.javablog.domain.Author;
import com.javablog.domain.ConstraintViolationException;
import com.javablog.domain.Languages;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.javablog.domain.Fixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TranslationRequestTest {

    @ParameterizedTest
    @MethodSource
    void validateConstraints(PostId postId, Author author, Languages languages) {
        assertThatThrownBy(() -> new TranslationRequest(postId, author, languages))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<Arguments> validateConstraints() {
        return Stream.of(
                Arguments.of(null, author(), languages()),
                Arguments.of(postId(), null, languages()),
                Arguments.of(postId(), author(), null)
        );
    }
}
