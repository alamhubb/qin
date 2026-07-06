package com.qin.runtime.core;

/**
 * Lightweight opt-in phase profiler for focused Qin diagnostics.
 */
public final class QinPhaseTimer {
    private final String scope;
    private final boolean enabled;
    private final long startedNanos;
    private long lastNanos;

    private QinPhaseTimer(String scope, boolean enabled) {
        this.scope = scope;
        this.enabled = enabled;
        this.startedNanos = System.nanoTime();
        this.lastNanos = startedNanos;
        if (enabled) {
            System.out.println("[QinProfile] " + scope + " start");
        }
    }

    public static QinPhaseTimer start(String scope) {
        return new QinPhaseTimer(scope, isEnabled());
    }

    public static boolean isEnabled() {
        return Boolean.getBoolean("qin.profile")
                || "1".equals(System.getenv("QIN_PROFILE"))
                || "true".equalsIgnoreCase(System.getenv("QIN_PROFILE"));
    }

    public void checkpoint(String phase) {
        checkpoint(phase, "");
    }

    public void checkpoint(String phase, String detail) {
        if (!enabled) {
            return;
        }
        long now = System.nanoTime();
        long phaseMs = (now - lastNanos) / 1_000_000L;
        long totalMs = (now - startedNanos) / 1_000_000L;
        lastNanos = now;
        String suffix = detail == null || detail.isBlank() ? "" : " :: " + detail;
        System.out.println("[QinProfile] " + scope + " " + phase
                + " +" + phaseMs + "ms total=" + totalMs + "ms" + suffix);
    }

    public void done() {
        done("");
    }

    public void done(String detail) {
        checkpoint("done", detail);
    }
}
