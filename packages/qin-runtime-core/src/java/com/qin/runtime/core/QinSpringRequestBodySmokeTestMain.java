package com.qin.runtime.core;

import java.lang.classfile.Attributes;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.MethodModel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Smoke test for Spring PostMapping + RequestBody parameter on the IR-based bridge.
 */
public final class QinSpringRequestBodySmokeTestMain {
    private static final String POST_MAPPING_DESCRIPTOR =
            "Lorg/springframework/web/bind/annotation/PostMapping;";
    private static final String REQUEST_BODY_DESCRIPTOR =
            "Lorg/springframework/web/bind/annotation/RequestBody;";

    private QinSpringRequestBodySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path source = Files.createTempFile("qin-post-body-controller-", ".qin");
        Files.writeString(source, """
                import { RestController as RC, PostMapping as POST, RequestBody as Body } from "java:org.springframework.web.bind.annotation"

                @RC
                class PostController {
                  @POST("/api/create")
                  create(@Body payload: string) {
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

        var methodAnnotations = method.findAttribute(Attributes.runtimeVisibleAnnotations())
                .orElseThrow(() -> new IllegalStateException("Missing method annotations"));
        methodAnnotations.annotations().stream()
                .filter(candidate -> candidate.className().stringValue().equals(POST_MAPPING_DESCRIPTOR))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing @PostMapping"));

        var parameterAnnotations = method.findAttribute(Attributes.runtimeVisibleParameterAnnotations())
                .orElseThrow(() -> new IllegalStateException("Missing parameter annotations"));
        if (parameterAnnotations.parameterAnnotations().isEmpty()
                || parameterAnnotations.parameterAnnotations().get(0).isEmpty()
                || parameterAnnotations.parameterAnnotations().get(0).stream()
                .noneMatch(candidate -> candidate.className().stringValue().equals(REQUEST_BODY_DESCRIPTOR))) {
            throw new IllegalStateException("Missing @RequestBody parameter annotation");
        }

        System.out.println("QinSpringRequestBodySmokeTestMain passed.");
    }
}
