package com.javablog.config;

import com.javablog.adapter.kafka.TranslationRequestMessage;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestSecurityConfig {

	@Bean
	public JwtDecoder jwtDecoder() {
		return mock(JwtDecoder.class);
	}

	@SuppressWarnings("unchecked")
	@Bean
	public KafkaTemplate<String, TranslationRequestMessage> kafkaTemplate() {
		return mock(KafkaTemplate.class);
	}
}
