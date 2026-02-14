package com.javablog.domain.blog;

import com.javablog.domain.ConstraintViolationException;
import com.javablog.domain.Language;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.javablog.domain.Fixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TranslationJobTest {

    @ParameterizedTest
    @MethodSource
    void validateConstraints(TranslationJobId id, PostId originalPostId, Language language) {
        assertThatThrownBy(() -> new TranslationJob(id, originalPostId, language))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<Arguments> validateConstraints() {
        return Stream.of(
                Arguments.of(null, postId(), Language.DE),
                Arguments.of(translationJobId(), null, Language.DE),
                Arguments.of(translationJobId(), postId(), null)
        );
    }
}
