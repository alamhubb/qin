package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

final class QinOvsCsstsDemoPaths {
    private static final String DEMO_PATH = "examples/ovs-cssts-demos/qin-ovs-cssts-generated-ts-slime-demo";
    private static final String LEGACY_DEMO_DIR = "qin-ovs-cssts-generated-ts-slime-demo";

    private QinOvsCsstsDemoPaths() {
    }

    static Path generatedTsSlimeDemoRoot() {
        Path cwd = Path.of("").toAbsolutePath().normalize();
        List<Path> candidates = new ArrayList<>();
        for (Path current = cwd; current != null; current = current.getParent()) {
            candidates.add(current.resolve(DEMO_PATH));
            candidates.add(current.resolve("qin").resolve(DEMO_PATH));
            candidates.add(current.resolve("examples").resolve("ovs-cssts-demos").resolve(LEGACY_DEMO_DIR));
            candidates.add(current.resolve(LEGACY_DEMO_DIR));
        }

        for (Path candidate : candidates) {
            Path normalized = candidate.toAbsolutePath().normalize();
            if (Files.isRegularFile(normalized.resolve("qin.config.js"))) {
                return normalized;
            }
        }
        throw new IllegalStateException("Cannot find generated TS Slime OVS/CSSTS demo. Checked: " + candidates);
    }

    static String generatedTsSlimePackageOverridesConfig() {
        Path demoRoot = generatedTsSlimeDemoRoot();
        Path qinRoot = demoRoot.getParent().getParent().getParent();
        Path workspaceRoot = qinRoot.getParent();
        return """
                  packageOverrides: {
                    "slime-parser": "%s",
                    "@qin/java-sdk-js": "%s",
                    "subhuti": "%s",
                    "cssts-compiler": "%s",
                    "ovs-compiler": "%s"
                  },
                """.formatted(
                jsPath(demoRoot.resolve("packages/slime-parser")),
                jsPath(demoRoot.resolve("packages/java-sdk-js")),
                jsPath(workspaceRoot.resolve("subhuti")),
                jsPath(demoRoot.resolve("packages/cssts-compiler")),
                jsPath(workspaceRoot.resolve("ovsjs/ovs/ovs-compiler")));
    }

    private static String jsPath(Path path) {
        return path.toAbsolutePath().normalize().toString().replace('\\', '/');
    }
}
