package com.qin.runtime.core.vue;

import com.qin.runtime.core.QinCsstsCompiler;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class QinVueScriptBlockCompiler {
    private QinVueScriptBlockCompiler() {
    }

    static CompiledScript compile(
            Path projectRoot,
            Path moduleFile,
            Object scriptBlock,
            Object scriptSetupBlock,
            QinVueModuleImportRewriter importRewriter) {
        StringBuilder builder = new StringBuilder();
        CompiledBlock script = compileSingleBlock(projectRoot, moduleFile, scriptBlock, importRewriter);
        CompiledBlock scriptSetup = compileSingleBlock(projectRoot, moduleFile, scriptSetupBlock, importRewriter);

        if (!script.code().isBlank()) {
            builder.append(script.code().trim()).append('\n');
        }
        if (!scriptSetup.code().isBlank()) {
            builder.append(scriptSetup.code().trim()).append('\n');
        }
        String compiled = builder.toString().trim();
        return new CompiledScript(
                compiled,
                firstNonBlank(script.css(), scriptSetup.css()),
                firstNonBlank(script.atomModule(), scriptSetup.atomModule()));
    }

    private static CompiledBlock compileSingleBlock(
            Path projectRoot,
            Path moduleFile,
            Object block,
            QinVueModuleImportRewriter importRewriter) {
        String source = QinVueSfcBlockSupport.extractBlockContent(block);
        if (source.isBlank()) {
            return new CompiledBlock("", "", "");
        }

        if (QinVueSfcBlockSupport.isCsstsLang(block)) {
            try {
                QinCsstsCompiler.QinCsstsCompileResult result = new QinCsstsCompiler().compile(projectRoot, source);
                return new CompiledBlock(
                        rewriteImports(result.rawCode(), importRewriter),
                        result.css(),
                        rewriteImports(result.atomModule(), importRewriter));
            } catch (Exception error) {
                throw new IllegalStateException(
                        "Qin Vue SFC compilation failed for " + moduleFile.toAbsolutePath()
                                + " using the formal npm cssts path (" + QinVueSfcBlockSupport.describeBlock(block) + ").",
                        error);
            }
        }

        source = rewriteImports(source, importRewriter);
        return new CompiledBlock(source.trim(), "", "");
    }

    private static String firstNonBlank(String first, String second) {
        if (first != null && !first.isBlank()) {
            return first;
        }
        return second == null ? "" : second;
    }

    private static String rewriteImports(String source, QinVueModuleImportRewriter importRewriter) {
        if (importRewriter == null || source.isBlank()) {
            return source;
        }
        String rewritten = rewriteSpecifierLines(source, importRewriter, Pattern.compile("(?m)(import\\s+[^;\\n]*?\\s+from\\s*[\"'])([^\"']+)([\"'])"));
        rewritten = rewriteSpecifierLines(rewritten, importRewriter, Pattern.compile("(?m)(export\\s+(?:\\*\\s*(?:as\\s+[A-Za-z_$][\\w$]*\\s*)?|\\{[^}\\n]*})\\s*from\\s*[\"'])([^\"']+)([\"'])"));
        rewritten = rewriteSpecifierLines(rewritten, importRewriter, Pattern.compile("(?m)(import\\s*[\"'])([^\"']+)([\"'])"));
        return rewritten;
    }

    private static String rewriteSpecifierLines(String source, QinVueModuleImportRewriter importRewriter, Pattern pattern) {
        Matcher matcher = pattern.matcher(source);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String prefix = matcher.group(1);
            String specifier = matcher.group(2);
            String suffix = matcher.group(3);
            String rewritten = importRewriter.rewrite(specifier);
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(prefix + rewritten + suffix));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    record CompiledScript(String code, String css, String atomModule) {
    }

    private record CompiledBlock(String code, String css, String atomModule) {
    }
}
