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
                import { ref } from "/@qin-mod/qin-vue-runtime.js?qin-vue=runtime"
                export default defineOvsComponent(props => {
                  const count = ref(0)
                  return $OvsHtmlTag.div({class:cssts.merge(colorBlue,fontWeight700,padding16px)},[
                    $OvsHtmlTag.h2({},[defineReactiveExpression(() => count.value)])
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
                || !module.contains("typeof target === 'string'")
                || !module.contains("document.querySelector(target)")
                || !module.contains("document.querySelector('[data-qin-component]') || document.querySelector('#ovs-demo')")
                || !module.contains("from \"/@qin-mod/qin-vue-runtime.js?qin-vue=runtime\"")
                || module.contains("?qin-ovs=vue")
                || !module.contains("export default __qinVueComponent")) {
            throw new IllegalStateException("OVS module did not include expected style/runtime wiring:\n" + module);
        }

        String implicitDefaultOvs = """
                import { nextTick } from "/@qin-mod/qin-vue-runtime.js?qin-vue=runtime";
                import { refresh } from "/@qin-mod/app/balanceState.js";
                import { AppShell } from "/@qin-mod/app/qin-ui.js";
                nextTick(refresh); AppShell([defineReactiveExpression(() => "ok")]);
                """;
        String implicitModule = (String) mountOvsModule.invoke(service, ovsFile, implicitDefaultOvs);
        if (implicitModule == null
                || !implicitModule.contains("nextTick(refresh);")
                || !implicitModule.contains("return (AppShell([defineReactiveExpression(() => \"ok\")])")
                || !implicitModule.contains("import { defineReactiveExpression }")
                || !implicitModule.contains("import { defineOvsComponent as __qinDefineOvsComponent }")
                || !implicitModule.contains("export default __qinVueComponent")) {
            throw new IllegalStateException("OVS implicit default module did not wrap the final expression:\n"
                    + implicitModule);
        }

        System.out.println("QinFrontendOvsEsmServiceSmokeTestMain passed.");
    }
}
