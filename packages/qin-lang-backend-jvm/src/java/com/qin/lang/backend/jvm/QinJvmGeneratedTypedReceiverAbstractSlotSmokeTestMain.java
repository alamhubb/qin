package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Proves generated Java-to-TS code that omits an abstract base prototype still
 * gets a JVM slot when a typed local/parameter receiver calls a subclass-owned method.
 */
public final class QinJvmGeneratedTypedReceiverAbstractSlotSmokeTestMain {
    private QinJvmGeneratedTypedReceiverAbstractSlotSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class GeneratedTypedReceiverBase {
                  baseMarker(): string {
                    return "base"
                  }
                }

                class GeneratedTypedReceiverRuntime {
                  call(parser: GeneratedTypedReceiverBase): string {
                    parser.dispatchRule()
                    return parser.result()
                  }
                }

                class GeneratedTypedReceiverChild extends GeneratedTypedReceiverBase {
                  value: string = "unset"

                  dispatchRule(): void {
                    this.value = "ok"
                  }

                  result(): string {
                    return this.value
                  }

                  run(): string {
                    const runtime = new GeneratedTypedReceiverRuntime()
                    return runtime.call(this)
                  }
                }
                """;
        String previousMode = System.getProperty("qin.dynamicSemanticMode");
        System.setProperty("qin.dynamicSemanticMode", "error");
        try {
            QinIrProgram program = new QinFrontendLowerer().lowerSource(source);
            Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
            ByteArrayClassLoader loader = new ByteArrayClassLoader(compiled);
            Class<?> baseClass = loader.loadClass("GeneratedTypedReceiverBase");
            if (!Modifier.isAbstract(baseClass.getModifiers())) {
                throw new IllegalStateException("Generated typed receiver base should be abstract");
            }
            if (!Modifier.isAbstract(baseClass.getDeclaredMethod("dispatchRule").getModifiers())) {
                throw new IllegalStateException("dispatchRule should be an abstract JVM method slot");
            }
            if (!Modifier.isAbstract(baseClass.getDeclaredMethod("result").getModifiers())) {
                throw new IllegalStateException("result should be an abstract JVM method slot");
            }
            Class<?> childClass = loader.loadClass("GeneratedTypedReceiverChild");
            Object child = childClass.getConstructor().newInstance();
            Object result = childClass.getMethod("run").invoke(child);
            if (!"ok".equals(result)) {
                throw new IllegalStateException("Unexpected typed receiver dispatch result: " + result);
            }
            System.out.println("QinJvmGeneratedTypedReceiverAbstractSlotSmokeTestMain OK");
        } finally {
            if (previousMode == null) {
                System.clearProperty("qin.dynamicSemanticMode");
            } else {
                System.setProperty("qin.dynamicSemanticMode", previousMode);
            }
        }
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private final Map<String, byte[]> classes;

        private ByteArrayClassLoader(Map<String, byte[]> classes) {
            this.classes = classes;
        }

        @Override
        protected Class<?> findClass(String binaryName) throws ClassNotFoundException {
            byte[] bytes = classes.get(binaryName);
            if (bytes == null) {
                return super.findClass(binaryName);
            }
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
