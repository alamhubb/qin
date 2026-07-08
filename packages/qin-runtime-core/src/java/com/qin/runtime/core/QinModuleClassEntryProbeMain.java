package com.qin.runtime.core;

import java.nio.file.Path;

public final class QinModuleClassEntryProbeMain {
    private QinModuleClassEntryProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: QinModuleClassEntryProbeMain <project-root> <entry-file>");
        }
        Path root = Path.of(args[0]).toAbsolutePath().normalize();
        Path entry = Path.of(args[1]).toAbsolutePath().normalize();
        String cacheSalt = args.length >= 3 ? args[2] : "";
        Object result = new QinInMemoryJvmRunner().compileAndRunModuleClasses(
                entry,
                root,
                "probe.QinModuleClassEntryProbe",
                cacheSalt,
                root.resolve(".qin").resolve("module-class-cache"),
                "");
        System.out.println("QinModuleClassEntryProbeMain result=" + result);
    }
}
