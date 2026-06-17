package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.slime.ast.AstNode;
import com.slime.ast.Position;
import com.slime.ast.SourceLocation;
import com.slime.ast.nodes.expressions.Identifier;
import com.slime.ast.nodes.misc.Property;
import com.slime.ast.nodes.patterns.ObjectPattern;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class QinDeclarationIrObjectPatternSmokeTestMain {
    private QinDeclarationIrObjectPatternSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        SourceLocation location = new SourceLocation(new Position(0, 0), new Position(0, 1));
        Identifier key = new Identifier("name", location);
        Identifier value = new Identifier("localName", location);
        Property property = new Property(key, value, "init", false, false, false, location);
        ObjectPattern pattern = new ObjectPattern(List.of(property), location);
        List<QinIrConstDeclaration> lowered = new ArrayList<>();

        QinDeclarationIrLowerer lowerer = new QinDeclarationIrLowerer(new QinSlimeFrontendAdapter());
        Method method = QinDeclarationIrLowerer.class.getDeclaredMethod(
                "lowerBindingPatternDeclarations",
                AstNode.class,
                com.qin.lang.ir.QinIrExpression.class,
                List.class);
        method.setAccessible(true);
        method.invoke(lowerer, pattern, new QinIrIdentifierReference("source"), lowered);

        if (lowered.size() != 1 || !"localName".equals(lowered.get(0).name())) {
            throw new IllegalStateException("Object pattern was not lowered through current Slime AST Property shape: " + lowered);
        }

        System.out.println("QinDeclarationIrObjectPatternSmokeTestMain OK");
    }
}
