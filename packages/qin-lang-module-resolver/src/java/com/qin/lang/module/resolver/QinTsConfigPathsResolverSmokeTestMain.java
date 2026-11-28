package com.qin.lang.module.resolver;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinTsConfigPathsResolverSmokeTestMain {
    private QinTsConfigPathsResolverSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-tsconfig-paths-");
        Files.writeString(root.resolve("tsconfig.json"), """
                {
                  // Root config inherited by packages below it.
                  "compilerOptions": {
                    "baseUrl": "base",
                    "paths": {
                      "@root/*": ["root/*"],
                    },
                  },
                }
                """, StandardCharsets.UTF_8);

        Path packageRoot = root.resolve("drizzle-orm");
        Path packageTsconfig = packageRoot.resolve("tsconfig.json");
        Files.createDirectories(packageRoot.resolve("src").resolve("sql"));
        Files.createDirectories(packageRoot.resolve("src").resolve("types"));
        Files.writeString(packageTsconfig, """
                {
                  "extends": "../tsconfig.json",
                  "compilerOptions": {
                    "baseUrl": ".",
                    "paths": {
                      "~/*": ["src/*"],
                      "@config": ["src/types/config.ts"],
                    },
                  },
                }
                """, StandardCharsets.UTF_8);

        Path appRoot = root.resolve("app");
        Path appImporter = appRoot.resolve("src").resolve("index.ts");
        Files.createDirectories(appImporter.getParent());
        Files.writeString(appRoot.resolve("tsconfig.json"), """
                {
                  "extends": "../tsconfig.json",
                  "compilerOptions": {
                    "paths": {
                      "@app/*": ["missing/*", "app/*"],
                      "@*": ["fallback/*"],
                      "@base/*": ["shared/*"],
                      "@config": ["app/config.ts"],
                    },
                  },
                }
                """, StandardCharsets.UTF_8);
        Path inheritedAppRoot = root.resolve("inherited-app");
        Path inheritedAppImporter = inheritedAppRoot.resolve("src").resolve("index.ts");
        Files.createDirectories(inheritedAppImporter.getParent());
        Files.writeString(inheritedAppRoot.resolve("tsconfig.json"), """
                {
                  "extends": "../tsconfig.json",
                }
                """, StandardCharsets.UTF_8);

        Path importer = packageRoot.resolve("src").resolve("sql").resolve("sql.ts");
        Path drizzleEntity = packageRoot.resolve("src").resolve("entity.ts");
        Path drizzleConfig = packageRoot.resolve("src").resolve("types").resolve("config.ts");
        Path appEntry = root.resolve("base").resolve("app").resolve("entry.ts");
        Path appConfig = root.resolve("base").resolve("app").resolve("config.ts");
        Path sharedValue = root.resolve("base").resolve("shared").resolve("value.ts");
        Path inheritedValue = root.resolve("base").resolve("root").resolve("value.ts");
        Files.createDirectories(appEntry.getParent());
        Files.createDirectories(sharedValue.getParent());
        Files.createDirectories(inheritedValue.getParent());
        Files.writeString(importer, "export {};\n", StandardCharsets.UTF_8);
        Files.writeString(drizzleEntity, "export {};\n", StandardCharsets.UTF_8);
        Files.writeString(drizzleConfig, "export {};\n", StandardCharsets.UTF_8);
        Files.writeString(appEntry, "export {};\n", StandardCharsets.UTF_8);
        Files.writeString(appConfig, "export {};\n", StandardCharsets.UTF_8);
        Files.writeString(sharedValue, "export {};\n", StandardCharsets.UTF_8);
        Files.writeString(inheritedValue, "export {};\n", StandardCharsets.UTF_8);

        QinEsmSpecifierResolver resolver = new QinEsmSpecifierResolver();
        assertResolves(resolver, importer, "~/entity.ts", drizzleEntity);
        assertResolves(resolver, importer, "@config", drizzleConfig);
        assertResolves(resolver, appImporter, "@app/entry", appEntry);
        assertResolves(resolver, appImporter, "@app/config", appConfig);
        assertResolves(resolver, appImporter, "@base/value", sharedValue);
        assertResolves(resolver, inheritedAppImporter, "@root/value", inheritedValue);

        System.out.println("QinTsConfigPathsResolverSmokeTestMain OK");
    }

    private static void assertResolves(
            QinEsmSpecifierResolver resolver,
            Path importer,
            String specifier,
            Path expected) {
        Path resolved = resolver.resolveModule(importer, specifier);
        Path normalizedExpected = expected.toAbsolutePath().normalize();
        if (!normalizedExpected.equals(resolved)) {
            throw new IllegalStateException("Expected " + specifier + " to resolve to " + normalizedExpected
                    + ", got " + resolved);
        }
    }
}
