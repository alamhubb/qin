package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrTypeRef;
import java.util.List;
import java.util.Map;

/**
 * Smoke test for treating a declared class name as a value reference.
 */
public final class QinJvmClassReferenceSmokeTestMain {
    private QinJvmClassReferenceSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrClassDeclaration configLookup = new QinIrClassDeclaration(
                null,
                "ConfigLookup",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(new QinIrMethodDeclaration(
                        "init",
                        QinIrTypeRef.classType("java.lang.Class"),
                        List.of(),
                        List.of(),
                        new QinIrIdentifierReference("ConfigLookup"))));
        QinIrProgram program = new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(configLookup));

        Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
        ByteArrayClassLoader loader = new ByteArrayClassLoader();
        Class<?> configLookupClass = loader.define("ConfigLookup", compiled.get("ConfigLookup"));
        Object configLookupInstance = configLookupClass.getDeclaredConstructor().newInstance();

        Object result = configLookupClass.getDeclaredMethod("init").invoke(configLookupInstance);
        if (result != configLookupClass) {
            throw new IllegalStateException("Unexpected class reference result: " + result);
        }

        System.out.println("QinJvmClassReferenceSmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
