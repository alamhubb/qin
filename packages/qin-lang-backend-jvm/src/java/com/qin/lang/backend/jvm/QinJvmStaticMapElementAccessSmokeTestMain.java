package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrElementAccessExpression;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrLocalDeclarationStatement;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrReturnStatement;
import com.qin.lang.ir.QinIrStatement;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrTypeRef;

import java.util.List;
import java.util.Map;

/**
 * Proves explicit Map/Dict locals keep a static collection-get lowering for
 * generated patterns like Record<K, V> typeMap followed by typeMap[key].
 */
public final class QinJvmStaticMapElementAccessSmokeTestMain {
    private QinJvmStaticMapElementAccessSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrClassDeclaration service = new QinIrClassDeclaration(
                null,
                "StaticMapElementAccessService",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(method()));
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
                List.of(service));

        Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
        ByteArrayClassLoader loader = new ByteArrayClassLoader();
        for (Map.Entry<String, byte[]> entry : compiled.entrySet()) {
            loader.define(entry.getKey(), entry.getValue());
        }
        Object instance = loader.loadClass(service.binaryName()).getDeclaredConstructor().newInstance();
        Object result = instance.getClass().getDeclaredMethod("tokenType", String.class).invoke(instance, "plus");
        if (!"PLUS".equals(result)) {
            throw new IllegalStateException("Unexpected static Map element result: " + result);
        }
        System.out.println("QinJvmStaticMapElementAccessSmokeTestMain OK");
    }

    private static QinIrMethodDeclaration method() {
        List<QinIrStatement> body = List.of(
                new QinIrLocalDeclarationStatement(
                        "typeMap",
                        new QinIrObjectLiteral(List.of(
                                new QinIrObjectProperty("plus", new QinIrStringLiteral("PLUS")),
                                new QinIrObjectProperty("minus", new QinIrStringLiteral("MINUS")))),
                        QinIrTypeRef.classType("java.util.Map")),
                new QinIrReturnStatement(new QinIrElementAccessExpression(
                        new QinIrIdentifierReference("typeMap"),
                        new QinIrIdentifierReference("operator"))));
        return new QinIrMethodDeclaration(
                "tokenType",
                QinIrTypeRef.stringType(),
                List.of(new QinIrParameter("operator", QinIrTypeRef.stringType(), List.of())),
                List.of(),
                null,
                body,
                List.of(),
                null,
                false);
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
