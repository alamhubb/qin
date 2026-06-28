package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrProgram;

import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.util.Map;

/**
 * Proves parsed Qin source can emit local class inheritance to JVM .class bytes
 * and execute inherited plus child methods through reflection.
 */
public final class QinJvmParsedLocalInheritanceSmokeTestMain {
    private QinJvmParsedLocalInheritanceSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class BaseService {
                  label(): string {
                    return "base"
                  }
                }

                class ChildService extends BaseService {
                  childLabel(): string {
                    return "child"
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        requireSuperclass(program, "BaseService", "java.lang.Object");
        requireSuperclass(program, "ChildService", "BaseService");

        Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
        requireClassfileSuperclass(compiled.get("ChildService"), "BaseService");

        ByteArrayClassLoader loader = new ByteArrayClassLoader();
        Class<?> baseClass = loader.define("BaseService", compiled.get("BaseService"));
        Class<?> childClass = loader.define("ChildService", compiled.get("ChildService"));
        if (!baseClass.isAssignableFrom(childClass)) {
            throw new IllegalStateException("ChildService does not extend BaseService");
        }

        Object child = childClass.getDeclaredConstructor().newInstance();
        Object inherited = childClass.getMethod("label").invoke(child);
        Object own = childClass.getDeclaredMethod("childLabel").invoke(child);
        if (!"base".equals(inherited) || !"child".equals(own)) {
            throw new IllegalStateException("Unexpected local inheritance results: " + inherited + ", " + own);
        }

        System.out.println("QinJvmParsedLocalInheritanceSmokeTestMain passed.");
    }

    private static void requireSuperclass(QinIrProgram program, String binaryName, String expectedSuperclass) {
        QinIrClassDeclaration declaration = program.classDeclarations().stream()
                .filter(candidate -> binaryName.equals(candidate.binaryName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing class declaration: " + binaryName));
        String actualSuperclass = declaration.superType().binaryName();
        if (!expectedSuperclass.equals(actualSuperclass)) {
            throw new IllegalStateException(
                    "Unexpected superclass for " + binaryName + ": " + actualSuperclass);
        }
    }

    private static void requireClassfileSuperclass(byte[] bytes, String expectedInternalName) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalStateException("Missing ChildService class bytes");
        }
        ClassModel classModel = ClassFile.of().parse(bytes);
        String superclass = classModel.superclass()
                .orElseThrow(() -> new IllegalStateException("Missing classfile superclass"))
                .asInternalName();
        if (!expectedInternalName.equals(superclass)) {
            throw new IllegalStateException("Unexpected classfile superclass: " + superclass);
        }
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            if (bytes == null || bytes.length == 0) {
                throw new IllegalStateException("Missing class bytes for " + binaryName);
            }
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
