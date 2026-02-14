package com.javablog.domain.article;

import com.javablog.domain.ConstraintViolationException;
import com.javablog.domain.CreatedAt;
import com.javablog.domain.Language;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.javablog.domain.Fixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TranslatedArticleTest {

    @ParameterizedTest
    @MethodSource
    void validateConstraints(ArticleId articleId, ArticleId originalArticleId, Language language, CreatedAt createdDate) {
        assertThatThrownBy(() -> new TranslatedArticle(articleId, originalArticleId, language, null, null, createdDate, null))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<Arguments> validateConstraints() {
        return Stream.of(
                Arguments.of(null, articleId(), Language.DE, createdAt()),
                Arguments.of(articleId(), null, Language.DE, createdAt()),
                Arguments.of(articleId(), articleId(), null, createdAt()),
                Arguments.of(articleId(), articleId(), Language.DE, null)
        );
    }
}
