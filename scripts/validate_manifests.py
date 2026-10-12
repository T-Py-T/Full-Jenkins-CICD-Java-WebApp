#!/usr/bin/env python3
"""Parse Kubernetes manifests and assert basic invariants used in the lab."""

from __future__ import annotations

import sys
from pathlib import Path

try:
    import yaml
except ImportError:  # pragma: no cover - CI installs PyYAML before running
    print("PyYAML is required: pip install pyyaml", file=sys.stderr)
    sys.exit(2)


ROOT = Path(__file__).resolve().parent.parent
MANIFESTS = (
    ROOT / "deployment-service.yml",
    ROOT / "deployment-service-canary.yml",
)


def load_documents(path: Path) -> list[dict]:
    return [doc for doc in yaml.safe_load_all(path.read_text()) if doc]


def assert_three_resources(path: Path, docs: list[dict]) -> None:
    if len(docs) != 3:
        raise AssertionError(f"{path.name}: expected 3 resources, found {len(docs)}")


def assert_deployment_probe(path: Path, deployment: dict) -> None:
    containers = deployment["spec"]["template"]["spec"]["containers"]
    probe = containers[0].get("livenessProbe")
    if probe is None:
        raise AssertionError(f"{path.name}: deployment missing livenessProbe")
    port = probe["httpGet"]["port"]
    container_port = containers[0]["ports"][0]["containerPort"]
    if port != container_port:
        raise AssertionError(
            f"{path.name}: liveness probe port {port} != containerPort {container_port}"
        )
    if port != 8080:
        raise AssertionError(f"{path.name}: app should listen on 8080, not {port}")


def assert_hpa_target(path: Path, hpa: dict) -> None:
    target = hpa["spec"]["scaleTargetRef"]["name"]
    if target != "bloggingapp-deployment":
        raise AssertionError(f"{path.name}: HPA must target bloggingapp-deployment, not {target}")


def validate_manifest(path: Path) -> None:
    docs = load_documents(path)
    assert_three_resources(path, docs)

    kinds = {doc.get("kind") for doc in docs}
    expected = {"Deployment", "HorizontalPodAutoscaler", "Service"}
    if kinds != expected:
        raise AssertionError(f"{path.name}: unexpected kinds {sorted(kinds)}")

    deployment = next(doc for doc in docs if doc["kind"] == "Deployment")
    hpa = next(doc for doc in docs if doc["kind"] == "HorizontalPodAutoscaler")
    service = next(doc for doc in docs if doc["kind"] == "Service")

    assert_deployment_probe(path, deployment)
    assert_hpa_target(path, hpa)

    target_port = service["spec"]["ports"][0]["targetPort"]
    if target_port != 8080:
        raise AssertionError(f"{path.name}: service targetPort must be 8080, not {target_port}")

    if "livinessProbe" in path.read_text():
        raise AssertionError(f"{path.name}: contains typo livinessProbe")

    print(f"ok: {path.name}")


def main() -> int:
    for manifest in MANIFESTS:
        validate_manifest(manifest)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
