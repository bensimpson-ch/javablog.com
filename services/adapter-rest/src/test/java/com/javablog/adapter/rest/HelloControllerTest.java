package com.javablog.adapter.rest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloControllerTest {

	@Test
	void helloReturnsJavablog() {
		HelloController controller = new HelloController();

		String result = controller.hello();

		assertEquals("javablog", result);
	}
}
