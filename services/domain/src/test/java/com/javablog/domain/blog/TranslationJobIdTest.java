package com.javablog.domain.blog;
import com.javablog.domain.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TranslationJobIdTest {

    @Test
    void validateConstraints() {
        assertThatThrownBy(() -> new TranslationJobId(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("TranslationJobId.value must not be null");
    }

    @Test
    void generateCreatesUniqueIds() {
        TranslationJobId id1 = TranslationJobId.generate();
        TranslationJobId id2 = TranslationJobId.generate();

        assertThat(id1).isNotEqualTo(id2);
    }
}
