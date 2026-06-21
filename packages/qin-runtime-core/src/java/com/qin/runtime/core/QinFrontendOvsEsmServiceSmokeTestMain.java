package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinModuleGraph;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Map;

public final class QinFrontendOvsEsmServiceSmokeTestMain {
    private QinFrontendOvsEsmServiceSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("examples", "ovs-style-smoke").toAbsolutePath().normalize();
        Path ovsFile = root.resolve("app").resolve("OvsDemo.ovs");
        String compiledOvs = """
                import {$OvsHtmlTag,defineOvsComponent,defineReactiveExpression} from "/@qin-mod/app/OvsDemo.ovs.js?qin-ovs=runtime"
                export default defineOvsComponent(props => {
                  return $OvsHtmlTag.div({class:cssts.merge(colorBlue,fontWeight700,padding16px)},[
                    $OvsHtmlTag.h2({},["Rendered from .ovs"])
                  ])
                })
                """;

        Constructor<QinFrontendEsmService> constructor = QinFrontendEsmService.class.getDeclaredConstructor(
                Path.class,
                Path.class,
                QinModuleGraph.class,
                Map.class,
                Map.class,
                Map.class,
                Map.class,
                String.class,
                QinVueSfcCompiler.class,
                QinOvsCompiler.class,
                QinCsstsCompiler.class);
        constructor.setAccessible(true);
        QinFrontendEsmService service = constructor.newInstance(
                root,
                ovsFile,
                null,
                Map.of(),
                Map.of(),
                Map.of(),
                Map.of(),
                "/@qin-mod/app/OvsDemo.ovs.js",
                null,
                null,
                null);

        Method mountOvsModule = QinFrontendEsmService.class.getDeclaredMethod(
                "mountOvsModule", Path.class, String.class);
        mountOvsModule.setAccessible(true);
        String module = (String) mountOvsModule.invoke(service, ovsFile, compiledOvs);
        if (module == null
                || !module.contains("import \"/@qin-mod/app/OvsDemo.ovs.js?qin-vue-cssts=style\"")
                || !module.contains("qin-vue-cssts=runtime")
                || !module.contains("qin-vue-cssts=atom")
                || !module.contains("const { colorBlue, fontWeight700, padding16px }")
                || !module.contains("__qinMountOvs")
                || !module.contains("__qinMountVue")
                || !module.contains("export default __qinVueComponent")) {
            throw new IllegalStateException("OVS module did not include expected style/runtime wiring:\n" + module);
        }

        System.out.println("QinFrontendOvsEsmServiceSmokeTestMain passed.");
    }
}
