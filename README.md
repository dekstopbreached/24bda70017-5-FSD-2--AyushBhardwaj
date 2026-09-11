# Post Service – Spring Boot REST API

A Spring Boot REST API demonstrating REST API design principles, request validation,
standardized responses, global exception handling, CORS, and correlation-ID based
request tracing.

Built as part of **Unit 2 – Experiment 5: Spring Boot REST API Design & Exception Handling**.

## Features

- **CRUD REST API** for posts (`/api/posts`)
- **Bean Validation** (`@NotBlank`, `@Size`) for request payloads
- **Standardized API responses** via a generic `ApiResponse<T>` wrapper
- **Global exception handling** using `@ControllerAdvice`
- **CORS configuration** for cross-origin frontend integration
- **Request logging filter** — logs URI, method, and execution time
- **Correlation ID tracing** — every request gets a UUID (via `MDC` + `X-Correlation-Id` header) so logs can be traced end-to-end

## Tech Stack

- Java 17
- Spring Boot 3.x (Spring Web, Spring Validation)
- Maven
- SLF4J + Logback (MDC for correlation IDs)
- JUnit 5 + MockMvc (testing)

## Project Structure
