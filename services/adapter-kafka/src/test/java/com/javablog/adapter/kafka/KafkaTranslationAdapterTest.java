package com.javablog.adapter.kafka;

import com.javablog.domain.Fixture;
import com.javablog.domain.article.Article;
import com.javablog.domain.Language;
import com.javablog.domain.blog.TranslationJobId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class KafkaTranslationAdapterTest {

    private KafkaTemplate<String, TranslationRequestMessage> kafkaTemplate;
    private KafkaTranslationAdapter adapter;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        adapter = new KafkaTranslationAdapter(kafkaTemplate);
    }

    @Test
    void translateArticleSendsMessageToKafka() {
        Article article = Fixture.article();

        TranslationJobId jobId = adapter.translate(article, Language.DE);

        assertThat(jobId).isNotNull();

        ArgumentCaptor<TranslationRequestMessage> captor = ArgumentCaptor.forClass(TranslationRequestMessage.class);
        verify(kafkaTemplate).send(eq("translation.requests"), anyString(), captor.capture());

        TranslationRequestMessage message = captor.getValue();
        assertThat(message.title()).isEqualTo(article.title().value());
        assertThat(message.summary()).isEqualTo(article.summary().value());
        assertThat(message.slug()).isEqualTo(article.slug().value());
        assertThat(message.content()).isEqualTo(article.content().value());
        assertThat(message.sourceLang()).isEqualTo(article.language().code());
        assertThat(message.targetLang()).isEqualTo("de");
    }
}
