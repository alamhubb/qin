package com.qin.debug.lsp;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class QinJavaImportTable {
    private static final Pattern JAVA_IMPORT = Pattern.compile(
            "import\\s*\\{(?<imports>[^}]*)}\\s*from\\s*[\"']java:(?<module>[^\"']+)[\"']",
            Pattern.DOTALL);
    private static final Pattern IMPORT_BINDING = Pattern.compile(
            "(?<exported>[A-Za-z_$][\\w$]*)(?:\\s+as\\s+(?<local>[A-Za-z_$][\\w$]*))?");

    private final Map<String, JavaImport> importsByLocalName;

    private QinJavaImportTable(Map<String, JavaImport> importsByLocalName) {
        this.importsByLocalName = importsByLocalName;
    }

    static QinJavaImportTable fromFile(@NotNull PsiFile file) {
        Map<String, JavaImport> imports = new LinkedHashMap<>();
        Matcher importMatcher = JAVA_IMPORT.matcher(file.getText());
        while (importMatcher.find()) {
            String moduleName = importMatcher.group("module").trim();
            Matcher bindingMatcher = IMPORT_BINDING.matcher(importMatcher.group("imports"));
            while (bindingMatcher.find()) {
                String exportedName = bindingMatcher.group("exported");
                String localName = bindingMatcher.group("local") == null
                        ? exportedName
                        : bindingMatcher.group("local");
                imports.put(localName, new JavaImport(moduleName, exportedName, localName));
            }
        }
        return new QinJavaImportTable(imports);
    }

    @Nullable JavaImport find(String localName) {
        return importsByLocalName.get(localName);
    }

    record JavaImport(String moduleName, String exportedName, String localName) {
        String qualifiedClassName() {
            return moduleName.isBlank() ? exportedName : moduleName + "." + exportedName;
        }
    }
}
