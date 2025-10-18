package dev.java.ecommerce.basket_service.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            Map<String, Object> dotenvMap = new HashMap<>();
            dotenv.entries().forEach(entry ->
                dotenvMap.put(entry.getKey(), entry.getValue())
            );

            environment.getPropertySources().addFirst(
                new MapPropertySource("dotenvProperties", dotenvMap)
            );
        } catch (Exception e) {
            // .env file not found or error loading, continue with default values
            System.out.println("Warning: .env file not found or error loading. Using default values.");
        }
    }
}
