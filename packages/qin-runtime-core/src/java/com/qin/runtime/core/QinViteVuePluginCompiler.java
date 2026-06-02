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
                %s

                %s
                const qinMainId = %s;
                const qinLoaded = qinApplyLoad(qinPlugins, qinPluginContext, qinMainId);
                const qinInitialCode = qinLoaded == null
                  ? %s
                  : (typeof qinLoaded === "string" ? qinLoaded : qinLoaded.code);
                const qinTransformResult = qinApplyTransforms(qinPlugins, qinPluginContext, qinInitialCode, qinMainId);
                ({
                  code: typeof qinTransformResult === "string" ? qinTransformResult : qinTransformResult.code,
                  map: typeof qinTransformResult === "string" ? null : qinTransformResult.map,
                  configMarker: qinResolvedConfig && qinResolvedConfig.__qinConfigMarker
                });
                """.formatted(
                viteConfigImportSource(projectRoot),
                pluginContainerSource(root),
                QinJsPackageRunner.renderJsLiteral(filename),
                QinJsPackageRunner.renderJsLiteral(source));
    }

    private String buildQueryWrapperSource(Path projectRoot, Path moduleFile, String query) {
        String root = projectRoot.toAbsolutePath().normalize().toString().replace('\\', '/');
        String filename = moduleFile.toAbsolutePath().normalize().toString().replace('\\', '/');
        String id = filename + "?" + query;
        return """
                import vuePlugin from "@vitejs/plugin-vue";
                %s

                %s
                const id = %s;
                const loaded = qinApplyLoad(qinPlugins, qinPluginContext, id);
                let code = typeof loaded === "string" ? loaded : loaded && loaded.code;
                const transformed = code == null ? null : qinApplyTransforms(qinPlugins, qinPluginContext, code, id);
                const finalResult = transformed || loaded;
                ({
                  code: typeof finalResult === "string" ? finalResult : finalResult && finalResult.code,
                  map: typeof finalResult === "string" ? null : finalResult && finalResult.map,
                  loadedType: typeof loaded,
                  transformedType: typeof transformed,
                  configMarker: qinResolvedConfig && qinResolvedConfig.__qinConfigMarker
                });
                """.formatted(
                viteConfigImportSource(projectRoot),
                pluginContainerSource(root),
                QinJsPackageRunner.renderJsLiteral(id));
    }

    private String pluginContainerSource(String root) {
        return """
                function qinCallHook(hook, thisArg, ...args) {
                  if (!hook) return null;
                  let result = null;
                  if (typeof hook === "function") result = hook.call(thisArg, ...args);
                  else if (hook.handler) result = hook.handler.call(thisArg, ...args);
                  if (result && result.then) {
                    result.then(value => { result = value; });
                  }
                  return result;
                }
                function qinIsPlainObject(value) {
                  return value != null && typeof value === "object" && !Array.isArray(value);
                }
                function qinMergeObject(target, source) {
                  const out = target || {};
                  if (!qinIsPlainObject(source)) return out;
                  for (const key of Object.keys(source)) {
                    const value = source[key];
                    if (qinIsPlainObject(value) && qinIsPlainObject(out[key])) {
                      out[key] = qinMergeObject({ ...out[key] }, value);
                    } else {
                      out[key] = value;
                    }
                  }
                  return out;
                }
                function qinFlattenPlugins(value, out = []) {
                  if (value == null || value === false) return out;
                  if (Array.isArray(value)) {
                    for (const item of value) qinFlattenPlugins(item, out);
                    return out;
                  }
                  out.push(value);
                  return out;
                }
                function qinPluginEnforceRank(plugin) {
                  if (plugin && plugin.enforce === "pre") return 0;
                  if (plugin && plugin.enforce === "post") return 2;
                  return 1;
                }
                function qinSortPlugins(plugins) {
                  return plugins
                    .map((plugin, index) => ({ plugin, index }))
                    .sort((left, right) => {
                      const rank = qinPluginEnforceRank(left.plugin) - qinPluginEnforceRank(right.plugin);
                      return rank !== 0 ? rank : left.index - right.index;
                    })
                    .map(item => item.plugin);
                }
                function qinResolveUserConfig(raw) {
                  let value = typeof raw === "function"
                    ? raw({ command: "serve", mode: "development", ssrBuild: false })
                    : raw;
                  if (value && value.then) {
                    value.then(result => { value = result; });
                  }
                  return value || {};
                }
                function qinHasVuePlugin(plugins) {
                  for (const plugin of plugins) {
                    if (plugin && plugin.name === "vite:vue") return true;
                  }
                  return false;
                }
                function qinCreateConfig(root, userConfig) {
                  const config = qinMergeObject({}, userConfig || {});
                  config.root = root;
                  config.base = config.base || "/";
                  config.command = "serve";
                  config.isProduction = false;
                  config.build = qinMergeObject({ sourcemap: false }, config.build || {});
                  config.css = qinMergeObject({ devSourcemap: false }, config.css || {});
                  config.define = config.define || {};
                  config.logger = config.logger || { warn(message) {} };
                  config.server = qinMergeObject({ hmr: true, origin: "" }, config.server || {});
                  return config;
                }
                function qinNormalizePath(path) {
                  return String(path || "").replace(/\\\\/g, "/");
                }
                function qinStripQuery(id) {
                  const text = qinNormalizePath(id);
                  const question = text.indexOf("?");
                  return question < 0 ? text : text.slice(0, question);
                }
                function qinDirname(id) {
                  const clean = qinStripQuery(id);
                  const slash = clean.lastIndexOf("/");
                  return slash < 0 ? "" : clean.slice(0, slash);
                }
                function qinJoinPath(base, specifier) {
                  const prefix = /^[A-Za-z]:\\//.test(base) ? base.slice(0, 3) : (base.startsWith("/") ? "/" : "");
                  const baseBody = prefix ? base.slice(prefix.length) : base;
                  const parts = (baseBody + "/" + specifier).split("/");
                  const out = [];
                  for (const part of parts) {
                    if (!part || part === ".") continue;
                    if (part === "..") out.pop();
                    else out.push(part);
                  }
                  return prefix + out.join("/");
                }
                function qinResolveId(id, importer, config) {
                  const specifier = qinNormalizePath(id);
                  if (!specifier) return null;
                  if (/^[A-Za-z]:\\//.test(specifier)) return { id: specifier };
                  if (specifier.startsWith("/")) return { id: qinJoinPath(config.root, "." + specifier) };
                  if (specifier.startsWith("./") || specifier.startsWith("../")) {
                    const base = importer ? qinDirname(importer) : config.root;
                    return { id: qinJoinPath(base || config.root, specifier) };
                  }
                  return { id: specifier };
                }
                function qinNormalizeResolvedId(result) {
                  if (typeof result === "string") return { id: result };
                  if (result && typeof result === "object" && result.id) return result;
                  return null;
                }
                function qinApplyResolveId(plugins, context, id, importer, skipPlugin) {
                  for (const plugin of plugins || []) {
                    if (!plugin || !plugin.resolveId || plugin === skipPlugin) continue;
                    const previous = context.__qinCurrentResolvePlugin;
                    context.__qinCurrentResolvePlugin = plugin;
                    const resolved = qinNormalizeResolvedId(qinCallHook(
                      plugin.resolveId,
                      context,
                      id,
                      importer,
                      { ssr: false, scan: false, isEntry: false }
                    ));
                    context.__qinCurrentResolvePlugin = previous;
                    if (resolved) return resolved;
                  }
                  return null;
                }
                function qinResolveWithPlugins(plugins, context, id, importer, config, skipPlugin) {
                  const resolved = qinApplyResolveId(plugins, context, id, importer, skipPlugin);
                  return resolved || qinResolveId(id, importer, config);
                }
                function qinCreatePluginContext(config, plugins) {
                  const watchFiles = [];
                  const emittedFiles = [];
                  const warnings = [];
                  return {
                    parse(code) { return {}; },
                    addWatchFile(file) {
                      watchFiles.push(qinNormalizePath(file));
                    },
                    emitFile(file) {
                      const refId = "qin-file-" + emittedFiles.length;
                      emittedFiles.push({ refId, file });
                      return refId;
                    },
                    getWatchFiles() {
                      return watchFiles.slice();
                    },
                    warn(message) {
                      warnings.push(message);
                      if (config.logger && config.logger.warn) config.logger.warn(message);
                    },
                    error(message) {
                      throw message instanceof Error ? message : new Error(String(message));
                    },
                    resolve(id, importer) {
                      return qinResolveWithPlugins(
                        plugins,
                        this,
                        id,
                        importer,
                        config,
                        this.__qinCurrentResolvePlugin
                      );
                    },
                    __qinWatchFiles: watchFiles,
                    __qinEmittedFiles: emittedFiles,
                    __qinWarnings: warnings,
                    __qinCurrentResolvePlugin: null
                  };
                }
                function qinCreateServer(config) {
                  return {
                    config,
                    watcher: { on(event, handler) {} },
                    moduleGraph: {
                      getModuleById(id) { return null; },
                      invalidateModule(module) {}
                    }
                  };
                }
                function qinRunPluginLifecycle(plugins, config, context, server) {
                  const env = { command: "serve", mode: "development", ssrBuild: false };
                  for (const plugin of plugins) {
                    const nextConfig = qinCallHook(plugin && plugin.config, plugin, config, env);
                    if (nextConfig) qinMergeObject(config, nextConfig);
                  }
                  for (const plugin of plugins) qinCallHook(plugin && plugin.configResolved, plugin, config);
                  for (const plugin of plugins) qinCallHook(plugin && plugin.configureServer, plugin, server);
                  for (const plugin of plugins) qinCallHook(plugin && plugin.options, context);
                  for (const plugin of plugins) qinCallHook(plugin && plugin.buildStart, context);
                }
                function qinApplyLoad(plugins, context, id) {
                  for (const plugin of plugins) {
                    const loaded = qinCallHook(plugin && plugin.load, context, id);
                    if (loaded != null) return loaded;
                  }
                  return null;
                }
                function qinApplyTransforms(plugins, context, initialCode, id) {
                  let code = initialCode;
                  let lastResult = null;
                  for (const plugin of plugins) {
                    const result = qinCallHook(plugin && plugin.transform, context, code, id);
                    if (result == null) continue;
                    lastResult = result;
                    code = typeof result === "string" ? result : result.code;
                  }
                  return lastResult || { code, map: null };
                }
                const qinUserConfig = qinResolveUserConfig(qinUserViteConfig);
                let qinPlugins = qinSortPlugins(qinFlattenPlugins(qinUserConfig.plugins));
                if (!qinHasVuePlugin(qinPlugins)) {
                  qinPlugins.push(vuePlugin({ sourceMap: false }));
                  qinPlugins = qinSortPlugins(qinPlugins);
                }
                const qinResolvedConfig = qinCreateConfig(%s, qinUserConfig);
                const qinPluginContext = qinCreatePluginContext(qinResolvedConfig, qinPlugins);
                const qinDevServer = qinCreateServer(qinResolvedConfig);
                qinRunPluginLifecycle(qinPlugins, qinResolvedConfig, qinPluginContext, qinDevServer);
                """.formatted(QinJsPackageRunner.renderJsLiteral(root));
    }

    private String viteConfigImportSource(Path projectRoot) {
        Path config = findViteConfig(projectRoot);
        if (config == null) {
            return "const qinUserViteConfig = null;";
        }
        Path wrapperDir = projectRoot.toAbsolutePath().normalize()
                .resolve(".qin")
                .resolve("runtime")
                .resolve("npm-host")
                .normalize();
        String relative = wrapperDir.relativize(config.toAbsolutePath().normalize()).toString().replace('\\', '/');
        if (!relative.startsWith(".")) {
            relative = "./" + relative;
        }
        return "import qinUserViteConfig from "
                + QinJsPackageRunner.renderJsLiteral(relative)
                + ";";
    }

    private Path findViteConfig(Path projectRoot) {
        Path root = projectRoot.toAbsolutePath().normalize();
        for (String name : new String[] {"vite.config.js", "vite.config.mjs", "vite.config.ts"}) {
            Path candidate = root.resolve(name);
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        return null;
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
