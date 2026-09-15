# Threat Model (draft)

Lightweight STRIDE-style pass over the main components. This is a working
document — it will be extended as features land.

| Threat | Component | Mitigation |
|---|---|---|
| Credential brute-forcing | `AuthController` / `BruteForceDetectorService` | Track failed attempts per account (and IP); lock the account after `security.brute-force.max-attempts` failures within a window. |
| Password compromise at rest | `User` / database | Passwords are never stored in plaintext — hashed with BCrypt (`PasswordEncoder` bean in `SecurityConfig`). |
| Token theft / replay | `JwtService` | Short-lived JWTs (`security.jwt.expiration-ms`); signature verified on every request; secret must be rotated out of `application.properties` before any real deployment. |
| Eavesdropping in transit | Transport layer | TLS enforced (`server.ssl.enabled=true`); the dev keystore is self-signed and gitignored — a real deployment needs a CA-signed cert. |
| Privilege escalation | `AdminController` | Admin routes gated by role (`hasRole("ADMIN")`) in `SecurityConfig`. |
| Missing audit trail | `AuditFilter` | Every request is intended to be logged (`AccessLog`) with actor, IP, event type and timestamp — useful for after-the-fact investigation. |
| Tampering with request data | Controllers | `spring-boot-starter-validation` is on the classpath for request-body validation as endpoints are implemented. |

## Out of scope (for now)

- Rate limiting at the network/reverse-proxy layer.
- Multi-factor authentication.
- Secrets management (the JWT secret and keystore password currently live in
  `application.properties` as placeholders — fine for local dev, not for
  anything beyond that).
