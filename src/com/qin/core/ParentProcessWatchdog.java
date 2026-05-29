package com.qin.core;

import java.time.Duration;
import java.util.Optional;

/**
 * Exits the current JVM when its direct parent process disappears.
 *
 * This prevents qin-launched long-running services from surviving after the
 * wrapper shell/window is terminated on Windows.
 */
public final class ParentProcessWatchdog {
    private static final Duration POLL_INTERVAL = Duration.ofSeconds(1);

    private ParentProcessWatchdog() {
    }

    public static void install() {
        Optional<ProcessHandle> parent = ProcessHandle.current().parent();
        if (parent.isEmpty()) {
            return;
        }

        long parentPid = parent.get().pid();
        Thread watcher = Thread.ofPlatform()
                .name("qin-parent-watchdog")
                .daemon(true)
                .start(() -> watchParent(parentPid));
        watcher.setUncaughtExceptionHandler((thread, error) -> {
            // Best-effort lifecycle support only.
        });
    }

    private static void watchParent(long parentPid) {
        while (true) {
            if (!ProcessHandle.of(parentPid).map(ProcessHandle::isAlive).orElse(false)) {
                System.exit(0);
                return;
            }
            try {
                Thread.sleep(POLL_INTERVAL.toMillis());
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
