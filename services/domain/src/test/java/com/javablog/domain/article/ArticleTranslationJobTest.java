package com.javablog.domain.article;

import com.javablog.domain.ConstraintViolationException;
import com.javablog.domain.Language;
import com.javablog.domain.blog.TranslationJobId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.javablog.domain.Fixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleTranslationJobTest {

    @ParameterizedTest
    @MethodSource
    void validateConstraints(TranslationJobId id, ArticleId originalArticleId, Language language) {
        assertThatThrownBy(() -> new ArticleTranslationJob(id, originalArticleId, language))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<Arguments> validateConstraints() {
        return Stream.of(
                Arguments.of(null, articleId(), Language.DE),
                Arguments.of(translationJobId(), null, Language.DE),
                Arguments.of(translationJobId(), articleId(), null)
        );
    }
}
