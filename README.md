# Secure Access Manager

A Spring Boot service that manages user accounts, issues JWTs on login, detects
brute-force login attempts, and logs every access event for audit purposes — all
served over TLS.

**Status: in development.** The architecture, entities and API surface are
scaffolded; authentication logic, JWT issuance and brute-force detection are
being implemented next. See [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) and
[`docs/THREAT-MODEL.md`](docs/THREAT-MODEL.md) for the design.

## Stack

- Java 17, Spring Boot 3 (Web, Security, Data JPA, Validation)
- JWT via `io.jsonwebtoken` (jjwt)
- H2 (in-memory) for local development
- TLS via a PKCS12 keystore (self-signed for local dev)
- JUnit 5

## Project layout

```
Secure_Access_Manager/
├── src/
│   ├── main/
│   │   ├── java/com/secureaccess/
│   │   │   ├── config/       # Spring Security + password encoding
│   │   │   ├── controller/   # Auth and Admin REST controllers
│   │   │   ├── model/        # JPA entities (User, AccessLog)
│   │   │   ├── repository/   # Spring Data repositories
│   │   │   ├── service/      # Auth, JWT and brute-force detection logic
│   │   │   └── audit/        # Request-auditing filter
│   │   └── resources/
│   │       ├── application.properties
│   │       └── keystore.p12  # gitignored — generate your own, see below
│   └── test/                 # JUnit 5 unit/integration tests
└── pom.xml
simulation/                   # Local-only scripts that exercise the security controls
docs/                         # Architecture and threat-model notes
```

## Running locally

1. Generate a dev TLS keystore (not committed — see `.gitignore`):
   ```bash
   keytool -genkeypair -alias secureaccess -keyalg RSA -keysize 2048 -validity 365 \
     -storetype PKCS12 -keystore Secure_Access_Manager/src/main/resources/keystore.p12 \
     -storepass changeit -keypass changeit \
     -dname "CN=localhost, OU=Secure Access Manager, O=Portfolio, L=Dublin, ST=Dublin, C=IE"
   ```
2. Open `Secure_Access_Manager/` as a Maven project in IntelliJ and run
   `SecureAccessManagerApplication`, or from the command line (requires Maven
   installed):
   ```bash
   cd Secure_Access_Manager
   mvn spring-boot:run
   ```
3. The API is served at `https://localhost:8443` (self-signed cert — your browser/`curl -k` will warn about that, expected for local dev).

## Why this project

Built to apply core security-engineering concepts hands-on: authentication,
credential hashing, brute-force detection, audit logging and TLS — rather than
just reading about them. Part of my
[cybersecurity portfolio](https://adrielkampa.github.io/Portifolio).
