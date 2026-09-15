# Architecture

## Overview

Secure Access Manager is a Spring Boot service that manages user accounts, issues
JWTs on successful login, detects brute-force login attempts, and logs every
access event for audit purposes. All traffic is served over TLS.

## Request flow

```
Client
  │
  ▼
AuditFilter (audit/)            — records every request (method, path, IP, outcome)
  │
  ▼
SecurityConfig filter chain     — validates JWT / enforces route access rules
  │
  ▼
Controller (controller/)        — AuthController, AdminController
  │
  ▼
Service (service/)              — AuthService, JwtService, BruteForceDetectorService
  │
  ▼
Repository (repository/)        — Spring Data JPA (UserRepository, AccessLogRepository)
  │
  ▼
Database                        — H2 (dev) / relational DB (prod)
```

## Modules

| Package | Responsibility |
|---|---|
| `config` | Spring Security filter chain, password encoding. TLS is configured via `application.properties` (`server.ssl.*`) against `keystore.p12`. |
| `controller` | REST endpoints for authentication (`/api/auth`) and admin operations (`/api/admin`). |
| `model` | JPA entities: `User`, `AccessLog`. |
| `repository` | Spring Data repositories for `User` and `AccessLog`. |
| `service` | `AuthService` (registration/login), `JwtService` (token issuance/validation), `BruteForceDetectorService` (failed-attempt tracking and lockout). |
| `audit` | Servlet filter that records every request as an `AccessLog` entry. |

## Status

This is an active, in-progress build — see the top-level README for what's
implemented versus planned.
