## Quick Start

Clone the repository and run with Docker Compose:

```bash
docker compose up --build
```

The application will be available at: `http://localhost:8080/api`

## Services

| Service | Port | Description |
|---------|------|-------------|
| app     | 8080 | Spring Boot API |
| db      | 5432 | PostgreSQL database |

## API Endpoints

Base URL: `http://localhost:8080/api`

### Authentication
- `POST /v1/auth/register` - Register new user
- `POST /v1/auth/login` - Login and get JWT token

some more are available

### Usage
Include the JWT token in the `Authorization` header:
```
Authorization: Bearer <your-token>
```

## Stopping the Application

```bash
docker compose down
```

To also remove the database volume:
```bash
docker compose down -v
```

## Development

To run locally without Docker (requires local PostgreSQL):

1. Start PostgreSQL on `localhost:5432` with database `gym`
2. Run: `./mvnw spring-boot:run`
