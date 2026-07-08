package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinLinkedModuleSection;
import com.qin.lang.module.resolver.QinLinkedModuleSource;
import com.qin.lang.module.resolver.QinLinkedModuleSourceEmitter;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinModuleClassSectionDumpMain {
    private QinModuleClassSectionDumpMain() {
    }

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("Usage: QinModuleClassSectionDumpMain <entry-file> <path-contains>");
        }
        Path entry = Path.of(args[0]).toAbsolutePath().normalize();
        String pathContains = args[1].replace('\\', '/');
        QinLinkedModuleSource linked = new QinLinkedModuleSourceEmitter()
                .emit(new QinModuleGraphBuilder().build(entry));
        for (QinLinkedModuleSection section : linked.moduleSections()) {
            String normalized = section.file().toString().replace('\\', '/');
            if (!normalized.contains(pathContains)) {
                continue;
            }
            System.out.println("file=" + section.file());
            System.out.println("index=" + section.index());
            System.out.println("chars=" + section.classSource().length());
            if (args.length >= 3) {
                Files.writeString(Path.of(args[2]), section.classSource());
                System.out.println("wrote=" + Path.of(args[2]).toAbsolutePath().normalize());
            } else {
                System.out.println(section.classSource());
            }
            return;
        }
        throw new IllegalStateException("No module section matched: " + pathContains);
    }
}
