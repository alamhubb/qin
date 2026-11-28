package com.qin.orm;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public final class QinOrmEntityProcessorSmokeTestMain {
    private QinOrmEntityProcessorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        require(compiler != null, "JDK compiler is unavailable");

        Path root = Files.createTempDirectory("qin-orm-processor-smoke-");
        Path sourceRoot = Files.createDirectories(root.resolve("source/com/qin/orm/generated"));
        Path generated = Files.createDirectories(root.resolve("generated"));
        Path output = Files.createDirectories(root.resolve("classes"));
        Path source = sourceRoot.resolve("ProcessorUser.java");
        Files.writeString(source, """
                package com.qin.orm.generated;

                import jakarta.persistence.Entity;
                import jakarta.persistence.Id;
                import jakarta.persistence.ManyToOne;
                import jakarta.persistence.OneToMany;
                import jakarta.persistence.Access;
                import jakarta.persistence.AccessType;
                import java.util.List;

                @Entity
                public class ProcessorUser {
                    @Id private long id;
                    private String name;
                    @ManyToOne private ProcessorUser manager;
                    @OneToMany private List<ProcessorUser> reports;
                }

                @Entity
                @Access(AccessType.PROPERTY)
                class PropertyUser {
                    private Long id;
                    private String name;

                    @Id public Long getId() { return id; }
                    public String getName() { return name; }
                }
                """);

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null)) {
            Iterable<? extends JavaFileObject> units = fileManager.getJavaFileObjects(source.toFile());
            List<String> options = List.of(
                    "--release", "25",
                    "-classpath", System.getProperty("java.class.path"),
                    "-processor", QinOrmEntityProcessor.class.getName(),
                    "-s", generated.toString(),
                    "-d", output.toString());
            boolean compiled = compiler.getTask(null, fileManager, diagnostics, options, null, units).call();
            if (!compiled) {
                throw new AssertionError("Entity descriptor compilation failed: " + diagnostics.getDiagnostics());
            }
        }

        String generatedSource = Files.readString(generated.resolve("com/qin/orm/generated/ProcessorUserQin.java"));
        require(generatedSource.contains("QinOrmColumn<com.qin.orm.generated.ProcessorUser, java.lang.Long> id"),
                "primitive identifier should be boxed in the descriptor");
        require(generatedSource.contains("column(\"name\", java.lang.String.class)"),
                "basic scalar property should be generated");
        require(!generatedSource.contains("manager") && !generatedSource.contains("reports"),
                "relationship properties should not be generated");

        try (URLClassLoader loader = new URLClassLoader(new java.net.URL[] { output.toUri().toURL() },
                QinOrmEntityProcessorSmokeTestMain.class.getClassLoader())) {
            Class<?> fieldModel = loader.loadClass("com.qin.orm.generated.ProcessorUserQin");
            fieldModel.getField("id");
            fieldModel.getField("name");
            Class<?> propertyModel = loader.loadClass("com.qin.orm.generated.PropertyUserQin");
            propertyModel.getField("id");
            propertyModel.getField("name");
        } finally {
            try (var paths = Files.walk(root)) {
                paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (Exception error) {
                        throw new IllegalStateException(error);
                    }
                });
            }
        }
        System.out.println("QinOrmEntityProcessorSmokeTestMain OK");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
