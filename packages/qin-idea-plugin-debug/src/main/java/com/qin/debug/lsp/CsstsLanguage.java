package com.qin.debug.lsp;

import com.intellij.lang.Language;

public final class CsstsLanguage extends Language {
    public static final CsstsLanguage INSTANCE = new CsstsLanguage();

    private CsstsLanguage() {
        super("CSSTS");
    }
}
