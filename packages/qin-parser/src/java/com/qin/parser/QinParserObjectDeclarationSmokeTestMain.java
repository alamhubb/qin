package com.qin.parser;

import com.slime.ast.AstNode;
import com.slime.ast.nodes.declarations.ClassDeclaration;
import com.slime.ast.nodes.declarations.VariableDeclaration;
import com.slime.ast.nodes.misc.Program;
import com.slime.ast.nodes.modules.ExportDefaultDeclaration;
import com.slime.ast.nodes.modules.ExportNamedDeclaration;
import com.subhuti.struct.SubhutiCst;

public final class QinParserObjectDeclarationSmokeTestMain {
    private QinParserObjectDeclarationSmokeTestMain() {
    }

    public static void main(String[] args) {
        parsesPlainObjectDeclaration();
        parsesDecoratedObjectDeclaration();
        parsesDecoratedObjectDeclarationWithImports();
        parsesNamedExportObjectDeclaration();
        parsesDefaultExportObjectDeclaration();
        keepsTypeKeywordObjectUntouched();
        System.out.println("QinParserObjectDeclarationSmokeTestMain passed.");
    }

    private static void parsesDecoratedObjectDeclarationWithImports() {
        QinParsedSource parsed = new QinParserFacade().parseSource("""
                import { RestController, RequestMapping, GetMapping } from "../qin-web-class"
                import { QinWeb } from "java:com.qin.web"
                import { db, users } from "../db/schema"

                @RestController
                @RequestMapping("/api/users")
                export object UserController {
                    @GetMapping("")
                    getAll(request) {
                        return QinWeb.jsonRaw(db.selectJson("users", users, "id", "asc"));
                    }
                }
                """);
        require(parsed.hasProgram(), "decorated exported object with imports should parse");
        requireContainsQinObjectCst(parsed);
        requireObjectAst(parsed.requireProgram(), "UserController", true, false);
    }

    private static void parsesDecoratedObjectDeclaration() {
        QinParsedSource parsed = new QinParserFacade().parseSource("""
                @RestController
                @RequestMapping("/api/users")
                export object UserController {
                    @GetMapping("")
                    getAll(request) {
                        return request;
                    }
                }
                """);
        require(parsed.hasProgram(), "decorated object declaration should parse");
        requireContainsQinObjectCst(parsed);
        requireObjectAst(parsed.requireProgram(), "UserController", true, false);
    }

    private static void parsesPlainObjectDeclaration() {
        QinParsedSource parsed = new QinParserFacade().parseSource("""
                object Counter {
                    value = 1;
                    inc() {
                        return this.value + 1;
                    }
                }
                Counter.inc();
                """);
        require(parsed.hasProgram(), "plain object declaration should parse");
        requireContainsQinObjectCst(parsed);
        requireObjectAst(parsed.requireProgram(), "Counter", false, false);
        require(!parsed.effectiveSource().contains("__QinObject_Counter"),
                "Qin object syntax must not be implemented by source string rewrite");
    }

    private static void parsesNamedExportObjectDeclaration() {
        QinParsedSource parsed = new QinParserFacade().parseSource("""
                export object Registry {
                    get() {
                        return "ok";
                    }
                }
                """);
        require(parsed.hasProgram(), "named export object declaration should parse");
        requireContainsQinObjectCst(parsed);
        requireObjectAst(parsed.requireProgram(), "Registry", true, false);
    }

    private static void parsesDefaultExportObjectDeclaration() {
        QinParsedSource parsed = new QinParserFacade().parseSource("""
                export default object App {
                    start() {
                        return true;
                    }
                }
                """);
        require(parsed.hasProgram(), "default export object declaration should parse");
        requireContainsQinObjectCst(parsed);
        requireObjectAst(parsed.requireProgram(), "App", true, true);
    }

    private static void keepsTypeKeywordObjectUntouched() {
        QinParsedSource parsed = new QinParserFacade().parseSource("""
                type Box = object;
                const text = "object NotADeclaration {}";
                """);
        require(parsed.hasProgram(), "object type keyword should keep parsing");
        require(!containsNode(parsed.cst(), "QinObjectDeclarationBody"),
                "TypeScript object keyword and string content must not parse as Qin object declaration");
    }

    private static void requireContainsQinObjectCst(QinParsedSource parsed) {
        require(parsed.hasCst(), "parsed Qin source should keep CST");
        require(containsNode(parsed.cst(), "QinObjectDeclarationBody"),
                "Expected PEG CST to contain QinObjectDeclarationBody");
    }

    private static void requireObjectAst(
            Program program,
            String publicName,
            boolean exported,
            boolean defaultExport) {
        String internalName = "__QinObject_" + publicName;
        boolean hasInternalClass = false;
        boolean hasSingleton = false;
        boolean hasExpectedExport = !exported;
        for (AstNode statement : program.body()) {
            if (statement instanceof ClassDeclaration classDeclaration
                    && classDeclaration.id() != null
                    && internalName.equals(classDeclaration.id().name())) {
                hasInternalClass = true;
            }
            if (statement instanceof VariableDeclaration variableDeclaration
                    && containsDeclarator(variableDeclaration, publicName)) {
                hasSingleton = true;
            }
            if (exported && !defaultExport && statement instanceof ExportNamedDeclaration exportNamedDeclaration
                    && exportNamedDeclaration.declaration() instanceof VariableDeclaration variableDeclaration
                    && containsDeclarator(variableDeclaration, publicName)) {
                hasExpectedExport = true;
                hasSingleton = true;
            }
            if (exported && defaultExport && statement instanceof ExportDefaultDeclaration exportDefaultDeclaration
                    && exportDefaultDeclaration.declaration() instanceof com.slime.ast.nodes.expressions.Identifier identifier
                    && publicName.equals(identifier.name())) {
                hasExpectedExport = true;
            }
        }
        require(hasInternalClass, "Expected internal object class AST: " + internalName);
        require(hasSingleton, "Expected singleton const AST: " + publicName);
        require(hasExpectedExport, "Expected object export AST for: " + publicName);
    }

    private static boolean containsDeclarator(VariableDeclaration declaration, String name) {
        return declaration.declarations().stream().anyMatch(declarator ->
                declarator.id() instanceof com.slime.ast.nodes.expressions.Identifier identifier
                        && name.equals(identifier.name()));
    }

    private static boolean containsNode(SubhutiCst cst, String name) {
        if (cst == null) {
            return false;
        }
        if (name.equals(cst.getName())) {
            return true;
        }
        if (cst.getChildren() != null) {
            for (SubhutiCst child : cst.getChildren()) {
                if (containsNode(child, name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
