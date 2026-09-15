#!/usr/bin/env bash
# Fires repeated bad-credential login attempts at a LOCAL instance of the app
# to confirm the brute-force detector locks the account after the configured
# threshold. Only ever point this at localhost.
#
# Usage: ./brute_force_test.sh <username>

set -euo pipefail

HOST="https://localhost:8443"
USERNAME="${1:?Usage: $0 <username>}"
ATTEMPTS=7

for i in $(seq 1 "$ATTEMPTS"); do
  echo "Attempt $i..."
  curl -sk -o /dev/null -w "  status: %{http_code}\n" \
    -X POST "$HOST/api/auth/login" \
    -H "Content-Type: application/json" \
    -d "{\"username\":\"$USERNAME\",\"password\":\"wrong-password-$i\"}"
done

echo "Done. Check the access_logs table / app logs for LOGIN_FAILURE and ACCOUNT_LOCKED entries."
