package com.qin.debug.lsp;

import com.intellij.lang.Language;

public final class OvsLanguage extends Language {
    public static final OvsLanguage INSTANCE = new OvsLanguage();

    private OvsLanguage() {
        super("OVS");
    }
}
