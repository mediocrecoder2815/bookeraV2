# Roadmap — bookeraV2 → Goodreads-like API

**Vision:** a Goodreads-style book-tracking API. Users register, track books
(want-to-read / currently-reading / read), rate & review them, and browse authors
and categories. Everything currently "unused" (`users`, `book_user`, `BookStatus`,
reviews) becomes core functionality — nothing gets cut.

**Note:** Phase 0 items are real bugs found during manual QA (see `report.md`).

---

## Phase 0 — Fix the foundation (bugs from manual QA)

- [x] Fix `POST /api/authors` → 500: NPE in `AuthorMapper.toResponseDto` because
      `Author.books` is never initialized (`Author.java:36`). Initialize it or null-guard the mapper
- [x] `pictureUrl`: add to `BookDtoRequest`/`AuthorDtoRequest` + mappers; persist on create **and** update
- [ ] Add `@Valid` + `@NotBlank`/`@NotNull`/`@Size` to all request DTOs; missing required fields → 400
- [ ] Handle `DataIntegrityViolationException` → 409 in `GlobalExceptionHandler` (FK-constrained deletes currently 500)
- [ ] Validate query params — whitelist `sortBy` columns, reject negative `page` → 400
- [ ] Tests for the error handler: 400 / 404 / 409 paths (only service-layer Mockito tests exist today)
- [ ] Remove unused imports / compile warnings

## Phase 1 — DB safety + observability

- [ ] Flyway: `V1__init.sql` from current schema, seed via migration
- [ ] Kill `data.sql` auto re-seed (`spring.sql.init.mode=always` + `DELETE FROM` wipes all data on every startup)
- [ ] `ddl-auto=update` → `validate` (Flyway owns the schema)
- [ ] Add `spring-boot-starter-actuator` + `/actuator/health`
- [ ] `docker-compose.yml` (app + postgres, secrets via env vars) + multi-stage `Dockerfile`

## Phase 2 — Users & auth (Goodreads core #1)

- [ ] Spring Security + JWT:
      `POST /api/auth/register`, `POST /api/auth/login` → JWT
- [ ] Protect write endpoints; keep read-only catalog (`GET`) public
- [ ] `UserService.getCurrentUser()` from JWT — no more guessing `userId` from request bodies
- [ ] Uncomment/adapt `SecurityConfig.java`

## Phase 3 — Book tracking (Goodreads core #2)

- [ ] Wire up `book_user` + `BookStatus`: `PUT /api/me/books/{bookId}` (status change), `GET /api/me/books?status=READ`
- [ ] `BookDtoResponse` includes the current user's status/rating for that book
- [ ] Reviews bound to the authenticated user (drop `userId` from `ReviewDtoRequest`)
- [ ] Public user profile: `GET /api/users/{id}` → shelves, reviews, stats

## Phase 4 — Goodreads depth

- [ ] Average rating + rating distribution per book (aggregation query)
- [ ] Custom bookshelves (user-defined many-to-many)
- [ ] Reading progress: % complete, start/finish dates on `book_user`
- [ ] Recommendations v1: "authors you already read" / "top-rated in your categories"
- [ ] Reading challenge: yearly goal + progress

## Phase 5 — Polish & scale

- [ ] `springdoc-openapi` (Swagger UI) — great for demos/interviews
- [ ] GitHub Actions CI: build + test on every push/PR
- [ ] Testcontainers integration tests against real Postgres
- [ ] Redis caching for hot reads (book details, top lists)
- [ ] Stretch: async events (RabbitMQ/Kafka) — e.g. "friend finished a book"
- [ ] README: how to run, env vars, API overview
- [ ] Final manual QA re-run (mirror `report.md` checklist) + cleanup

---

## Tech-learning notes (why these choices)

Each phase doubles as a "playground" for one job-relevant technology:

- **Flyway** — schema migrations, used in every production Java shop
- **Spring Security + JWT** — the single most-asked Spring topic in junior interviews
- **Testcontainers** — integration testing without brittle mocks
- **springdoc/OpenAPI** — API documentation you can show in a portfolio
- **GitHub Actions** — CI/CD basics, near-universal in job postings
- **Redis** — caching + `@Cacheable`, common follow-up question after "why is it slow?"
- **RabbitMQ/Kafka** — async/messaging, the classic "what do you want to learn?" answer
