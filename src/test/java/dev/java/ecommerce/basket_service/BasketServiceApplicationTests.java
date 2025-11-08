package dev.java.ecommerce.basket_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BasketServiceApplicationTests {

	@Value("${SPRING_APPLICATION_NAME}")
	private String applicationName;

	@Value("${MONGODB_HOST}")
	private String mongodbHost;

	@Value("${REDIS_HOST}")
	private String redisHost;

	@Test
	void contextLoads() {
	}

	@Test
	void testEnvironmentVariablesLoaded() {
		assertNotNull(applicationName, "SPRING_APPLICATION_NAME should be loaded from .env");
		assertEquals("basket-service", applicationName, "Application name should be basket-service");
	}

	@Test
	void testMongodbEnvironmentVariableLoaded() {
		assertNotNull(mongodbHost, "MONGODB_HOST should be loaded from .env");
		assertEquals("localhost", mongodbHost, "MongoDB host should be localhost");
	}

	@Test
	void testRedisEnvironmentVariableLoaded() {
		assertNotNull(redisHost, "REDIS_HOST should be loaded from .env");
		assertEquals("localhost", redisHost, "Redis host should be localhost");
	}

}
