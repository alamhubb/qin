package com.qin.debug.lsp;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

final class QinModuleImportTable {
    private final @Nullable PsiFile importingFile;
    private final Map<String, QinImportBindings.ImportBinding> importsByLocalName;

    private QinModuleImportTable(
            @Nullable PsiFile importingFile,
            @NotNull Map<String, QinImportBindings.ImportBinding> importsByLocalName) {
        this.importingFile = importingFile;
        this.importsByLocalName = importsByLocalName;
    }

    static @NotNull QinModuleImportTable fromFile(@NotNull PsiFile file) {
        Map<String, QinImportBindings.ImportBinding> imports = new LinkedHashMap<>();
        for (QinImportBindings.ImportBinding binding : QinImportBindings.collect(file)) {
            if (isQinModuleImportSpecifier(binding)) {
                imports.put(binding.localName(), binding);
            }
        }
        return new QinModuleImportTable(file, imports);
    }

    static @NotNull QinModuleImportTable fromElement(@NotNull PsiElement element) {
        PsiFile file = QinPsiTree.containingFile(element);
        return file == null ? new QinModuleImportTable(null, Map.of()) : fromFile(file);
    }

    @Nullable QinImportBindings.ImportBinding find(@NotNull String localName) {
        return importsByLocalName.get(localName);
    }

    private static boolean isQinModuleImportSpecifier(@NotNull QinImportBindings.ImportBinding binding) {
        return QinModuleSpecifierFacts.isQinModuleSpecifier(binding.moduleSpecifier());
    }

    @Nullable VirtualFile resolveFile(@NotNull QinImportBindings.ImportBinding binding) {
        if (!isQinModuleImportSpecifier(binding)) {
            return null;
        }
        if (importingFile == null) {
            return null;
        }
        VirtualFile sourceFile = QinPsiTree.originalVirtualFile(importingFile);
        if (sourceFile == null || sourceFile.getParent() == null) {
            return null;
        }

        String normalized = QinModuleSpecifierFacts.normalizePathSeparators(binding.moduleSpecifier());
        VirtualFile resolved = sourceFile.getParent().findFileByRelativePath(normalized);
        if (resolved == null && !normalized.endsWith(".qin")) {
            resolved = sourceFile.getParent().findFileByRelativePath(normalized + ".qin");
        }
        return resolved != null && resolved.getFileType() == QinLspFileType.INSTANCE ? resolved : null;
    }
}
