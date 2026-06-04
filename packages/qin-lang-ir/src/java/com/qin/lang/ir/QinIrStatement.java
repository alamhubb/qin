package com.qin.lang.ir;

public sealed interface QinIrStatement permits
        QinIrBreakStatement,
        QinIrContinueStatement,
        QinIrDoWhileStatementNode,
        QinIrForEachStatement,
        QinIrForStatement,
        QinIrIfStatement,
        QinIrLocalDeclarationStatement,
        QinIrReturnStatement,
        QinIrStatementExpression,
        QinIrThrowStatement,
        QinIrTryStatement,
        QinIrWhileStatementNode {
}
