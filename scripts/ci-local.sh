#!/usr/bin/env bash
set -euo pipefail

root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$root"

if [[ -f ./mvnw && -f ./.mvn/wrapper/maven-wrapper.properties ]]; then
  bash ./mvnw --batch-mode --no-transfer-progress verify
elif command -v mvn >/dev/null 2>&1; then
  mvn --batch-mode --no-transfer-progress verify
else
  echo "Maven wrapper or mvn not found" >&2
  exit 1
fi
