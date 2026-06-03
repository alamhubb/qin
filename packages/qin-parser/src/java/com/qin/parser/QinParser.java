package com.qin.parser;

import com.slime.parser.SlimeParser;
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
     * Placeholder top-level entry for future Qin-specific module customization.
     *
     * <p>At the current migration stage the production facade calls the inherited
     * {@code Program} rule directly. Keeping this method outside the Subhuti rule
     * table prevents the Qin subclass from perturbing the inherited Slime parser
     * packrat keys while still documenting the planned Qin-owned entry point.
     */
    public void QinModule(SourceType sourceType) {
        Program(sourceType);
    }
}
