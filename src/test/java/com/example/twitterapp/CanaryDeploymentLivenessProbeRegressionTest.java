package com.example.twitterapp;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CanaryDeploymentLivenessProbeRegressionTest {

    @Test
    void canaryManifestUsesKubernetesLivenessProbeKey() throws Exception {
        Path manifest = Path.of("deployment-service-canary.yml");
        String yaml = Files.readString(manifest);

        assertTrue(yaml.contains("livenessProbe:"), "canary deployment must declare livenessProbe");
        assertFalse(yaml.contains("livinessProbe:"), "misspelled livinessProbe is ignored by Kubernetes");
    }
}
