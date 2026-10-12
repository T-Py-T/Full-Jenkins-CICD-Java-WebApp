package com.example.twitterapp;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DeploymentManifestRegressionTest {

    private static final Pattern MEMORY_LIMIT =
            Pattern.compile("limits:\\s*\\n\\s*memory:\\s*(\\d+)(Mi|Gi)", Pattern.MULTILINE);

    @Test
    void stableDeploymentAllocatesEnoughMemoryForJvm() throws Exception {
        String yaml = Files.readString(Path.of("deployment-service.yml"));
        Matcher matcher = MEMORY_LIMIT.matcher(yaml);
        assertTrue(matcher.find(), "expected a container memory limit in deployment-service.yml");

        int mebibytes = Integer.parseInt(matcher.group(1));
        String unit = matcher.group(2);
        if ("Gi".equals(unit)) {
            mebibytes *= 1024;
        }

        assertTrue(mebibytes >= 256,
                "Spring Boot on JVM needs more than 128Mi; container was limited to " + mebibytes + "Mi");
    }
}
