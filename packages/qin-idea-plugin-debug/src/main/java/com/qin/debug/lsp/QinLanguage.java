package com.qin.debug.lsp;

import com.intellij.lang.Language;

public final class QinLanguage extends Language {
    public static final QinLanguage INSTANCE = new QinLanguage();

    private QinLanguage() {
        super("Qin");
    }
}
