package com.qin.debug.lsp;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

final class QinModuleImportTable {
    private final PsiFile importingFile;
    private final Map<String, QinImport> importsByLocalName;

    private QinModuleImportTable(
            @NotNull PsiFile importingFile,
            @NotNull Map<String, QinImport> importsByLocalName) {
        this.importingFile = importingFile;
        this.importsByLocalName = importsByLocalName;
    }

    static @NotNull QinModuleImportTable fromFile(@NotNull PsiFile file) {
        Map<String, QinImport> imports = new LinkedHashMap<>();
        for (QinImportBindings.ImportBinding binding : QinImportBindings.collect(file)) {
            if (isQinModuleSpecifier(binding.moduleSpecifier())) {
                imports.put(binding.localName(), new QinImport(
                        binding.moduleSpecifier(),
                        binding.exportedName(),
                        binding.localName()));
            }
        }
        return new QinModuleImportTable(file, imports);
    }

    @Nullable QinImport find(@NotNull String localName) {
        return importsByLocalName.get(localName);
    }

    static boolean isQinModuleImportSpecifier(@NotNull QinImportBindings.ImportBinding binding) {
        return isQinModuleSpecifier(binding.moduleSpecifier());
    }

    @Nullable VirtualFile resolveFile(@NotNull QinImport qinImport) {
        VirtualFile sourceFile = importingFile.getOriginalFile().getVirtualFile();
        if (sourceFile == null || sourceFile.getParent() == null) {
            return null;
        }

        String normalized = qinImport.moduleSpecifier().replace('\\', '/');
        VirtualFile resolved = sourceFile.getParent().findFileByRelativePath(normalized);
        if (resolved == null && !normalized.endsWith(".qin")) {
            resolved = sourceFile.getParent().findFileByRelativePath(normalized + ".qin");
        }
        return resolved != null && resolved.getFileType() == QinLspFileType.INSTANCE ? resolved : null;
    }

    private static boolean isQinModuleSpecifier(@NotNull String moduleSpecifier) {
        String normalized = moduleSpecifier.replace('\\', '/');
        return !normalized.startsWith("java:")
                && (normalized.startsWith("./") || normalized.startsWith("../"))
                && (normalized.endsWith(".qin") || !normalized.endsWith("/"));
    }

    record QinImport(
            @NotNull String moduleSpecifier,
            @NotNull String exportedName,
            @NotNull String localName) {
    }
}
