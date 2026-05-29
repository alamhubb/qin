package com.qin.runtime.core;

import java.lang.classfile.Attributes;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.MethodModel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Smoke test for Spring PostMapping subset on the IR-based bridge.
 */
public final class QinSpringPostMappingSmokeTestMain {
    private static final String POST_MAPPING_DESCRIPTOR =
            "Lorg/springframework/web/bind/annotation/PostMapping;";

    private QinSpringPostMappingSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path source = Files.createTempFile("qin-post-controller-", ".qin");
        Files.writeString(source, """
                import { RestController as RC, PostMapping as POST } from "java:org.springframework.web.bind.annotation"

                @RC
                class PostController {
                  @POST("/api/create")
                  create() {
                    return "created"
                  }
                }
                """, StandardCharsets.UTF_8);

        QinSpringControllerCompiler.CompiledController compiled =
                new QinSpringControllerCompiler().compile(source, "server.generated.PostController");
        ClassModel classModel = ClassFile.of().parse(compiled.classBytes());

        MethodModel method = classModel.methods().stream()
                .filter(candidate -> candidate.methodName().stringValue().equals("create"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing create method"));

        var attribute = method.findAttribute(Attributes.runtimeVisibleAnnotations())
                .orElseThrow(() -> new IllegalStateException("Missing method annotations"));
        var annotation = attribute.annotations().stream()
                .filter(candidate -> candidate.className().stringValue().equals(POST_MAPPING_DESCRIPTOR))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing @PostMapping"));

        String path = annotation.elements().get(0).value() instanceof java.lang.classfile.AnnotationValue.OfArray arrayValue
                && !arrayValue.values().isEmpty()
                && arrayValue.values().get(0) instanceof java.lang.classfile.AnnotationValue.OfString stringValue
                ? stringValue.stringValue()
                : null;
        if (!"/api/create".equals(path)) {
            throw new IllegalStateException("Unexpected PostMapping path: " + path);
        }

        System.out.println("QinSpringPostMappingSmokeTestMain passed.");
    }
}
