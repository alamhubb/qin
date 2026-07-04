package com.qin.debug;

import java.nio.file.Path;
import java.util.List;

public final class QinProjectDiscovery {
    private QinProjectDiscovery() {
    }

    public static List<Path> discoverQinProjects(Path ideaProjectDir) {
        return com.qin.core.LocalProjectResolver.scanAllProjects(ideaProjectDir.toString());
    }
}
