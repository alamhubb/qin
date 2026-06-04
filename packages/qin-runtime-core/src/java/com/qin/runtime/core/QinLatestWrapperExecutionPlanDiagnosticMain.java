package com.qin.runtime.core;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.pipeline.cfa.QinIrToCfaIrLowerer;
import com.qin.lang.pipeline.cfa.ir.QinCfaProgram;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public final class QinLatestWrapperExecutionPlanDiagnosticMain {
    private static final List<String> WATCHED_NAMES = List.of(
            "com_subhuti_struct_SubhutiCreateToken",
            "SubhutiCreateToken",
            "com_subhuti_struct_SubhutiTokenContextConstraint",
            "SubhutiTokenContextConstraint",
            "com_slime_token_JavaScriptTokens",
            "JavaScriptTokens",
            "tokenCount",
            "parser",
            "constructError");

    private QinLatestWrapperExecutionPlanDiagnosticMain() {
    }

    public static void main(String[] args) throws Exception {
        Path wrapper = latestWrapper();
        QinIrProgram ir = new QinFrontendLowerer().lowerSource(Files.readString(wrapper));
        QinCfaProgram cfa = new QinIrToCfaIrLowerer().lower(ir);
        System.out.println("Wrapper source: " + wrapper);
        System.out.println("IR declarations=" + ir.declarations().size()
                + ", IR steps=" + ir.executionSteps().size());
        System.out.println("CFA declarations=" + cfa.declarations().size()
                + ", CFA steps=" + cfa.executionSteps().size());
        for (String name : WATCHED_NAMES) {
            int declarationIndex = declarationIndex(cfa, name);
            String initializer = declarationIndex >= 0
                    ? describe(cfa.declarations().get(declarationIndex).initializer())
                    : "<missing>";
            System.out.println(name + " declarationIndex=" + declarationIndex
                    + ", firstStep=" + firstStep(cfa, QinCfaProgram.TopLevelStatementKind.DECLARATION, declarationIndex)
                    + ", initializer=" + initializer);
        }
        for (int i = 0; i < cfa.executionSteps().size(); i++) {
            QinCfaProgram.TopLevelExecutionStep step = cfa.executionSteps().get(i);
            String label = step.kind() + "#" + step.index();
            if (step.kind() == QinCfaProgram.TopLevelStatementKind.DECLARATION) {
                label = label + " " + cfa.declarations().get(step.index()).name();
            }
            if (label.contains("SubhutiCreateToken")
                    || label.contains("SubhutiTokenContextConstraint")
                    || label.contains("JavaScriptTokens")
                    || (step.kind() == QinCfaProgram.TopLevelStatementKind.EXPRESSION_STATEMENT && i > 0)) {
                System.out.println("step " + i + " -> " + label);
            }
        }
        for (int expressionIndex : List.of(33, 34, 35)) {
            if (expressionIndex < cfa.expressionStatements().size()) {
                QinCfaProgram.Expression expression = cfa.expressionStatements().get(expressionIndex).expression();
                System.out.println("expression#" + expressionIndex + " root=" + describe(expression));
                System.out.println("expression#" + expressionIndex + " tree:");
                printExpressionTree(expression, "  ", new int[]{0});
                System.out.println("expression#" + expressionIndex + " first call-method targets:");
                printCallMethods(expression, 0, new int[]{0});
            }
        }
    }

    private static void printExpressionTree(QinCfaProgram.Expression expression, String indent, int[] count) {
        if (expression == null || count[0] >= 80) {
            return;
        }
        System.out.println(indent + describe(expression));
        count[0]++;
        if (expression instanceof QinCfaProgram.BuiltinCallExpression call) {
            for (QinCfaProgram.Expression argument : call.arguments()) {
                printExpressionTree(argument, indent + "  ", count);
            }
            return;
        }
        if (expression instanceof QinCfaProgram.LetExpression letExpression) {
            for (QinCfaProgram.LocalVariableDeclaration declaration : letExpression.localDeclarations()) {
                System.out.println(indent + "  local " + declaration.name() + " = " + describe(declaration.initializer()));
                printExpressionTree(declaration.initializer(), indent + "    ", count);
            }
            for (QinCfaProgram.Expression leadingExpression : letExpression.leadingExpressions()) {
                System.out.println(indent + "  leading:");
                printExpressionTree(leadingExpression, indent + "    ", count);
            }
            System.out.println(indent + "  result:");
            printExpressionTree(letExpression.resultExpression(), indent + "    ", count);
            return;
        }
        if (expression instanceof QinCfaProgram.FunctionLiteral functionLiteral) {
            printExpressionTree(functionLiteral.returnExpression(), indent + "  ", count);
            return;
        }
        if (expression instanceof QinCfaProgram.SequenceExpression sequenceExpression) {
            for (QinCfaProgram.Expression leadingExpression : sequenceExpression.leadingExpressions()) {
                System.out.println(indent + "  seq leading:");
                printExpressionTree(leadingExpression, indent + "    ", count);
            }
            System.out.println(indent + "  seq result:");
            printExpressionTree(sequenceExpression.resultExpression(), indent + "    ", count);
            return;
        }
        if (expression instanceof QinCfaProgram.ObjectLiteral objectLiteral) {
            for (QinCfaProgram.ObjectProperty property : objectLiteral.properties()) {
                System.out.println(indent + "  property " + property.key() + ":");
                printExpressionTree(property.value(), indent + "    ", count);
            }
            return;
        }
        if (expression instanceof QinCfaProgram.ArrayLiteral arrayLiteral) {
            for (QinCfaProgram.Expression element : arrayLiteral.elements()) {
                printExpressionTree(element, indent + "  ", count);
            }
        }
    }

    private static void printCallMethods(QinCfaProgram.Expression expression, int depth, int[] count) {
        if (expression == null || count[0] >= 30) {
            return;
        }
        if (expression instanceof QinCfaProgram.BuiltinCallExpression call) {
            if ("Global".equals(call.receiverName())
                    && "__qin_call_method__".equals(call.methodName())
                    && call.arguments().size() >= 2) {
                System.out.println("  call#" + count[0]
                        + " target=" + describe(call.arguments().get(0))
                        + " method=" + describe(call.arguments().get(1)));
                count[0]++;
            }
            for (QinCfaProgram.Expression argument : call.arguments()) {
                printCallMethods(argument, depth + 1, count);
            }
            return;
        }
        if (expression instanceof QinCfaProgram.LetExpression letExpression) {
            for (QinCfaProgram.LocalVariableDeclaration declaration : letExpression.localDeclarations()) {
                printCallMethods(declaration.initializer(), depth + 1, count);
            }
            for (QinCfaProgram.Expression leadingExpression : letExpression.leadingExpressions()) {
                printCallMethods(leadingExpression, depth + 1, count);
            }
            printCallMethods(letExpression.resultExpression(), depth + 1, count);
            return;
        }
        if (expression instanceof QinCfaProgram.FunctionLiteral functionLiteral) {
            printCallMethods(functionLiteral.returnExpression(), depth + 1, count);
            return;
        }
        if (expression instanceof QinCfaProgram.SequenceExpression sequenceExpression) {
            for (QinCfaProgram.Expression leadingExpression : sequenceExpression.leadingExpressions()) {
                printCallMethods(leadingExpression, depth + 1, count);
            }
            printCallMethods(sequenceExpression.resultExpression(), depth + 1, count);
            return;
        }
        if (expression instanceof QinCfaProgram.ObjectLiteral objectLiteral) {
            for (QinCfaProgram.ObjectProperty property : objectLiteral.properties()) {
                printCallMethods(property.value(), depth + 1, count);
            }
            return;
        }
        if (expression instanceof QinCfaProgram.ArrayLiteral arrayLiteral) {
            for (QinCfaProgram.Expression element : arrayLiteral.elements()) {
                printCallMethods(element, depth + 1, count);
            }
        }
    }

    private static String describe(QinCfaProgram.Expression expression) {
        if (expression instanceof QinCfaProgram.IdentifierReference identifierReference) {
            return "Identifier(" + identifierReference.name() + ")";
        }
        if (expression instanceof QinCfaProgram.StringLiteral stringLiteral) {
            return "String(" + stringLiteral.value() + ")";
        }
        if (expression instanceof QinCfaProgram.BuiltinCallExpression call) {
            return "Builtin(" + call.receiverName() + "." + call.methodName() + ")";
        }
        if (expression instanceof QinCfaProgram.JavaNewExpression javaNewExpression) {
            return "JavaNew(" + javaNewExpression.classLocalName()
                    + ", owner=" + javaNewExpression.ownerBinaryName()
                    + ", args=" + javaNewExpression.arguments().size() + ")";
        }
        if (expression instanceof QinCfaProgram.MemberAccessExpression memberAccessExpression) {
            return "Member(" + memberAccessExpression.objectName() + "." + memberAccessExpression.propertyName() + ")";
        }
        return expression.getClass().getSimpleName();
    }

    private static int declarationIndex(QinCfaProgram cfa, String name) {
        for (int i = 0; i < cfa.declarations().size(); i++) {
            if (name.equals(cfa.declarations().get(i).name())) {
                return i;
            }
        }
        return -1;
    }

    private static int firstStep(
            QinCfaProgram cfa,
            QinCfaProgram.TopLevelStatementKind kind,
            int index) {
        if (index < 0) {
            return -1;
        }
        for (int i = 0; i < cfa.executionSteps().size(); i++) {
            QinCfaProgram.TopLevelExecutionStep step = cfa.executionSteps().get(i);
            if (step.kind() == kind && step.index() == index) {
                return i;
            }
        }
        return -1;
    }

    private static Path latestWrapper() throws Exception {
        Path tempRoot = Path.of(System.getProperty("java.io.tmpdir"));
        try (Stream<Path> stream = Files.walk(tempRoot, 6)) {
            return stream.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().equals("invoke-java_project_slime_parser_js_parse-1.js"))
                    .max(Comparator.comparingLong(path -> path.toFile().lastModified()))
                    .orElseThrow();
        }
    }
}
