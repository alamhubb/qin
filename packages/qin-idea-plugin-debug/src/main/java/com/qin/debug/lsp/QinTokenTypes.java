package com.qin.debug.lsp;

import com.intellij.psi.tree.IElementType;

final class QinTokenTypes {
    static final IElementType KEYWORD = new IElementType("QIN_KEYWORD", QinLanguage.INSTANCE);
    static final IElementType IDENTIFIER = new IElementType("QIN_IDENTIFIER", QinLanguage.INSTANCE);
    static final IElementType CLASS_NAME = new IElementType("QIN_CLASS_NAME", QinLanguage.INSTANCE);
    static final IElementType FUNCTION_IDENTIFIER = new IElementType("QIN_FUNCTION_IDENTIFIER", QinLanguage.INSTANCE);
    static final IElementType MEMBER_IDENTIFIER = new IElementType("QIN_MEMBER_IDENTIFIER", QinLanguage.INSTANCE);
    static final IElementType REFERENCE_IDENTIFIER = new IElementType("QIN_REFERENCE_IDENTIFIER", QinLanguage.INSTANCE);
    static final IElementType IMPORT_DECLARATION = new IElementType("QIN_IMPORT_DECLARATION", QinLanguage.INSTANCE);
    static final IElementType IMPORT_SPECIFIER = new IElementType("QIN_IMPORT_SPECIFIER", QinLanguage.INSTANCE);
    static final IElementType IMPORT_ALIAS_NAME = new IElementType("QIN_IMPORT_ALIAS_NAME", QinLanguage.INSTANCE);
    static final IElementType OBJECT_DECLARATION = new IElementType("QIN_OBJECT_DECLARATION", QinLanguage.INSTANCE);
    static final IElementType OBJECT_NAME = new IElementType("QIN_OBJECT_NAME", QinLanguage.INSTANCE);
    static final IElementType METHOD_DECLARATION = new IElementType("QIN_METHOD_DECLARATION", QinLanguage.INSTANCE);
    static final IElementType METHOD_NAME = new IElementType("QIN_METHOD_NAME", QinLanguage.INSTANCE);
    static final IElementType FIELD_DECLARATION = new IElementType("QIN_FIELD_DECLARATION", QinLanguage.INSTANCE);
    static final IElementType FIELD_NAME = new IElementType("QIN_FIELD_NAME", QinLanguage.INSTANCE);
    static final IElementType MEMBER_ACCESS = new IElementType("QIN_MEMBER_ACCESS", QinLanguage.INSTANCE);
    static final IElementType NUMBER = new IElementType("QIN_NUMBER", QinLanguage.INSTANCE);
    static final IElementType STRING = new IElementType("QIN_STRING", QinLanguage.INSTANCE);
    static final IElementType LINE_COMMENT = new IElementType("QIN_LINE_COMMENT", QinLanguage.INSTANCE);
    static final IElementType BLOCK_COMMENT = new IElementType("QIN_BLOCK_COMMENT", QinLanguage.INSTANCE);
    static final IElementType BRACE = new IElementType("QIN_BRACE", QinLanguage.INSTANCE);
    static final IElementType PAREN = new IElementType("QIN_PAREN", QinLanguage.INSTANCE);
    static final IElementType BRACKET = new IElementType("QIN_BRACKET", QinLanguage.INSTANCE);
    static final IElementType OPERATOR = new IElementType("QIN_OPERATOR", QinLanguage.INSTANCE);
    static final IElementType COMMA = new IElementType("QIN_COMMA", QinLanguage.INSTANCE);
    static final IElementType SEMICOLON = new IElementType("QIN_SEMICOLON", QinLanguage.INSTANCE);
    static final IElementType DOT = new IElementType("QIN_DOT", QinLanguage.INSTANCE);

    private QinTokenTypes() {
    }
}
