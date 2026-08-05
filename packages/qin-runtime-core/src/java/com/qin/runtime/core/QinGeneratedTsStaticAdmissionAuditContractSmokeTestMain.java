package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.List;

public final class QinGeneratedTsStaticAdmissionAuditContractSmokeTestMain {
    private QinGeneratedTsStaticAdmissionAuditContractSmokeTestMain() {
    }

    public static void main(String[] args) {
        QinGeneratedTsStaticAdmissionAudit.assertRejectsUnprovenDynamicShapes();
        QinGeneratedTsStaticAdmissionAudit.Result result = QinGeneratedTsStaticAdmissionAudit.audit(List.of(
                new QinJavaProjectJsCompiler.EsmFileOutput(
                        "demo.Generated",
                        Path.of("demo", "Generated.java"),
                        Path.of("demo", "Generated.ts"),
                        """
                        export function generated(method, receiver) {
                          return /* @qin-static-admission member=call owner=demo.Generated method=rule receiver=receiver arity=0 */ method.call(receiver);
                        }
                        """)));
        if (result.contractAllowedDynamicWrapperCount() != 1
                || result.legacyAllowedDynamicWrapperCount() != 0) {
            throw new IllegalStateException("Unexpected audit result: " + result);
        }
        System.out.println("QinGeneratedTsStaticAdmissionAuditContractSmokeTestMain OK");
    }
}
