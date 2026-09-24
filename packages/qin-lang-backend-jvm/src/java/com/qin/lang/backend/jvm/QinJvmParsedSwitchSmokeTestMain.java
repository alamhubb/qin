package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrBreakStatement;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrSwitchStatement;
import com.qin.lang.ir.QinIrProgram;

/**
 * Proves parsed Qin switch/case/default method bodies lower to switch IR and
 * execute as JVM .class control flow.
 */
public final class QinJvmParsedSwitchSmokeTestMain {
    private QinJvmParsedSwitchSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class ParsedSwitchService {
                  label(status: string): string {
                    switch (status) {
                      case "ready":
                        return "Ready"
                      case "done":
                        return "Done"
                      default:
                        return "Other"
                    }
                  }

                  code(status: string): string {
                    let result = "Other"
                    switch (status) {
                      case "ready":
                        result = "Ready"
                        break
                      case "done":
                        result = "Done"
                        break
                      default:
                        result = "Other"
                    }
                    return result
                  }

                  fallthroughEmptyBlocks(tokenName: string): boolean {
                    switch (tokenName) {
                      case "This": {
                      }
                      case "IdentifierName": {
                      }
                      case "New": {
                        return true
                      }
                      case "Yield": {
                        return false
                      }
                      default: {
                        return false
                      }
                    }
                  }
                }
                """;

        String previousMode = System.getProperty("qin.dynamicSemanticMode");
        System.setProperty("qin.dynamicSemanticMode", "error");
        try {
            QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
            QinIrClassDeclaration declaration = requireClass(program, "ParsedSwitchService");
            if (declaration.methods().size() != 3) {
                throw new IllegalStateException("Expected three parsed switch methods");
            }
            QinIrSwitchStatement labelSwitch = declaration.methods().get(0).bodyStatements().stream()
                    .filter(QinIrSwitchStatement.class::isInstance)
                    .map(QinIrSwitchStatement.class::cast)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Parsed method did not lower switch statement IR"));
            if (labelSwitch.cases().size() != 3
                    || labelSwitch.cases().stream().filter(QinIrSwitchCase -> QinIrSwitchCase.test() != null).count() != 2
                    || labelSwitch.cases().stream().noneMatch(QinIrSwitchCase -> QinIrSwitchCase.test() == null)) {
                throw new IllegalStateException("Parsed switch did not lower case/default IR");
            }
            QinIrSwitchStatement codeSwitch = declaration.methods().get(1).bodyStatements().stream()
                    .filter(QinIrSwitchStatement.class::isInstance)
                    .map(QinIrSwitchStatement.class::cast)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Parsed method did not lower break switch IR"));
            boolean hasBreak = codeSwitch.cases().stream()
                    .flatMap(switchCase -> switchCase.consequent().stream())
                    .anyMatch(QinIrBreakStatement.class::isInstance);
            if (!hasBreak) {
                throw new IllegalStateException("Parsed switch case did not lower break IR");
            }

            byte[] classBytes = new QinJvmDeclarationClassEmitter()
                    .compileSingleClass(program, "ParsedSwitchService");
            Class<?> defined = new ByteArrayClassLoader().define("ParsedSwitchService", classBytes);
            Object instance = defined.getDeclaredConstructor().newInstance();

            Object readyLabel = defined.getDeclaredMethod("label", String.class).invoke(instance, "ready");
            Object doneLabel = defined.getDeclaredMethod("label", String.class).invoke(instance, "done");
            Object otherLabel = defined.getDeclaredMethod("label", String.class).invoke(instance, "x");
            Object readyCode = defined.getDeclaredMethod("code", String.class).invoke(instance, "ready");
            Object doneCode = defined.getDeclaredMethod("code", String.class).invoke(instance, "done");
            Object otherCode = defined.getDeclaredMethod("code", String.class).invoke(instance, "x");
            Object fallthroughThis = defined.getDeclaredMethod("fallthroughEmptyBlocks", String.class).invoke(instance, "This");
            Object fallthroughIdentifier = defined.getDeclaredMethod("fallthroughEmptyBlocks", String.class).invoke(instance, "IdentifierName");
            Object fallthroughNew = defined.getDeclaredMethod("fallthroughEmptyBlocks", String.class).invoke(instance, "New");
            Object fallthroughYield = defined.getDeclaredMethod("fallthroughEmptyBlocks", String.class).invoke(instance, "Yield");
            Object fallthroughOther = defined.getDeclaredMethod("fallthroughEmptyBlocks", String.class).invoke(instance, "Other");
            if (!"Ready".equals(readyLabel)
                    || !"Done".equals(doneLabel)
                    || !"Other".equals(otherLabel)
                    || !"Ready".equals(readyCode)
                    || !"Done".equals(doneCode)
                    || !"Other".equals(otherCode)
                    || !Boolean.TRUE.equals(fallthroughThis)
                    || !Boolean.TRUE.equals(fallthroughIdentifier)
                    || !Boolean.TRUE.equals(fallthroughNew)
                    || !Boolean.FALSE.equals(fallthroughYield)
                    || !Boolean.FALSE.equals(fallthroughOther)) {
                throw new IllegalStateException("Unexpected parsed switch results: "
                        + readyLabel + ", " + doneLabel + ", " + otherLabel + ", "
                        + readyCode + ", " + doneCode + ", " + otherCode + ", "
                        + fallthroughThis + ", " + fallthroughIdentifier + ", " + fallthroughNew + ", "
                        + fallthroughYield + ", " + fallthroughOther);
            }
        } finally {
            if (previousMode == null) {
                System.clearProperty("qin.dynamicSemanticMode");
            } else {
                System.setProperty("qin.dynamicSemanticMode", previousMode);
            }
        }

        System.out.println("QinJvmParsedSwitchSmokeTestMain passed.");
    }

    private static QinIrClassDeclaration requireClass(QinIrProgram program, String binaryName) {
        return program.classDeclarations().stream()
                .filter(candidate -> binaryName.equals(candidate.binaryName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing class declaration: " + binaryName));
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
