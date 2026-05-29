package com.qin.parser;

import com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams;
import com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams;
import com.slime.parser.base.SlimeJavascriptParserBase.StatementParams;
import com.subhuti.parser.SubhutiParser;
import com.subhuti.struct.SubhutiMatchToken;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinParserRuleProbeMain {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("Usage: QinParserRuleProbeMain <rule> <source>|--file <path> [--no-cache]");
        }

        String rule = args[0];
        String source;
        int optionStart;
        if ("--file".equals(args[1])) {
            if (args.length < 3) {
                throw new IllegalArgumentException("Usage: QinParserRuleProbeMain <rule> --file <path> [--no-cache]");
            }
            source = Files.readString(Path.of(args[2]), StandardCharsets.UTF_8);
            optionStart = 3;
        } else {
            source = args[1];
            optionStart = 2;
        }
        QinParser parser = SubhutiParser.create(QinParser.class, source);
        boolean cacheEnabled = true;
        for (int i = optionStart; i < args.length; i++) {
            if ("--no-cache".equals(args[i])) {
                cacheEnabled = false;
            }
        }
        parser.cache(cacheEnabled);
        try {
            switch (rule) {
                case "Program" -> parser.Program();
                case "ClassDeclaration" ->
                    parser.ClassDeclaration(new DeclarationParams(false, true, false));
                case "ClassBody" ->
                    parser.ClassBody(new DeclarationParams(false, true, false));
                case "ClassElement" ->
                    parser.ClassElement(new DeclarationParams(false, true, false));
                case "MethodDefinition" ->
                    parser.MethodDefinition(new ExpressionParams(true, false, true));
                case "FieldDefinition" ->
                    parser.FieldDefinition(new DeclarationParams(false, true, false));
                case "FunctionDeclaration" ->
                    parser.FunctionDeclaration(new DeclarationParams(false, true, false));
                case "FunctionBody" -> parser.FunctionBody();
                case "ReturnStatement" ->
                    parser.ReturnStatement(new StatementParams(false, true, true));
                case "Expression" ->
                    parser.Expression(new ExpressionParams(true, false, true));
                case "ObjectLiteral" ->
                    parser.ObjectLiteral(new ExpressionParams(true, false, true));
                case "AssignmentExpression" ->
                    parser.AssignmentExpression(new ExpressionParams(true, false, true));
                case "LeftHandSideExpression" ->
                    parser.LeftHandSideExpression(new ExpressionParams(true, false, true));
                case "MemberExpression" ->
                    parser.MemberExpression(new ExpressionParams(true, false, true));
                case "NewExpression" ->
                    parser.NewExpression(new ExpressionParams(true, false, true));
                case "PrimaryExpression" ->
                    parser.PrimaryExpression(new ExpressionParams(true, false, true));
                case "PropertyDefinition" ->
                    parser.PropertyDefinition(new ExpressionParams(true, false, true));
                case "LiteralPropertyName" -> parser.LiteralPropertyName();
                case "StringLiteral" -> parser.StringLiteral();
                case "IdentifierReference" ->
                    parser.IdentifierReference(new ExpressionParams(true, false, true));
                case "Identifier" -> parser.Identifier();
                case "Arguments" ->
                    parser.Arguments(new ExpressionParams(true, false, true));
                default -> throw new IllegalArgumentException("Unsupported rule: " + rule);
            }
            printState(rule, parser);
        } catch (Exception e) {
            printFailure(rule, parser, e);
        }
    }

    private static void printState(String rule, QinParser parser) {
        SubhutiMatchToken token = parser.curToken();
        System.out.println("rule=" + rule);
        System.out.println("success=" + !parser.isParserFail());
        System.out.println("index=" + parser.getCurrentIndex());
        System.out.println("next=" + (token == null ? "null" : token.tokenName() + ":" + token.value()));
    }

    private static void printFailure(String rule, QinParser parser, Exception e) {
        SubhutiMatchToken token = parser.curToken();
        System.out.println("rule=" + rule);
        System.out.println("success=false");
        System.out.println("index=" + parser.getCurrentIndex());
        System.out.println("next=" + (token == null ? "null" : token.tokenName() + ":" + token.value()));
        System.out.println("error=" + e.getClass().getSimpleName() + ": " + e.getMessage());
        e.printStackTrace(System.out);
    }
}
