package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.SequencedMap;

public final class QinGeneratedSlimeParserValidator {
    private final QinJsPackageRunner packageRunner = new QinJsPackageRunner();
    private final Path bundle;

    public QinGeneratedSlimeParserValidator(Path bundle) {
        this.bundle = bundle.toAbsolutePath().normalize();
    }

    public static QinGeneratedSlimeParserValidator usingDefaultBundle(Path projectRoot) throws Exception {
        Path bundle = locateDefaultBundle(projectRoot);
        if (!Files.isRegularFile(bundle)) {
            generateDefaultBundle(bundle);
        }
        return new QinGeneratedSlimeParserValidator(bundle);
    }

    public ParseResult parse(Path projectRoot, String label, String source) throws Exception {
        if (!Files.isRegularFile(bundle)) {
            throw new IllegalStateException("Generated SlimeParser bundle does not exist: " + bundle);
        }
        String generated = Files.readString(bundle, StandardCharsets.UTF_8);
        Object result = packageRunner.runModuleSource(
                projectRoot,
                generated + "\n" + buildParseWrapperSource(label, source),
                "generated_slime_parser_validate");
        if (!(result instanceof Map<?, ?> raw)) {
            throw new IllegalStateException("Generated SlimeParser validation returned non-object result: " + result);
        }
        return ParseResult.from(raw);
    }

    public void assertParses(Path projectRoot, String label, String source) throws Exception {
        ParseResult result = parse(projectRoot, label, source);
        if (!result.ok()) {
            throw new IllegalStateException("Generated SlimeParser JS failed on " + label + ":\n" + result);
        }
    }

    public void assertAllParse(Path projectRoot, SequencedMap<String, String> sources) throws Exception {
        for (ParseResult result : parseAll(projectRoot, sources)) {
            if (!result.ok()) {
                throw new IllegalStateException("Generated SlimeParser JS failed on " + result.label() + ":\n" + result);
            }
        }
    }

    public List<ParseResult> parseAll(Path projectRoot, SequencedMap<String, String> sources) throws Exception {
        if (sources == null || sources.isEmpty()) {
            return List.of();
        }
        if (!Files.isRegularFile(bundle)) {
            throw new IllegalStateException("Generated SlimeParser bundle does not exist: " + bundle);
        }
        String generated = Files.readString(bundle, StandardCharsets.UTF_8);
        Object result = packageRunner.runModuleSource(
                projectRoot,
                generated + "\n" + buildParseAllWrapperSource(sources),
                "generated_slime_parser_validate_all");
        if (!(result instanceof List<?> rawList)) {
            throw new IllegalStateException("Generated SlimeParser batch validation returned non-array result: " + result);
        }
        return rawList.stream()
                .map(item -> {
                    if (!(item instanceof Map<?, ?> raw)) {
                        throw new IllegalStateException(
                                "Generated SlimeParser batch validation item returned non-object result: " + item);
                    }
                    return ParseResult.from(raw);
                })
                .toList();
    }

    private static Path locateDefaultBundle(Path projectRoot) {
        Path root = projectRoot.toAbsolutePath().normalize();
        Path current = root;
        while (current != null) {
            Path direct = current.resolve(".qin")
                    .resolve("generated")
                    .resolve("slime-parser")
                    .resolve("slime-parser.bundle.js");
            if (Files.isRegularFile(direct)) {
                return direct;
            }
            Path siblingQin = current.resolve("qin")
                    .resolve(".qin")
                    .resolve("generated")
                    .resolve("slime-parser")
                    .resolve("slime-parser.bundle.js");
            if (Files.isRegularFile(siblingQin)) {
                return siblingQin;
            }
            current = current.getParent();
        }
        return root.resolve(".qin")
                .resolve("generated")
                .resolve("slime-parser")
                .resolve("slime-parser.bundle.js");
    }

    private static void generateDefaultBundle(Path outputFile) throws Exception {
        Path qinRoot = locateQinRoot(outputFile);
        Path workspaceRoot = qinRoot.getParent();
        List<Path> sourceRoots = List.of(
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-parser")
                        .resolve("src").resolve("main").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-token")
                        .resolve("src").resolve("main").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("subhuti-java")
                        .resolve("src").resolve("main").resolve("java"));
        new QinJavaProjectJsCompiler()
                .compileSuperclassClosure(sourceRoots, "com.slime.parser.SlimeParser", outputFile);
    }

    private static Path locateQinRoot(Path start) {
        Path current = start.toAbsolutePath().normalize();
        while (current != null) {
            if (Files.isDirectory(current.resolve("packages").resolve("qin-runtime-core"))) {
                return current;
            }
            Path sibling = current.resolve("qin");
            if (Files.isDirectory(sibling.resolve("packages").resolve("qin-runtime-core"))) {
                return sibling;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Cannot locate qin repository from " + start.toAbsolutePath());
    }

    private static String buildParseWrapperSource(String label, String source) {
        return """
                const __qin_validate_label__ = %s;
                const __qin_validate_source__ = String(%s)
                  .split(/\\r?\\n/)
                  .map(line => /^\\s*import\\b/.test(line) && !/[;]\\s*$/.test(line) ? line + ";" : line)
                  .join("\\n");
                const __qin_exports__ = globalThis.__qinJavaProjectExports || {};
                const SlimeParser = __qin_exports__["com.slime.parser.SlimeParser"];
                const SourceType = __qin_exports__["com.slime.parser.SlimeJavascriptParser$SourceType"];
                if (typeof SlimeParser !== "function") {
                  throw new Error("Generated SlimeParser export is missing");
                }
                const parser = new SlimeParser(__qin_validate_source__);
                let value = null;
                const moduleSourceType = SourceType && SourceType.__qin_field_MODULE;
                if (moduleSourceType != null && typeof parser.Program === "function") {
                  value = parser.Program(moduleSourceType);
                } else if (typeof parser.parse === "function") {
                  value = parser.parse();
                } else if (typeof parser.ModuleBody === "function") {
                  value = parser.ModuleBody();
                }
                const parsedTokens = parser.getParsedTokens();
                const unparsedTokens = parser.getUnparsedTokens();
                const ok = !parser.isParserFail() && unparsedTokens.size() === 0;
                ({
                  ok,
                  label: __qin_validate_label__,
                  parserFail: parser.isParserFail(),
                  errorInfo: parser.getErrorInfo(),
                  currentIndex: parser.getCurrentIndex(),
                  parsedTokens: parsedTokens.size(),
                  unparsedTokens: unparsedTokens.size(),
                  resultNull: value == null,
                  preview: __qin_validate_source__.slice(0, 240)
                });
                """.formatted(
                QinJsPackageRunner.renderJsLiteral(label),
                QinJsPackageRunner.renderJsLiteral(source));
    }

    private static String buildParseAllWrapperSource(SequencedMap<String, String> sources) {
        StringBuilder cases = new StringBuilder("[");
        boolean first = true;
        for (Map.Entry<String, String> entry : sources.entrySet()) {
            if (!first) {
                cases.append(", ");
            }
            first = false;
            cases.append("{ label: ")
                    .append(QinJsPackageRunner.renderJsLiteral(entry.getKey()))
                    .append(", source: ")
                    .append(QinJsPackageRunner.renderJsLiteral(entry.getValue()))
                    .append(" }");
        }
        cases.append("]");
        return """
                const __qin_validate_cases__ = %s;
                const __qin_exports__ = globalThis.__qinJavaProjectExports || {};
                const SlimeParser = __qin_exports__["com.slime.parser.SlimeParser"];
                const SourceType = __qin_exports__["com.slime.parser.SlimeJavascriptParser$SourceType"];
                if (typeof SlimeParser !== "function") {
                  throw new Error("Generated SlimeParser export is missing");
                }
                function __qin_normalize_source__(source) {
                  return String(source)
                    .split(/\\r?\\n/)
                    .map(line => /^\\s*import\\b/.test(line) && !/[;]\\s*$/.test(line) ? line + ";" : line)
                    .join("\\n");
                }
                function __qin_parse_one__(item) {
                  const source = __qin_normalize_source__(item.source);
                  const parser = new SlimeParser(source);
                  let value = null;
                  const moduleSourceType = SourceType && SourceType.__qin_field_MODULE;
                  if (moduleSourceType != null && typeof parser.Program === "function") {
                    value = parser.Program(moduleSourceType);
                  } else if (typeof parser.parse === "function") {
                    value = parser.parse();
                  } else if (typeof parser.ModuleBody === "function") {
                    value = parser.ModuleBody();
                  }
                  const parsedTokens = parser.getParsedTokens();
                  const unparsedTokens = parser.getUnparsedTokens();
                  const ok = !parser.isParserFail() && unparsedTokens.size() === 0;
                  return {
                    ok,
                    label: item.label,
                    parserFail: parser.isParserFail(),
                    errorInfo: parser.getErrorInfo(),
                    currentIndex: parser.getCurrentIndex(),
                    parsedTokens: parsedTokens.size(),
                    unparsedTokens: unparsedTokens.size(),
                    resultNull: value == null,
                    preview: source.slice(0, 240)
                  };
                }
                (__qin_validate_cases__.map(__qin_parse_one__));
                """.formatted(cases);
    }

    public record ParseResult(
            boolean ok,
            String label,
            boolean parserFail,
            String errorInfo,
            int currentIndex,
            int parsedTokens,
            int unparsedTokens,
            boolean resultNull,
            String preview) {
        private static ParseResult from(Map<?, ?> map) {
            return new ParseResult(
                    Boolean.TRUE.equals(map.get("ok")),
                    stringValue(map.get("label")),
                    Boolean.TRUE.equals(map.get("parserFail")),
                    stringValue(map.get("errorInfo")),
                    intValue(map.get("currentIndex")),
                    intValue(map.get("parsedTokens")),
                    intValue(map.get("unparsedTokens")),
                    Boolean.TRUE.equals(map.get("resultNull")),
                    stringValue(map.get("preview")));
        }

        private static String stringValue(Object value) {
            return value == null ? null : String.valueOf(value);
        }

        private static int intValue(Object value) {
            if (value instanceof Number number) {
                return number.intValue();
            }
            return Integer.parseInt(String.valueOf(value));
        }
    }
}
