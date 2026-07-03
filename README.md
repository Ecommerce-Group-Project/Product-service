# Product Service

This microservice manages product data for the e-commerce system. It provides a RESTful API to create, retrieve, and delete products, and persists all data to a dedicated PostgreSQL database running in a Docker container.

---

## 📋 Overview

| Property | Value |
|---|---|
| Service Name | product-service |
| Port | 8081 |
| Database | PostgreSQL 15 (Docker container) |
| Database Name | product_db |
| Framework | Spring Boot 3.5.x |
| Java Version | 17 |

---

## 🏗️ Architecture

```
Client (Postman)  --REST-->  Product Service  --JPA-->  PostgreSQL (Docker)
                                   ↑
                         Order Service calls
                         GET /api/products/{id}
                         to fetch product details
```

The Product Service operates independently and exposes product data via REST APIs. The **Order Service** consumes this service to fetch product details when creating an order.

---

## 🔧 Tech Stack

- Java 17
- Spring Boot 3.5.x
- Spring Data JPA + Hibernate
- PostgreSQL 15 (Docker)
- Lombok
- SpringDoc OpenAPI (Swagger)
- Maven
- JUnit 5 + Mockito

---

## 📦 API Endpoints

| Method | Endpoint | Description | Response |
|---|---|---|---|
| `POST` | `/api/products` | Create a new product | 201 Created |
| `GET` | `/api/products/{id}` | Get product by ID | 200 OK / 404 |
| `DELETE` | `/api/products/{id}` | Delete product by ID | 204 No Content / 404 |

### Request Body (POST /api/products)

```json
{
  "name": "Laptop",
  "unitPrice": 999.99,
  "description": "High performance laptop",
  "category": "Electronics",
  "stock": 50
}
```

### Response Body

```json
{
  "productId": 1,
  "name": "Laptop",
  "unitPrice": 999.99,
  "description": "High performance laptop",
  "category": "Electronics",
  "stock": 50,
  "createdAt": "2026-06-18T10:30:00"
}
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17+ (JDK)
- Maven 3.9+
- Docker Desktop
- Git

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/product-service.git
cd product-service
```

### 2. Create the `.env` file

Create a `.env` file in the project root (same level as `pom.xml`):

```env
DB_HOST=localhost
DB_PORT=5433
DB_NAME=product_db
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
SERVER_PORT=8081
```

> ⚠️ Never commit the `.env` file to GitHub. It is already listed in `.gitignore`.

### 3. Start PostgreSQL (Docker)

```bash
docker-compose up -d
```

Verify the container is running:

```bash
docker ps
```

You should see `product-db` listed with status `Up (healthy)`.

### 4. Run the application

Run `ProductserviceApplication.java` directly from IntelliJ, or via terminal:

```bash
mvn spring-boot:run
```

The service starts on **port 8081** and connects to PostgreSQL at `localhost:5433`.  
Hibernate automatically creates the `product` table on first startup.

### 5. Access API docs (Swagger)

```
http://localhost:8081/swagger-ui/index.html
```

### 6. Run unit tests

```bash
mvn test
```

Tests use an H2 in-memory database — no Docker required for testing.

---

## ⚙️ Configuration

Database credentials are configured via environment variables in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5433}/${DB_NAME:product_db}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
```

To override defaults, set environment variables in your `.env` file or in the IntelliJ Run Configuration under **Environment Variables**.

---

## 🐳 Docker — PostgreSQL Container

The `docker-compose.yml` provisions a dedicated PostgreSQL 15 container for this service:

```yaml
services:
  product-db:
    image: postgres:15-alpine
    container_name: product-db
    environment:
      POSTGRES_DB: product_db
      POSTGRES_USER: your_db_username
      POSTGRES_PASSWORD: your_db_password
    ports:
      - "5433:5432"    # Host 5433 → Container 5432
    volumes:
      - product-db-data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U your_db_username -d product_db -p 5432"]
      interval: 10s
      timeout: 5s
      retries: 5
```

> Port `5433` is used on the host to avoid conflicts with any locally installed PostgreSQL instance (which uses `5432`).

### Useful Docker commands

```bash
# Start container
docker-compose up -d

# Stop container (keeps data)
docker-compose down

# Stop and wipe all data (fresh start)
docker-compose down -v

# Check running containers
docker ps

# View container logs
docker-compose logs -f product-db

# Connect to database directly
docker exec -it product-db psql -U your_db_username -d product_db

# Verify product table exists
docker exec -it product-db psql -U your_db_username -d product_db -c "\dt"
```

---

## 📁 Project Structure

```
product-service/
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/productservice/
│   │   │   ├── config/
│   │   │   │   └── SwaggerConfig.java              # OpenAPI / Swagger setup
│   │   │   ├── controller/
│   │   │   │   └── ProductController.java          # REST endpoints
│   │   │   ├── service/
│   │   │   │   ├── ProductService.java             # Service interface
│   │   │   │   └── ProductServiceImpl.java         # Business logic
│   │   │   ├── repository/
│   │   │   │   └── ProductRepository.java          # JPA data access
│   │   │   ├── entity/
│   │   │   │   └── Product.java                    # JPA entity → product table
│   │   │   ├── dto/
│   │   │   │   ├── ProductRequest.java             # API input with validation
│   │   │   │   └── ProductResponse.java            # API output
│   │   │   ├── exception/
│   │   │   │   ├── ProductNotFoundException.java   # Custom exception
│   │   │   │   └── GlobalExceptionHandler.java     # @RestControllerAdvice
│   │   │   └── ProductserviceApplication.java      # Entry point
│   │   └── resources/
│   │       └── application.yml                     # Config with env variables
│   └── test/
│       ├── java/com/ecommerce/productservice/
│       │   ├── controller/
│       │   │   └── ProductControllerTest.java      # MockMvc controller tests
│       │   └── service/
│       │       └── ProductServiceImplTest.java     # Mockito service tests
│       └── resources/
│           └── application.yml                     # H2 in-memory DB for tests
├── sql/
│   └── init.sql                                    # DB init script (optional)
├── docker-compose.yml                              # PostgreSQL container
├── Dockerfile                                      # Multi-stage container build
├── .env                                            # Environment variables (gitignored)
├── .gitignore
└── pom.xml
```

---

## 🧪 Unit Tests

| Test Class | Tests | Coverage |
|---|---|---|
| `ProductServiceImplTest` | 9 | createProduct, getProductById, deleteProduct — all success and error paths |
| `ProductControllerTest` | 9 | All 3 endpoints — valid requests, validation errors, 404 responses |

```bash
# Run all tests and see results
mvn test

# Expected output
Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
```

---

## ✅ Features

- [x] Create, retrieve, and delete products via REST API
- [x] PostgreSQL database via Docker container
- [x] Hibernate auto-creates `product` table on startup
- [x] Input validation with `@Valid`, `@NotBlank`, `@Positive`
- [x] Global exception handling with consistent JSON error responses
- [x] Swagger UI for API documentation
- [x] Unit tests for service and controller layers (JUnit 5 + Mockito)
- [x] Environment-variable based configuration (no hardcoded credentials)
- [x] SOLID principles and layered architecture
- [x] Dockerized with multi-stage Dockerfile

---

## 👥 Related Microservices

| Service | Repository |
|---|---|
| Product Service | this repository |
| Order Service | [_link here_](https://github.com/Ecommerce-Group-Project/Order-service) |
| Notification Service | [_link here_](https://github.com/Ecommerce-Group-Project/Notification-service) |

---
