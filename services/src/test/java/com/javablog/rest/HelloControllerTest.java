package com.javablog.rest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloControllerTest {

	private final HelloController controller = new HelloController();

	@Test
	void helloReturnsJavablog() {
		String result = controller.hello();
		assertEquals("javablog", result);
	}
}
