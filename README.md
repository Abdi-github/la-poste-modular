# 📮 La Poste Modular — Swiss Postal Service Platform

A **production-ready modular monolith** built with Spring Boot 3, Spring Modulith, and Thymeleaf, modeling the Swiss postal service (Die Post / La Poste / La Posta).

## Tech Stack

| Layer | Technology |
|---|---|
| **Runtime** | Java 21, Spring Boot 3.4 |
| **Architecture** | Spring Modulith (event-driven modules) |
| **Security** | Keycloak (OAuth2 + OIDC), dual chain (JWT API + session Web) |
| **Database** | PostgreSQL 17, Flyway migrations |
| **Caching** | Redis 7 |
| **Frontend** | Thymeleaf + Bootstrap 5 + HTMX (admin dashboard) |
| **API** | REST (ready for React/Angular frontend) |
| **Mapping** | MapStruct |
| **i18n** | DE / FR / IT / EN (Swiss national languages) |
| **Docs** | OpenAPI 3 / Swagger UI |
| **Infra** | Docker Compose (dev + prod), Nginx reverse proxy |

## Modules

| Module | Description |
|---|---|
| `user` | Employees, customers, roles & permissions |
| `branch` | Post office branches with multilingual translations |
| `address` | Swiss address registry |
| `parcel` | Parcel lifecycle (create, track, cancel) |
| `delivery` | Delivery routes, slots, pickup requests |
| `tracking` | Tracking timeline & scan events |
| `notification` | Email notifications with i18n templates |
| `dashboard` | Admin dashboard (Thymeleaf web views) |
| `shared` | Cross-cutting: config, DTOs, events, exceptions, i18n |

## Quick Start

### Prerequisites
- Java 21+
- Docker & Docker Compose
- Maven (or use `./mvnw`)

### Development

```bash
# Start infrastructure (PostgreSQL, Redis, pgAdmin, Mailpit)
make dev-infra

# Run app locally
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# OR: Start everything in Docker (with hot-reload)
make dev
```

### Access Points

| Service | URL |
|---|---|
| **Dashboard** | http://localhost:8080/dashboard |
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **pgAdmin** | http://localhost:5050 |
| **Mailpit** | http://localhost:8028 |

### Production

```bash
# Copy and configure production env
cp .env.example .env.prod
# Edit .env.prod with real values

# Deploy
make prod-up
```

## Makefile Commands

```
make help          Show all commands
make dev           Start full dev stack
make dev-infra     Start infra only
make down          Stop all containers
make build         Build JAR (skip tests)
make test          Run tests
make db-reset      Reset database
make prod-up       Deploy production
make prod-down     Stop production
```

## API Design

All REST endpoints are under `/api/v1/**` and protected with JWT (Keycloak).
The Thymeleaf dashboard uses OAuth2 session-based login.

The API is designed to be consumed by a future React or Angular admin dashboard.

## Project Structure

```
src/main/java/ch/swiftapp/laposte/
├── shared/          # Cross-cutting concerns
│   ├── config/      # Security, i18n, WebMvc, CORS
│   ├── constants/   # API paths, supported locales
│   ├── dto/         # ApiResponse, PagedResponse
│   ├── event/       # Domain event records
│   ├── exception/   # Business + NotFound exceptions
│   ├── i18n/        # Translation utilities
│   └── model/       # BaseEntity
├── user/            # Employee & Customer management
├── branch/          # Branch management with i18n
├── address/         # Swiss address registry
├── parcel/          # Parcel lifecycle
├── delivery/        # Delivery routes & pickups
├── tracking/        # Tracking timeline
├── notification/    # Notification engine
└── dashboard/       # Thymeleaf admin dashboard
```

