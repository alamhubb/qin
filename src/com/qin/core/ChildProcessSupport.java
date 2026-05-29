package com.qin.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages child process lifecycle so qin-launched runtimes do not remain alive
 * after the parent CLI exits or is interrupted.
 */
public final class ChildProcessSupport {
    private static final long GRACEFUL_WAIT_MILLIS = 1500L;

    private ChildProcessSupport() {
    }

    public static int waitFor(Process process, String description) throws InterruptedException {
        return waitFor(process, description, null);
    }

    public static int waitFor(Process process, String description, Runnable finallyAction) throws InterruptedException {
        if (process == null) {
            throw new IllegalArgumentException("process cannot be null");
        }

        Thread shutdownHook = new Thread(
                () -> stop(process, description),
                "qin-child-process-cleanup-" + process.pid());
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        try {
            return process.waitFor();
        } finally {
            removeShutdownHook(shutdownHook);
            try {
                if (finallyAction != null) {
                    finallyAction.run();
                }
            } finally {
                stop(process, description);
            }
        }
    }

    public static void stop(Process process, String description) {
        if (process == null) {
            return;
        }
        stop(process.toHandle(), description);
    }

    private static void stop(ProcessHandle root, String description) {
        if (root == null) {
            return;
        }

        List<ProcessHandle> descendants = new ArrayList<>(root.descendants().toList());
        destroy(descendants, false);
        destroy(root, false);
        sleepQuietly(GRACEFUL_WAIT_MILLIS);
        destroy(descendants, true);
        destroy(root, true);
    }

    private static void destroy(List<ProcessHandle> handles, boolean forcibly) {
        for (int i = handles.size() - 1; i >= 0; i--) {
            destroy(handles.get(i), forcibly);
        }
    }

    private static void destroy(ProcessHandle handle, boolean forcibly) {
        if (handle == null || !handle.isAlive()) {
            return;
        }
        try {
            if (forcibly) {
                handle.destroyForcibly();
            } else {
                handle.destroy();
            }
        } catch (UnsupportedOperationException | SecurityException ignored) {
            // Best-effort cleanup only.
        }
    }

    private static void removeShutdownHook(Thread shutdownHook) {
        try {
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
        } catch (IllegalStateException | IllegalArgumentException ignored) {
            // JVM is already shutting down or hook was never registered.
        }
    }

    private static void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
