#!/usr/bin/env bash
# Structural checks for the declarative Jenkinsfile (no Jenkins controller required).
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
JENKINSFILE="${ROOT}/Jenkinsfile"

if [[ ! -f "${JENKINSFILE}" ]]; then
  echo "missing Jenkinsfile at repo root" >&2
  exit 1
fi

content="$(<"${JENKINSFILE}")"

require_pattern() {
  local pattern="$1"
  local message="$2"
  if ! grep -Eq "${pattern}" "${JENKINSFILE}"; then
    echo "${message}" >&2
    exit 1
  fi
}

require_pattern '^pipeline[[:space:]]*\{' 'Jenkinsfile must declare a declarative pipeline block'
require_pattern "stage\\('Checkout'\\)" 'Jenkinsfile must include a Checkout stage'
require_pattern "stage\\('Compile and Unit Tests'\\)" 'Jenkinsfile must include Compile and Unit Tests stage'
require_pattern "stage\\('Docker Build and Tag'\\)" 'Jenkinsfile must include Docker Build and Tag stage'
require_pattern "stage\\('Deploy to Kubernetes'\\)" 'Jenkinsfile must include Deploy to Kubernetes stage'
require_pattern 'withCredentials' 'Jenkinsfile must use withCredentials for secrets'
require_pattern 'checkout scm' 'Jenkinsfile must checkout scm'

if grep -Eiq '(password|api[_-]?key|secret)[[:space:]]*=[[:space:]]*["\x27][^"\x27]{8,}' "${JENKINSFILE}"; then
  echo 'Jenkinsfile appears to embed a literal secret; use Jenkins credentials instead' >&2
  exit 1
fi

if grep -q 'livinessProbe' "${JENKINSFILE}"; then
  echo 'Jenkinsfile contains typo livinessProbe' >&2
  exit 1
fi

echo "ok: Jenkinsfile structure validated"
