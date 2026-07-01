package com.qin.npm;

import com.sun.net.httpserver.HttpServer;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public final class NpmPackageManagerRegistryFailoverSmokeTestMain {
    private NpmPackageManagerRegistryFailoverSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        HttpServer brokenRegistry = startRegistry("{\"versions\":{\"1.0.0\":{\"author\":", 200);
        HttpServer healthyRegistry = startRegistry("""
                {
                  "name": "demo-pkg",
                  "version": "1.0.0",
                  "dist": {
                    "tarball": "http://127.0.0.1/demo-pkg-1.0.0.tgz"
                  }
                }
                """, 200);
        try {
            String brokenUrl = "http://127.0.0.1:" + brokenRegistry.getAddress().getPort();
            String healthyUrl = "http://127.0.0.1:" + healthyRegistry.getAddress().getPort();
            NpmPackageManager npm = new NpmPackageManager(
                    Files.createTempDirectory("qin-npm-registry-failover-").toString(),
                    List.of(brokenUrl, healthyUrl));
            Method fetchPackageInfo = NpmPackageManager.class.getDeclaredMethod(
                    "fetchPackageInfo",
                    String.class,
                    String.class);
            fetchPackageInfo.setAccessible(true);
            Object result = fetchPackageInfo.invoke(npm, "demo-pkg", "1.0.0");
            require(result != null, "package info resolved from healthy registry");
            require(healthyUrl.equals(npm.getRegistry()), "active registry switches to healthy registry");
            System.out.println("NpmPackageManagerRegistryFailoverSmokeTestMain OK");
        } finally {
            brokenRegistry.stop(0);
            healthyRegistry.stop(0);
        }
    }

    private static HttpServer startRegistry(String body, int status) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(status, bytes.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(bytes);
            }
        });
        server.start();
        return server;
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
