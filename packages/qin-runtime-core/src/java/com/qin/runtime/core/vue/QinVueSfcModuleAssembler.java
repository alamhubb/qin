package com.qin.runtime.core.vue;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QinVueSfcModuleAssembler {
    private static final Pattern COMPONENT_TAG_PATTERN = Pattern.compile(
            "<\\s*([A-Z][A-Za-z0-9_$]*)\\b");

    private QinVueSfcModuleAssembler() {
    }

    public static AssembledVueModule assemble(
            Path projectRoot,
            Path moduleFile,
            Map<String, Object> descriptor,
            String descriptorJson,
            QinVueModuleImportRewriter importRewriter) {
        String templateSource = QinVueTemplateBlockCompiler.compile(descriptor.get("template"));
        QinVueScriptBlockCompiler.CompiledScript script = QinVueScriptBlockCompiler.compile(
                projectRoot,
                moduleFile,
                descriptor.get("script"),
                descriptor.get("scriptSetup"),
                importRewriter);
        String styleSource = QinVueStyleBlockCompiler.compile(descriptor.get("styles"));
        String componentMounts = componentMounts(descriptor.get("template"));

        String moduleCode = """
                const __qin_vue_descriptor = %s;
                export const __qinVueDescriptor = __qin_vue_descriptor;
                %s
                %s
                %s
                function __qinMountVue(targetSelector = '#app') {
                  const target = typeof document !== 'undefined'
                    ? (typeof targetSelector === 'string'
                      ? (document.querySelector(targetSelector) || document.body)
                      : targetSelector)
                    : null;
                  if (!target) return null;
                  const html = __qinRenderVueTemplate();
                  target.innerHTML = html;
                  %s
                  return target;
                }
                if (typeof document !== 'undefined') {
                  __qinMountVue();
                }
                const __qin_vue_component = { ...__qin_vue_descriptor, __qinMountVue };
                export { __qinMountVue };
                export default __qin_vue_component;
                """.formatted(
                descriptorJson,
                styleSource.isBlank() ? "" : styleSource,
                script.code().isBlank() ? "" : script.code(),
                templateSource,
                componentMounts);
        return new AssembledVueModule(moduleCode, script.css(), script.atomModule());
    }

    private static String componentMounts(Object templateBlock) {
        String template = QinVueSfcBlockSupport.extractBlockContent(templateBlock);
        Set<String> names = new LinkedHashSet<>();
        Matcher matcher = COMPONENT_TAG_PATTERN.matcher(template);
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
        if (names.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (String name : names) {
            builder.append("for (const __qinChildTarget of target.querySelectorAll('[data-qin-component=\"")
                    .append(name)
                    .append("\"]')) {\n")
                    .append("  const __qinChild = typeof ")
                    .append(name)
                    .append(" !== 'undefined' ? ")
                    .append(name)
                    .append(" : null;\n")
                    .append("  if (__qinChild && typeof __qinChild.__qinMountVue === 'function') {\n")
                    .append("    __qinChild.__qinMountVue(__qinChildTarget);\n")
                    .append("  }\n")
                    .append("}\n");
        }
        return builder.toString();
    }

    public record AssembledVueModule(
            String moduleCode,
            String csstsCss,
            String csstsAtomModule) {
    }
}
