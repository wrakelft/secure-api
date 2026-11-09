package ru.itmo.secure_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.security.SecureRandom;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecureApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@DynamicPropertySource
	static void configureJwtSecret(DynamicPropertyRegistry registry) {
		byte[] keyBytes = new byte[32];
		new SecureRandom().nextBytes(keyBytes);

		String secret = Base64.getEncoder().encodeToString(keyBytes);

		registry.add("app.jwt.secret", () -> secret);
	}

	@Test
	void rejectsRequestWithoutToken() throws Exception {
		mockMvc.perform(get("/api/data"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void rejectsMalformedToken() throws Exception {
		mockMvc.perform(get("/api/data")
						.header(
								HttpHeaders.AUTHORIZATION,
								"Bearer invalid-token"
						))
				.andExpect(status().isUnauthorized());
	}
}