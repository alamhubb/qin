package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Map;

public final class QinJsInheritedMethodDynamicThisSmokeTestMain {
    private QinJsInheritedMethodDynamicThisSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("").toAbsolutePath();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                class Base {
                  constructor() {
                    this.hit = false;
                    this.owner = "base";
                  }

                  PrimaryExpression() {
                    this.hit = true;
                    this.owner = "base-primary";
                  }

                  MemberExpression() {
                    const run = () => this.PrimaryExpression();
                    run();
                  }
                }

                class Probe extends Base {
                  PrimaryExpression() {
                    this.hit = true;
                    this.owner = "probe-primary";
                  }
                }

                const probe = new Probe();
                probe.MemberExpression();
                ({ hit: probe.hit, owner: probe.owner });
                """, "js_inherited_method_dynamic_this");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got " + result);
        }
        if (!Boolean.TRUE.equals(map.get("hit")) || !"probe-primary".equals(String.valueOf(map.get("owner")))) {
            throw new IllegalStateException("Expected inherited method to dispatch through child this: "
                    + QinObjectJsonEncoder.toJson(result));
        }
        System.out.println("QinJsInheritedMethodDynamicThisSmokeTestMain OK");
    }
}
