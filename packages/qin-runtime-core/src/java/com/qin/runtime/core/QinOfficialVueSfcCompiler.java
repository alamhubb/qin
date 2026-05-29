package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinModuleSource;
import com.qin.runtime.core.vue.QinVueModuleImportRewriter;
import com.qin.runtime.core.vue.QinVueSfcModuleAssembler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Official Vue SFC compiler provider executed through Qin's own JS->JVM path.
 *
 * <p>Current stage:
 * invoke the official {@code @vue/compiler-sfc} exported {@code parse(...)}
 * function through {@link QinJsPackageRunner}. Missing packages or unsupported
 * Qin/JS host features fail fast instead of falling back to a local Vue parser.
 */
final class QinOfficialVueSfcCompiler implements QinVueSfcCompiler {
    private final QinVueCompilerSfcPackageLocator packageLocator = new QinVueCompilerSfcPackageLocator();
    private final QinJsPackageRunner packageRunner = new QinJsPackageRunner();

    QinOfficialVueSfcCompiler() {
    }

    @Override
    public QinVueSfcModuleResult transpileVueModule(
            Path moduleFile,
            String source,
            QinModuleSource sourceModule,
            QinVueSpecifierRewriter specifierRewriter) {
        QinVueCompilerSfcPackageLocator.QinVueCompilerSfcPackageLocation location =
                packageLocator.locate(moduleFile);
        if (!location.found()) {
            throw new IllegalStateException(
                    "Qin official Vue SFC compiler package not found for " + moduleFile.toAbsolutePath()
                            + ". Expected @vue/compiler-sfc to be available as a Qin-compilable npm dependency.");
        }

        try {
            Path projectRoot = findProjectRoot(moduleFile);
            Object parseResult = packageRunner.invokeNamedExport(
                    projectRoot,
                    "@vue/compiler-sfc",
                    "parse",
                    List.of(source, Map.of(
                            "filename", moduleFile.getFileName().toString(),
                            "sourceMap", false)));
            Map<String, Object> descriptor = extractDescriptor(parseResult);
            String descriptorJson = QinObjectJsonEncoder.toJson(descriptor);
            QinModuleSource effectiveModule = sourceModule != null
                    ? sourceModule
                    : new QinModuleSource(moduleFile.toAbsolutePath().normalize(), source, List.of());
            QinVueModuleImportRewriter importRewriter = specifier -> specifierRewriter.rewrite(specifier);
            QinVueSfcModuleAssembler.AssembledVueModule assembled = QinVueSfcModuleAssembler.assemble(
                    projectRoot,
                    moduleFile,
                    descriptor,
                    descriptorJson,
                    importRewriter);
            return new QinVueSfcModuleResult(
                    assembled.moduleCode(),
                    assembled.csstsCss(),
                    assembled.csstsAtomModule());
        } catch (IllegalStateException error) {
            throw error;
        } catch (Exception error) {
            throw new IllegalStateException(
                    "Qin official Vue SFC compiler failed for " + moduleFile.toAbsolutePath()
                            + ". Qin should compile @vue/compiler-sfc directly without legacy fallback.",
                    error);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractDescriptor(Object parseResult) {
        if (!(parseResult instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Vue parse() did not return an object: " + parseResult);
        }
        Object descriptor = map.get("descriptor");
        if (descriptor instanceof Map<?, ?> descriptorMap) {
            return (Map<String, Object>) descriptorMap;
        }
        if (map.containsKey("template")
                || map.containsKey("script")
                || map.containsKey("scriptSetup")
                || map.containsKey("styles")) {
            return (Map<String, Object>) map;
        }
        throw new IllegalStateException("Vue parse() result did not expose descriptor payload.");
    }

    private Path findProjectRoot(Path moduleFile) {
        Path current = moduleFile.toAbsolutePath().normalize().getParent();
        while (current != null) {
            if (Files.exists(current.resolve("qin.config.json"))
                    || Files.isDirectory(current.resolve("node_modules"))
                    || Files.isDirectory(current.resolve(".qin"))) {
                return current;
            }
            current = current.getParent();
        }
        return moduleFile.toAbsolutePath().normalize().getParent();
    }
}
