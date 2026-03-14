package com.qin.runtime.core;

/**
 * Fixed file paths for import-policy verification.
 */
public final class QinImportPolicyTestConstants {
    public static final String EXAMPLE_ROOT = "examples/import-policy";
    public static final String ALT_EXAMPLE_ROOT = "packages/qin-runtime-core/examples/import-policy";
    public static final String ALT_EXAMPLE_ROOT_2 = "qin/packages/qin-runtime-core/examples/import-policy";

    public static final String FRONTEND_OK = "app/frontend-ok.qin";
    public static final String FRONTEND_BAD_JAVA = "app/frontend-bad-java.qin";
    public static final String BACKEND_OK = "main/backend-ok.qin";
    public static final String BACKEND_BAD_JS = "main/backend-bad-js.qin";
    public static final String SHARED_OK = "shared/shared-ok.qin";
    public static final String SHARED_BAD_JS = "shared/shared-bad-js.qin";
    public static final String SHARED_BAD_JAVA = "shared/shared-bad-java.qin";

    private QinImportPolicyTestConstants() {
    }
}
