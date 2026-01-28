package com.javablog.adapter.rest;

import com.javablog.domain.port.in.GreetingUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	private final GreetingUseCase greetingUseCase;

	public HelloController(GreetingUseCase greetingUseCase) {
		this.greetingUseCase = greetingUseCase;
	}

	@GetMapping("/hello")
	public String hello() {
		return greetingUseCase.getGreeting().message();
	}
}
