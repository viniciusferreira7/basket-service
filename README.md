# Basket Service

> **Work In Progress** - This project is currently under active development.

## Overview

Basket Service is a microservice application built with Spring Boot that manages shopping basket functionality. It uses MongoDB for data persistence and Redis for caching.

## Tech Stack

- **Java** - Spring Boot
- **MongoDB** - NoSQL database for storing basket data
- **Redis** - In-memory cache for improved performance
- **Docker** - Containerization for MongoDB and Redis services

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Maven

## Configuration

The application uses environment variables for configuration. Copy the `.env.example` file to `.env` and adjust the values as needed:

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

## Docker Services

The `docker-compose.yaml` file includes:

- **MongoDB 7.0** - Available on port 27017
- **Redis 7.2** - Available on port 6379

To stop the services:

```bash
docker-compose down
```

To stop and remove volumes:

```bash
docker-compose down -v
```

## License

This project is for educational purposes.
