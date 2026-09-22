# bookeraV2

A Goodreads-style REST API. Users register, track books on a shelf
(want-to-read / currently-reading / read), rate and review them, and browse
authors and categories.

Built as a portfolio project focused on production-shaped Spring Boot: JWT auth,
schema migrations, validation, integration tests, containerized runtime, and
caching.

## Tech stack

| Layer | Choice |
|---|---|
| Language | Java 26 |
| Framework | Spring Boot 4 (Web MVC, Security, Data JPA) |
| Database | PostgreSQL 16, schema managed by Flyway |
| Cache | Redis 7 via Spring Cache / `@Cacheable` |
| Auth | JWT (jjwt) with role-based access (USER / ADMIN) |
| API docs | springdoc-openapi (Swagger UI) |
| Build | Maven |
| Runtime | Docker / Docker Compose |

## Features

- JWT register/login; read-only catalog is public, writes require a token
- Book / author / category CRUD with validation and a global error handler
- Reviews bound to the authenticated user, with average rating per book
- User shelf with reading status (`IN_PLANS`, `READING`, `DONE`)
- Image upload for books, authors, and avatars (local filesystem)
- Redis caching on book details
- Actuator endpoints (health, metrics, ...) under `/m`

## Getting started

### Prerequisites

- Docker + Docker Compose (easiest), or
- Java 26 + Maven + a local PostgreSQL + Redis

### Run everything with Docker Compose

```bash
export JWT_SECRET=change-me-to-a-long-random-string
docker compose up --build
```

This starts the API (`:8082`), PostgreSQL, and Redis. Flyway migrates and seeds
the database on startup.

- Swagger UI: http://localhost:8082/swagger-ui.html
- Health: http://localhost:8082/m/health

### Run locally

Start PostgreSQL (database `bookera`) and Redis, then:

```bash
export DB_PASSWORD=your-password
export JWT_SECRET=change-me-to-a-long-random-string
export SPRING_DATA_REDIS_HOST=localhost   # app defaults to host "redis" for Docker
./mvnw spring-boot:run
```

### Environment variables

| Variable | Required | Default | Notes |
|---|---|---|---|
| `DB_URL` | no | `jdbc:postgresql://localhost:5432/bookera` | JDBC URL |
| `DB_USERNAME` | no | `postgres` | Database user |
| `DB_PASSWORD` | yes | – | Database password |
| `JWT_SECRET` | yes | – | HMAC signing key for JWTs |
| `SPRING_DATA_REDIS_HOST` | no | `redis` | Override for local runs |
| `SPRING_DATA_REDIS_PORT` | no | `6379` | Redis port |

## API overview

All endpoints are under `/api`. Protected routes need
`Authorization: Bearer <token>`.

| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/register` | – | Register, returns JWT |
| POST | `/api/auth/login` | – | Login, returns JWT |
| GET | `/api/books` | – | Paginated books (`page`, `size`, `sortBy`, `ascending`) |
| GET | `/api/books/{id}` | – | Book details (cached) |
| POST | `/api/books` | token | Create book |
| PUT | `/api/books/{id}` | token | Update book |
| DELETE | `/api/books/{id}` | token | Delete book |
| POST | `/api/books/upload/{bookId}` | token | Upload book picture (multipart `file`) |
| GET | `/api/authors` / `/{id}` | – | Authors |
| POST/PUT/DELETE | `/api/authors` | token | Manage authors |
| POST | `/api/authors/upload/{authorId}` | token | Upload author picture |
| GET | `/api/categories` / `/{id}` | – | Categories |
| POST/PUT/DELETE | `/api/categories` | token | Manage categories |
| POST | `/api/reviews/{bookId}` | token | Create review |
| PUT/DELETE | `/api/reviews/{id}` | token | Update / delete own review |
| GET | `/api/users/me` | token | Current profile |
| GET/POST/PUT | `/api/users/shelf` | token | Read / add / update shelf entry |
| POST | `/api/users/upload` | token | Upload avatar |

Shelf `statusId`: `1` = IN_PLANS, `2` = READING, `3` = DONE.

## Example requests

```bash
# Register
curl -s -X POST localhost:8082/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"alice","password":"secret","name":"Alice","surname":"Doe"}'

# Login (returns {"token": "...", ...})
TOKEN=$(curl -s -X POST localhost:8082/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"alice","password":"secret"}' | jq -r .token)

# Browse books (public)
curl -s "localhost:8082/api/books?page=0&size=10&sortBy=name&ascending=true"

# Create a book (needs authorId)
curl -s -X POST localhost:8082/api/books \
  -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
  -d '{"name":"Dune","isbn":"9780441013593","totalPages":412,
       "description":"...","dateOfPublish":"1965-08-01","authorId":1}'

# Add a book to the shelf
curl -s -X POST localhost:8082/api/users/shelf \
  -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
  -d '{"bookId":1,"statusId":2}'

# Review a book
curl -s -X POST localhost:8082/api/reviews/1 \
  -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
  -d '{"content":"Great read","rating":5}'
```

## Project structure

```
src/main/java/personal/bookerav2
├── configs/      Spring config (security, cache, web, OpenAPI)
├── controller/   REST endpoints
├── dto/          Request/response records + mappers
├── entities/     JPA entities and enums
├── exceptions/   Domain exceptions
├── handlers/     Global exception handler
├── repository/   Spring Data repositories
├── security/     JWT service and authentication filter
├── service/      Business logic
└── util/         Helpers (pagination)

src/main/resources/db/migration   Flyway migrations (V1 ...)
```

## Testing

```bash
./mvnw test
```

Unit tests cover services, the JWT service, the exception handler, and
pagination, backed by an H2 database for slice tests.

## Known limitations / next steps

- Cache invalidation is incomplete: review changes do not evict `BOOK_CACHE`,
  so a book's rating can be stale for the TTL (5 min); `updateBookImage` may
  return a cached pre-upload DTO.
- Actuator is exposed at `/m/**` with `permitAll` and `include=*`, including
  `heapdump`;
- Pictures are stored on the local filesystem; a production deployment would
  swap this for object storage (S3/MinIO).
