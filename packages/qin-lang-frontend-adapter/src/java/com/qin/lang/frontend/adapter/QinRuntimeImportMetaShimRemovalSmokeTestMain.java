package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrStringLiteral;
import com.slime.ast.Position;
import com.slime.ast.SourceLocation;
import com.slime.ast.nodes.expressions.Identifier;
import com.slime.ast.nodes.expressions.MemberExpression;
import com.slime.ast.nodes.expressions.MetaProperty;

import java.util.Map;

public final class QinRuntimeImportMetaShimRemovalSmokeTestMain {
    private QinRuntimeImportMetaShimRemovalSmokeTestMain() {
    }

    public static void main(String[] args) {
        QinSlimeFrontendAdapter adapter = new QinSlimeFrontendAdapter();
        SourceLocation loc = new SourceLocation(new Position(1, 0, 0), new Position(1, 1, 1));

        QinIrExpression legacyShimName = adapter.lowerRuntimeExpression(
                new Identifier("__qin_import_meta_url__", loc),
                Map.of(),
                Map.of());
        if (!(legacyShimName instanceof QinIrIdentifierReference identifierReference)
                || !"__qin_import_meta_url__".equals(identifierReference.name())) {
            throw new IllegalStateException(
                    "Expected old import.meta.url shim name to lower as an ordinary identifier, got: "
                            + legacyShimName);
        }

        MetaProperty importMeta = new MetaProperty(
                new Identifier("import", loc),
                new Identifier("meta", loc),
                loc);
        MemberExpression importMetaUrl = new MemberExpression(
                importMeta,
                new Identifier("url", loc),
                false,
                false,
                loc);
        QinIrExpression formalImportMetaUrl = adapter.lowerRuntimeExpression(importMetaUrl, Map.of(), Map.of());
        if (!(formalImportMetaUrl instanceof QinIrStringLiteral stringLiteral)
                || !"import.meta.url".equals(stringLiteral.value())) {
            throw new IllegalStateException(
                    "Expected formal import.meta.url AST to lower through MetaProperty, got: "
                            + formalImportMetaUrl);
        }

        System.out.println("QinRuntimeImportMetaShimRemovalSmokeTestMain OK");
    }
}
