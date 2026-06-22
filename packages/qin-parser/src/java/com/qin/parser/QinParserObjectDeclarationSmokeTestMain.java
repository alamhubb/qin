package com.qin.parser;

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
                import { RestController, RequestMapping, GetMapping } from "../qono-class"
                import { Qono } from "java:com.qin.runtime.core.qono"
                import { db, users } from "../db/schema"

                @RestController
                @RequestMapping("/api/users")
                export object UserController {
                    @GetMapping("")
                    getAll(request) {
                        return Qono.jsonRaw(db.selectJson("users", users, "id", "asc"));
                    }
                }
                """);
        require(parsed.hasProgram(), "decorated object declaration with imports should parse");
        requireContains(parsed.effectiveSource(), "export const UserController = new __QinObject_UserController();");
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
        requireContains(parsed.effectiveSource(), "@RestController");
        requireContains(parsed.effectiveSource(), "class __QinObject_UserController");
        requireContains(parsed.effectiveSource(), "export const UserController = new __QinObject_UserController();");
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
        requireContains(parsed.effectiveSource(), "class __QinObject_Counter");
        requireContains(parsed.effectiveSource(), "const Counter = new __QinObject_Counter();");
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
        requireContains(parsed.effectiveSource(), "class __QinObject_Registry");
        requireContains(parsed.effectiveSource(), "export const Registry = new __QinObject_Registry();");
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
        requireContains(parsed.effectiveSource(), "class __QinObject_App");
        requireContains(parsed.effectiveSource(), "const App = new __QinObject_App();");
        requireContains(parsed.effectiveSource(), "export default App;");
    }

    private static void keepsTypeKeywordObjectUntouched() {
        QinParsedSource parsed = new QinParserFacade().parseSource("""
                type Box = object;
                const text = "object NotADeclaration {}";
                """);
        require(parsed.hasProgram(), "object type keyword should keep parsing");
        require(!parsed.effectiveSource().contains("__QinObject_NotADeclaration"),
                "object keyword inside string must not lower");
    }

    private static void requireContains(String text, String expected) {
        require(text.contains(expected), "Expected lowered source to contain: " + expected + "\n" + text);
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
