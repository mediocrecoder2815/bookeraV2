# Roadmap — bookeraV2 Production Readiness

## Week 1 — Unbreak The Build

- Add `@RestController` to `AuthorController`, `BookController`, `CategoryController`
- Add `@RequestBody` to `BookController.addBook()`
- Fix `BookService.deleteBookById()` — Lombok `@Slf4j` instead of `java.rmi.server.LogStream.log()`
- Fix `AuthorService.getAllAuthors()` to accept/use `Pageable`
- Change `ddl-auto=create` → `update`
- Move DB credentials to env vars or `application-prod.properties`
- Remove unused imports, fix any other compile warnings

## Week 2 — Testing & Error Handling

- Add H2 test dependency + `application-test.properties`
- Write Mockito unit tests for all 4 services
- Create `@ControllerAdvice` global exception handler (`EntityNotFoundException`, `DuplicateResourceException`, validation errors)
- Add `@Valid` + `@NotBlank`/`@NotNull`/`@Size` to all DTOs

## Week 3 — Auth & Users

- Study Spring Security + JWT basics
- Implement user registration endpoint (`POST /api/auth/register`)
- Implement login endpoint (`POST /api/auth/login` → returns JWT)
- Protect all POST/PUT/DELETE endpoints behind auth
- Uncomment and adapt `SecurityConfig.java`
- Add `UserService` if needed

## Week 4 — Containerize + DB Migrations

- Add Flyway dependency, create `V1__init.sql` from current schema
- Replace `ddl-auto=update` with Flyway-managed migrations
- Write multi-stage `Dockerfile` (build with Maven, run with JRE)
- Write `docker-compose.yml` (app + postgres, env vars for secrets)
- Test with `docker compose up`

## Week 5 — CI/CD & Polish

- Set up GitHub Actions: build → test on every push/PR
- Fix `pictureUrl` not being saved in Author/Book creation
- Either use or remove orphaned `BookStatus` enum
- Add `spring-boot-starter-actuator` with `/actuator/health`
- Write `README.md` — how to run locally, env vars, API overview
- Final review pass
