# Simulation Scripts

Scripts used to exercise and validate the security controls in this project locally
(brute-force detection, account lockout, audit logging) — **never point these at
anything other than your own local instance.**

## Planned / in progress

- `brute_force_test.sh` — hammers `POST /api/auth/login` on `localhost` with bad
  credentials to confirm `BruteForceDetectorService` locks the account after the
  configured threshold (`security.brute-force.max-attempts`).
- A JWT-tampering script (Java) to confirm `JwtService` rejects modified/expired
  tokens.

Nothing here talks to a remote host — every script is scoped to `localhost` and is
meant to be run against your own dev instance while it's up.
