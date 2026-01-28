# javablog Services

REST API backend using Spring Boot with hexagonal architecture.

## Architecture

```
services/
├── domain/              # Pure Java domain model (no Spring)
├── application/         # Use cases and services
├── adapter-rest/        # REST controllers (inbound)
├── adapter-persistence/ # JPA repositories (outbound)
└── bootstrap/           # Spring Boot entry point
```

## Prerequisites

- Java 21
- Maven 3.9+
- PostgreSQL 17

## Build

```bash
mvn clean install
```

## Run

### Option 1: Docker Compose (recommended)

Starts both PostgreSQL and the API:

```bash
docker-compose up
```

### Option 2: Podman

Start PostgreSQL:

```bash
podman run -d \
  --name javablog-postgres \
  -e POSTGRES_USER=javablog \
  -e POSTGRES_PASSWORD=javablog \
  -e POSTGRES_DB=javablog \
  -p 5432:5432 \
  postgres:17-alpine
```

Then run the application:

```bash
mvn -pl bootstrap spring-boot:run
```

### Option 3: Existing PostgreSQL

Set environment variables and run:

```bash
export JAVABLOG_DB_HOST=localhost
export JAVABLOG_DB_PORT=5432
export JAVABLOG_DB_NAME=javablog
export JAVABLOG_DB_USER=your_user
export JAVABLOG_DB_PASS=your_password

mvn -pl bootstrap spring-boot:run
```

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `JAVABLOG_DB_HOST` | `localhost` | PostgreSQL host |
| `JAVABLOG_DB_PORT` | `5432` | PostgreSQL port |
| `JAVABLOG_DB_NAME` | `javablog` | Database name |
| `JAVABLOG_DB_USER` | `javablog` | Database user |
| `JAVABLOG_DB_PASS` | `javablog` | Database password |

## Debug

The application starts with JDWP debug enabled on port 5005. Connect your IDE's remote debugger to `localhost:5005`.

## Test

Tests use H2 in-memory database (no PostgreSQL required):

```bash
mvn verify
```
