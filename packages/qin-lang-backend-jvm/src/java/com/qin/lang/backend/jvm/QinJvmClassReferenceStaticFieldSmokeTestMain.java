package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrTypeRef;
import com.qin.lang.runtime.JavaEsmGlobal;

import java.util.List;
import java.util.Map;

/**
 * Proves static field access through a generated class reference emits only
 * GETSTATIC and does not leave the class reference value on the operand stack.
 */
public final class QinJvmClassReferenceStaticFieldSmokeTestMain {
    private QinJvmClassReferenceStaticFieldSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrClassDeclaration holder = new QinIrClassDeclaration(
                null,
                "StaticFieldHolder",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(new QinIrFieldDeclaration(
                        "__qin_field_MARKER",
                        QinIrTypeRef.stringType(),
                        List.of(),
                        null,
                        true)),
                List.of(new QinIrMethodDeclaration(
                        "read",
                        QinIrTypeRef.stringType(),
                        List.of(),
                        List.of(),
                        new QinIrMemberAccessExpression("StaticFieldHolder", "__qin_field_MARKER"))));
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
                List.of(holder));

        Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
        ByteArrayClassLoader loader = new ByteArrayClassLoader();
        Class<?> holderClass = loader.define("StaticFieldHolder", compiled.get("StaticFieldHolder"));
        holderClass.getField("__qin_field_MARKER").set(null, "class-reference-static-field-ok");
        Object instance = holderClass.getDeclaredConstructor().newInstance();

        Object result = holderClass.getDeclaredMethod("read").invoke(instance);
        if (!"class-reference-static-field-ok".equals(result)) {
            throw new IllegalStateException("Unexpected class-reference static field result: " + result);
        }

        QinIrClassDeclaration expandoHolder = new QinIrClassDeclaration(
                null,
                "StaticExpandoHolder",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(new QinIrMethodDeclaration(
                        "read",
                        QinIrTypeRef.classType("java.lang.Object"),
                        List.of(),
                        List.of(),
                        new QinIrMemberAccessExpression("StaticExpandoHolder", "CASE_INSENSITIVE"))));
        QinIrProgram expandoProgram = new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(expandoHolder));
        Map<String, byte[]> expandoCompiled = new QinJvmDeclarationClassEmitter().compileAllClasses(expandoProgram);
        Class<?> expandoClass = loader.define("StaticExpandoHolder", expandoCompiled.get("StaticExpandoHolder"));
        JavaEsmGlobal.__qin_member_set__(expandoClass, "CASE_INSENSITIVE", Integer.valueOf(2));
        Object expandoInstance = expandoClass.getDeclaredConstructor().newInstance();
        Object expandoResult = expandoClass.getDeclaredMethod("read").invoke(expandoInstance);
        if (!Integer.valueOf(2).equals(expandoResult)) {
            throw new IllegalStateException("Unexpected class-reference static expando result: " + expandoResult);
        }

        System.out.println("QinJvmClassReferenceStaticFieldSmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
