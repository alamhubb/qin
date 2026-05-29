package com.qin.runtime.core;

import java.lang.classfile.Attributes;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.MethodModel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Smoke test for Spring RequestBody parameter using a local Qin-declared type.
 */
public final class QinSpringRequestBodyLocalTypeSmokeTestMain {
    private static final String REQUEST_BODY_DESCRIPTOR =
            "Lorg/springframework/web/bind/annotation/RequestBody;";
    private static final String EXPECTED_METHOD_DESCRIPTOR =
            "(LPayload;)Ljava/lang/String;";

    private QinSpringRequestBodyLocalTypeSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path source = Files.createTempFile("qin-post-body-local-type-controller-", ".qin");
        Files.writeString(source, """
                import { RestController as RC, PostMapping as POST, RequestBody as Body } from "java:org.springframework.web.bind.annotation"

                class Payload {
                }

                @RC
                class PostController {
                  @POST("/api/create")
                  create(@Body payload: Payload) {
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

        String descriptor = method.methodType().stringValue();
        if (!EXPECTED_METHOD_DESCRIPTOR.equals(descriptor)) {
            throw new IllegalStateException("Unexpected local type method descriptor: " + descriptor);
        }

        var parameterAnnotations = method.findAttribute(Attributes.runtimeVisibleParameterAnnotations())
                .orElseThrow(() -> new IllegalStateException("Missing parameter annotations"));
        if (parameterAnnotations.parameterAnnotations().isEmpty()
                || parameterAnnotations.parameterAnnotations().get(0).isEmpty()
                || parameterAnnotations.parameterAnnotations().get(0).stream()
                .noneMatch(candidate -> candidate.className().stringValue().equals(REQUEST_BODY_DESCRIPTOR))) {
            throw new IllegalStateException("Missing @RequestBody parameter annotation");
        }

        System.out.println("QinSpringRequestBodyLocalTypeSmokeTestMain passed.");
    }
}
