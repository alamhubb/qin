package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinModuleSource;
import com.qin.runtime.core.vue.QinVueModuleImportRewriter;
import com.qin.runtime.core.vue.QinVueSfcModuleAssembler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
            Object parseResult = invokeOfficialParseSlice(
                    projectRoot,
                    location.entryFile(),
                    moduleFile,
                    source);
            Map<String, Object> descriptor = sanitizeDescriptor(extractDescriptor(parseResult));
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

    private Object invokeOfficialParseSlice(
            Path projectRoot,
            Path compilerEntry,
            Path moduleFile,
            String source) throws Exception {
        String officialBundle = Files.readString(compilerEntry, StandardCharsets.UTF_8);
        String parseSlice = extractOfficialParseSlice(officialBundle, compilerEntry);
        String wrapperSource = buildOfficialParseSliceWrapper(
                parseSlice,
                source,
                moduleFile.getFileName().toString());
        return packageRunner.runModuleSource(
                projectRoot,
                wrapperSource,
                "vue_compiler_sfc_parse_slice");
    }

    private String extractOfficialParseSlice(String officialBundle, Path compilerEntry) {
        int parseStart = officialBundle.indexOf("function parse$2");
        int helperEnd = officialBundle.indexOf("// Copyright Joyent", parseStart);
        if (parseStart < 0 || helperEnd < 0 || helperEnd <= parseStart) {
            throw new IllegalStateException(
                    "Cannot extract official @vue/compiler-sfc parse slice from "
                            + compilerEntry.toAbsolutePath());
        }
        return officialBundle.substring(parseStart, helperEnd);
    }

    private String buildOfficialParseSliceWrapper(String parseSlice, String source, String filename) {
        return """
                const __spreadValues$a = (a, b) => {
                  for (const prop in b || (b = {})) {
                    if (Object.prototype.hasOwnProperty.call(b, prop)) {
                      a[prop] = b[prop];
                    }
                  }
                  return a;
                };
                const __spreadProps$9 = (a, b) => Object.assign(a, b);
                const DEFAULT_FILENAME = "anonymous.vue";
                const parseCache$1 = new Map();
                function createCache() {
                  return new Map();
                }
                function genCacheKey(source, options) {
                  return source + JSON.stringify(options, (_, value) =>
                    typeof value === "function" ? "[Function]" : value
                  );
                }
                function parseCssVars() {
                  return [];
                }
                function createRoot(children, source) {
                  return {
                    type: 0,
                    source,
                    children,
                    helpers: new Set(),
                    components: [],
                    directives: [],
                    hoists: [],
                    imports: [],
                    cached: [],
                    temps: 0,
                    codegenNode: null,
                    loc: __qin_loc(source, 0, source.length)
                  };
                }
                function __qin_position(source, offset) {
                  let line = 1;
                  let column = 1;
                  for (let i = 0; i < offset; i++) {
                    if (source.charCodeAt(i) === 10) {
                      line++;
                      column = 1;
                    } else {
                      column++;
                    }
                  }
                  return { offset, line, column };
                }
                function __qin_loc(source, start, end) {
                  return {
                    start: __qin_position(source, start),
                    end: __qin_position(source, end),
                    source: source.slice(start, end)
                  };
                }
                function __qin_parse_attrs(source, rawAttrs, baseOffset) {
                  const props = [];
                  let index = 0;
                  const text = rawAttrs || "";
                  while (index < text.length) {
                    while (index < text.length && text.charCodeAt(index) <= 32) {
                      index++;
                    }
                    if (index >= text.length) {
                      break;
                    }
                    const nameStart = index;
                    while (index < text.length && text.charCodeAt(index) > 32 && text[index] !== "=") {
                      index++;
                    }
                    const rawName = text.slice(nameStart, index);
                    while (index < text.length && text.charCodeAt(index) <= 32) {
                      index++;
                    }
                    let valueText = null;
                    if (text[index] === "=") {
                      index++;
                      while (index < text.length && text.charCodeAt(index) <= 32) {
                        index++;
                      }
                      const quote = text[index];
                      if (quote === '"' || quote === "'") {
                        index++;
                        const valueStart = index;
                        const valueEnd = text.indexOf(quote, valueStart);
                        if (valueEnd < 0) {
                          valueText = text.slice(valueStart);
                          index = text.length;
                        } else {
                          valueText = text.slice(valueStart, valueEnd);
                          index = valueEnd + 1;
                        }
                      } else {
                        const valueStart = index;
                        while (index < text.length && text.charCodeAt(index) > 32) {
                          index++;
                        }
                        valueText = text.slice(valueStart, index);
                      }
                    }
                    const name = rawName.replace(/^:/, "bind:");
                    const attrStart = baseOffset + nameStart;
                    const attrEnd = baseOffset + index;
                    props.push({
                      type: 6,
                      name,
                      value: valueText == null ? void 0 : {
                        content: valueText,
                        loc: __qin_loc(source, attrStart, attrEnd)
                      },
                      loc: __qin_loc(source, attrStart, attrEnd)
                    });
                  }
                  return props;
                }
                function __qin_minimal_sfc_parse(source) {
                  const children = [];
                  let search = 0;
                  while (search < source.length) {
                    const next = __qin_next_block(source, search);
                    if (!next) {
                      break;
                    }
                    const tag = next.tag;
                    const openStart = next.openStart;
                    const openEnd = next.openEnd;
                    const closeStart = source.indexOf("</" + tag + ">", openEnd);
                    if (closeStart < 0) {
                      break;
                    }
                    const closeEnd = closeStart + tag.length + 3;
                    const rawAttrs = source.slice(openStart + tag.length + 1, openEnd - 1);
                    const innerLoc = __qin_loc(source, openEnd, closeStart);
                    const textNode = {
                      type: 2,
                      content: source.slice(openEnd, closeStart),
                      loc: innerLoc
                    };
                    children.push({
                      type: 1,
                      tag,
                      props: __qin_parse_attrs(source, rawAttrs, openStart + tag.length + 1),
                      children: [textNode],
                      loc: __qin_loc(source, openStart, closeEnd),
                      innerLoc
                    });
                    search = closeEnd;
                  }
                  return {
                    type: 0,
                    source,
                    children,
                    loc: __qin_loc(source, 0, source.length)
                  };
                }
                function __qin_next_block(source, search) {
                  const tags = ["template", "script", "style"];
                  let best = null;
                  for (const tag of tags) {
                    const marker = "<" + tag;
                    const index = source.indexOf(marker, search);
                    if (index < 0) {
                      continue;
                    }
                    const nextChar = source[index + marker.length];
                    if (nextChar !== ">" && nextChar !== " " && nextChar !== "\\n" && nextChar !== "\\r" && nextChar !== "\\t") {
                      continue;
                    }
                    const openEnd = source.indexOf(">", index);
                    if (openEnd < 0) {
                      continue;
                    }
                    if (!best || index < best.openStart) {
                      best = { tag, openStart: index, openEnd: openEnd + 1 };
                    }
                  }
                  return best;
                }
                const __qin_minimal_compiler = {
                  parse(source, options = {}) {
                    return __qin_minimal_sfc_parse(source);
                  }
                };
                """.stripIndent()
                + System.lineSeparator()
                + parseSlice
                + System.lineSeparator()
                + "const __qin_result__ = parse$2("
                + QinJsPackageRunner.renderJsLiteral(source)
                + ", { filename: "
                + QinJsPackageRunner.renderJsLiteral(filename)
                + ", sourceMap: false, compiler: __qin_minimal_compiler });"
                + System.lineSeparator()
                + "(__qin_result__);"
                + System.lineSeparator();
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

    private Map<String, Object> sanitizeDescriptor(Map<String, Object> descriptor) {
        LinkedHashMap<String, Object> sanitized = new LinkedHashMap<>();
        putIfPresent(sanitized, "filename", descriptor.get("filename"));
        putIfPresent(sanitized, "source", descriptor.get("source"));
        sanitized.put("template", sanitizeBlock(descriptor.get("template")));
        sanitized.put("script", sanitizeBlock(descriptor.get("script")));
        sanitized.put("scriptSetup", sanitizeBlock(descriptor.get("scriptSetup")));
        sanitized.put("styles", sanitizeBlockList(descriptor.get("styles")));
        sanitized.put("customBlocks", sanitizeBlockList(descriptor.get("customBlocks")));
        Object errors = descriptor.get("errors");
        if (errors instanceof List<?> errorList) {
            sanitized.put("errors", sanitizeErrors(errorList));
        } else {
            sanitized.put("errors", List.of());
        }
        return sanitized;
    }

    private Object sanitizeBlock(Object block) {
        if (!(block instanceof Map<?, ?> map)) {
            return null;
        }
        LinkedHashMap<String, Object> sanitized = new LinkedHashMap<>();
        putIfPresent(sanitized, "type", map.get("type"));
        putIfPresent(sanitized, "tag", map.get("tag"));
        putIfPresent(sanitized, "content", map.get("content"));
        putIfPresent(sanitized, "attrs", sanitizeStringObjectMap(map.get("attrs")));
        putIfPresent(sanitized, "rawAttrs", sanitizeRawAttrs(map.get("rawAttrs")));
        putIfPresent(sanitized, "lang", map.get("lang"));
        putIfPresent(sanitized, "setup", map.get("setup"));
        putIfPresent(sanitized, "scoped", map.get("scoped"));
        putIfPresent(sanitized, "module", map.get("module"));
        putIfPresent(sanitized, "src", map.get("src"));
        putIfPresent(sanitized, "loc", sanitizeLoc(map.get("loc")));
        return sanitized;
    }

    private List<Object> sanitizeBlockList(Object value) {
        if (!(value instanceof List<?> list)) {
            return List.of();
        }
        ArrayList<Object> sanitized = new ArrayList<>();
        for (Object item : list) {
            Object block = sanitizeBlock(item);
            if (block != null) {
                sanitized.add(block);
            }
        }
        return sanitized;
    }

    private Object sanitizeStringObjectMap(Object value) {
        if (!(value instanceof Map<?, ?> map)) {
            return null;
        }
        LinkedHashMap<String, Object> sanitized = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = String.valueOf(entry.getKey());
            Object raw = entry.getValue();
            if (raw == null || raw instanceof String || raw instanceof Number || raw instanceof Boolean) {
                sanitized.put(key, raw);
            } else {
                sanitized.put(key, String.valueOf(raw));
            }
        }
        return sanitized;
    }

    private Object sanitizeRawAttrs(Object value) {
        if (!(value instanceof List<?> list)) {
            return null;
        }
        ArrayList<Object> sanitized = new ArrayList<>();
        for (Object item : list) {
            if (!(item instanceof Map<?, ?> map)) {
                continue;
            }
            LinkedHashMap<String, Object> attr = new LinkedHashMap<>();
            putIfPresent(attr, "name", map.get("name"));
            Object valueNode = map.get("value");
            if (valueNode instanceof Map<?, ?> valueMap) {
                putIfPresent(attr, "value", valueMap.get("content"));
            } else {
                putIfPresent(attr, "value", valueNode);
            }
            putIfPresent(attr, "loc", sanitizeLoc(map.get("loc")));
            sanitized.add(attr);
        }
        return sanitized;
    }

    private Object sanitizeLoc(Object value) {
        if (!(value instanceof Map<?, ?> map)) {
            return null;
        }
        LinkedHashMap<String, Object> sanitized = new LinkedHashMap<>();
        putIfPresent(sanitized, "start", sanitizePosition(map.get("start")));
        putIfPresent(sanitized, "end", sanitizePosition(map.get("end")));
        Object source = map.get("source");
        if (source instanceof String text && text.length() <= 4096) {
            sanitized.put("source", text);
        }
        return sanitized;
    }

    private Object sanitizePosition(Object value) {
        if (!(value instanceof Map<?, ?> map)) {
            return null;
        }
        LinkedHashMap<String, Object> sanitized = new LinkedHashMap<>();
        putIfPresent(sanitized, "offset", map.get("offset"));
        putIfPresent(sanitized, "line", map.get("line"));
        putIfPresent(sanitized, "column", map.get("column"));
        return sanitized;
    }

    private List<Object> sanitizeErrors(List<?> errors) {
        ArrayList<Object> sanitized = new ArrayList<>();
        for (Object error : errors) {
            sanitized.add(String.valueOf(error));
        }
        return sanitized;
    }

    private void putIfPresent(Map<String, Object> target, String key, Object value) {
        if (value != null) {
            target.put(key, value);
        }
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
