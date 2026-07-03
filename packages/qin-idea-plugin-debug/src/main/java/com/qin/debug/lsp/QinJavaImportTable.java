package com.qin.debug.lsp;

import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

final class QinJavaImportTable {
    private final Map<String, JavaImport> importsByLocalName;

    private QinJavaImportTable(Map<String, JavaImport> importsByLocalName) {
        this.importsByLocalName = importsByLocalName;
    }

    static QinJavaImportTable fromFile(@NotNull PsiFile file) {
        Map<String, JavaImport> imports = new LinkedHashMap<>();
        for (QinImportBindings.ImportBinding binding : QinImportBindings.collect(file)) {
            String moduleName = readJavaModuleName(binding.moduleSpecifier());
            if (moduleName != null) {
                imports.put(binding.localName(),
                        new JavaImport(moduleName, binding.exportedName(), binding.localName()));
            }
        }
        return new QinJavaImportTable(imports);
    }

    @Nullable JavaImport find(String localName) {
        return importsByLocalName.get(localName);
    }

    static @Nullable JavaImport findForSpecifierElement(@NotNull PsiElement element) {
        QinImportBindings.ImportBinding binding = QinImportBindings.findForSpecifierElement(element);
        if (binding == null) {
            return null;
        }
        String moduleName = readJavaModuleName(binding.moduleSpecifier());
        return moduleName == null
                ? null
                : new JavaImport(moduleName, binding.exportedName(), binding.localName());
    }

    private static @Nullable String readJavaModuleName(@NotNull String moduleSpecifier) {
        return moduleSpecifier.startsWith("java:") ? moduleSpecifier.substring("java:".length()).trim() : null;
    }

    record JavaImport(String moduleName, String exportedName, String localName) {
        String qualifiedClassName() {
            return moduleName.isBlank() ? exportedName : moduleName + "." + exportedName;
        }
    }

}
