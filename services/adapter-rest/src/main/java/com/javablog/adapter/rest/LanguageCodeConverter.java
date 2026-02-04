package com.javablog.adapter.rest;

import com.javablog.api.v1.model.LanguageCode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LanguageCodeConverter implements Converter<String, LanguageCode> {

	@Override
	public LanguageCode convert(String source) {
		return LanguageCode.fromValue(source);
	}
}
