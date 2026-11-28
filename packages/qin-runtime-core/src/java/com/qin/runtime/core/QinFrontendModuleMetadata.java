package com.qin.runtime.core;

import com.qin.lang.sema.esm.QinEsmExportKind;
import com.qin.lang.sema.esm.QinEsmImportKind;
import com.qin.lang.sema.esm.QinEsmModuleBindings;
import com.slime.generator.SlimeGenerator.ModuleLayout;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Stores source-relative module bindings alongside cached frontend code. */
final class QinFrontendModuleMetadata {
    private QinFrontendModuleMetadata() {
    }

    static void updateBindingFingerprint(MessageDigest digest) throws IOException {
        for (String resource : List.of("QinEsmModuleBindings.class", "QinEsmAstBindingCollector.class")) {
            digest.update(resource.getBytes(StandardCharsets.UTF_8));
            try (var input = QinEsmModuleBindings.class.getResourceAsStream(resource)) {
                if (input != null) digest.update(input.readAllBytes());
            }
            digest.update((byte) 0);
        }
    }

    static void writeBindings(Map<String, String> values, QinEsmModuleBindings bindings) {
        values.put("imports", Integer.toString(bindings.imports().size()));
        for (int i = 0; i < bindings.imports().size(); i++) {
            var binding = bindings.imports().get(i);
            String key = "import." + i + ".";
            values.put(key + "source", binding.moduleSpecifier());
            values.put(key + "kind", binding.kind().name());
            values.put(key + "name", binding.importedName());
            values.put(key + "local", binding.localName());
            values.put(key + "line", Integer.toString(binding.line()));
            values.put(key + "column", Integer.toString(binding.column()));
        }
        values.put("exports", Integer.toString(bindings.exports().size()));
        for (int i = 0; i < bindings.exports().size(); i++) {
            var binding = bindings.exports().get(i);
            String key = "export." + i + ".";
            if (binding.moduleSpecifier() != null) values.put(key + "source", binding.moduleSpecifier());
            values.put(key + "kind", binding.kind().name());
            values.put(key + "name", binding.exportName());
            values.put(key + "local", binding.localName());
            values.put(key + "typeOnly", Boolean.toString(binding.typeOnly()));
            values.put(key + "line", Integer.toString(binding.line()));
            values.put(key + "column", Integer.toString(binding.column()));
        }
    }

    static QinEsmModuleBindings readBindings(Map<String, String> values) {
        try {
            int importCount = count(values, "imports");
            int exportCount = count(values, "exports");
            List<QinEsmModuleBindings.Import> imports = new ArrayList<>(importCount);
            List<QinEsmModuleBindings.Export> exports = new ArrayList<>(exportCount);
            for (int i = 0; i < importCount; i++) {
                String key = "import." + i + ".";
                imports.add(new QinEsmModuleBindings.Import(required(values, key + "source"),
                        QinEsmImportKind.valueOf(required(values, key + "kind")), required(values, key + "name"),
                        required(values, key + "local"), positive(values, key + "line"), positive(values, key + "column")));
            }
            for (int i = 0; i < exportCount; i++) {
                String key = "export." + i + ".";
                exports.add(new QinEsmModuleBindings.Export(QinEsmExportKind.valueOf(required(values, key + "kind")),
                        required(values, key + "name"), required(values, key + "local"), bool(values, key + "typeOnly"),
                        values.get(key + "source"), positive(values, key + "line"), positive(values, key + "column")));
            }
            return new QinEsmModuleBindings(imports, exports);
        } catch (IllegalArgumentException | NullPointerException invalid) {
            return null;
        }
    }

    static void writeLayout(Map<String, String> values, ModuleLayout layout) {
        values.put("defaultExportOffset", Integer.toString(layout.defaultExportOffset()));
        values.put("expressionStart", Integer.toString(layout.expressionStart()));
        values.put("expressionEnd", Integer.toString(layout.expressionEnd()));
        values.put("hasExports", Boolean.toString(layout.hasExports()));
    }

    static ModuleLayout readLayout(Map<String, String> values, String code) {
        try {
            int offset = Integer.parseInt(values.get("defaultExportOffset"));
            int start = Integer.parseInt(values.get("expressionStart"));
            int end = Integer.parseInt(values.get("expressionEnd"));
            boolean hasExports = bool(values, "hasExports");
            if (offset != -1 && (offset < 0 || !hasExports || !code.startsWith("export default ", offset))) return null;
            if ((start != -1 || end != -1) && (start < 0 || end <= start || end > code.length()
                    || code.charAt(end - 1) != ';')) return null;
            return new ModuleLayout(offset, start, end, hasExports);
        } catch (IllegalArgumentException | NullPointerException invalid) {
            return null;
        }
    }

    private static String required(Map<String, String> values, String key) {
        return Objects.requireNonNull(values.get(key));
    }

    private static boolean bool(Map<String, String> values, String key) {
        return switch (required(values, key)) {
            case "true" -> true;
            case "false" -> false;
            default -> throw new IllegalArgumentException("Invalid cached boolean " + key);
        };
    }

    private static int positive(Map<String, String> values, String key) {
        int number = Integer.parseInt(values.get(key));
        if (number < 1) throw new IllegalArgumentException("Invalid cached position " + key);
        return number;
    }

    private static int count(Map<String, String> values, String key) {
        int number = Integer.parseInt(values.get(key));
        if (number < 0 || number > values.size()) throw new IllegalArgumentException("Invalid cached count " + key);
        return number;
    }
}
