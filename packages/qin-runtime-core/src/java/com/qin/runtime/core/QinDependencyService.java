package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.List;

/**
 * Dependency resolution boundary for runtime builds.
 *
 * Current stage: no-op placeholder.
 * Future stage: reuse Qin workspace/local/remote dependency resolvers.
 */
public class QinDependencyService {
    public QinResolvedDependencies resolve(Path projectRoot) {
        return new QinResolvedDependencies(List.of());
    }
}
