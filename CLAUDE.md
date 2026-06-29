# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 3.3.1 / Java 21 application demonstrating RabbitMQ messaging patterns, PostgreSQL persistence via JPA + Flyway, JWT authentication, and a REST API with a vanilla JS frontend.

## Build & Run

```powershell
# Start infrastructure (RabbitMQ + PostgreSQL)
docker-compose up -d

# Build
mvn clean package

# Run
mvn spring-boot:run
```

App runs on `http://localhost:8080`. RabbitMQ Management UI at `http://localhost:15672` (guest/guest).

## Architecture

### RabbitMQ exchange configs (`org.example`)
Three exchange strategies, each in its own `@Configuration` class:
- `RabbitMQConfig` — Direct exchange (`hello.exchange`), routing by exact key
- `FanoutRabbitMQConfig` — Fanout exchange (`fanout.exchange`), broadcasts to all bound queues; no routing key
- `TopicRabbitMQConfig` — Topic exchange (`topic.exchange`), wildcard routing (`*` = one word, `#` = zero or more)
- `HeadersRabbitMQConfig` — Headers exchange (`headers.exchange`), routes by message headers; `whereAll` = x-match:all, `whereAny` = x-match:any

`MessageConsumer` listens on `hello.queue` via `@RabbitListener`.

### Security (`org.example.security`, `org.example.config`)
Stateless JWT auth. Flow:
1. Client calls `POST /api/auth/register` or `POST /api/auth/login` → receives `{token, username}`
2. Token is sent as `Authorization: Bearer <token>` on subsequent requests
3. `JwtAuthFilter` (OncePerRequestFilter) extracts and validates the token, sets `SecurityContextHolder`
4. `SecurityConfig` uses `@Lazy JwtAuthFilter` to avoid circular dependency with the `UserDetailsService` bean defined in the same class

JWT secret and expiration are configured in `application.properties` (`jwt.secret`, `jwt.expiration`).

### Persistence
- Flyway migrations in `src/main/resources/db/migration/` (naming: `V{n}__{description}.sql`)
- `ddl-auto=validate` — Hibernate validates schema against migrations, never modifies it
- Entities: `Message` (table `messages`), `User` (table `users`)

### REST API
All endpoints under `/api/` require `Authorization: Bearer <token>` except `/api/auth/**`.

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/auth/register` | Register, returns JWT |
| POST | `/api/auth/login` | Login, returns JWT |
| GET | `/api/messages` | List all messages |
| GET | `/api/messages/{id}` | Get by id |
| POST | `/api/messages` | Create message |
| DELETE | `/api/messages/{id}` | Delete message |

### Frontend
Single-page vanilla JS app served from `src/main/resources/static/index.html`. Stores JWT in `localStorage`. Shows login/register screen until authenticated, then the messages UI.

## Infrastructure

`docker-compose.yml` starts two services:
- `rabbitmq` — built from local `Dockerfile`, ports 5672 (AMQP) and 15672 (UI)
- `postgres:16` — database `rabbit_db`, user/password `postgres`, port 5432