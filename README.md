# SmashIt Server

Backend service for **SmashIt**, a badminton court booking and management platform. It handles everything a court facility needs to run day-to-day: online bookings and payments, member accounts, staff scheduling, equipment/inventory, promotions, and social features for players to find and play with each other.

Built with **Spring Boot** (Java 21) using a modular monolith architecture ([Spring Modulith](https://spring.io/projects/spring-modulith)).

## Features

- **Identity & Auth** — customer/member and admin/employee authentication with JWT access & refresh tokens, employee scheduling
- **Booking** — court order management, order details, invoicing, and payment processing (including [ZaloPay](https://www.zalopay.vn/) integration)
- **Resource** — facilities and courts, facility categories/criteria, broken equipment reports, and maintenance tracking
- **Inventory** — products, product categories, stock imports, and stocktaking
- **Promotion** — configurable promotion rules and discount engine
- **Social** — "Together" matchmaking for members to team up and play, plus real-time chat over WebSocket/STOMP

## Tech Stack

| Concern | Technology |
|---|---|
| Language / Runtime | Java 21 |
| Framework | Spring Boot, Spring Modulith |
| Primary database | PostgreSQL (via Spring Data JPA/Hibernate) |
| Secondary database | MongoDB (via Spring Data MongoDB) |
| Caching / sessions | Redis |
| Messaging | RabbitMQ (STOMP over WebSocket for real-time chat) |
| Auth | JWT (jjwt) |
| Email | SMTP via Brevo |
| Payments | ZaloPay |
| API docs | springdoc-openapi (Swagger UI) |
| Build tool | Gradle (Kotlin DSL) |

## Prerequisites

- JDK 21
- Docker & Docker Compose (recommended, for Postgres/MongoDB/RabbitMQ)
- A [Brevo](https://www.brevo.com/) account for sending transactional email (optional in dev)
- ZaloPay sandbox credentials (optional, only needed to test payments)

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/UIT-SmashIt/server.git
cd server
```

### 2. Configure environment variables

The app reads its configuration from `src/main/resources/application.yaml`, with sensible env-var overrides. Create a `.env` file (used by `docker-compose.yml`) with values such as:

```env
POSTGRES_USERNAME=your_postgres_username
POSTGRES_PASSWORD=your_postgres_password

MONGO_HOST=mongodb
MONGO_PORT=27017
MONGO_DB=your_mongo_db
MONGO_USER=admin
MONGO_PASS=your_mongo_password

JWT_SECRET=at_least_32_characters_long_secret

BREVO_LOGIN=your_brevo_login_email
BREVO_PASSWORD=your_brevo_smtp_password
BREVO_EMAIL=your_verified_sender_email

ZALOPAY_APP_ID=your_zalopay_app_id
ZALOPAY_KEY1=your_zalopay_key1
ZALOPAY_KEY2=your_zalopay_key2

NGROK_AUTHTOKEN=your_ngrok_authtoken   # optional, only if you use the ngrok service
NGROK_DOMAIN=your-static-domain.ngrok.io
```

### 3. Run with Docker Compose (recommended)

This spins up PostgreSQL, MongoDB, RabbitMQ, the backend itself, and an optional ngrok tunnel:

```bash
docker compose up --build
```

The API will be available at `http://localhost:8080`.

### 4. Run locally without Docker

Start Postgres, MongoDB, and RabbitMQ yourself (or via `docker compose up postgres mongodb rabbitmq`), export the environment variables above, then run:

```bash
./gradlew bootRun
```

On Windows, use `gradlew.bat bootRun`.

### 5. Run tests

```bash
./gradlew test
```

## API Documentation

Once the app is running, Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

Raw OpenAPI spec: `http://localhost:8080/v3/api-docs`

### Notable endpoint groups

| Base path | Purpose |
|---|---|
| `/api/auth`, `/api/admin/auth` | Customer / admin login, registration, token refresh |
| `/api/customer/member`, `/api/admin/member` | Member profile management |
| `/api/admin/employee`, `/api/admin/schedule` | Employee accounts and work schedules |
| `/api/facility`, `/api/facility-category`, `/api/facility-criterion` | Facility catalog |
| `/api/admin/court` | Court management |
| `/api/broken-report`, `/api/maintain` | Equipment fault reporting & maintenance |
| `/api/admin/product`, `/api/admin/product/category`, `/api/admin/product/import` | Inventory management |
| `/api/customer/order`, `/api/admin/order` | Court/product orders |
| `/api/payment` | Payment processing (ZaloPay) |
| `/api/admin/promotion`, `/api/operator/promotion` | Promotions and discounts |
| `/api/member/together` | Player matchmaking / social features |

## Project Structure

The codebase is organized as a set of Spring Modulith application modules, each owning its own entities, repositories, and services:

```
src/main/java/edu/uit/se122/server/
├── booking/       # Orders, invoices, payments
├── common/        # Shared config, security, exceptions, enums
├── identity/       # Auth, members, employees, schedules
├── inventory/     # Products and stock
├── promotion/      # Promotion rules and discount engine
├── resource/       # Facilities, courts, maintenance
└── social/        # Matchmaking and real-time chat
```

Each module exposes its public API at the top level of its package and keeps implementation details under an `internal` sub-package.

## Contributing

Issues and pull requests are welcome. Please make sure `./gradlew test` passes before submitting a PR.
