package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrTypeRef;
import java.util.List;
import java.util.Map;

/**
 * Smoke test for declaration-class host runtime owner aliases.
 */
public final class QinJvmDeclarationHostRuntimeOwnerAliasSmokeTestMain {
    private QinJvmDeclarationHostRuntimeOwnerAliasSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrClassDeclaration flattenedShadow = new QinIrClassDeclaration(
                null,
                "com_qin_lang_runtime_JavaEsmGlobal",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of());
        QinIrClassDeclaration caller = callerDeclaration("HostRuntimeAliasCaller");
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
                List.of(flattenedShadow, caller));

        Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
        byte[] callerBytes = compiled.get("HostRuntimeAliasCaller");
        if (callerBytes == null) {
            throw new IllegalStateException("HostRuntimeAliasCaller was not compiled");
        }
        ByteArrayClassLoader loader = new ByteArrayClassLoader();
        Class<?> callerClass = loader.define("HostRuntimeAliasCaller", callerBytes);
        Object callerInstance = callerClass.getDeclaredConstructor().newInstance();
        Object result = callerClass.getDeclaredMethod("same").invoke(callerInstance);
        if (!Boolean.TRUE.equals(result)) {
            throw new IllegalStateException("Unexpected host runtime binary result: " + result);
        }

        System.out.println("QinJvmDeclarationHostRuntimeOwnerAliasSmokeTestMain passed.");
    }

    private static QinIrClassDeclaration callerDeclaration(String simpleName) {
        return new QinIrClassDeclaration(
                null,
                simpleName,
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(new QinIrMethodDeclaration(
                        "same",
                        QinIrTypeRef.classType("java.lang.Object"),
                        List.of(),
                        List.of(),
                        new QinIrStaticMethodCallExpression(
                                "JavaEsmGlobal",
                                "com_qin_lang_runtime_JavaEsmGlobal",
                                "__qin_binary__",
                                List.of(
                                        new QinIrStringLiteral("=="),
                                        new QinIrNumberLiteral(1.0),
                                        new QinIrNumberLiteral(1.0))))));
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
