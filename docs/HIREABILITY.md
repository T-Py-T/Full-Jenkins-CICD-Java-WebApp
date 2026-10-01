# Hireability & discoverability

Thin index for skimming this public Jenkins/Java CI/CD case study. Step-by-step
lab detail lives in [README.md](../README.md).

## Cross-links

| Topic | Document |
| --- | --- |
| Pipeline lab, local validation, deployment | [README.md](../README.md) |
| License (MIT) | [LICENSE](../LICENSE) |
| Maven wrapper and other third-party terms | [THIRD_PARTY_NOTICES.md](../THIRD_PARTY_NOTICES.md) |
| Vulnerability reporting | [SECURITY.md](../SECURITY.md) |
| Jenkins/EKS Terraform install scripts | [devops-install-scripts](https://github.com/T-Py-T/devops-install-scripts) |

## GitHub topics

Repository tags on GitHub:

`cicd` · `devsecops` · `eks` · `java` · `jenkins` · `terraform`

## What this case study demonstrates

- Jenkins-driven build, test, analysis, artifact publish, image build/scan, and EKS rollout for a small Spring Boot service
- DevSecOps tooling in the path: SonarQube, Nexus, Docker, Trivy, Prometheus, Grafana
- PR-time Maven checks via [`.github/workflows/pr_init_checks.yml`](../.github/workflows/pr_init_checks.yml)

## Tip cite

Documentation baseline: `54a8abf` (`main` tip). This hireability lean is pending Steward resolve in its shipping PR.
