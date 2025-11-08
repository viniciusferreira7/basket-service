# Basket Service

> **Work In Progress** - This project is currently under active development.

## Overview

Basket Service is a microservice application built with Spring Boot that manages shopping basket functionality. It integrates with the Platzi Fake Store API for product information, uses MongoDB for data persistence, and Redis for caching.

## Tech Stack

- **Java 17** - Spring Boot 3.4.10
- **Spring Cloud OpenFeign** - Declarative REST client for external APIs
- **MongoDB** - NoSQL database for storing basket data
- **Redis** - In-memory cache for improved performance
- **Docker** - Containerization for MongoDB and Redis services
- **OpenAPI 3.0 / Swagger UI** - API documentation and testing

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Maven

## Configuration

The application uses environment variables for configuration loaded from a `.env` file. Copy the `.env.example` file to `.env` and adjust the values as needed:

```bash
cp .env.example .env
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_APPLICATION_NAME` | Application name | `basket-service` |
| `MONGODB_HOST` | MongoDB host | `localhost` |
| `MONGODB_PORT` | MongoDB port | `27017` |
| `MONGODB_DATABASE` | MongoDB database name | `basket-service` |
| `REDIS_HOST` | Redis host | `localhost` |
| `REDIS_PORT` | Redis port | `6379` |
| `REDIS_PASSWORD` | Redis password | `sa` |
| `CACHE_TTL` | Cache time-to-live in milliseconds | `60000` |
| `PLATZIS_STORE_URL` | Platzi Fake Store API base URL | `https://api.escuelajs.co/api/v1` |

## Getting Started

### 1. Start Infrastructure Services

Start MongoDB and Redis using Docker Compose:

```bash
docker-compose up -d
```

### 2. Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### 3. Access Swagger UI

Once the application is running, you can access the interactive API documentation at:

```
http://localhost:8080/swagger-ui.html
```

## API Endpoints

### Basket Management

- **Create Basket** - `POST /api/basket`
  - Creates a new basket with specified products

- **Get Basket by ID** - `GET /api/basket/{id}`
  - Retrieves a basket by its unique identifier

### Product Management

- **Get All Products** - `GET /api/products`
  - Retrieves all available products from Platzi Store API (cached)

- **Get Product by ID** - `GET /api/products/{id}`
  - Retrieves a specific product by ID (cached)

## Error Handling

The application includes comprehensive exception handling for:

- **Input Validation** - `MethodArgumentNotValidException`
- **Illegal Arguments** - `IllegalArgumentException`
- **Feign Exceptions** - All external API errors:
  - BadRequest (400)
  - Unauthorized (401)
  - Forbidden (403)
  - NotFound (404)
  - TooManyRequests (429)
  - InternalServerError (500)
  - ServiceUnavailable (503)

All errors are returned in a consistent `ErrorResponse` format with timestamp, status code, error message, and request path.

## Docker Services

The `docker-compose.yaml` file includes:

- **MongoDB 7.0** - Available on port 27017
  - Persistent storage with named volumes
  - Health check enabled

- **Redis 7.2** - Available on port 6379
  - Password authentication enabled
  - Persistent storage with named volumes
  - Health check enabled

To stop the services:

```bash
docker-compose down
```

To stop and remove volumes:

```bash
docker-compose down -v
```

## Testing

Run the test suite:

```bash
./mvnw test
```

The test suite includes verification of:
- Environment variable loading from `.env` file
- Spring context initialization
- Feign client integration

## Architecture

### Layered Architecture

```
controller/           # REST API endpoints
  ├── BasketController
  ├── ProductController
  └── response/       # Response DTOs and error handling

Service/              # Business logic layer
  ├── BasketService
  └── ProductService

repository/           # Data access layer
  └── BasketRepository

entity/               # Domain models
  ├── Basket
  └── Product

client/               # External API clients
  ├── PlatziStoreClient
  └── response/       # External API response DTOs

config/               # Spring configuration
  ├── DotenvConfig    # Environment variable loading
  ├── OpenApiConfig   # Swagger/OpenAPI configuration
  └── ApplicationControllerAdvice  # Global exception handling
```

## License

This project is for educational purposes.
