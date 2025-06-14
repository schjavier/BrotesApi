# Brotes Order Management API

## Overview

This is a modern, robust RESTful API built with Spring Boot for managing orders, products, and customer information for "Brotes" business. The application follows industry best practices with a clean architecture, comprehensive testing, and a modern tech stack.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.4-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8-blue)
![JUnit 5](https://img.shields.io/badge/JUnit-5.10.0-green)
![Mockito](https://img.shields.io/badge/Mockito-5.5.0-yellow)

## Features

- **Customer Management**: Register, update, activate/deactivate, and query customer information
- **Product Management**: Create, update, categorize products with inventory tracking
- **Order Processing**: Create, track, and manage customer orders with line items
- **Data Validation**: Comprehensive input validation across all entities
- **Exception Handling**: Global exception handling with meaningful error responses
- **RESTful Architecture**: Well-designed API endpoints following REST principles
- **Database Migrations**: Versioned database schema management with Flyway

## Tech Stack

### Backend
- **Java 17**: Latest LTS version with modern language features
- **Spring Boot 3.1.4**: For rapid application development
- **Spring Data JPA**: ORM for database operations
- **Spring Validation**: For input validation
- **Spring Web MVC**: For RESTful API development
- **Lombok**: To reduce boilerplate code
- **MySQL**: Relational database for data persistence
- **Flyway**: Database migration and version control

### Testing
- **JUnit 5**: Modern testing framework
- **Mockito**: Mocking framework for unit tests
- **Spring Boot Test**: For integration testing

## Architecture

The application follows a clean, layered architecture:

- **Controllers**: Handle HTTP requests/responses and API endpoints
- **Services**: Contain business logic and validation
- **Repositories**: Manage data access and database operations
- **Models**: Define entity classes and data transfer objects
- **Exceptions**: Custom exception handling
- **Validations**: Business rule validations

## Project Structure

```
api/
├── src/
│   ├── main/
│   │   ├── java/com/brotes/api/
│   │   │   ├── config/             # Application configurations
│   │   │   ├── controller/         # REST controllers
│   │   │   ├── exceptions/         # Custom exceptions
│   │   │   ├── modelo/             # Domain models
│   │   │   │   ├── categoria/      # Product categories
│   │   │   │   ├── cliente/        # Customer management
│   │   │   │   ├── itemPedido/     # Order items
│   │   │   │   ├── pedidos/        # Order processing
│   │   │   │   └── producto/       # Product management
│   │   │   └── validations/        # Business validations
│   │   └── resources/
│   │       └── db/migration/       # Flyway SQL migration scripts
│   └── test/                       # Comprehensive test suite
└── pom.xml                         # Maven dependencies
```

## Testing Strategy

The application has a comprehensive test suite:

- **Unit Tests**: For testing individual components in isolation
- **Service Tests**: For business logic validation
- **Integration Tests**: For testing component interactions

The test coverage includes:
- Customer service tests
- Product service tests
- Order service tests
- Utility tests