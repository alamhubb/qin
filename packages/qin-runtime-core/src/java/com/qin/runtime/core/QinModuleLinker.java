package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinLinkedModuleSource;
import com.qin.lang.module.resolver.QinLinkedModuleSourceEmitter;
import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Runtime-core adapter that links Qin modules via qin-lang-module-resolver.
 */
public final class QinModuleLinker {
    private final QinModuleGraphBuilder graphBuilder = new QinModuleGraphBuilder();
    private final QinLinkedModuleSourceEmitter sourceEmitter = new QinLinkedModuleSourceEmitter();

    public QinLinkedSource link(Path entryFile) throws IOException {
        QinModuleGraph graph = graphBuilder.build(entryFile);
        QinLinkedModuleSource linked = sourceEmitter.emit(graph);
        return new QinLinkedSource(
                linked.entryFile(),
                linked.source(),
                linked.modules(),
                linked.imports(),
                linked.moduleGraph());
    }
}
