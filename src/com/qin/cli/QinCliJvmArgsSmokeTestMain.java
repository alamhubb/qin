package com.qin.cli;

import java.lang.reflect.Method;
import java.util.List;

public final class QinCliJvmArgsSmokeTestMain {
    private QinCliJvmArgsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Method method = QinCli.class.getDeclaredMethod("resolveJvmArgs", String[].class);
        method.setAccessible(true);

        List<String> equalsForm = invoke(method, "--jvm-args=-Xmx512m -Dqin.smoke=ok", "--port", "19114");
        require(equalsForm.equals(List.of("-Xmx512m", "-Dqin.smoke=ok")), "equals-form JVM args");

        List<String> separatedForm = invoke(method, "--jvm-args", "-Dqin.flag=\"hello world\" -Dempty=");
        require(separatedForm.equals(List.of("-Dqin.flag=hello world", "-Dempty=")), "separated JVM args");

        List<String> combined = invoke(method,
                "--jvm-args=-Xms64m",
                "--frontend-file",
                "app/main.js",
                "--jvm-args",
                "-Dqin.second=true");
        require(combined.equals(List.of("-Xms64m", "-Dqin.second=true")), "combined JVM args");

        List<String> windowsSplitEqualsForm = invoke(method,
                "--jvm-args=-Xms16m",
                "-Xmx384m",
                "-XX:+UseSerialGC",
                "-XX:-UseJVMCICompiler",
                "-XX:TieredStopAtLevel=1",
                "--port",
                "19115");
        require(windowsSplitEqualsForm.equals(List.of(
                "-Xms16m",
                "-Xmx384m",
                "-XX:+UseSerialGC",
                "-XX:-UseJVMCICompiler",
                "-XX:TieredStopAtLevel=1")), "windows split equals-form JVM args");

        System.out.println("QinCliJvmArgsSmokeTestMain OK");
    }

    @SuppressWarnings("unchecked")
    private static List<String> invoke(Method method, String... args) throws Exception {
        return (List<String>) method.invoke(null, (Object) args);
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
