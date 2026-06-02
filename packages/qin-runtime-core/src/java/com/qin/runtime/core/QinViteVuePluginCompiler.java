package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinModuleSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Vue SFC compiler path that executes the real @vitejs/plugin-vue transform hook
 * through Qin's JS package runner.
 */
final class QinViteVuePluginCompiler implements QinVueSfcCompiler {
    private static final Pattern IMPORT_FROM_PATTERN = Pattern.compile(
            "(?m)(import\\s+[^;\\n]*?\\s+from\\s*[\"'])([^\"']+)([\"'])");
    private static final Pattern IMPORT_SIDE_EFFECT_PATTERN = Pattern.compile(
            "(?m)(import\\s*[\"'])([^\"']+)([\"'])");
    private static final Pattern EXPORT_FROM_PATTERN = Pattern.compile(
            "(?m)(export\\s+(?:\\*\\s*(?:as\\s+[A-Za-z_$][\\w$]*\\s*)?|\\{[^}\\n]*})\\s*from\\s*[\"'])([^\"']+)([\"'])");

    private final QinJsPackageRunner packageRunner = new QinJsPackageRunner();

    static boolean isEnabled(Path projectRoot) {
        Path root = projectRoot.toAbsolutePath().normalize();
        if (Files.isRegularFile(root.resolve("vite.config.js"))
                || Files.isRegularFile(root.resolve("vite.config.mjs"))
                || Files.isRegularFile(root.resolve("vite.config.ts"))) {
            return true;
        }
        Path qinConfig = root.resolve("qin.config.json");
        if (!Files.isRegularFile(qinConfig)) {
            return false;
        }
        try {
            return Files.readString(qinConfig).contains("\"@vitejs/plugin-vue\"");
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public QinVueSfcModuleResult transpileVueModule(
            Path moduleFile,
            String source,
            QinModuleSource sourceModule,
            QinVueSpecifierRewriter specifierRewriter) {
        try {
            Path projectRoot = findProjectRoot(moduleFile);
            Object result = packageRunner.runModuleSource(
                    projectRoot,
                    buildWrapperSource(projectRoot, moduleFile, source),
                    "vite_plugin_vue_sfc_transform");
            if (!(result instanceof Map<?, ?> map)) {
                throw new IllegalStateException("Expected plugin-vue transform result object, got: " + result);
            }
            Object code = map.get("code");
            if (!(code instanceof String text) || text.isBlank()) {
                throw new IllegalStateException("plugin-vue transform returned empty code: " + result);
            }
            String rewritten = rewriteSpecifiers(text, specifierRewriter, IMPORT_FROM_PATTERN);
            rewritten = rewriteSpecifiers(rewritten, specifierRewriter, EXPORT_FROM_PATTERN);
            rewritten = rewriteSpecifiers(rewritten, specifierRewriter, IMPORT_SIDE_EFFECT_PATTERN);
            return new QinVueSfcModuleResult(rewritten, "", "");
        } catch (IllegalStateException error) {
            throw error;
        } catch (Exception error) {
            throw new IllegalStateException(
                    "Qin @vitejs/plugin-vue transform failed for " + moduleFile.toAbsolutePath(),
                    error);
        }
    }

    private String rewriteSpecifiers(String source, QinVueSpecifierRewriter specifierRewriter, Pattern pattern) {
        if (source == null || source.isBlank() || specifierRewriter == null) {
            return source;
        }
        Matcher matcher = pattern.matcher(source);
        StringBuffer out = new StringBuffer();
        while (matcher.find()) {
            String prefix = matcher.group(1);
            String specifier = matcher.group(2);
            String suffix = matcher.group(3);
            matcher.appendReplacement(
                    out,
                    Matcher.quoteReplacement(prefix + specifierRewriter.rewrite(specifier) + suffix));
        }
        matcher.appendTail(out);
        return out.toString();
    }

    private String buildWrapperSource(Path projectRoot, Path moduleFile, String source) {
        String root = projectRoot.toAbsolutePath().normalize().toString().replace('\\', '/');
        String filename = moduleFile.toAbsolutePath().normalize().toString().replace('\\', '/');
        return """
                import vuePlugin from "@vitejs/plugin-vue";

                const plugin = vuePlugin({ sourceMap: false });
                const config = {
                  root: %s,
                  command: "serve",
                  isProduction: false,
                  build: { sourcemap: false },
                  css: { devSourcemap: false },
                  define: {},
                  logger: { warn(message) {} },
                  server: { hmr: true }
                };
                const ctx = {
                  parse(code) { return {}; },
                  addWatchFile(file) {},
                  emitFile(file) {},
                  warn(message) {},
                  error(message) { throw message; },
                  async resolve(id) { return { id }; }
                };
                const server = {
                  config,
                  watcher: { on(event, handler) {} },
                  moduleGraph: {
                    getModuleById(id) { return null; },
                    invalidateModule(module) {}
                  }
                };
                function callHook(hook, thisArg, ...args) {
                  if (!hook) return null;
                  if (typeof hook === "function") return hook.call(thisArg, ...args);
                  if (hook.handler) return hook.handler.call(thisArg, ...args);
                  return null;
                }
                callHook(plugin.config, plugin, config);
                callHook(plugin.configResolved, plugin, config);
                callHook(plugin.configureServer, plugin, server);
                callHook(plugin.options, ctx);
                callHook(plugin.buildStart, ctx);
                let transformed = callHook(plugin.transform, ctx, %s, %s);
                if (transformed && transformed.then) {
                  transformed.then(result => { transformed = result; });
                }
                ({
                  code: typeof transformed === "string" ? transformed : transformed.code,
                  map: typeof transformed === "string" ? null : transformed.map
                });
                """.formatted(
                QinJsPackageRunner.renderJsLiteral(root),
                QinJsPackageRunner.renderJsLiteral(source),
                QinJsPackageRunner.renderJsLiteral(filename));
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
