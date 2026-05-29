package com.qin.runtime.core.vue;

import java.nio.file.Path;
import java.util.Map;

public final class QinVueSfcModuleAssembler {
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

        String moduleCode = """
                const __qin_vue_descriptor = %s;
                export const __qinVueDescriptor = __qin_vue_descriptor;
                %s
                %s
                %s
                function __qinMountVue(targetSelector = '#app') {
                  const target = typeof document !== 'undefined'
                    ? (document.querySelector(targetSelector) || document.body)
                    : null;
                  if (!target) return null;
                  const html = __qinRenderVueTemplate();
                  target.innerHTML = html;
                  return target;
                }
                if (typeof document !== 'undefined') {
                  __qinMountVue();
                }
                export { __qinMountVue };
                export default __qin_vue_descriptor;
                """.formatted(
                descriptorJson,
                styleSource.isBlank() ? "" : styleSource,
                script.code().isBlank() ? "" : script.code(),
                templateSource);
        return new AssembledVueModule(moduleCode, script.css(), script.atomModule());
    }

    public record AssembledVueModule(
            String moduleCode,
            String csstsCss,
            String csstsAtomModule) {
    }
}
