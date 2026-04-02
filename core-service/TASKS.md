# Tunhire — Fix & Improvement Tasks

All identified issues organized by priority and theme.
Check off tasks as they are completed.

---

## 🔴 Priority 1 — Modular Monolith Architecture

The project has the shape of a modular monolith but the boundaries are not enforced and the
data model has a missing relationship that breaks the entire cross-module contract.
These tasks must be completed first because everything else builds on top of a correct module structure.

### TASK-001 — Link `Job` to `Company` via a proper foreign key
> This is the most foundational fix. Without it the `jobs` and `companies` modules have no real
> relationship and the two interface stubs (TASK-002) cannot be implemented correctly.
- [x] Replace `private String company` in the `Job` entity with a `@ManyToOne` to `Company`
- [x] Add `@JoinColumn(name = "company_id", nullable = false)` to the new field
- [x] Update `JobRequest` DTO to accept `companyId` (Long) instead of a raw company name string
- [x] Update `JobResponse` DTO to expose both `companyId` and `companyName`
- [x] Update `JobServiceImpl` to resolve the `Company` entity by ID on create and update
- [x] Add `findByCompanyId(Long companyId)` to `JobRepository`
- [x] Verify DB schema is updated correctly via `ddl-auto=update` or a migration script

### TASK-002 — Implement `DefaultJobLookupService` and `DefaultJobSummaryProvider`
> Depends on TASK-001. These two interfaces are the designated cross-module communication
> contracts between `jobs` ↔ `applications` and `jobs` ↔ `companies`. Right now they are stubs
> that return empty lists, making the dashboard and company job endpoints silently broken.
- [x] Implement `DefaultJobLookupService.getJobIdsByCompanyId()` using `JobRepository.findByCompanyId()`
- [x] Implement `DefaultJobSummaryProvider.getJobsByCompanyId()` using `JobRepository.findByCompanyId()`
- [x] Map `Job` entities to `JobSummaryDto` inside `DefaultJobSummaryProvider`
- [x] Verify `GET /companies/{id}/jobs` returns real job data
- [x] Verify `GET /companies/{id}/applications` returns real application data
- [x] Verify `GET /companies/{id}/dashboard` returns a fully populated response

### TASK-003 — Remove `UserRepository` from `JobController`
> `JobController` lives in the `jobs` module but directly imports `UserRepository` from the
> `auth` module. This is a hard domain boundary violation — a controller must never reach into
> another module's repository.
- [x] Add a `getUserIdByEmail(String email)` method to `AuthService` (and `AuthServiceImpl`)
- [x] Replace the `UserRepository` injection in `JobController` with `AuthService`
- [x] Remove the `com.example.tunhire.auth.repository` import from the `jobs` package entirely
- [x] Verify that `POST /jobs`, `PUT /jobs/{id}`, and `DELETE /jobs/{id}` still resolve the recruiter correctly

### TASK-004 — Remove cross-domain service injections from `CompaniesController`
> `CompaniesController` directly injects `ApplicationService` (from `applications`) and
> `JobSummaryProvider`. `CompanyDashboardService` already aggregates both — the controller
> should not bypass it.
- [x] Remove `ApplicationService` injection from `CompaniesController`
- [x] Remove `JobSummaryProvider` injection from `CompaniesController`
- [x] Route `GET /companies/{id}/applications` through `CompanyDashboardService` or a dedicated method
- [x] Route `GET /companies/{id}/jobs` through `CompanyDashboardService` or a dedicated method
- [x] Verify all company endpoints return correct responses after the refactor

### TASK-005 — Enforce module encapsulation (package-private internals)
> In a modular monolith, each module should only expose its service interfaces and DTOs.
> Repositories and entities are internal implementation details and must not be accessible
> from outside their own module.
- [x] Make `UserRepository` package-private
- [x] Make `JobRepository` package-private
- [x] Make `ApplicationRepository` package-private
- [x] Make `CompanyRepository` package-private
- [x] Make all `@Entity` classes package-private where they are not referenced outside their module
- [x] Make `CompanyService.getEntityById()` private
- [x] Fix any compilation errors caused by the visibility changes (these are exactly the boundary violations to eliminate)
- [x] Verify compilation and all tests pass

### TASK-006 — Add ArchUnit tests to enforce module boundary rules
> Without automated checks, boundary violations will silently creep back in.
- [x] Add `com.tngtech.archunit:archunit-junit5` to `pom.xml` (test scope)
- [x] Create `ArchitectureTest.java` in the test root
- [x] Write a rule: no class in `jobs` may access any class in `applications` or `companies`
- [x] Write a rule: no class in `applications` may access any class in `jobs` directly (must go through `JobLookupService`)
- [x] Write a rule: no class in `companies` may access any class in `applications` directly
- [x] Write a rule: controllers may not import repositories from any module
- [x] Write a rule: no class in `recruiter` or `candidate` may access each other's internals directly
- [x] Verify rules catch existing violations before TASK-003 to TASK-005 are fixed
- [x] Verify all rules pass after TASK-003 to TASK-005 are complete

### TASK-007 — (Optional) Split into a Maven multi-module project
> The strongest form of boundary enforcement is to make modules separate Maven artifacts so the
> compiler itself rejects illegal imports. This is optional but recommended as the project grows.
- [ ] Create a parent `pom.xml` at the root level
- [ ] Extract `auth`, `jobs`, `applications`, `companies`, and `common` into separate Maven modules
- [ ] Declare explicit inter-module dependencies in each module's `pom.xml`
- [ ] Verify that `jobs` cannot compile if it tries to import `ApplicationRepository`
- [ ] Verify the full project builds with `./mvnw package` from the root
### Section B — Recruiter Module & Company Team

> A company account can be accessed by many recruiters simultaneously — it is a shared team
> workspace, not a single-user account. The `recruiter` module owns the membership relationship
> between a `User` and a `Company` and changes job authorization from user-level to company-level.

### TASK-008 — Create the `recruiter` module structure
- [x] Create the package tree: `recruiter/entity`, `recruiter/dto`, `recruiter/service`, `recruiter/controller`, `recruiter/repository`
- [x] Register the module in `ArchitectureTest.java` (TASK-006) so its boundaries are checked from the start

### TASK-009 — Model `MemberRole` enum and `CompanyMembership` entity
> `CompanyMembership` is the source of truth for which recruiters belong to which company
> and what level of access they have within the team.
- [x] Create `MemberRole` enum: `OWNER`, `ADMIN`, `MEMBER`
- [x] Create `CompanyMembership` entity: `id`, `userId` (Long), `companyId` (Long), `memberRole`, `joinedAt`
- [x] Add `@UniqueConstraint(columnNames = {"user_id", "company_id"})` to prevent duplicate memberships
- [x] Create `CompanyMembershipRepository` with: `findByCompanyId`, `findByUserId`, `findByUserIdAndCompanyId`, `existsByUserIdAndCompanyId`

### TASK-010 — Implement `MembershipService`
- [x] Create `MembershipService` interface with methods:
  - `isMember(Long userId, Long companyId): boolean`
  - `isOwnerOrAdmin(Long userId, Long companyId): boolean`
  - `getCompanyIdByUserId(Long userId): Optional<Long>`
  - `getMembersByCompanyId(Long companyId): List<MemberResponse>`
  - `addMember(Long companyId, Long invitedUserId, MemberRole role)`
  - `removeMember(Long companyId, Long targetUserId)`
  - `updateMemberRole(Long companyId, Long targetUserId, MemberRole newRole)`
- [x] Implement `MembershipServiceImpl` using `CompanyMembershipRepository`
- [x] When a `Company` is created, automatically add the creating recruiter as `OWNER`

### TASK-011 — Add membership management endpoints
- [x] `POST /companies/{id}/members` — add a recruiter to the team (OWNER or ADMIN only)
- [x] `GET /companies/{id}/members` — list all team members with their roles
- [x] `DELETE /companies/{id}/members/{userId}` — remove a member (OWNER or ADMIN only; OWNER cannot remove themselves)
- [x] `PATCH /companies/{id}/members/{userId}/role` — change a member's role (OWNER only)
- [x] Validate that the invited user exists and has `ROLE_RECRUITER`
- [x] Return `MemberResponse` DTO: `userId`, `firstName`, `lastName`, `memberRole`, `joinedAt`

### TASK-012 — Update `Job` to belong to the company, not the individual recruiter
> The unit of job ownership is the company team, not the individual recruiter.
> The recruiter who posted it is recorded for audit purposes only.
- [x] Remove `@ManyToOne User recruiter` from the `Job` entity
- [x] Add `Long postedById` to `Job` (audit field — not a JPA entity reference)
- [x] `Job` now belongs to a `Company` via `companyId` from TASK-001
- [x] Update `JobServiceImpl.create()` to set `postedById` from the authenticated user and `companyId` from their membership
- [x] Update `JobResponse` to expose `postedById` and `postedByName` (resolved via an interface, not a direct import)

### TASK-013 — Update job authorization from user-level to company-level
> Any recruiter who is a member of the company that owns a job can edit or delete that job.
- [x] In `JobServiceImpl.update()`, replace the user ID check with `membershipService.isMember(currentUserId, job.getCompanyId())`
- [x] In `JobServiceImpl.delete()`, apply the same company-level check
- [x] Define `MembershipService` access via an interface so `jobs` does not import `recruiter` internals
- [x] Verify that a recruiter from a different company cannot edit or delete another company's job

### TASK-014 — Enforce company-scoped access on all company endpoints
> A recruiter should only be able to access the dashboard, jobs, and applications of their own company.
- [x] Resolve the authenticated recruiter's `companyId` via `MembershipService.getCompanyIdByUserId()`
- [x] Compare it to the `{id}` path variable — if they differ, throw `AccessDeniedException` (403)
- [x] Apply to: `GET /companies/{id}/dashboard`, `GET /companies/{id}/members`, `GET /companies/{id}/applications`, `PUT /companies/{id}`
- [x] Write tests: recruiter A cannot access company B's dashboard

---

### Section C — Candidate Module

> The `candidate` module owns all profile data specific to a candidate: bio, resume, skills,
> and availability. The `auth` module remains thin (identity only). A `CandidateProfile` is
> linked to a `User` by `userId` (Long), not by a JPA entity reference.

### TASK-015 — Create the `candidate` module structure
- [x] Create the package tree: `candidate/entity`, `candidate/dto`, `candidate/service`, `candidate/controller`, `candidate/repository`
- [x] Register the module in `ArchitectureTest.java` (TASK-006)

### TASK-016 — Model `CandidateProfile` entity
- [x] Fields: `id`, `userId` (Long, unique), `bio`, `resumeUrl`, `location`, `availableFrom` (LocalDate), `yearsOfExperience` (Integer)
- [x] Add `@UniqueConstraint(columnNames = "user_id")`
- [x] Create `CandidateProfileRepository` with `findByUserId(Long userId)`

### TASK-017 — Model candidate skills
- [x] Create `ProficiencyLevel` enum: `BEGINNER`, `INTERMEDIATE`, `EXPERT`
- [x] Create `CandidateSkill` entity: `id`, `profileId` (Long), `skillName`, `proficiencyLevel`
- [x] Create `CandidateSkillRepository` with `findByProfileId` and `deleteByIdAndProfileId`

### TASK-018 — Implement `CandidateService`
- [x] Create `CandidateService` interface:
  - `getMyProfile(Long userId): CandidateProfileResponse`
  - `updateProfile(Long userId, UpdateProfileRequest request): CandidateProfileResponse`
  - `addSkill(Long userId, SkillRequest request): CandidateSkillResponse`
  - `removeSkill(Long userId, Long skillId)`
  - `getPublicProfile(Long userId): CandidateProfileResponse`
- [x] Implement `CandidateServiceImpl`
- [x] Use lazy-creation: if no profile exists for a `userId`, create an empty one on first access

### TASK-019 — Add candidate profile endpoints
- [x] `GET /candidates/me` — get the authenticated candidate's full profile (`ROLE_CANDIDATE` only)
- [x] `PUT /candidates/me` — update bio, resumeUrl, location, availableFrom, yearsOfExperience
- [x] `POST /candidates/me/skills` — add a skill to the profile
- [x] `DELETE /candidates/me/skills/{id}` — remove a skill from the profile
- [x] `GET /candidates/{id}` — get a candidate's public profile (accessible to `ROLE_RECRUITER`)

### TASK-020 — Create `CandidateProfileProvider` interface for cross-module access
> The `applications` module needs basic candidate info to enrich application responses for recruiters.
> It must not import `CandidateProfileRepository` directly — same pattern as `JobLookupService`.
- [x] Define `CandidateProfileProvider` interface in the `applications` module (or `common`)
- [x] Method: `getCandidateSummary(Long userId): CandidateSummaryDto`
- [x] `CandidateSummaryDto`: `userId`, `firstName`, `lastName`, `resumeUrl`
- [x] Implement `DefaultCandidateProfileProvider` in the `candidate` module
- [x] Inject `CandidateProfileProvider` into `ApplicationService` to enrich `ApplicationResponse`
- [x] Add ArchUnit rule: `applications` must not import from `candidate` directly

### TASK-021 — Auto-create a `CandidateProfile` on candidate registration
- [x] Option A (simple): lazily create the profile on first `getMyProfile()` call
- [x] Option B (event-driven): publish a `UserRegisteredEvent` from `AuthServiceImpl` and handle it in `CandidateService` — keeps `auth` decoupled from `candidate`
- [x] Decide and implement one approach consistently
- [x] Verify a newly registered candidate can immediately call `GET /candidates/me` without a 404

---

---

## 🔴 Priority 2 — Security Issues

### TASK-008 — Stop trusting `userId` from the request body
- [ ] Remove `userId` from `ApplicationCreateRequest`
- [ ] Update `ApplicationsController.create()` to accept `Authentication` and extract the user ID from the JWT token
- [ ] Pass the resolved `userId` into `ApplicationService.create()`
- [ ] Update `ApplicationServiceTest` to reflect the new method signature

### TASK-009 — Add role-based access control (RBAC)
- [ ] Enable `@EnableMethodSecurity` in `SecurityConfig`
- [ ] Restrict `POST /jobs`, `PUT /jobs/{id}`, `DELETE /jobs/{id}` to `ROLE_RECRUITER` only
- [ ] Restrict `POST /applications` to `ROLE_CANDIDATE` only
- [ ] Restrict `POST /companies`, `PUT /companies/{id}` to `ROLE_RECRUITER` or `ROLE_ADMIN`
- [ ] Restrict `GET /companies/{id}/applications` and `GET /companies/{id}/dashboard` to `ROLE_RECRUITER`
- [ ] Write tests verifying a `ROLE_CANDIDATE` cannot create a job and vice versa

### TASK-010 — Fix user enumeration vulnerability in login
- [ ] Replace the separate "User not found" and "Invalid password" errors in `AuthServiceImpl.login()` with a single `InvalidCredentialsException`
- [ ] Register `InvalidCredentialsException` in `GlobalExceptionHandler` mapped to HTTP 401
- [ ] Verify both a bad email and a bad password return the exact same response body and status code

### TASK-011 — Externalize secrets from `application.properties`
- [ ] Replace hardcoded `jwt.secret` with `${JWT_SECRET}`
- [ ] Replace hardcoded `spring.datasource.username` with `${DB_USERNAME}`
- [ ] Replace hardcoded `spring.datasource.password` with `${DB_PASSWORD}`
- [ ] Set `JWT_SECRET`, `DB_USERNAME`, `DB_PASSWORD` as environment variables in the `app` service in `compose.yaml`
- [ ] Add a `.env.example` file documenting all required environment variables
- [ ] Confirm `.env` is present in `.gitignore`

---

## 🟠 Priority 3 — Broken Functionality

### TASK-012 — Fix test database configuration
- [x] Override `application-test.properties` to use H2 in-memory instead of PostgreSQL
- [x] Set `spring.datasource.url=jdbc:h2:mem:testdb`
- [x] Set `spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect`
- [x] Run all tests and confirm they pass without a running PostgreSQL instance
- [ ] (Optional) Replace H2 with Testcontainers for closer production parity

---

## 🟠 Priority 4 — Architecture Refinements

### TASK-013 — Replace `IllegalArgumentException` with custom exceptions
- [ ] Create `ConflictException` (HTTP 409) for duplicate email on registration
- [ ] Create `UnauthorizedException` (HTTP 401) for invalid credentials (see TASK-010)
- [ ] Register both in `GlobalExceptionHandler` with correct HTTP status codes
- [ ] Replace all `throw new IllegalArgumentException(...)` calls in services with the appropriate custom exception
- [ ] Verify correct HTTP status codes are returned for each error case

### TASK-014 — Replace raw `String status` in `Job` with an enum
- [ ] Create `JobStatus` enum (`ACTIVE`, `CLOSED`, `DRAFT`) in `jobs/entity`
- [ ] Replace `private String status` in `Job` with `@Enumerated(EnumType.STRING) private JobStatus status`
- [ ] Update `JobServiceImpl` to use `JobStatus.ACTIVE` instead of the hardcoded string `"active"`
- [ ] Update `JobRepository.findByStatus()` to accept `JobStatus`
- [ ] Update `JobRequest` and `JobResponse` DTOs accordingly

---

## 🟡 Priority 5 — Code Quality

### TASK-015 — Use Lombok on all entities
- [ ] Annotate `User`, `Job`, `Company`, and `Application` with `@Getter`, `@Setter`, `@NoArgsConstructor`
- [ ] Remove all manually written getters, setters, and no-arg constructors
- [ ] Verify compilation and tests pass

### TASK-016 — Replace manual timestamp management with JPA Auditing
- [ ] Add `@EnableJpaAuditing` to `TunhireApplication` or a dedicated config class
- [ ] Annotate `createdAt` with `@CreatedDate` and `updatedAt` with `@LastModifiedDate` on `User`, `Job`, and `Application`
- [ ] Add `@EntityListeners(AuditingEntityListener.class)` to each audited entity
- [ ] Add `createdAt` and `updatedAt` fields to the `Company` entity
- [ ] Remove all `entity.setCreatedAt(Instant.now())` and `entity.setUpdatedAt(Instant.now())` calls from service methods
- [ ] Verify timestamps are populated correctly on save and update

### TASK-017 — Add `@Transactional` to service methods
- [ ] Annotate write methods in `AuthServiceImpl`, `JobServiceImpl`, `ApplicationService`, and `CompanyService` with `@Transactional`
- [ ] Annotate read-only methods with `@Transactional(readOnly = true)`

### TASK-018 — Add pagination to `GET /jobs`
- [ ] Update `JobService.getAll()` to accept `Pageable` and return `Page<JobResponse>`
- [ ] Update `JobServiceImpl` and `JobRepository` accordingly
- [ ] Update `JobController.getAll()` to accept `Pageable` (bound automatically from `?page=0&size=20&sort=createdAt`)

### TASK-019 — Rename base package from `com.example` to `com.tunhire`
- [ ] Rename all Java packages from `com.example.tunhire` to `com.tunhire`
- [ ] Update `<groupId>` in `pom.xml` from `com.example` to `com.tunhire`
- [ ] Update all import statements across the codebase
- [ ] Verify compilation and tests pass

---

## 🟡 Priority 6 — Infrastructure & DevOps

### TASK-020 — Pin PostgreSQL version in `compose.yaml`
- [ ] Replace `image: 'postgres:latest'` with `postgres:16`
- [ ] Verify `docker compose up` still works correctly

### TASK-021 — Optimize Dockerfile with Maven dependency caching
- [ ] Add `RUN ./mvnw dependency:go-offline -B` after copying `pom.xml` and before copying `src`
- [ ] Verify a source-only rebuild reuses the cached dependency layer
- [ ] Verify the final image still starts and runs correctly

### TASK-022 — Gate Swagger UI behind a Spring profile
- [ ] Restrict the Swagger `requestMatchers` permit to the `dev` profile only
- [ ] Verify Swagger is not publicly accessible when running with the `prod` profile
- [ ] Document Swagger access instructions in the README

---

## 🟡 Priority 7 — Test Coverage

### TASK-023 — Add unit tests for `AuthService`
- [ ] Test `register()` happy path — user is saved and a JWT is returned
- [ ] Test `register()` with a duplicate email — expect `ConflictException`
- [ ] Test `login()` happy path — correct credentials return a JWT
- [ ] Test `login()` with wrong password — expect `UnauthorizedException`
- [ ] Test `getCurrentUser()` — returns correct DTO

### TASK-024 — Add unit tests for `JobService`
- [ ] Test `create()` — job saved with correct fields and `ACTIVE` status
- [ ] Test `update()` by owner — fields are updated correctly
- [ ] Test `update()` by non-owner — expect authorization error
- [ ] Test `delete()` by owner — job is removed
- [ ] Test `getById()` with an unknown ID — expect `ResourceNotFoundException`

### TASK-025 — Add integration / controller tests
- [ ] Write `@SpringBootTest` + `@AutoConfigureMockMvc` test for `POST /auth/register`
- [ ] Write test for `POST /auth/login` and assert a JWT is returned
- [ ] Write test for `POST /jobs` with a valid recruiter JWT — expect 200
- [ ] Write test for `POST /jobs` with a candidate JWT — expect 403
- [ ] Write test for `POST /applications` ensuring `userId` is taken from the token, not the body

---

## 📋 Task Summary

| ID | Title | Priority | Status |
|---|---|---|---|
| TASK-001 | Link Job to Company via FK | 🔴 Modular Architecture | ✅ Done |
| TASK-002 | Implement job lookup stubs | 🔴 Modular Architecture | ✅ Done |
| TASK-003 | Remove UserRepository from JobController | 🔴 Modular Architecture | ✅ Done |
| TASK-004 | Remove cross-domain injections from CompaniesController | 🔴 Modular Architecture | ✅ Done |
| TASK-005 | Enforce module encapsulation (package-private internals) | 🔴 Modular Architecture | ✅ Done |
| TASK-006 | Add ArchUnit boundary tests | 🔴 Modular Architecture | ✅ Done |
| TASK-007 | Split into Maven multi-module project (optional) | 🔴 Modular Architecture | ⬜ Todo |
| TASK-008 | Create recruiter module structure | 🔴 Modular Architecture | ⬜ Todo |
| TASK-009 | Model MemberRole and CompanyMembership | 🔴 Modular Architecture | ⬜ Todo |
| TASK-010 | Implement MembershipService | 🔴 Modular Architecture | ⬜ Todo |
| TASK-011 | Add membership management endpoints | 🔴 Modular Architecture | ⬜ Todo |
| TASK-012 | Update Job ownership to company-level | 🔴 Modular Architecture | ⬜ Todo |
| TASK-013 | Update job authorization to company-level | 🔴 Modular Architecture | ⬜ Todo |
| TASK-014 | Enforce company-scoped access on company endpoints | 🔴 Modular Architecture | ⬜ Todo |
| TASK-015 | Create candidate module structure | 🔴 Modular Architecture | ⬜ Todo |
| TASK-016 | Model CandidateProfile entity | 🔴 Modular Architecture | ⬜ Todo |
| TASK-017 | Model candidate skills | 🔴 Modular Architecture | ⬜ Todo |
| TASK-018 | Implement CandidateService | 🔴 Modular Architecture | ⬜ Todo |
| TASK-019 | Add candidate profile endpoints | 🔴 Modular Architecture | ⬜ Todo |
| TASK-020 | Create CandidateProfileProvider interface | 🔴 Modular Architecture | ⬜ Todo |
| TASK-021 | Auto-create CandidateProfile on registration | 🔴 Modular Architecture | ⬜ Todo |
| TASK-008 | Stop trusting userId from request body | 🔴 Security | ⬜ Todo |
| TASK-009 | Add RBAC | 🔴 Security | ⬜ Todo |
| TASK-010 | Fix user enumeration on login | 🔴 Security | ⬜ Todo |
| TASK-011 | Externalize secrets | 🔴 Security | ⬜ Todo |
| TASK-012 | Fix test DB configuration | 🟠 Broken | ⬜ Todo |
| TASK-013 | Replace IllegalArgumentException with custom exceptions | 🟠 Architecture | ⬜ Todo |
| TASK-014 | Replace Job.status String with enum | 🟠 Architecture | ⬜ Todo |
| TASK-015 | Use Lombok on entities | 🟡 Quality | ⬜ Todo |
| TASK-016 | Use JPA Auditing for timestamps | 🟡 Quality | ⬜ Todo |
| TASK-017 | Add @Transactional to services | 🟡 Quality | ⬜ Todo |
| TASK-018 | Add pagination to GET /jobs | 🟡 Quality | ⬜ Todo |
| TASK-019 | Rename base package to com.tunhire | 🟡 Quality | ⬜ Todo |
| TASK-020 | Pin PostgreSQL version | 🟡 Infra | ⬜ Todo |
| TASK-021 | Optimize Dockerfile caching | 🟡 Infra | ⬜ Todo |
| TASK-022 | Gate Swagger behind dev profile | 🟡 Infra | ⬜ Todo |
| TASK-023 | Unit tests for AuthService | 🟡 Tests | ⬜ Todo |
| TASK-024 | Unit tests for JobService | 🟡 Tests | ⬜ Todo |
| TASK-025 | Integration/controller tests | 🟡 Tests | ⬜ Todo |