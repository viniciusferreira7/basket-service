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
  - **Request Body**:
    ```json
    {
      "clientId": 1,
      "productsRequest": [
        {
          "id": 1,
          "quantity": 2
        }
      ]
    }
    ```
  - **Response**: HTTP 201 Created with Basket object
  - **Validation**:
    - `clientId`: Required (not null)
    - `productsRequest`: Required, must contain at least 1 product
    - All products must exist in Platzi Store API
  - **Business Rules**: Client cannot have multiple OPEN baskets simultaneously

- **Get Basket by ID** - `GET /api/basket/{id}`
  - Retrieves a basket by its unique identifier
  - **Response**: HTTP 200 OK with Basket object
  - **Error**: HTTP 400 if basket not found

- **Update Basket** - `PATCH /api/basket/{id}`
  - Updates an existing basket with new products
  - **Request Body**:
    ```json
    {
      "clientId": 1,
      "productsRequest": [
        {
          "id": 2,
          "quantity": 3
        }
      ]
    }
    ```
  - **Response**: HTTP 204 No Content
  - **Error**: HTTP 400 if basket not found
  - **Validation**: Same as create basket

- **Pay Basket** - `PATCH /api/basket/{id}/payment`
  - Processes payment for a basket, marking it as SOLD
  - **Request Body**:
    ```json
    {
      "payMethod": "PIX"
    }
    ```
  - **Accepted Payment Methods**: `PIX`, `CREDIT`, `DEBIT`
  - **Response**: HTTP 204 No Content
  - **Error Conditions**:
    - HTTP 400 if basket not found
    - HTTP 400 if basket already SOLD
    - HTTP 400 if payment method is null or invalid

- **Delete Basket** - `DELETE /api/basket/{id}`
  - Removes a basket from the system
  - **Response**: HTTP 204 No Content
  - **Error**: HTTP 400 if basket not found

### Product Management

- **Get All Products** - `GET /api/products`
  - Retrieves all available products from Platzi Store API (cached)
  - **Response**: HTTP 200 OK with array of products
  - **Caching**: Results cached with key `products` and TTL configured by `CACHE_TTL`

- **Get Product by ID** - `GET /api/products/{id}`
  - Retrieves a specific product by ID (cached)
  - **Path Parameter**: `id` (must be a positive integer)
  - **Response**: HTTP 200 OK with Product object
  - **Caching**: Results cached with key `product#{id}` and TTL configured by `CACHE_TTL`
  - **Error**: HTTP 404 if product not found

## Basket States and Payment Methods

### Basket Status

A basket has two possible states during its lifecycle:

- **OPEN** - Initial state when created. Basket can be updated or deleted. Only one OPEN basket allowed per client.
- **SOLD** - Final state after payment is processed. Basket cannot be modified or deleted.

### Payment Methods

When paying for a basket, one of the following payment methods must be specified:

- **PIX** - Brazilian instant payment system (Pix)
- **CREDIT** - Credit card payment
- **DEBIT** - Debit card payment

## Caching Strategy

The application uses Redis for caching product information to improve performance:

### Cached Endpoints

1. **Get All Products** - `GET /api/products`
   - **Cache Key**: `products`
   - **TTL**: Configured via `CACHE_TTL` environment variable (default: 60000ms = 60 seconds)
   - Caches the entire product list from Platzi Store API

2. **Get Product by ID** - `GET /api/products/{id}`
   - **Cache Key**: `product#{id}` (e.g., `product#1` for product with ID 1)
   - **TTL**: Configured via `CACHE_TTL` environment variable (default: 60000ms = 60 seconds)
   - Caches individual product details from Platzi Store API

### Cache Invalidation

Cache entries automatically expire based on the configured TTL. No manual invalidation is needed.

## Error Handling

The application includes comprehensive exception handling for:

- **Input Validation** - `MethodArgumentNotValidException`
  - HTTP 400 Bad Request
  - Returns validation errors per field

- **Illegal Arguments** - `IllegalArgumentException`
  - HTTP 400 Bad Request
  - Examples: Client already has OPEN basket, invalid payment method

- **Entity Not Found** - `EntityNotFound`
  - HTTP 400 Bad Request
  - Returned when basket or resource doesn't exist

- **Feign Exceptions** - All external API errors:
  - BadRequest (400) → HTTP 502 Bad Gateway
  - Unauthorized (401) → HTTP 401 Unauthorized
  - Forbidden (403) → HTTP 403 Forbidden
  - NotFound (404) → HTTP 404 Not Found
  - TooManyRequests (429) → HTTP 429 Too Many Requests (with "retry later" message)
  - InternalServerError (500) → HTTP 502 Bad Gateway
  - ServiceUnavailable (503) → HTTP 503 Service Unavailable

### Error Response Format

All errors are returned in a consistent `ErrorResponse` format:

```json
{
  "timestamp": "2024-01-15T10:30:45.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Error description",
  "path": "/api/basket",
  "fieldErrors": {
    "clientId": "must not be null",
    "productsRequest": "must not be empty"
  }
}
```

**Response Fields**:
- `timestamp`: ISO 8601 timestamp when error occurred
- `status`: HTTP status code
- `error`: Error category/type
- `message`: Detailed error message
- `path`: Request URI that triggered the error
- `fieldErrors`: Validation errors per field (only present for validation errors)

## Data Model

### Basket Entity

Stored in MongoDB collection: `basket`

```json
{
  "_id": "ObjectId",
  "client": 1,
  "totalPrice": 199.98,
  "products": [
    {
      "id": 1,
      "title": "Wireless Headphones",
      "price": 99.99,
      "quantity": 2
    }
  ],
  "status": "OPEN",
  "payMethod": "PIX"
}
```

**Fields**:
- `_id` (ObjectId): MongoDB auto-generated document identifier
- `client` (Long): Client/user ID from the request
- `totalPrice` (BigDecimal): Sum of (product price × quantity) for all products
- `products` (Array): List of Product objects with:
  - `id` (Long): Product ID from Platzi Store API
  - `title` (String): Product name
  - `price` (BigDecimal): Product price (from API at time of basket creation)
  - `quantity` (Integer): Quantity of this product in basket
- `status` (String): Basket state - `OPEN` or `SOLD`
- `payMethod` (String/Enum): Payment method - `PIX`, `CREDIT`, or `DEBIT` (null if not yet paid)

### External API Product Model

Products are fetched from Platzi Store API with the following structure:

```json
{
  "id": 1,
  "title": "Wireless Headphones",
  "price": 99.99
}
```

**Fields**:
- `id` (Long): Unique product identifier from Platzi API
- `title` (String): Product name/description
- `price` (BigDecimal): Current product price in Platzi API

**Note**: Product information is cached in Redis for improved performance.

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
  ├── BasketController       # 5 endpoints for basket operations
  ├── ProductController      # 2 endpoints for product queries
  ├── request/               # Request DTOs with validation
  │   ├── BasketRequest
  │   ├── ProductRequest
  │   └── PaymentRequest
  └── response/              # Response DTOs
      └── ErrorResponse

Service/              # Business logic layer
  ├── BasketService         # Basket CRUD, payment processing
  └── ProductService        # Product queries with caching

repository/           # Data access layer
  └── BasketRepository      # MongoDB operations

entity/               # Domain models
  ├── Basket                # Main entity (stored in MongoDB)
  ├── Product               # Embedded in Basket
  ├── Status                # Enum: OPEN, SOLD
  └── PaymentMethod         # Enum: PIX, CREDIT, DEBIT

client/               # External API clients
  ├── PlatziStoreClient     # OpenFeign REST client
  └── response/
      └── PlatziProductResponse

exception/            # Custom exceptions
  ├── BadRequest
  └── EntityNotFound

config/               # Spring configuration
  ├── DotenvConfig         # .env file loading
  ├── OpenApiConfig        # Swagger/OpenAPI setup
  └── ApplicationControllerAdvice  # Global exception handling (12 handlers)
```

### Key Design Patterns

1. **Repository Pattern**: `BasketRepository` extends `MongoRepository` for data access
2. **Service Layer Pattern**: Business logic isolated in `BasketService` and `ProductService`
3. **DTO Pattern**: Separate request/response objects for API contracts
4. **Exception Handling**: Centralized with `@RestControllerAdvice` and custom exceptions
5. **Caching Pattern**: `@Cacheable` annotations with Redis backend
6. **Feign Client Pattern**: Declarative REST client for external APIs

### Data Flow

```
Client Request
    ↓
Controller (validates input)
    ↓
Service (business logic, caching)
    ↓
Repository / External Client
    ↓
MongoDB / Platzi API
    ↓
Response (consistent ErrorResponse format)
```

## License

This project is for educational purposes.
