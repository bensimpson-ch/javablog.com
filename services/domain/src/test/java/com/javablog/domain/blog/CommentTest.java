package com.javablog.domain.blog;

import com.javablog.domain.ConstraintViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.javablog.domain.Fixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentTest {

    @ParameterizedTest
    @MethodSource
    void validateConstraints(CommentId id, PostId postId, Author author, Content content, CreatedAt createdAt) {
        assertThatThrownBy(() -> new Comment(id, postId, author, content, createdAt))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<Arguments> validateConstraints() {
        return Stream.of(
                Arguments.of(null, postId(), author(), content(), createdAt()),
                Arguments.of(commentId(), null, author(), content(), createdAt()),
                Arguments.of(commentId(), postId(), null, content(), createdAt()),
                Arguments.of(commentId(), postId(), author(), null, createdAt()),
                Arguments.of(commentId(), postId(), author(), content(), null)
        );
    }
}
