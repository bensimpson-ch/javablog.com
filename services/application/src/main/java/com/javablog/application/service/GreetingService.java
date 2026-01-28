package com.javablog.application.service;

import com.javablog.domain.model.Greeting;
import com.javablog.domain.port.in.GreetingUseCase;
import org.springframework.stereotype.Service;

@Service
public class GreetingService implements GreetingUseCase {

	@Override
	public Greeting getGreeting() {
		return new Greeting("javablog");
	}
}
