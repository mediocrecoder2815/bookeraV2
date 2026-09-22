# Roadmap — bookeraV2 (Goodreads-like API)

**Vision:** a Goodreads-style book-tracking API. Users register, track books
(want-to-read / currently-reading / read), rate & review them, and browse authors
and categories.

**Reality check (2026-09):** the app compiles and the core flow works — auth with
JWT, book/author/category CRUD, reviews, user shelf, Swagger UI. **99 tests. 
This roadmap only lists what is true or actually getting built.

---

## Current state — things that are genuinely done

- [x] `Flyway` owns the schema (12 migrations), `ddl-auto=validate`
- [x] JWT auth: `POST /api/auth/register|login` (Roles: USER/ADMIN)
- [x] Read-only catalog (`GET /api/books|authors|categories`) public; writes require a token
- [x] Book / author / category CRUD with validation + global error handler (400/404/409)
- [x] Reviews bound to the authenticated user via `Principal`
- [x] User shelf: add book, change `BookStatus`, get shelf (`/api/users/shelf`)
- [x] Average rating on books (aggregation query)
- [x] Swagger UI at `/swagger-ui.html` (springdoc, Bearer JWT scheme)
- [x] Dockerfile + docker-compose (app + postgres, secrets via env)

### Not actually done (either claims or checked off prematurely)

- [x] **Rating distribution** per book — checked in the old roadmap, not implemented

---

## Step 1 — Make the build honest ("green or gone")

- [x] Fix `BookServiceTest.shouldReturnBookWhenFound` — broken mock: `findById` returns
      `null`, NPE at `BookService.getBookById` (BookService.java:39)
- [x] Fix `ReviewServiceTest.shouldThrowWhenPrincipalMismatch` — flaky
      (mock ordering issue in `createReview` path)
- [x] Un-check "rating distribution" claim → implement it (grouped `count(*)` query)
      **or** delete the claim from the docs entirely
- [ ] Add a `README.md`: how to run, required env vars (`DB_URL`, `DB_PASSWORD`,
      `JWT_SECRET`), API overview, how to open Swagger
- [x] Decide the fate of `report.md`: create it for real or drop all references
- [x] `git status` clean, `./mvnw test` green, repeat the run 3x to confirm no flakes

## Step 2 — Picture upload (chosen next feature)

- [x] `POST /api/books/{id}/picture` + `POST /api/authors/{id}/picture`
- [x] Start local-filesystem storage: `MultipartFile` → `uploads/`, UUID filename,
      store URL in `pictureUrl`, static-resource serving
- [x] Validate: content type (`image/png|jpeg`), max size, reject empty payloads
- [x] Add request param → OpenAPI docs for the new endpoints (springdoc picks up multipart)
- [x] Tests: success path, wrong type, oversized file, book not found (404)
- [x] Follow-up (only if needed for an interview answer): S3/MinIO swap-out in mind

## Step 3 — ONE depth feature (not five)

Pick a single feature that reuses existing code rather than adding a new domain:
- [x] Added caching for book entity using Redis
- [ ] **Reading progress** (% complete, start/finish dates on `book_user`) — reuses the
      shelf feature that already exists
- [ ] **OR** custom bookshelves (user-defined many-to-many) — same `book_user` shape
- [ ] Skipped for now: recommendations, reading challenge, RabbitMQ/Kafka, Redis

Rationale: every item so far is built and tested. Adding five half-finished features
makes the repo *worse for interviews* than zero unused ones. When recommending myself,
a reviewer can actually run this thing and see tests pass.

## Step 4 — Polish (optional, only after Steps 1–3 are green)

- [x] GitHub Actions: `mvn test` on push (cheap, high signal)
- [ ] Testcontainers integration test against real Postgres
- [x] Redis `@Cacheable` on hot reads (book details)
- [x] Final manual QA pass + clean up `.idea/` from the repo if not intended
