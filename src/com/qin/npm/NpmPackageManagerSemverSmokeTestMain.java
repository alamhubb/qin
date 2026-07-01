package com.qin.npm;

import java.util.Set;

public final class NpmPackageManagerSemverSmokeTestMain {
    private NpmPackageManagerSemverSmokeTestMain() {
    }

    public static void main(String[] args) {
        NpmPackageManager npm = new NpmPackageManager();
        Set<String> versions = Set.of(
                "0.0.3",
                "0.0.4",
                "0.18.0",
                "0.18.1",
                "0.18.9",
                "0.19.0",
                "0.26.0",
                "1.0.0");

        require("0.18.9".equals(npm.findMatchingVersion(versions, "^0.18.1")),
                "^0.18.1 stays below 0.19.0");
        require("0.0.3".equals(npm.findMatchingVersion(versions, "^0.0.3")),
                "^0.0.3 stays below 0.0.4");
        require("0.18.9".equals(npm.findMatchingVersion(versions, "~0.18.1")),
                "~0.18.1 stays below 0.19.0");
        require("1.0.0".equals(npm.findMatchingVersion(versions, "^1.0.0")),
                "^1.0.0 floats within major 1");

        System.out.println("NpmPackageManagerSemverSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
