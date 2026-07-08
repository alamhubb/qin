package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinLinkedModuleSource;
import com.qin.lang.module.resolver.QinLinkedModuleSourceEmitter;
import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.module.resolver.QinModuleSource;
import com.qin.lang.sema.esm.QinEsmImportBinding;
import com.qin.lang.sema.esm.QinEsmModuleSemantic;
import com.qin.lang.sema.esm.QinEsmSemanticAnalyzer;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Dumps and times the CSSTS compiler ESM toolchain graph for cross-tool benchmarks.
 */
public final class QinCsstsToolchainEsmBenchmarkMain {
    private static final String INPUT_SOURCE = "const s = css { colorRed }\n";

    private QinCsstsToolchainEsmBenchmarkMain() {
    }

    public static void main(String[] args) throws Exception {
        Path workspaceRoot = args.length > 0 && !args[0].isBlank()
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : discoverWorkspaceRoot();
        Path projectRoot = args.length > 1 && !args[1].isBlank()
                ? Path.of(args[1]).toAbsolutePath().normalize()
                : Files.createTempDirectory("qin-cssts-toolchain-esm-benchmark-");
        Files.createDirectories(projectRoot);
        Files.writeString(
                projectRoot.resolve("qin.config.js"),
                "{ \"name\": \"qin-cssts-toolchain-esm-benchmark\" }\n",
                StandardCharsets.UTF_8);

        String wrapperSource = buildWrapperSource(INPUT_SOURCE);
        PreparedModuleHost host = new QinJsPackageRunner().prepareModuleSourceForDiagnostics(
                projectRoot,
                wrapperSource,
                "cssts_compiler_benchmark");
        System.out.println("benchmark=qin-cssts-toolchain stage=prepare"
                + " materializeMs=" + host.materializeMs()
                + " dependencyFingerprintMs=" + host.dependencyFingerprintMs()
                + " writeWrapperMs=" + host.writeWrapperMs()
                + " totalMs=" + host.totalPrepareMs()
                + " activePackages=" + host.activePackages().size()
                + " wrapperFile=" + host.wrapperFile());

        long graphStarted = System.nanoTime();
        QinModuleGraph graph = new QinModuleGraphBuilder().build(host.wrapperFile());
        long graphMs = elapsedMs(graphStarted);
        long chars = graph.modules().stream().mapToLong(module -> module.source().length()).sum();
        long resolvedImports = graph.modules().stream().mapToLong(module -> module.imports().size()).sum();
        System.out.println("benchmark=qin-cssts-toolchain stage=module-graph"
                + " modules=" + graph.modules().size()
                + " chars=" + chars
                + " resolvedImports=" + resolvedImports
                + " elapsedMs=" + graphMs);

        long linkStarted = System.nanoTime();
        QinLinkedModuleSource linked = new QinLinkedModuleSourceEmitter().emit(graph);
        System.out.println("benchmark=qin-cssts-toolchain stage=linked-source"
                + " chars=" + linked.source().length()
                + " imports=" + linked.imports().size()
                + " elapsedMs=" + elapsedMs(linkStarted));

        Path modulesFile = projectRoot.resolve("qin-cssts-toolchain-modules.txt");
        StringBuilder moduleList = new StringBuilder();
        for (QinModuleSource module : graph.modules()) {
            moduleList.append(module.file().toAbsolutePath().normalize()).append('\n');
        }
        Files.writeString(modulesFile, moduleList.toString(), StandardCharsets.UTF_8);
        System.out.println("benchmark=qin-cssts-toolchain stage=module-list"
                + " file=" + modulesFile);

        printPackageSummary(graph);

        long semanticStarted = System.nanoTime();
        QinEsmSemanticModel semanticModel = new QinEsmSemanticAnalyzer().analyze(graph);
        long semanticMs = elapsedMs(semanticStarted);
        long semanticImports = 0;
        long semanticExports = 0;
        for (QinEsmModuleSemantic semantic : semanticModel.modules().values()) {
            semanticImports += semantic.imports().size();
            semanticExports += semantic.exports().size();
        }
        System.out.println("benchmark=qin-cssts-toolchain stage=qin-semantic"
                + " modules=" + semanticModel.modules().size()
                + " imports=" + semanticImports
                + " exports=" + semanticExports
                + " elapsedMs=" + semanticMs);
        System.out.println("QinCsstsToolchainEsmBenchmarkMain OK"
                + " workspaceRoot=" + workspaceRoot
                + " projectRoot=" + projectRoot);
    }

    private static void printPackageSummary(QinModuleGraph graph) {
        Map<String, PackageSummary> summaries = new LinkedHashMap<>();
        for (QinModuleSource module : graph.modules()) {
            String key = packageKey(module.file());
            PackageSummary summary = summaries.computeIfAbsent(key, ignored -> new PackageSummary());
            summary.modules++;
            summary.chars += module.source().length();
            summary.imports += module.imports().size();
        }
        for (Map.Entry<String, PackageSummary> entry : summaries.entrySet()) {
            PackageSummary summary = entry.getValue();
            System.out.println("benchmark=qin-cssts-toolchain stage=package-summary"
                    + " package=" + quote(entry.getKey())
                    + " modules=" + summary.modules
                    + " chars=" + summary.chars
                    + " resolvedImports=" + summary.imports);
        }
    }

    private static String packageKey(Path file) {
        String normalized = file.toAbsolutePath().normalize().toString().replace('\\', '/');
        int nodeModules = normalized.lastIndexOf("/node_modules/");
        if (nodeModules < 0) {
            return "<wrapper>";
        }
        String rest = normalized.substring(nodeModules + "/node_modules/".length());
        String[] parts = rest.split("/");
        if (parts.length == 0) {
            return "<unknown>";
        }
        if (parts[0].startsWith("@") && parts.length > 1) {
            return parts[0] + "/" + parts[1];
        }
        return parts[0];
    }

    private static String buildWrapperSource(String source) {
        String sourceLiteral = QinJsPackageRunner.renderJsLiteral(source);
        return """
                import { CsstsInit, RuntimeStore, transformCssTs, generateStylesCss, generateCsstsAtomModule } from "cssts-compiler";
                CsstsInit.init({ dts: false });
                const __qin_result__ = transformCssTs(%s);
                const __qin_css__ = generateStylesCss.length > 0
                  ? generateStylesCss(RuntimeStore.getUsedStyles())
                  : generateStylesCss();
                const __qin_atom__ = generateCsstsAtomModule.length > 0
                  ? generateCsstsAtomModule(RuntimeStore.getUsedStyles())
                  : generateCsstsAtomModule();
                ({
                  code: __qin_result__.code,
                  hasStyles: __qin_result__.hasStyles,
                  css: __qin_css__,
                  atomModule: __qin_atom__
                });
                """.formatted(sourceLiteral);
    }

    private static Path discoverWorkspaceRoot() {
        Path cursor = Path.of("").toAbsolutePath().normalize();
        while (cursor != null) {
            if (Files.isDirectory(cursor.resolve("qin"))
                    && Files.isDirectory(cursor.resolve("cssts"))
                    && Files.isDirectory(cursor.resolve("ovsjs"))) {
                return cursor;
            }
            cursor = cursor.getParent();
        }
        return Path.of("D:/project/qkyproject/qinall").toAbsolutePath().normalize();
    }

    private static long elapsedMs(long started) {
        return (System.nanoTime() - started) / 1_000_000L;
    }

    private static String quote(String text) {
        return "\"" + text.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private static final class PackageSummary {
        private int modules;
        private long chars;
        private long imports;
    }
}
