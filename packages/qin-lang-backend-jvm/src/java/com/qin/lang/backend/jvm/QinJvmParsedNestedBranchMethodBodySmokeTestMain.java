package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrProgram;

/**
 * Proves parsed Qin method bodies with nested branches, multiple local
 * bindings, and fallthrough returns lower to executable JVM .class bytes.
 */
public final class QinJvmParsedNestedBranchMethodBodySmokeTestMain {
    private QinJvmParsedNestedBranchMethodBodySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class ParsedNestedBranchMethodBodyService {
                  describe(name: string, premium: boolean, active: boolean): string {
                    const base = "hello "
                    if (active) {
                      if (premium) {
                        const label = "vip "
                        return label + name
                      }
                      const standard = "std "
                      return standard + name
                    }
                    return base + name
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "ParsedNestedBranchMethodBodyService");
        Class<?> defined = new ByteArrayClassLoader().define("ParsedNestedBranchMethodBodyService", classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();

        Object vip = defined.getDeclaredMethod("describe", String.class, boolean.class, boolean.class)
                .invoke(instance, "qin", true, true);
        Object standard = defined.getDeclaredMethod("describe", String.class, boolean.class, boolean.class)
                .invoke(instance, "qin", false, true);
        Object inactive = defined.getDeclaredMethod("describe", String.class, boolean.class, boolean.class)
                .invoke(instance, "qin", true, false);
        if (!"vip qin".equals(vip) || !"std qin".equals(standard) || !"hello qin".equals(inactive)) {
            throw new IllegalStateException(
                    "Unexpected parsed nested-branch method results: " + vip + ", " + standard + ", " + inactive);
        }

        System.out.println("QinJvmParsedNestedBranchMethodBodySmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
