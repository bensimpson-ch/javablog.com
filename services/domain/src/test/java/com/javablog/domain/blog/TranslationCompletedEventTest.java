package com.javablog.domain.blog;

import com.javablog.domain.ConstraintViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.javablog.domain.Fixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TranslationCompletedEventTest {

    @ParameterizedTest
    @MethodSource
    void validateConstraints(TranslationJobId jobId, Title title, Summary summary, Slug slug, Content content) {
        assertThatThrownBy(() -> new TranslationCompletedEvent(jobId, title, summary, slug, content))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<Arguments> validateConstraints() {
        return Stream.of(
                Arguments.of(null, title(), summary(), slug(), content()),
                Arguments.of(translationJobId(), null, summary(), slug(), content()),
                Arguments.of(translationJobId(), title(), null, slug(), content()),
                Arguments.of(translationJobId(), title(), summary(), null, content()),
                Arguments.of(translationJobId(), title(), summary(), slug(), null)
        );
    }
}
