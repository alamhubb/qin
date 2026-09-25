package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrBreakStatement;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrContinueStatement;
import com.qin.lang.ir.QinIrForEachStatement;
import com.qin.lang.ir.QinIrIfStatement;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Proves parsed Qin for...of method bodies lower to Qin for-each IR and
 * execute as JVM .class iterator bytecode.
 */
public final class QinJvmParsedForOfSmokeTestMain {
    private QinJvmParsedForOfSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class ParsedForOfService {
                  sum(values: __QinJavaUtilList<number>): number {
                    let total = 0
                    for (const item of values) {
                      if (item == 2) {
                        continue
                      }
                      if (item == 5) {
                        break
                      }
                      total = total + item
                    }
                    return total
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = requireClass(program, "ParsedForOfService");
        if (declaration.methods().size() != 1) {
            throw new IllegalStateException("Expected one parsed for...of method");
        }
        QinIrForEachStatement forEachStatement = declaration.methods().get(0).bodyStatements().stream()
                .filter(QinIrForEachStatement.class::isInstance)
                .map(QinIrForEachStatement.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Parsed method did not lower for...of statement IR"));
        if (!"item".equals(forEachStatement.itemName())) {
            throw new IllegalStateException("Parsed for...of item binding was not preserved");
        }
        boolean hasContinue = forEachStatement.body().stream()
                .filter(QinIrIfStatement.class::isInstance)
                .map(QinIrIfStatement.class::cast)
                .flatMap(statement -> statement.consequent().stream())
                .anyMatch(QinIrContinueStatement.class::isInstance);
        boolean hasBreak = forEachStatement.body().stream()
                .filter(QinIrIfStatement.class::isInstance)
                .map(QinIrIfStatement.class::cast)
                .flatMap(statement -> statement.consequent().stream())
                .anyMatch(QinIrBreakStatement.class::isInstance);
        if (!hasContinue || !hasBreak) {
            throw new IllegalStateException("Parsed for...of body did not lower break/continue IR");
        }

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "ParsedForOfService");
        Class<?> defined = new ByteArrayClassLoader().define("ParsedForOfService", classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();

        Object result = defined.getDeclaredMethod("sum", List.class).invoke(instance, List.of(1.0d, 2.0d, 3.0d, 5.0d, 8.0d));
        if (!Double.valueOf(4.0d).equals(result)) {
            throw new IllegalStateException("Unexpected parsed for...of result: " + result);
        }

        assertGeneratedSubhutiCstThisFieldForOfCompiles();
        assertActualGeneratedSubhutiCstThisFieldForOfCompiles();
        assertConditionalTypedListForOfCompilesStatically();
        assertIterableParameterForOfCompilesStatically();
        assertJavaMapKeysForOfCompilesStatically();
        assertJavaScriptMapConstructorStaticArgumentCompiles();

        System.out.println("QinJvmParsedForOfSmokeTestMain passed.");
    }

    private static void assertGeneratedSubhutiCstThisFieldForOfCompiles() {
        String text = """
                class com_subhuti_struct_SubhutiCst {
                  __qin_field_name: string | null = null as any;
                  __qin_field_children: __QinJavaUtilList<com_subhuti_struct_SubhutiCst> | null = null as any;

                  getName(): string {
                    return this.__qin_field_name;
                  }

                  __qin_overload_getChildren_1_1(name: string): __QinJavaUtilList<com_subhuti_struct_SubhutiCst> {
                    let result: __QinJavaUtilList<com_subhuti_struct_SubhutiCst> = new __QinJavaUtilArrayList();
                    for (const child of this.__qin_field_children) {
                      if (__QinJavaLangString.equals(name, child.getName())) {
                        result.add(child);
                      }
                    }
                    return result;
                  }
                }
                const SubhutiCst = com_subhuti_struct_SubhutiCst;
                export { com_subhuti_struct_SubhutiCst };
                """;
        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = requireClass(program, "com_subhuti_struct_SubhutiCst");
        QinIrForEachStatement forEachStatement = declaration.methods().stream()
                .filter(method -> "__qin_overload_getChildren_1_1".equals(method.name()))
                .flatMap(method -> method.bodyStatements().stream())
                .filter(QinIrForEachStatement.class::isInstance)
                .map(QinIrForEachStatement.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Generated SubhutiCst for...of did not lower"));
        if (!"com_subhuti_struct_SubhutiCst".equals(forEachStatement.itemType().binaryName())) {
            throw new IllegalStateException("Generated SubhutiCst for...of item type was not static: "
                    + forEachStatement.itemType());
        }
        new QinJvmDeclarationClassEmitter().compileAllClasses(program);
    }

    private static void assertActualGeneratedSubhutiCstThisFieldForOfCompiles() throws Exception {
        Path repoRoot = findQinRepositoryRoot();
        String text = Files.readString(
                repoRoot.resolve("packages/qin-language/generated/qin-parser-ts/com/subhuti/struct/SubhutiCst.ts"),
                StandardCharsets.UTF_8);
        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = requireClass(program, "com_subhuti_struct_SubhutiCst");
        QinIrForEachStatement forEachStatement = declaration.methods().stream()
                .filter(method -> "__qin_overload_getChildren_1_1".equals(method.name()))
                .flatMap(method -> method.bodyStatements().stream())
                .filter(QinIrForEachStatement.class::isInstance)
                .map(QinIrForEachStatement.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Actual generated SubhutiCst for...of did not lower"));
        if (!"com_subhuti_struct_SubhutiCst".equals(forEachStatement.itemType().binaryName())) {
            throw new IllegalStateException("Actual generated SubhutiCst for...of item type was not static: "
                    + forEachStatement.itemType());
        }
        new QinJvmDeclarationClassEmitter().compileAllClasses(program);
    }

    private static Path findQinRepositoryRoot() throws Exception {
        Path candidate = Path.of("").toAbsolutePath().normalize();
        while (candidate != null) {
            if (Files.isRegularFile(candidate.resolve("qin.config.js"))
                    && Files.isDirectory(candidate.resolve("packages/qin-parser"))
                    && Files.isDirectory(candidate.resolve("packages/qin-runtime-core"))) {
                return candidate;
            }
            candidate = candidate.getParent();
        }
        throw new IllegalStateException("Could not locate Qin repository root from " + Path.of("").toAbsolutePath());
    }

    private static void assertConditionalTypedListForOfCompilesStatically() throws Exception {
        String previousMode = System.getProperty("qin.dynamicSemanticMode");
        System.setProperty("qin.dynamicSemanticMode", "error");
        try {
            String text = """
                    class ConditionalTypedForOfAddress {
                      __qin_field_name: string
                      constructor(name: string) {
                        this.__qin_field_name = name
                      }
                      name(): string {
                        return this.__qin_field_name
                      }
                    }

                    class ConditionalTypedForOfProbe {
                      static collect(addresses: __QinJavaUtilList<ConditionalTypedForOfAddress>): string {
                        let result = ""
                        for (const address of (__qin_binary__("==", addresses, null) ? __QinJavaUtilList.of() : addresses)) {
                          result = result + address.name()
                        }
                        return result
                      }
                    }
                    """;
            QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
            Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
            ByteArrayClassLoader loader = new ByteArrayClassLoader();
            for (Map.Entry<String, byte[]> entry : compiled.entrySet()) {
                loader.define(entry.getKey(), entry.getValue());
            }
            Class<?> addressClass = loader.loadClass("ConditionalTypedForOfAddress");
            Object address = addressClass.getDeclaredConstructor(String.class).newInstance("ok");
            Object result = loader.loadClass("ConditionalTypedForOfProbe")
                    .getDeclaredMethod("collect", List.class)
                    .invoke(null, List.of(address));
            if (!"ok".equals(result)) {
                throw new IllegalStateException("Unexpected conditional typed for...of result: " + result);
            }
        } finally {
            if (previousMode == null) {
                System.clearProperty("qin.dynamicSemanticMode");
            } else {
                System.setProperty("qin.dynamicSemanticMode", previousMode);
            }
        }
    }

    private static void assertIterableParameterForOfCompilesStatically() throws Exception {
        String previousMode = System.getProperty("qin.dynamicSemanticMode");
        System.setProperty("qin.dynamicSemanticMode", "error");
        try {
            String text = """
                    class IterableForOfProbe {
                      static sum(candidates: Iterable<number | null>): number {
                        let total = 0
                        for (const candidate of candidates) {
                          total = total + candidate
                        }
                        return total
                      }
                    }
                    """;
            QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
            Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
            ByteArrayClassLoader loader = new ByteArrayClassLoader();
            for (Map.Entry<String, byte[]> entry : compiled.entrySet()) {
                loader.define(entry.getKey(), entry.getValue());
            }
            Object result = loader.loadClass("IterableForOfProbe")
                    .getDeclaredMethod("sum", Iterable.class)
                    .invoke(null, List.of(1.0d, 2.0d, 3.0d));
            if (!Double.valueOf(6.0d).equals(result)) {
                throw new IllegalStateException("Unexpected Iterable for...of result: " + result);
            }
        } finally {
            if (previousMode == null) {
                System.clearProperty("qin.dynamicSemanticMode");
            } else {
                System.setProperty("qin.dynamicSemanticMode", previousMode);
            }
        }
    }

    private static void assertJavaMapKeysForOfCompilesStatically() throws Exception {
        String previousMode = System.getProperty("qin.dynamicSemanticMode");
        System.setProperty("qin.dynamicSemanticMode", "error");
        try {
            String text = """
                    class RuntimeAtomData {
                      group: string
                      constructor(group: string) {
                        this.group = group
                      }
                    }

                    class RuntimeStoreProbe {
                      static accept(name: string): string {
                        return name
                      }

                      static first(runtimeMap: Map<string, RuntimeAtomData>): string {
                        for (const name of runtimeMap.keys()) {
                          return RuntimeStoreProbe.accept(name)
                        }
                        return ""
                      }
                    }
                    """;
            QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
            QinIrClassDeclaration probe = requireClass(program, "RuntimeStoreProbe");
            QinIrForEachStatement forEachStatement = probe.methods().stream()
                    .filter(method -> "first".equals(method.name()))
                    .flatMap(method -> method.bodyStatements().stream())
                    .filter(QinIrForEachStatement.class::isInstance)
                    .map(QinIrForEachStatement.class::cast)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Map.keys for...of did not lower"));
            if (forEachStatement.itemType().kind() != com.qin.lang.ir.QinIrTypeKind.STRING) {
                throw new IllegalStateException("Map.keys for...of item type should stay string: "
                        + forEachStatement.itemType());
            }
            Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
            ByteArrayClassLoader loader = new ByteArrayClassLoader();
            for (Map.Entry<String, byte[]> entry : compiled.entrySet()) {
                loader.define(entry.getKey(), entry.getValue());
            }
            com.qin.lang.runtime.JavaEsmMapObject runtimeMap = new com.qin.lang.runtime.JavaEsmMapObject();
            runtimeMap.set("displayFlex", "atom");
            Object result = loader.loadClass("RuntimeStoreProbe")
                    .getDeclaredMethod("first", com.qin.lang.runtime.JavaEsmMapObject.class)
                    .invoke(null, runtimeMap);
            if (!"displayFlex".equals(result)) {
                throw new IllegalStateException("Unexpected Map.keys for...of result: " + result);
            }
        } finally {
            if (previousMode == null) {
                System.clearProperty("qin.dynamicSemanticMode");
            } else {
                System.setProperty("qin.dynamicSemanticMode", previousMode);
            }
        }
    }

    private static void assertJavaScriptMapConstructorStaticArgumentCompiles() throws Exception {
        String previousMode = System.getProperty("qin.dynamicSemanticMode");
        System.setProperty("qin.dynamicSemanticMode", "error");
        try {
            String text = """
                    class RuntimeAtomData {
                      group: string
                      constructor(group: string) {
                        this.group = group
                      }
                    }

                    class RuntimeStoreProbe {
                      static setRuntimeMap(map: Map<string, RuntimeAtomData>): string {
                        for (const name of map.keys()) {
                          return name
                        }
                        return ""
                      }

                      static init(): string {
                        const runtimeMap = new Map<string, RuntimeAtomData>()
                        runtimeMap.set("displayFlex", new RuntimeAtomData("atom"))
                        return RuntimeStoreProbe.setRuntimeMap(runtimeMap)
                      }
                    }
                    """;
            QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
            Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
            ByteArrayClassLoader loader = new ByteArrayClassLoader();
            for (Map.Entry<String, byte[]> entry : compiled.entrySet()) {
                loader.define(entry.getKey(), entry.getValue());
            }
            Object result = loader.loadClass("RuntimeStoreProbe")
                    .getDeclaredMethod("init")
                    .invoke(null);
            if (!"displayFlex".equals(result)) {
                throw new IllegalStateException("Unexpected new Map<K,V> static argument result: " + result);
            }
        } finally {
            if (previousMode == null) {
                System.clearProperty("qin.dynamicSemanticMode");
            } else {
                System.setProperty("qin.dynamicSemanticMode", previousMode);
            }
        }
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
