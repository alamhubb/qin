package com.qin.lang.sema.esm;

import com.slime.ast.nodes.misc.Program;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/** Bindings from an AST before its module specifiers are resolved in a project. */
public record QinEsmModuleBindings(List<Import> imports, List<Export> exports) {
    public static final QinEsmModuleBindings EMPTY = new QinEsmModuleBindings(List.of(), List.of());

    public QinEsmModuleBindings {
        imports = List.copyOf(imports);
        exports = List.copyOf(exports);
    }

    public static QinEsmModuleBindings fromProgram(Program program) {
        return QinEsmAstBindingCollector.describe(program, true);
    }

    public record Import(String moduleSpecifier, QinEsmImportKind kind, String importedName, String localName,
                         int line, int column) {
    }

    public record Export(QinEsmExportKind kind, String exportName, String localName, boolean typeOnly,
                         String moduleSpecifier, int line, int column) {
        public static Export local(QinEsmExportKind kind, String name) {
            return new Export(kind, name, name, false, null, 1, 1);
        }
    }

    public QinEsmModuleSemantic resolve(Path file, Function<String, Path> resolveSpecifier) {
        List<QinEsmImportBinding> resolvedImports = new ArrayList<>(imports.size());
        for (Import binding : imports) {
            resolvedImports.add(new QinEsmImportBinding(file, binding.moduleSpecifier(), binding.kind(),
                    binding.importedName(), binding.localName(), binding.line(), binding.column(),
                    resolveSpecifier.apply(binding.moduleSpecifier())));
        }
        List<QinEsmExportBinding> resolvedExports = new ArrayList<>(exports.size());
        for (Export binding : exports) {
            String specifier = binding.moduleSpecifier();
            resolvedExports.add(new QinEsmExportBinding(file, binding.kind(), binding.exportName(), binding.localName(),
                    binding.typeOnly(), specifier, specifier == null ? null : resolveSpecifier.apply(specifier),
                    binding.line(), binding.column()));
        }
        return new QinEsmModuleSemantic(file, resolvedImports, resolvedExports);
    }
}
