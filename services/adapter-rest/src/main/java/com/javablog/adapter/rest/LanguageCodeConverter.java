package com.javablog.adapter.rest;

import com.javablog.api.v1.model.LanguageCodeDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LanguageCodeConverter implements Converter<String, LanguageCodeDto> {

	@Override
	public LanguageCodeDto convert(String source) {
		return LanguageCodeDto.fromValue(source);
	}
}
