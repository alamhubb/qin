package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrTypeRef;

import java.util.ArrayList;
import java.util.List;

public final class QinJvmJavaSuperConstructorSmokeTestMain {
    private QinJvmJavaSuperConstructorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrClassDeclaration declaration = new QinIrClassDeclaration(
                null,
                "SizedList",
                QinIrTypeRef.classType("java.util.ArrayList"),
                List.of(),
                List.of(),
                List.of());
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
                List.of(declaration));

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, declaration.binaryName());
        Class<?> defined = new ByteArrayClassLoader().define(declaration.binaryName(), classBytes);
        Object instance = defined.getDeclaredConstructor(int.class).newInstance(8);
        if (!(instance instanceof ArrayList<?>)) {
            throw new IllegalStateException("Generated class is not an ArrayList: " + instance);
        }
        @SuppressWarnings("unchecked")
        ArrayList<Object> list = (ArrayList<Object>) instance;
        list.add("qin");
        if (!"qin".equals(list.get(0))) {
            throw new IllegalStateException("ArrayList super constructor smoke failed: " + list);
        }

        System.out.println("QinJvmJavaSuperConstructorSmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
