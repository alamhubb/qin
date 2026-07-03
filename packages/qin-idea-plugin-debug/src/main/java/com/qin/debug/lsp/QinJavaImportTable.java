package com.qin.debug.lsp;

import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class QinJavaImportTable {
    private final Map<String, JavaImport> importsByLocalName;

    private QinJavaImportTable(Map<String, JavaImport> importsByLocalName) {
        this.importsByLocalName = importsByLocalName;
    }

    static QinJavaImportTable fromFile(@NotNull PsiFile file) {
        Map<String, JavaImport> imports = new LinkedHashMap<>();
        List<QinPsiToken> tokens = QinPsiTokenStream.collect(file);
        for (int index = 0; index < tokens.size(); index++) {
            QinPsiToken token = tokens.get(index);
            if (!token.isKeyword("import")) {
                continue;
            }

            int openBraceIndex = nextTokenIndex(tokens, index + 1, QinTokenTypes.BRACE, "{");
            if (openBraceIndex < 0) {
                continue;
            }
            int closeBraceIndex = nextTokenIndex(tokens, openBraceIndex + 1, QinTokenTypes.BRACE, "}");
            if (closeBraceIndex < 0) {
                continue;
            }
            int fromIndex = nextKeywordIndex(tokens, closeBraceIndex + 1, "from");
            if (fromIndex < 0 || fromIndex + 1 >= tokens.size()) {
                continue;
            }
            String moduleName = readJavaModuleName(tokens.get(fromIndex + 1));
            if (moduleName == null) {
                continue;
            }
            for (ImportBinding binding : parseBindings(tokens.subList(openBraceIndex + 1, closeBraceIndex))) {
                imports.put(binding.localName(), new JavaImport(moduleName, binding.exportedName(), binding.localName()));
            }
        }
        return new QinJavaImportTable(imports);
    }

    @Nullable JavaImport find(String localName) {
        return importsByLocalName.get(localName);
    }

    private static List<ImportBinding> parseBindings(List<QinPsiToken> tokens) {
        List<ImportBinding> bindings = new ArrayList<>();
        int index = 0;
        while (index < tokens.size()) {
            QinPsiToken exported = tokens.get(index);
            if (!exported.isIdentifier()) {
                index++;
                continue;
            }
            String exportedName = exported.text();
            String localName = exportedName;
            if (index + 2 < tokens.size() && tokens.get(index + 1).isKeyword("as") && tokens.get(index + 2).isIdentifier()) {
                localName = tokens.get(index + 2).text();
                index += 3;
            } else {
                index++;
            }
            bindings.add(new ImportBinding(exportedName, localName));
            if (index < tokens.size() && tokens.get(index).is(QinTokenTypes.COMMA, ",")) {
                index++;
            }
        }
        return bindings;
    }

    private static int nextTokenIndex(List<QinPsiToken> tokens, int startIndex, IElementType type, String text) {
        for (int index = startIndex; index < tokens.size(); index++) {
            if (tokens.get(index).is(type, text)) {
                return index;
            }
        }
        return -1;
    }

    private static int nextKeywordIndex(List<QinPsiToken> tokens, int startIndex, String text) {
        for (int index = startIndex; index < tokens.size(); index++) {
            if (tokens.get(index).isKeyword(text)) {
                return index;
            }
        }
        return -1;
    }

    private static @Nullable String readJavaModuleName(QinPsiToken token) {
        if (token.type() != QinTokenTypes.STRING) {
            return null;
        }
        String text = token.text();
        if (text.length() < 2) {
            return null;
        }
        String value = text.substring(1, text.length() - 1);
        return value.startsWith("java:") ? value.substring("java:".length()).trim() : null;
    }

    record JavaImport(String moduleName, String exportedName, String localName) {
        String qualifiedClassName() {
            return moduleName.isBlank() ? exportedName : moduleName + "." + exportedName;
        }
    }

    private record ImportBinding(String exportedName, String localName) {
    }
}
