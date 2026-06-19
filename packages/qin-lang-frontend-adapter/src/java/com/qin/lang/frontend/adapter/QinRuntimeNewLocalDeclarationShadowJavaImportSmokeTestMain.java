package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.slime.ast.Position;
import com.slime.ast.SourceLocation;
import com.slime.ast.nodes.expressions.Identifier;
import com.slime.ast.nodes.expressions.Literal;
import com.slime.ast.nodes.expressions.NewExpression;
import com.slime.ast.nodes.expressions.ObjectExpression;
import com.slime.ast.nodes.misc.Property;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class QinRuntimeNewLocalDeclarationShadowJavaImportSmokeTestMain {
    private QinRuntimeNewLocalDeclarationShadowJavaImportSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinSlimeFrontendAdapter adapter = new QinSlimeFrontendAdapter();
        SourceLocation loc = new SourceLocation(new Position(1, 0, 0), new Position(1, 1, 1));
        Identifier callee = new Identifier("SubhutiCreateToken", loc);
        ObjectExpression options = new ObjectExpression(List.of(new Property(
                new Identifier("name", loc),
                Literal.string("Identifier", loc),
                "init",
                false,
                false,
                false,
                loc)), loc);
        NewExpression newExpression = new NewExpression(callee, List.of(options), true, loc);
        Map<String, String> javaImportLookup = Map.of("SubhutiCreateToken", "com.subhuti.struct.SubhutiCreateToken");
        Map<String, QinIrExpression> declarationLookup = new LinkedHashMap<>();
        declarationLookup.put("SubhutiCreateToken", new QinIrIdentifierReference("SubhutiCreateToken"));

        Method method = QinSlimeFrontendAdapter.class.getDeclaredMethod(
                "lowerRuntimeNewExpression",
                NewExpression.class,
                Map.class,
                Map.class);
        method.setAccessible(true);
        QinIrExpression lowered = (QinIrExpression) method.invoke(adapter, newExpression, javaImportLookup, declarationLookup);

        if (lowered instanceof QinIrJavaNewExpression javaNewExpression) {
            throw new IllegalStateException("Expected local JS constructor to shadow Java import, got Java new: "
                    + javaNewExpression.ownerBinaryName());
        }
        if (!(lowered instanceof QinIrBuiltinCallExpression callExpression)
                || !"Global".equals(callExpression.receiverName())
                || !"__qin_new__".equals(callExpression.methodName())) {
            throw new IllegalStateException("Expected runtime __qin_new__ call, got: " + lowered);
        }
        if (callExpression.arguments().isEmpty()
                || !(callExpression.arguments().get(0) instanceof QinIrIdentifierReference identifierReference)
                || !"SubhutiCreateToken".equals(identifierReference.name())) {
            throw new IllegalStateException("Expected first __qin_new__ argument to be local identifier, got: "
                    + callExpression.arguments());
        }
        if (callExpression.arguments().size() != 2) {
            throw new IllegalStateException("Expected one constructor argument, got: " + callExpression.arguments());
        }
        if (!(callExpression.arguments().get(1) instanceof com.qin.lang.ir.QinIrObjectLiteral objectLiteral)
                || objectLiteral.properties().isEmpty()
                || !(objectLiteral.properties().get(0).value() instanceof QinIrStringLiteral stringLiteral)
                || !"Identifier".equals(stringLiteral.value())) {
            throw new IllegalStateException("Expected object constructor argument to survive lowering, got: "
                    + callExpression.arguments().get(1));
        }

        System.out.println("QinRuntimeNewLocalDeclarationShadowJavaImportSmokeTestMain OK");
    }
}
