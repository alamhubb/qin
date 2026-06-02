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

    @Override
    public String transpileVueQueryModule(
            Path moduleFile,
            String source,
            String query,
            QinModuleSource sourceModule,
            QinVueSpecifierRewriter specifierRewriter) {
        try {
            Path projectRoot = findProjectRoot(moduleFile);
            Object result = packageRunner.runModuleSource(
                    projectRoot,
                    buildQueryWrapperSource(projectRoot, moduleFile, query),
                    "vite_plugin_vue_query_transform");
            if (!(result instanceof Map<?, ?> map)) {
                throw new IllegalStateException("Expected plugin-vue query result object, got: " + result);
            }
            Object code = map.get("code");
            if (!(code instanceof String text) || text.isBlank()) {
                return null;
            }
            if (query.contains("type=style")) {
                return renderCssInjectionModule(text, moduleFile, query);
            }
            String rewritten = rewriteSpecifiers(text, specifierRewriter, IMPORT_FROM_PATTERN);
            rewritten = rewriteSpecifiers(rewritten, specifierRewriter, EXPORT_FROM_PATTERN);
            return rewriteSpecifiers(rewritten, specifierRewriter, IMPORT_SIDE_EFFECT_PATTERN);
        } catch (IllegalStateException error) {
            throw error;
        } catch (Exception error) {
            throw new IllegalStateException(
                    "Qin @vitejs/plugin-vue query transform failed for " + moduleFile.toAbsolutePath()
                            + "?" + query,
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
                  base: "/",
                  command: "serve",
                  isProduction: false,
                  build: { sourcemap: false },
                  css: { devSourcemap: false },
                  define: {},
                  logger: { warn(message) {} },
                  server: { hmr: true, origin: "" }
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

    private String buildQueryWrapperSource(Path projectRoot, Path moduleFile, String query) {
        String root = projectRoot.toAbsolutePath().normalize().toString().replace('\\', '/');
        String filename = moduleFile.toAbsolutePath().normalize().toString().replace('\\', '/');
        String id = filename + "?" + query;
        return """
                import vuePlugin from "@vitejs/plugin-vue";

                const plugin = vuePlugin({ sourceMap: false });
                const config = {
                  root: %s,
                  base: "/",
                  command: "serve",
                  isProduction: false,
                  build: { sourcemap: false },
                  css: { devSourcemap: false },
                  define: {},
                  logger: { warn(message) {} },
                  server: { hmr: true, origin: "" }
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
                const id = %s;
                let loaded = callHook(plugin.load, ctx, id);
                if (loaded && loaded.then) {
                  loaded.then(result => { loaded = result; });
                }
                let code = typeof loaded === "string" ? loaded : loaded && loaded.code;
                let transformed = code == null ? null : callHook(plugin.transform, ctx, code, id);
                if (transformed && transformed.then) {
                  transformed.then(result => { transformed = result; });
                }
                const finalResult = transformed || loaded;
                ({
                  code: typeof finalResult === "string" ? finalResult : finalResult && finalResult.code,
                  map: typeof finalResult === "string" ? null : finalResult && finalResult.map,
                  loadedType: typeof loaded,
                  transformedType: typeof transformed
                });
                """.formatted(
                QinJsPackageRunner.renderJsLiteral(root),
                QinJsPackageRunner.renderJsLiteral(id));
    }

    private String renderCssInjectionModule(String css, Path moduleFile, String query) {
        String styleId = "qin-vue-plugin-style-"
                + Integer.toHexString((moduleFile.toAbsolutePath().normalize() + "?" + query).hashCode());
        String escaped = css == null ? "" : css
                .replace("\\", "\\\\")
                .replace("`", "\\`")
                .replace("${", "\\${");
        return """
                const css = `%s`;
                const styleId = "%s";
                if (typeof document !== 'undefined') {
                  let style = document.querySelector('style[data-qin-style-id="' + styleId + '"]');
                  if (!style) {
                    style = document.createElement('style');
                    style.setAttribute('data-qin-style-id', styleId);
                    document.head.appendChild(style);
                  }
                  style.setAttribute('data-qin-vue-plugin', 'true');
                  style.textContent = css;
                }
                export default css;
                """.formatted(escaped, styleId);
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
