package com.qin.parser;

import java.util.regex.Pattern;

/**
 * Shared Qin runtime helper names used by frontend lowering.
 */
public final class QinParserRuntimeNames {
    public static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[A-Za-z_$][A-Za-z0-9_$]*$");
    public static final String DYNAMIC_IMPORT_SHIM = "__qin_dynamic_import__";
    public static final String TOP_LEVEL_AWAIT_SHIM = "__qin_top_level_await__";
    public static final String FUNCTION_CALL_SHIM = "__qin_call__";
    public static final String FUNCTION_MAKE_SHIM = "__qin_make_function__";

    private QinParserRuntimeNames() {
    }
}
