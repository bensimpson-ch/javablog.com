package com.javablog.domain.blog;

import com.javablog.domain.ConstraintViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TitleTest {

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource
    void validateConstraints(String value) {
        assertThatThrownBy(() -> new Title(value))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<String> validateConstraints() {
        return Stream.of("   ");
    }
}
