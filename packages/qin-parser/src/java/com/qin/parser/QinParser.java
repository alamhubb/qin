package com.qin.parser;

import com.slime.parser.SlimeParser;
import com.subhuti.parser.SubhutiRule;

/**
 * Qin parser entry built on top of Slime's TypeScript-capable parser layer.
 *
 * <p>Current stage goal:
 * keep Qin parser ownership separate from generic JS/TS parser infrastructure,
 * while still reusing Slime grammar and CST/AST machinery.
 */
public class QinParser extends SlimeParser {

    public QinParser(String sourceCode) {
        super(sourceCode);
    }

    /**
     * Placeholder top-level rule for future Qin-specific module entry customization.
     *
     * <p>At the current migration stage we delegate to the inherited program entry.
     */
    @SubhutiRule
    public void QinModule(SourceType sourceType) {
        Program(sourceType);
    }
}
