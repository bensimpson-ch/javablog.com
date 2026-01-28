package com.javablog.adapter.rest;

import com.javablog.domain.model.Greeting;
import com.javablog.domain.port.in.GreetingUseCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloControllerTest {

	@Test
	void helloReturnsJavablog() {
		GreetingUseCase useCase = () -> new Greeting("javablog");
		HelloController controller = new HelloController(useCase);

		String result = controller.hello();

		assertEquals("javablog", result);
	}
}
