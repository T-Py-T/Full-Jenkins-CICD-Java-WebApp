#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "${ROOT}"

if [[ ! -x ./mvnw ]]; then
  echo 'mvnw must be executable' >&2
  exit 1
fi

if [[ ! -f ./.mvn/wrapper/maven-wrapper.properties ]]; then
  echo 'missing .mvn/wrapper/maven-wrapper.properties' >&2
  exit 1
fi

./mvnw --version >/dev/null
echo 'ok: Maven wrapper is usable'
