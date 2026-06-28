package com.qin.lang.cli;

import com.qin.lang.backend.jvm.QinClassFileWriter;
import com.qin.lang.backend.jvm.QinJvmClassFileBackend;
import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * End-to-end smoke test:
 * source string -> Slime parse -> Qin IR -> JVM class file -> run().
 */
public final class SmokeTestMain {
    private SmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        List<SmokeCase> cases = List.of(
                new SmokeCase(
                        "function-call",
                        "const a = () => 1; console.log(a()); a()",
                        1.0d),
                new SmokeCase(
                        "export-const",
                        "export const answer = 41; answer",
                        41.0d),
                new SmokeCase(
                        "object-member",
                        "const model = { value: 41, name: 'qin' }; console.log(model.value); model.name",
                        "qin"),
                new SmokeCase(
                        "array-literal",
                        "const values = [1, 'two', true]; values",
                        List.of(1.0d, "two", Boolean.TRUE)),
                new SmokeCase(
                        "java-import-instance",
                        "import { ArrayList } from 'java:java.util'; const items = new ArrayList(); items.add('qin'); console.log(items.size()); items.size()",
                        List.of("qin")));
        Path outputDir = Path.of("build", "generated-classes");

        QinFrontendLowerer lowerer = new QinFrontendLowerer();
        QinJvmClassFileBackend backend = new QinJvmClassFileBackend();
        ByteArrayClassLoader classLoader = new ByteArrayClassLoader(SmokeTestMain.class.getClassLoader());

        for (int i = 0; i < cases.size(); i++) {
            SmokeCase smokeCase = cases.get(i);
            String className = "com.qin.generated.SmokeCase" + i;
            QinIrProgram program = lowerer.lowerSource(smokeCase.source());
            byte[] classBytes = backend.compileProgram(program, className);
            Path classFile = QinClassFileWriter.writeClassFile(outputDir, className, classBytes);

            Class<?> generatedClass = classLoader.define(className, classBytes);
            Object runResult = generatedClass.getMethod("run").invoke(null);
            assertResult(smokeCase, runResult);

            System.out.println("Case: " + smokeCase.name());
            System.out.println("Source: " + smokeCase.source());
            System.out.println("Generated .class: " + classFile.toAbsolutePath());
            System.out.println("run() result: " + runResult);
        }

        System.out.println("Qin JVM .class smoke corpus passed: " + cases.size() + " cases");
    }

    private static void assertResult(SmokeCase smokeCase, Object actual) {
        Object expected = smokeCase.expectedResult();
        if (expected instanceof Double expectedDouble && actual instanceof Number actualNumber) {
            double actualDouble = actualNumber.doubleValue();
            if (Double.compare(expectedDouble, actualDouble) == 0) {
                return;
            }
        }
        if (expected instanceof List<?> expectedList && actual instanceof List<?> actualList) {
            if (expectedList.size() == actualList.size()) {
                boolean same = true;
                for (int i = 0; i < expectedList.size(); i++) {
                    Object expectedItem = expectedList.get(i);
                    Object actualItem = actualList.get(i);
                    if (expectedItem instanceof Double expectedDouble && actualItem instanceof Number actualNumber) {
                        same = Double.compare(expectedDouble, actualNumber.doubleValue()) == 0;
                    } else {
                        same = Objects.equals(expectedItem, actualItem);
                    }
                    if (!same) {
                        break;
                    }
                }
                if (same) {
                    return;
                }
            }
        }
        if (Objects.equals(expected, actual)) {
            return;
        }
        throw new IllegalStateException("Unexpected run() result for " + smokeCase.name()
                + ": expected=" + expected + ", actual=" + actual);
    }

    private record SmokeCase(String name, String source, Object expectedResult) {
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private ByteArrayClassLoader(ClassLoader parent) {
            super(parent);
        }

        private Class<?> define(String binaryName, byte[] classBytes) {
            return defineClass(binaryName, classBytes, 0, classBytes.length);
        }
    }
}

