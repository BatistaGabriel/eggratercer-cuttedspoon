package com.bobathief.UrlShortener;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {

	private Main main;

	@BeforeEach
	void setUp() {
		main = new Main();
	}

	@Test
	void testHandleRequestWithValidInput() {
		// Arrange
		Map<String, Object> input = new HashMap<>();
		Map<String, String> bodyMap = new HashMap<>();
		bodyMap.put("originalUrl", "https://example.com");
		bodyMap.put("expirationTime", "2024-11-30");

		input.put("body", "{\"originalUrl\": \"https://example.com\", \"expirationTime\": \"2024-11-30\"}");

		// Act
		Map<String, String> response = main.handleRequest(input, null);

		// Assert
		assertNotNull(response);
		assertTrue(response.containsKey("urlCode"));
		assertEquals(8, response.get("urlCode").length());
	}

	@Test
	void testHandleRequestWithInvalidJson() {
		// Arrange
		Map<String, Object> input = new HashMap<>();
		input.put("body", "INVALID_JSON");

		// Act & Assert
		RuntimeException exception = assertThrows(RuntimeException.class, () -> main.handleRequest(input, null));
		assertTrue(exception.getMessage().contains("Error parsing JSON body"));
	}
}
