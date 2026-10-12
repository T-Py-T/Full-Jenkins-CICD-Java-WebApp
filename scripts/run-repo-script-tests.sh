#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "${ROOT}"

bash scripts/validate-jenkinsfile.sh
bash scripts/validate-mvnw.sh
python3 scripts/validate_manifests.py

echo 'ok: repository script tests passed'
