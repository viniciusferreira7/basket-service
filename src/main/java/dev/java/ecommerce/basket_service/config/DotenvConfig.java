package dev.java.ecommerce.basket_service.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            System.out.println("Loading .env file...");
            Dotenv dotenv = Dotenv.configure()
                    .directory(".")
                    .filename(".env")
                    .ignoreIfMissing()
                    .load();

            Map<String, Object> dotenvMap = new HashMap<>();
            dotenv.entries().forEach(entry -> {
                dotenvMap.put(entry.getKey(), entry.getValue());
                System.out.println("Loaded property: " + entry.getKey() + " = " + (entry.getKey().contains("PASSWORD") ? "***" : entry.getValue()));
            });

            if (!dotenvMap.isEmpty()) {
                environment.getPropertySources().addFirst(
                    new MapPropertySource("dotenvProperties", dotenvMap)
                );
                System.out.println("Successfully loaded " + dotenvMap.size() + " properties from .env file");
            } else {
                System.out.println("No properties found in .env file or .env file not found");
            }
        } catch (Exception e) {
            System.err.println("ERROR: Failed to load .env file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
