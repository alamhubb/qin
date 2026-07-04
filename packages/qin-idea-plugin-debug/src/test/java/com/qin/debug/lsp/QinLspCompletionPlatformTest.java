package com.qin.debug.lsp;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.codeInsight.lookup.impl.LookupImpl;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.platform.lsp.tests.LspTestUtilKt;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.refactoring.rename.RenameProcessor;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.ui.UIUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class QinLspCompletionPlatformTest extends BasePlatformTestCase {
    private String previousWorkspaceRoot;

    @Override
    protected void setUp() throws Exception {
        previousWorkspaceRoot = System.getProperty("qin.lsp.workspaceRoot");
        System.setProperty("qin.lsp.workspaceRoot", Path.of("..", "..", "..").toAbsolutePath().normalize().toString());
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            super.tearDown();
        } finally {
            if (previousWorkspaceRoot == null) {
                System.clearProperty("qin.lsp.workspaceRoot");
            } else {
                System.setProperty("qin.lsp.workspaceRoot", previousWorkspaceRoot);
            }
        }
    }

    public void testQinFileTypeAndHighlighterAreRegistered() {
        assertSame(QinLspFileType.INSTANCE, FileTypeManager.getInstance().getFileTypeByExtension("qin"));

        SyntaxHighlighter highlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(
                QinLanguage.INSTANCE,
                getProject(),
                null);
        assertNotNull(highlighter);
        assertInstanceOf(highlighter, QinSyntaxHighlighter.class);
    }

    public void testQinHighlighterClassAndMemberTokens() {
        Lexer lexer = new QinLexer();
        lexer.start("""
                import { Greeter } from "java:demo"

                const message = Greeter.greet("Qin")
                """);

        List<IElementType> tokens = new ArrayList<>();
        while (lexer.getTokenType() != null) {
            tokens.add(lexer.getTokenType());
            lexer.advance();
        }

        assertTrue("Qin lexer should classify imported Java-style class identifiers for IDEA highlighting",
                tokens.contains(QinTokenTypes.CLASS_NAME));
        assertTrue("Qin lexer should classify member access identifiers for IDEA highlighting",
                tokens.contains(QinTokenTypes.MEMBER_IDENTIFIER));
    }

    public void testQinHighlighterFunctionCallTokens() {
        String source = """
                const message = formatMessage("Qin")
                const reply = Greeter.greet("Qin")
                """;
        List<String> tokens = collectLexerTokenEntries(source);

        assertTrue("Qin lexer should classify function call identifiers for IDEA highlighting: " + tokens,
                tokens.contains("formatMessage:" + QinTokenTypes.FUNCTION_IDENTIFIER));
        assertTrue("Qin lexer should keep Java-style class identifiers for IDEA highlighting: " + tokens,
                tokens.contains("Greeter:" + QinTokenTypes.CLASS_NAME));
        assertTrue("Qin lexer should keep member identifiers for IDEA highlighting: " + tokens,
                tokens.contains("greet:" + QinTokenTypes.MEMBER_IDENTIFIER));
    }

    public void testQinSyntaxHighlighterMapsCoreTokensToIdeaTextAttributes() {
        QinSyntaxHighlighter highlighter = new QinSyntaxHighlighter();

        assertTokenHighlight(highlighter, QinTokenTypes.KEYWORD, DefaultLanguageHighlighterColors.KEYWORD);
        assertTokenHighlight(highlighter, QinTokenTypes.IDENTIFIER, DefaultLanguageHighlighterColors.IDENTIFIER);
        assertTokenHighlight(highlighter, QinTokenTypes.CLASS_NAME, DefaultLanguageHighlighterColors.CLASS_NAME);
        assertTokenHighlight(highlighter, QinTokenTypes.FUNCTION_IDENTIFIER, DefaultLanguageHighlighterColors.FUNCTION_CALL);
        assertTokenHighlight(highlighter, QinTokenTypes.MEMBER_IDENTIFIER, DefaultLanguageHighlighterColors.INSTANCE_METHOD);
        assertTokenHighlight(highlighter, QinTokenTypes.STRING, DefaultLanguageHighlighterColors.STRING);
        assertTokenHighlight(highlighter, QinTokenTypes.NUMBER, DefaultLanguageHighlighterColors.NUMBER);
        assertTokenHighlight(highlighter, QinTokenTypes.LINE_COMMENT, DefaultLanguageHighlighterColors.LINE_COMMENT);
        assertTokenHighlight(highlighter, QinTokenTypes.BLOCK_COMMENT, DefaultLanguageHighlighterColors.BLOCK_COMMENT);
        assertTokenHighlight(highlighter, QinTokenTypes.BRACE, DefaultLanguageHighlighterColors.BRACES);
        assertTokenHighlight(highlighter, QinTokenTypes.PAREN, DefaultLanguageHighlighterColors.PARENTHESES);
        assertTokenHighlight(highlighter, QinTokenTypes.BRACKET, DefaultLanguageHighlighterColors.BRACKETS);
        assertTokenHighlight(highlighter, QinTokenTypes.OPERATOR, DefaultLanguageHighlighterColors.OPERATION_SIGN);
        assertTokenHighlight(highlighter, QinTokenTypes.COMMA, DefaultLanguageHighlighterColors.COMMA);
        assertTokenHighlight(highlighter, QinTokenTypes.SEMICOLON, DefaultLanguageHighlighterColors.SEMICOLON);
        assertTokenHighlight(highlighter, QinTokenTypes.DOT, DefaultLanguageHighlighterColors.DOT);
    }

    public void testQinSymbolAnnotatorHighlightsObjectDeclarationsAndReferences() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }

                const value = Counter.next()
                """);

        List<HighlightInfo> highlights = myFixture.doHighlighting(HighlightSeverity.INFORMATION);
        assertHighlightContains(highlights, "Qin object symbol");
        assertHighlightContains(highlights, "Qin field symbol");
        assertHighlightContains(highlights, "Qin method symbol");
        assertHighlightContains(highlights, "Qin object reference");
        assertHighlightContains(highlights, "Qin method reference");
        assertHighlightContains(highlights, "Qin field reference");
    }

    public void testQinSymbolAnnotatorHighlightsImportAliasDeclarationsAndReferences() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C } from "./Counter.qin"

                const value = C.value
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        List<HighlightInfo> highlights = myFixture.doHighlighting(HighlightSeverity.INFORMATION);
        assertHighlightContains(highlights, "Qin object reference");
        assertHighlightContains(highlights, "Qin import alias symbol");
        assertHighlightContains(highlights, "Qin import alias reference");

        String text = myFixture.getEditor().getDocument().getText();
        PsiReference aliasReference = myFixture.getFile().findReferenceAt(text.indexOf("C.value"));
        assertNotNull("Qin aliased object usage should have a local alias reference", aliasReference);
        assertSingleQinImportAliasReference(aliasReference.getElement());
        assertNoQinObjectReference(aliasReference.getElement());
    }

    public void testQinSymbolAnnotatorHighlightsJavaImportAliasReferencesAsLocalAliases() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const message = G.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        List<HighlightInfo> highlights = myFixture.doHighlighting(HighlightSeverity.INFORMATION);
        assertHighlightContains(highlights, "Java class reference");
        assertHighlightContains(highlights, "Qin import alias symbol");
        assertHighlightContains(highlights, "Qin import alias reference");
        assertHighlightContains(highlights, "Java static method reference");
    }

    public void testQinSymbolAnnotatorHighlightsJavaAliasedStaticFieldReferences() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static final String DEFAULT_NAME = "Qin";
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const name = G.DEFAULT_NAME
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        List<HighlightInfo> highlights = myFixture.doHighlighting(HighlightSeverity.INFORMATION);
        assertHighlightContains(highlights, "Java class reference");
        assertHighlightContains(highlights, "Qin import alias symbol");
        assertHighlightContains(highlights, "Qin import alias reference");
        assertHighlightContains(highlights, "Java static field reference");
    }

    public void testQinSymbolAnnotatorHighlightsJavaPsiReferences() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static final String DEFAULT_NAME = "Qin";

                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const name = Greeter.DEFAULT_NAME
                const message = Greeter.greet(name)
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        List<HighlightInfo> highlights = myFixture.doHighlighting(HighlightSeverity.INFORMATION);
        assertHighlightContains(highlights, "Java class reference");
        assertHighlightContains(highlights, "Java static field reference");
        assertHighlightContains(highlights, "Java static method reference");
    }

    public void testQinLexerUsesSlimeTokenDefinitionsForModernSyntax() {
        String source = """
                object Store {
                  value = 0xFFn
                  maybe = target?.field
                }
                """;
        List<String> tokens = collectLexerTokenEntries(source);

        assertTrue("Qin lexer should classify Qin object as a contextual keyword: " + tokens,
                tokens.contains("object:" + QinTokenTypes.KEYWORD));
        assertTrue("Qin lexer should classify Slime bigint numeric literals: " + tokens,
                tokens.contains("0xFFn:" + QinTokenTypes.NUMBER));
        assertTrue("Qin lexer should map Slime optional chaining token into IDEA operator highlighting: " + tokens,
                tokens.contains("?.:" + QinTokenTypes.OPERATOR));
    }

    public void testQinLexerHighlightsImportContextualKeywordsOnlyInsideImports() {
        String source = """
                import { Greeter as G } from "java:demo"

                const from = "local"
                """;
        List<String> tokens = collectLexerTokenEntries(source);

        assertTrue("Qin lexer should highlight import alias as contextual keyword: " + tokens,
                tokens.contains("as:" + QinTokenTypes.KEYWORD));
        assertTrue("Qin lexer should highlight import source marker as contextual keyword: " + tokens,
                tokens.contains("from:" + QinTokenTypes.KEYWORD));
        assertTrue("Qin lexer should keep non-import from identifiers as identifiers: " + tokens,
                tokens.contains("from:" + QinTokenTypes.IDENTIFIER));
    }

    public void testQinLexerStopsImportContextualKeywordHighlightAtNextLine() {
        String source = """
                import { Greeter }
                const as = "local"
                """;
        List<String> tokens = collectLexerTokenEntries(source);

        assertTrue("Qin lexer should not treat as on the next statement as an import keyword: " + tokens,
                tokens.contains("as:" + QinTokenTypes.IDENTIFIER));
    }
    public void testQinLexerKeepsUnterminatedStringHighlightableForIdeaEditing() {
        String source = "const message = \"unterminated";
        List<String> tokens = collectLexerTokenEntries(source);

        assertTrue("Qin lexer should keep editor-time unterminated strings as string tokens: " + tokens,
                tokens.contains("\"unterminated:" + QinTokenTypes.STRING));
    }

    public void testQinSourceStructureFindsUniqueObjectNamesFromSharedTokenStream() {
        String source = """
                // object IgnoredComment
                export object Counter {
                  value = 0xFFn
                }

                object Store {
                  maybe = target?.field
                }

                object Counter {
                  next() {
                    return this.value
                  }
                }
                """;

        List<String> objectNames = QinSourceStructure.objectNames(source);

        assertEquals(List.of("Counter", "Store"), objectNames);
    }

    public void testQinSourceStructureFindsObjectMembersFromSharedTokenStream() {
        String source = """
                export object Counter {
                  value = 41
                  total = value + 1

                  next() {
                    return this.value
                  }
                }
                """;

        List<QinSourceStructure.ObjectDeclaration> declarations = QinSourceStructure.parse(source).objectDeclarations();

        assertEquals(1, declarations.size());
        QinSourceStructure.ObjectDeclaration counter = declarations.get(0);
        assertEquals("Counter", counter.name());
        assertEquals(List.of("value", "total"), counter.fieldNames());
        assertEquals(List.of("next"), counter.methodNames());
        assertTrue(counter.keywordRange().isPresent());
        assertTrue(counter.nameRange().isPresent());
        assertTrue(counter.bodyRange().isPresent());
        assertTrue(counter.fields().get(0).nameRange().isPresent());
        assertTrue(counter.methods().get(0).nameRange().isPresent());
        assertTrue(counter.nameRange().startsAt(source.indexOf("Counter")));
        assertTrue(counter.bodyRange().startsAfter(source.indexOf("Counter")));
        assertTrue(counter.bodyRange().containsOffset(source.indexOf("return this.value")));
        assertFalse(counter.bodyRange().containsOffset(counter.bodyRange().endOffset()));

        QinSourceStructure structure = QinSourceStructure.parse(source);
        QinSourceStructure.ObjectDeclaration namedCounter = structure.objectDeclarationNamed("Counter");
        QinSourceStructure.ObjectDeclaration offsetCounter = structure.objectDeclarationAtKeywordOffset(source.indexOf("object"));
        assertNotNull("Qin source structure should find object declarations by name", namedCounter);
        assertNotNull("Qin source structure should find object declarations by keyword offset", offsetCounter);
        assertEquals("Counter", namedCounter.name());
        assertEquals("Counter", offsetCounter.name());
    }

    public void testQinSourceStructureCarriesMethodBodyRange() {
        String source = """
                object Counter {
                  next(): Int {
                    return 42
                  }
                }
                """;

        QinSourceStructure.ObjectDeclaration counter = QinSourceStructure.parse(source)
                .objectDeclarations()
                .get(0);
        QinSourceStructure.MemberDeclaration method = counter.methods().get(0);

        assertEquals("next", method.name());
        assertTrue("Qin method declarations should preserve method body range", method.bodyRange().isPresent());
        assertEquals(source.indexOf("{", source.indexOf("next")), method.bodyRange().startOffset());
        assertEquals(source.lastIndexOf("  }") + "  }".length(), method.bodyRange().endOffset());
    }
    public void testQinSourceStructureRequiresMethodBodyFromSharedStructureFacts() {
        String source = """
                object Counter {
                  value = helper()

                  next() {
                    return this.value
                  }
                }
                """;

        List<QinSourceStructure.ObjectDeclaration> declarations = QinSourceStructure.parse(source).objectDeclarations();

        assertEquals(1, declarations.size());
        QinSourceStructure.ObjectDeclaration counter = declarations.get(0);
        assertEquals(List.of("value"), counter.fieldNames());
        assertEquals(List.of("next"), counter.methodNames());
    }

    public void testQinSourceStructureFindsImportSpecifiersFromSharedTokenStream() {
        String source = """
                import { Greeter as G, Counter } from "java:demo"
                """;

        QinSourceStructure structure = QinSourceStructure.parse(source);

        assertEquals(1, structure.importDeclarations().size());
        QinSourceStructure.ImportDeclaration declaration = structure.importDeclarations().get(0);
        assertEquals("java:demo", declaration.moduleSpecifier());
        assertTrue(declaration.keywordRange().isPresent());
        assertTrue(declaration.declarationRange().isPresent());
        assertTrue(declaration.moduleSpecifierRange().isPresent());
        assertEquals(2, declaration.specifiers().size());
        assertEquals("Greeter", declaration.specifiers().get(0).exportedName());
        assertEquals("G", declaration.specifiers().get(0).localName());
        assertTrue(declaration.specifiers().get(0).exportedNameRange().isPresent());
        assertTrue(declaration.specifiers().get(0).localNameRange().isPresent());
        assertEquals("Counter", declaration.specifiers().get(1).exportedName());
        assertEquals("Counter", declaration.specifiers().get(1).localName());
    }

    public void testQinSourceStructureFindsImportDeclarationRangesWithoutSemicolons() {
        String source = """
                import { Greeter as G } from "java:demo"
                import { Counter as C } from "./Counter.qin"
                """;

        QinSourceStructure structure = QinSourceStructure.parse(source);

        assertEquals(2, structure.importDeclarations().size());
        QinSourceStructure.ImportDeclaration first = structure.importDeclarations().get(0);
        QinSourceStructure.ImportDeclaration second = structure.importDeclarations().get(1);
        assertEquals(source.indexOf("import { Greeter"), first.declarationRange().startOffset());
        assertEquals(source.indexOf("\"java:demo\"") + "\"java:demo\"".length(), first.declarationRange().endOffset());
        assertEquals(source.indexOf("import { Counter"), second.declarationRange().startOffset());
        assertEquals(source.indexOf("\"./Counter.qin\"") + "\"./Counter.qin\"".length(),
                second.declarationRange().endOffset());
    }

    public void testQinSourceStructureStopsIncompleteImportAtNextStatement() {
        String source = """
                import { Greeter }
                const as = "local"
                """;

        QinSourceStructure structure = QinSourceStructure.parse(source);

        assertEquals(1, structure.importDeclarations().size());
        QinSourceStructure.ImportDeclaration declaration = structure.importDeclarations().get(0);
        assertEquals(source.indexOf("import { Greeter }"), declaration.declarationRange().startOffset());
        assertEquals(source.indexOf("}") + 1, declaration.declarationRange().endOffset());
    }
    public void testQinParserBuildsStructuredPsiForJavaInterop() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { Greeter as G } from "java:demo"

                const message = G.greet("Qin")
                """);

        assertTrue("Qin PSI should include structured import declarations",
                hasPsiElementType(myFixture.getFile(), QinTokenTypes.IMPORT_DECLARATION));
        assertTrue("Qin PSI should include structured import specifiers",
                hasPsiElementType(myFixture.getFile(), QinTokenTypes.IMPORT_SPECIFIER));
        assertTrue("Qin PSI should include structured member access nodes",
                hasPsiElementType(myFixture.getFile(), QinTokenTypes.MEMBER_ACCESS));
    }

    public void testQinParserUsesSharedImportSpecifierPsiForJavaAndQinImports() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { Greeter as G } from "java:demo"
                import { Counter as C } from "./Counter.qin"
                """);

        assertEquals("Qin PSI should use one import specifier node type for Java and Qin module imports",
                2,
                countPsiElementType(myFixture.getFile(), QinTokenTypes.IMPORT_SPECIFIER));
    }

    public void testQinParserKeepsNewlineSeparatedImportsDistinctWithoutSemicolons() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { Greeter as G } from "java:demo"
                import { Counter as C } from "./Counter.qin"
                """);

        List<PsiElement> declarations = descendantsOfType(
                myFixture.getFile(),
                QinTokenTypes.IMPORT_DECLARATION);

        assertEquals(2, declarations.size());
        assertEquals("import { Greeter as G } from \"java:demo\"", declarations.get(0).getText().trim());
        assertEquals("import { Counter as C } from \"./Counter.qin\"", declarations.get(1).getText().trim());
    }

    public void testQinParserStopsIncompleteImportDeclarationAtNextStatement() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { Greeter }
                const as = "local"
                """);

        List<PsiElement> declarations = descendantsOfType(
                myFixture.getFile(),
                QinTokenTypes.IMPORT_DECLARATION);

        assertEquals(1, declarations.size());
        assertEquals("import { Greeter }", declarations.get(0).getText().trim());
    }
    public void testQinParserUsesSourceStructureOffsetsForImportSpecifiers() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { Greeter as G, Counter } from "java:demo"
                """);

        assertEquals("Only exported import names should start IMPORT_SPECIFIER nodes",
                2,
                countPsiElementType(myFixture.getFile(), QinTokenTypes.IMPORT_SPECIFIER));
        assertEquals("Only local aliases should become IMPORT_ALIAS_NAME nodes",
                1,
                countPsiElementType(myFixture.getFile(), QinTokenTypes.IMPORT_ALIAS_NAME));
    }

    public void testQinParserBuildsImportAliasNamePsi() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { Greeter as G } from "java:demo"
                import { Counter as C } from "./Counter.qin"
                """);

        assertEquals("Qin PSI should expose local import aliases as named PSI nodes",
                2,
                countPsiElementType(myFixture.getFile(), QinTokenTypes.IMPORT_ALIAS_NAME));
    }

    public void testQinImportBindingsUseCurrentImportSpecifierPsi() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { Greeter as Counter, Counter as C } from "./Counter.qin"
                """);

        PsiElement exportedCounter = findFirstChildOfText(
                findImportSpecifierStartingWith(myFixture.getFile(), "Counter"),
                QinTokenTypes.REFERENCE_IDENTIFIER,
                "Counter");
        QinImportBindings.ImportBinding binding = QinImportBindings.findForSpecifierElement(exportedCounter);

        assertNotNull("Qin import binding should be read from the current IMPORT_SPECIFIER PSI node", binding);
        assertEquals("./Counter.qin", binding.moduleSpecifier());
        assertEquals("Counter", binding.exportedName());
        assertEquals("C", binding.localName());

        PsiElement localCounterAlias = findFirstChildOfText(
                findImportSpecifierStartingWith(myFixture.getFile(), "Counter"),
                QinTokenTypes.IMPORT_ALIAS_NAME,
                "C");
        assertNotNull("Local alias PSI node should be available", localCounterAlias);
        QinImportBindings.ImportBinding aliasBinding = QinImportBindings.findForSpecifierElement(localCounterAlias);

        assertNotNull("Qin import binding should also be read from the local alias PSI node", aliasBinding);
        assertEquals("./Counter.qin", aliasBinding.moduleSpecifier());
        assertEquals("Counter", aliasBinding.exportedName());
        assertEquals("C", aliasBinding.localName());
    }

    public void testQinImportBindingsCollectAliasedImportsFromSourceStructure() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { Greeter as G } from "java:demo"
                import { Counter as C } from "./Counter.qin"
                """);

        List<QinImportBindings.ImportBinding> bindings = QinImportBindings.collect(myFixture.getFile());

        assertEquals(2, bindings.size());
        assertEquals("java:demo", bindings.get(0).moduleSpecifier());
        assertEquals("Greeter", bindings.get(0).exportedName());
        assertEquals("G", bindings.get(0).localName());
        assertEquals("./Counter.qin", bindings.get(1).moduleSpecifier());
        assertEquals("Counter", bindings.get(1).exportedName());
        assertEquals("C", bindings.get(1).localName());
    }

    public void testQinImportBindingsFindAliasNameFromSourceStructureRange() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { C } from "./C.qin"
                import { Counter as C } from "./Counter.qin"

                const value = C.value
                """);

        String text = myFixture.getFile().getText();
        PsiElement usage = myFixture.getFile().findElementAt(text.lastIndexOf("C.value"));
        assertNotNull("Qin alias usage token should be present", usage);

        PsiElement aliasName = QinImportBindings.findAliasName(usage, "C");

        assertNotNull("Qin alias lookup should use the alias source range", aliasName);
        assertEquals(QinTokenTypes.IMPORT_ALIAS_NAME, aliasName.getNode().getElementType());
        assertEquals(text.indexOf("as C") + "as ".length(), aliasName.getTextRange().getStartOffset());
    }

    public void testQinPsiTreeBridgesSourceRangesToStructuredPsiWrappers() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { Counter as C } from "./Counter.qin"

                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }
                """);

        QinSourceStructure structure = QinSourceStructure.parse(myFixture.getFile().getText());
        QinSourceStructure.ImportSpecifier specifier = structure.importDeclarations().get(0).specifiers().get(0);
        QinSourceStructure.ObjectDeclaration object = structure.objectDeclarations().get(0);
        QinSourceStructure.MemberDeclaration field = object.fields().get(0);
        QinSourceStructure.MemberDeclaration method = object.methods().get(0);

        assertEquals(QinTokenTypes.IMPORT_ALIAS_NAME, QinPsiTree.elementAtRangeOrParentOfType(
                myFixture.getFile(), specifier.localNameRange(), QinTokenTypes.IMPORT_ALIAS_NAME).getNode().getElementType());
        assertEquals(QinTokenTypes.OBJECT_NAME, QinPsiTree.elementAtRangeOrParentOfType(
                myFixture.getFile(), object.nameRange(), QinTokenTypes.OBJECT_NAME).getNode().getElementType());
        assertEquals(QinTokenTypes.FIELD_NAME, QinPsiTree.elementAtRangeOrParentOfType(
                myFixture.getFile(), field.nameRange(), QinTokenTypes.FIELD_NAME).getNode().getElementType());
        assertEquals(QinTokenTypes.METHOD_NAME, QinPsiTree.elementAtRangeOrParentOfType(
                myFixture.getFile(), method.nameRange(), QinTokenTypes.METHOD_NAME).getNode().getElementType());
    }

    public void testQinModuleSpecifierFactsClassifyJavaAndQinImports() {
        assertEquals("demo", QinModuleSpecifierFacts.javaModuleName("java:demo"));
        assertEquals("java.util", QinModuleSpecifierFacts.javaModuleName("java:java.util"));
        assertNull(QinModuleSpecifierFacts.javaModuleName("./Counter.qin"));
        assertTrue(QinModuleSpecifierFacts.isQinModuleSpecifier("./Counter.qin"));
        assertTrue(QinModuleSpecifierFacts.isQinModuleSpecifier("..\\Counter"));
        assertFalse(QinModuleSpecifierFacts.isQinModuleSpecifier("java:demo"));
        assertFalse(QinModuleSpecifierFacts.isQinModuleSpecifier("./components/"));
    }

    public void testQinParserBuildsStructuredPsiForObjectDeclaration() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41

                  next() {
                    return value + 1
                  }
                }
                """);

        assertTrue("Qin PSI should include object declaration nodes",
                hasPsiElementType(myFixture.getFile(), QinTokenTypes.OBJECT_DECLARATION));
        assertTrue("Qin PSI should include object name nodes",
                hasPsiElementType(myFixture.getFile(), QinTokenTypes.OBJECT_NAME));
    }

    public void testQinParserBuildsStructuredPsiForObjectMethodDeclaration() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next() {
                    return 42
                  }
                }
                """);

        assertTrue("Qin PSI should include object method declaration nodes",
                hasPsiElementType(myFixture.getFile(), QinTokenTypes.METHOD_DECLARATION));
        assertTrue("Qin PSI should include object method name nodes",
                hasPsiElementType(myFixture.getFile(), QinTokenTypes.METHOD_NAME));
    }

    public void testQinParserUsesSharedStructureFactsForObjectMethodDeclaration() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  Next() {
                    return 42
                  }
                }
                """);

        PsiElement methodName = findPsiElementType(myFixture.getFile(), QinTokenTypes.METHOD_NAME);

        assertNotNull("Qin PSI should classify method declarations through shared structure facts", methodName);
        assertEquals("Next", methodName.getText());
    }

    public void testQinParserUsesSourceStructureOffsetsForObjectMembers() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = helper()

                  next() {
                    return this.value
                  }
                }
                """);

        PsiElement fieldName = findPsiElementType(myFixture.getFile(), QinTokenTypes.FIELD_NAME);
        PsiElement methodName = findPsiElementType(myFixture.getFile(), QinTokenTypes.METHOD_NAME);

        assertNotNull("Qin field name PSI should come from source-structure member offset", fieldName);
        assertNotNull("Qin method name PSI should come from source-structure member offset", methodName);
        assertEquals("value", fieldName.getText());
        assertEquals("next", methodName.getText());
        assertEquals("Only the object method declaration should become METHOD_NAME",
                1,
                countPsiElementType(myFixture.getFile(), QinTokenTypes.METHOD_NAME));
    }

    public void testQinParserUsesSourceStructureBodyRangeForObjectDeclaration() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter extends Base {
                  value = 41
                }
                """);

        PsiElement fieldName = findPsiElementType(myFixture.getFile(), QinTokenTypes.FIELD_NAME);

        assertNotNull("Qin object PSI should consume the SourceStructure body range after object metadata", fieldName);
        assertEquals("value", fieldName.getText());
    }
    public void testQinParserUsesSourceStructureBodyRangeForMethodDeclaration() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next(): Int {
                    return this.value
                  }
                }
                """);

        List<PsiElement> methods = descendantsOfType(
                myFixture.getFile(),
                QinTokenTypes.METHOD_DECLARATION);

        assertEquals(1, methods.size());
        assertTrue("Method PSI should include the method body after signature metadata: " + methods.get(0).getText(),
                methods.get(0).getText().contains("return this.value"));
    }
    public void testQinParserBuildsStructuredPsiForObjectFieldDeclaration() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41
                }
                """);

        assertTrue("Qin PSI should include object field declaration nodes",
                hasPsiElementType(myFixture.getFile(), QinTokenTypes.FIELD_DECLARATION));
        assertTrue("Qin PSI should include object field name nodes",
                hasPsiElementType(myFixture.getFile(), QinTokenTypes.FIELD_NAME));
    }

    public void testQinParserBuildsStructuredPsiForThisMemberAccess() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }
                """);

        PsiElement thisAccess = descendantsOfType(myFixture.getFile(), QinTokenTypes.MEMBER_ACCESS).stream()
                .filter(element -> element.getText().contains("this.value"))
                .findFirst()
                .orElse(null);

        assertNotNull("Qin PSI should structure this.member access for IDEA references", thisAccess);
        PsiElement valueReference = findFirstChildOfText(thisAccess, QinTokenTypes.REFERENCE_IDENTIFIER, "value");
        assertNotNull("Qin PSI should wrap this.member name as a REFERENCE_IDENTIFIER", valueReference);
    }

    public void testQinPsiTokenStreamUsesSharedFactsForThisMemberBoundaries() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }

                  again() {
                    return this.next()
                  }
                }
                """);

        String text = myFixture.getEditor().getDocument().getText();
        PsiElement value = myFixture.getFile().findElementAt(text.indexOf("value", text.indexOf("this.value")));
        PsiElement next = myFixture.getFile().findElementAt(text.indexOf("next", text.indexOf("this.next")));
        PsiElement valueReference = QinReferenceElements.referenceElement(value);
        PsiElement nextReference = QinReferenceElements.referenceElement(next);

        assertNotNull("this.value should expose a Qin reference element", valueReference);
        assertNotNull("this.next() should expose a Qin reference element", nextReference);
        assertEquals("this", QinPsiTokenStream.previousQualifierName(valueReference));
        assertEquals("this", QinPsiTokenStream.previousQualifierName(nextReference));
        assertFalse("this.value should be classified as field access",
                QinPsiTokenStream.isFollowedByCallParenthesis(valueReference));
        assertTrue("this.next() should be classified as method call",
                QinPsiTokenStream.isFollowedByCallParenthesis(nextReference));
    }

    public void testQinParserKeepsJavaMemberReferencesInsideObjectDeclaration() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                export object Counter {
                  message() {
                    return Greeter.gr<caret>eet("Qin")
                  }
                }
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        assertTrue("Qin PSI should include object declaration nodes",
                hasPsiElementType(myFixture.getFile(), QinTokenTypes.OBJECT_DECLARATION));
        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Qin Java member reference inside object declaration was not registered", reference);
        PsiMethod method = assertInstanceOf(reference.resolve(), PsiMethod.class);
        assertEquals("greet", method.getName());
        assertEquals("demo.Greeter", method.getContainingClass().getQualifiedName());
    }

    public void testQinObjectReferenceResolvesToObjectName() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next() {
                    return 42
                  }
                }

                const value = Counter.next()
                """);

        String text = myFixture.getEditor().getDocument().getText();
        PsiReference reference = myFixture.getFile().findReferenceAt(text.indexOf("Counter.next"));
        assertNotNull("Qin object reference was not registered", reference);
        PsiElement objectName = assertInstanceOf(reference.resolve(), PsiElement.class);
        assertEquals(QinTokenTypes.OBJECT_NAME, objectName.getNode().getElementType());
        assertEquals("Counter", objectName.getText());
        assertSingleQinObjectReference(reference.getElement());
    }

    public void testQinObjectReferenceParticipatesInReferencesSearch() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next() {
                    return 42
                  }
                }

                const value = Counter.next()
                """);

        PsiElement objectName = findPsiElementType(myFixture.getFile(), QinTokenTypes.OBJECT_NAME);
        assertNotNull("Qin object name PSI was not built", objectName);
        Collection<PsiReference> references = ReferencesSearch.search(
                objectName,
                GlobalSearchScope.allScope(getProject())).findAll();
        assertTrue("Find Usages should include Qin object reference: " + describeReferences(references),
                references.stream().anyMatch(item -> item.getElement().getContainingFile() instanceof QinPsiFile
                        && "Counter".equals(item.getElement().getText())
                        && item.getElement() != objectName));
    }

    public void testQinObjectReferenceResolvesAcrossRelativeImport() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter } from "./Counter.qin"

                const value = Cou<caret>nter.value
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Imported Qin object reference was not registered", reference);
        PsiElement objectName = assertInstanceOf(reference.resolve(), PsiElement.class);
        assertEquals(QinTokenTypes.OBJECT_NAME, objectName.getNode().getElementType());
        assertEquals("Counter", objectName.getText());
        assertEquals("Counter.qin", objectName.getContainingFile().getName());
        assertSingleQinObjectReference(reference.getElement());
    }

    public void testQinObjectGoToDeclarationTargetsImportedObjectName() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter } from "./Counter.qin"

                const value = Cou<caret>nter.value
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiElement objectName = assertInstanceOf(myFixture.getElementAtCaret(), PsiElement.class);

        assertEquals(QinTokenTypes.OBJECT_NAME, objectName.getNode().getElementType());
        assertEquals("Counter", objectName.getText());
        assertEquals("Counter.qin", objectName.getContainingFile().getName());
    }

    public void testQinObjectAliasedImportSpecifierReferencesOnlyExportedName() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Cou<caret>nter as C } from "./Counter.qin"

                const value = C.value
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference exportedReference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Aliased Qin exported import name should resolve to remote object", exportedReference);
        PsiElement objectName = assertInstanceOf(exportedReference.resolve(), PsiElement.class);
        assertEquals(QinTokenTypes.OBJECT_NAME, objectName.getNode().getElementType());
        assertEquals("Counter.qin", objectName.getContainingFile().getName());
        assertSingleQinObjectReference(exportedReference.getElement());

        String text = myFixture.getEditor().getDocument().getText();
        PsiReference aliasReference = myFixture.getFile().findReferenceAt(text.indexOf("C }"));
        assertNull("Aliased Qin local import name should be a local declaration, not a remote object reference",
                aliasReference);
    }

    public void testQinAliasedImportSpecifierGoToDeclarationTargetsRemoteObjectName() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Cou<caret>nter as C } from "./Counter.qin"

                const value = C.value
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiElement objectName = assertInstanceOf(myFixture.getElementAtCaret(), PsiElement.class);

        assertEquals(QinTokenTypes.OBJECT_NAME, objectName.getNode().getElementType());
        assertEquals("Counter", objectName.getText());
        assertEquals("Counter.qin", objectName.getContainingFile().getName());
    }

    public void testQinAliasedImportSpecifierDoesNotReferenceLocalAliasName() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C<caret> } from "./Counter.qin"

                const value = C.value
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNull("Aliased Qin local import name should be a local declaration, not a remote object reference",
                reference);
    }

    public void testQinImportAliasRenameProcessorUpdatesQinAliasUsages() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C } from "./Counter.qin"

                const value = C.value
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiElement aliasName = findPsiElementType(myFixture.getFile(), QinTokenTypes.IMPORT_ALIAS_NAME);
        assertNotNull("Qin import alias name PSI was not built", aliasName);
        new RenameProcessor(getProject(), aliasName, "CounterAlias", false, false).run();

        String text = myFixture.getEditor().getDocument().getText();
        assertTrue("Qin alias rename should update import alias declaration: " + text,
                text.contains("import { Counter as CounterAlias }"));
        assertTrue("Qin alias rename should update local alias usages: " + text,
                text.contains("CounterAlias.value"));
    }

    public void testQinImportAliasReferenceParticipatesInReferencesSearch() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C } from "./Counter.qin"

                const value = C.value
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiElement aliasName = findPsiElementType(myFixture.getFile(), QinTokenTypes.IMPORT_ALIAS_NAME);
        assertNotNull("Qin import alias name PSI was not built", aliasName);
        Collection<PsiReference> references = ReferencesSearch.search(
                aliasName,
                GlobalSearchScope.allScope(getProject())).findAll();
        assertTrue("Find Usages should include local Qin alias usage: " + describeReferences(references),
                references.stream().anyMatch(item -> item.getElement().getContainingFile() instanceof QinPsiFile
                        && "C".equals(item.getElement().getText())
                        && item.getElement() != aliasName));
    }

    public void testQinImportAliasUsageGoToDeclarationTargetsAliasName() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C } from "./Counter.qin"

                const value = <caret>C.value
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiElement aliasName = assertInstanceOf(myFixture.getElementAtCaret(), PsiElement.class);

        assertEquals(QinTokenTypes.IMPORT_ALIAS_NAME, aliasName.getNode().getElementType());
        assertEquals("C", aliasName.getText());
        assertSame(myFixture.getFile(), aliasName.getContainingFile());
    }

    public void testQinObjectNameStubIndexFindsObjectDeclarationFile() {
        var counterFile = myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        myFixture.addFileToProject("src/main/Store.qin", """
                export object Store {
                  value = 1
                }
                """);

        Collection<QinPsiFile> files = StubIndex.getElements(
                QinObjectNameStubIndex.KEY,
                "Counter",
                getProject(),
                GlobalSearchScope.allScope(getProject()),
                QinPsiFile.class);

        assertTrue("Qin object name StubIndex should locate Counter.qin: " + describePsiFiles(files),
                files.stream().anyMatch(file -> file.getVirtualFile().equals(counterFile.getVirtualFile())));
    }

    public void testQinObjectMemberStubIndexesFindObjectDeclarationFile() {
        var counterFile = myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }
                """);

        Collection<QinPsiFile> fieldFiles = StubIndex.getElements(
                QinObjectFieldNameStubIndex.KEY,
                QinSourceStructure.objectMemberKey("Counter", "value"),
                getProject(),
                GlobalSearchScope.allScope(getProject()),
                QinPsiFile.class);
        Collection<QinPsiFile> methodFiles = StubIndex.getElements(
                QinObjectMethodNameStubIndex.KEY,
                QinSourceStructure.objectMemberKey("Counter", "next"),
                getProject(),
                GlobalSearchScope.allScope(getProject()),
                QinPsiFile.class);

        assertTrue("Qin object field StubIndex should locate Counter.qin: " + describePsiFiles(fieldFiles),
                fieldFiles.stream().anyMatch(file -> file.getVirtualFile().equals(counterFile.getVirtualFile())));
        assertTrue("Qin object method StubIndex should locate Counter.qin: " + describePsiFiles(methodFiles),
                methodFiles.stream().anyMatch(file -> file.getVirtualFile().equals(counterFile.getVirtualFile())));
    }

    public void testQinObjectMemberStubIndexesDoNotExposeBareMemberNames() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }
                """);

        Collection<QinPsiFile> bareFieldFiles = StubIndex.getElements(
                QinObjectFieldNameStubIndex.KEY,
                "value",
                getProject(),
                GlobalSearchScope.allScope(getProject()),
                QinPsiFile.class);
        Collection<QinPsiFile> bareMethodFiles = StubIndex.getElements(
                QinObjectMethodNameStubIndex.KEY,
                "next",
                getProject(),
                GlobalSearchScope.allScope(getProject()),
                QinPsiFile.class);

        assertTrue("Qin object field StubIndex must use object-qualified keys, not bare field names: "
                + describePsiFiles(bareFieldFiles), bareFieldFiles.isEmpty());
        assertTrue("Qin object method StubIndex must use object-qualified keys, not bare method names: "
                + describePsiFiles(bareMethodFiles), bareMethodFiles.isEmpty());
    }

    public void testQinObjectReferenceAcrossRelativeImportParticipatesInReferencesSearch() {
        var counterFile = myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  next() {
                    return 42
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter } from "./Counter.qin"

                const value = Counter.next()
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiFile counterPsi = PsiManager.getInstance(getProject()).findFile(counterFile.getVirtualFile());
        assertNotNull("Counter.qin PSI was not available", counterPsi);
        PsiElement objectName = findPsiElementType(counterPsi, QinTokenTypes.OBJECT_NAME);
        assertNotNull("Imported Qin object name PSI was not built", objectName);
        Collection<PsiReference> references = ReferencesSearch.search(
                objectName,
                GlobalSearchScope.allScope(getProject())).findAll();
        assertTrue("Find Usages should include imported Qin object reference: " + describeReferences(references),
                references.stream().anyMatch(item -> item.getElement().getContainingFile() instanceof QinPsiFile
                        && "App.qin".equals(item.getElement().getContainingFile().getName())
                        && "Counter".equals(item.getElement().getText())
                        && item.getElement() != objectName));
    }

    public void testQinObjectReferencesSearchIncludesAliasedExportedImportName() {
        var counterFile = myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C } from "./Counter.qin"

                const value = C.value
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiFile counterPsi = PsiManager.getInstance(getProject()).findFile(counterFile.getVirtualFile());
        assertNotNull("Counter.qin PSI was not available", counterPsi);
        PsiElement objectName = findPsiElementType(counterPsi, QinTokenTypes.OBJECT_NAME);
        assertNotNull("Imported Qin object name PSI was not built", objectName);
        Collection<PsiReference> references = ReferencesSearch.search(
                objectName,
                GlobalSearchScope.allScope(getProject())).findAll();
        assertTrue("Find Usages should include aliased exported Qin import name: " + describeReferences(references),
                references.stream().anyMatch(item -> item.getElement().getContainingFile() instanceof QinPsiFile
                        && "App.qin".equals(item.getElement().getContainingFile().getName())
                        && "Counter".equals(item.getElement().getText())));
        assertReferencesMissingQinElement(
                references,
                "App.qin",
                "C",
                "Find Usages for the remote Qin object must not include the local import alias");
    }

    public void testQinObjectRenameProcessorUpdatesRelativeImportUsages() {
        var counterFile = myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  next() {
                    return 42
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter } from "./Counter.qin"

                const value = Counter.next()
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiFile counterPsi = PsiManager.getInstance(getProject()).findFile(counterFile.getVirtualFile());
        assertNotNull("Counter.qin PSI was not available", counterPsi);
        PsiElement objectName = findPsiElementType(counterPsi, QinTokenTypes.OBJECT_NAME);
        assertNotNull("Imported Qin object name PSI was not built", objectName);
        new RenameProcessor(getProject(), objectName, "Store", false, false).run();

        assertTrue("Qin object rename should update imported declaration: " + counterPsi.getText(),
                counterPsi.getText().contains("object Store"));
        String appText = myFixture.getEditor().getDocument().getText();
        assertTrue("Qin object rename should update relative import specifier: " + appText,
                appText.contains("import { Store } from \"./Counter.qin\""));
        assertTrue("Qin object rename should update cross-file usages: " + appText,
                appText.contains("Store.next()"));
        assertFalse("Qin object rename should remove old cross-file usages: " + appText,
                appText.contains("Counter.next()"));
    }

    public void testQinObjectRenameProcessorPreservesRelativeImportAliasUsages() {
        var counterFile = myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  next() {
                    return 42
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C } from "./Counter.qin"

                const value = C.next()
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiFile counterPsi = PsiManager.getInstance(getProject()).findFile(counterFile.getVirtualFile());
        assertNotNull("Counter.qin PSI was not available", counterPsi);
        PsiElement objectName = findPsiElementType(counterPsi, QinTokenTypes.OBJECT_NAME);
        assertNotNull("Imported Qin object name PSI was not built", objectName);
        new RenameProcessor(getProject(), objectName, "Store", false, false).run();

        String appText = myFixture.getEditor().getDocument().getText();
        assertTrue("Qin object rename should update the imported exported name: " + appText,
                appText.contains("import { Store as C } from \"./Counter.qin\""));
        assertTrue("Qin object rename should preserve local alias usages: " + appText,
                appText.contains("C.next()"));
        assertFalse("Qin object rename should not rewrite local alias usages to the exported name: " + appText,
                appText.contains("Store.next()"));
    }

    public void testQinObjectNameSupportsIdeaRenamePrimitive() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next() {
                    return 42
                  }
                }
                """);

        PsiElement objectName = findPsiElementType(myFixture.getFile(), QinTokenTypes.OBJECT_NAME);
        PsiNameIdentifierOwner namedObject = assertInstanceOf(objectName, PsiNameIdentifierOwner.class);
        WriteCommandAction.runWriteCommandAction(
                getProject(),
                (Runnable) () -> namedObject.setName("Store"));

        assertTrue("Qin object rename should update the declaration name: "
                        + myFixture.getEditor().getDocument().getText(),
                myFixture.getEditor().getDocument().getText().contains("object Store"));
    }

    public void testQinObjectRenameProcessorUpdatesReferences() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next() {
                    return 42
                  }
                }

                const value = Counter.next()
                """);

        PsiElement objectName = findPsiElementType(myFixture.getFile(), QinTokenTypes.OBJECT_NAME);
        assertNotNull("Qin object name PSI was not built", objectName);
        new RenameProcessor(getProject(), objectName, "Store", false, false).run();

        String text = myFixture.getEditor().getDocument().getText();
        assertTrue("Qin object rename should update declaration: " + text, text.contains("object Store"));
        assertTrue("Qin object rename should update usages: " + text, text.contains("Store.next()"));
        assertFalse("Qin object rename should remove old usage: " + text, text.contains("Counter.next()"));
    }

    public void testQinObjectMethodReferenceResolvesToMethodName() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next() {
                    return 42
                  }
                }

                const value = Counter.ne<caret>xt()
                """);

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Qin object method reference was not registered", reference);
        PsiElement methodName = assertInstanceOf(reference.resolve(), PsiElement.class);
        assertEquals(QinTokenTypes.METHOD_NAME, methodName.getNode().getElementType());
        assertEquals("next", methodName.getText());
        assertSingleQinObjectMethodReference(reference.getElement());
    }

    public void testQinObjectMethodGoToDeclarationTargetsSameFileMethodName() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next() {
                    return 42
                  }
                }

                const value = Counter.ne<caret>xt()
                """);

        PsiElement methodName = assertInstanceOf(myFixture.getElementAtCaret(), PsiElement.class);

        assertEquals(QinTokenTypes.METHOD_NAME, methodName.getNode().getElementType());
        assertEquals("next", methodName.getText());
        assertSame(myFixture.getFile(), methodName.getContainingFile());
    }
    public void testQinObjectMethodReferenceResolvesAcrossAliasedRelativeImport() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  next() {
                    return 42
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C } from "./Counter.qin"

                const value = C.ne<caret>xt()
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Imported Qin object method reference was not registered", reference);
        PsiElement methodName = assertInstanceOf(reference.resolve(), PsiElement.class);
        assertEquals(QinTokenTypes.METHOD_NAME, methodName.getNode().getElementType());
        assertEquals("next", methodName.getText());
        assertEquals("Counter.qin", methodName.getContainingFile().getName());
        assertSingleQinObjectMethodReference(reference.getElement());
    }

    public void testQinObjectMethodGoToDeclarationTargetsImportedMethodName() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  next() {
                    return 42
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C } from "./Counter.qin"

                const value = C.ne<caret>xt()
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiElement methodName = assertInstanceOf(myFixture.getElementAtCaret(), PsiElement.class);

        assertEquals(QinTokenTypes.METHOD_NAME, methodName.getNode().getElementType());
        assertEquals("next", methodName.getText());
        assertEquals("Counter.qin", methodName.getContainingFile().getName());
    }

    public void testQinThisMethodReferenceResolvesToCurrentObjectMethodName() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next() {
                    return this.ne<caret>xt()
                  }
                }
                """);

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Qin this method reference was not registered", reference);
        PsiElement methodName = assertInstanceOf(reference.resolve(), PsiElement.class);
        assertEquals(QinTokenTypes.METHOD_NAME, methodName.getNode().getElementType());
        assertEquals("next", methodName.getText());
        assertSingleQinObjectMethodReference(reference.getElement());
    }

    public void testQinThisMethodGoToDeclarationTargetsCurrentObjectMethodName() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next() {
                    return this.ne<caret>xt()
                  }
                }
                """);

        PsiElement methodName = assertInstanceOf(myFixture.getElementAtCaret(), PsiElement.class);

        assertEquals(QinTokenTypes.METHOD_NAME, methodName.getNode().getElementType());
        assertEquals("next", methodName.getText());
        assertSame(myFixture.getFile(), methodName.getContainingFile());
    }

    public void testQinObjectMethodReferenceParticipatesInReferencesSearch() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next() {
                    return this.next()
                  }
                }

                const value = Counter.next()
                """);

        PsiElement methodName = findPsiElementType(myFixture.getFile(), QinTokenTypes.METHOD_NAME);
        assertNotNull("Qin method name PSI was not built", methodName);
        Collection<PsiReference> references = ReferencesSearch.search(
                methodName,
                GlobalSearchScope.allScope(getProject())).findAll();
        assertTrue("Find Usages should include Qin object and this method references: " + describeReferences(references),
                references.stream().filter(item -> item.getElement().getContainingFile() instanceof QinPsiFile
                        && "next".equals(item.getElement().getText())
                        && item.getElement() != methodName).count() >= 2);
    }

    public void testQinObjectMethodRenameProcessorUpdatesReferences() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next() {
                    return this.next()
                  }
                }

                const value = Counter.next()
                """);

        PsiElement methodName = findPsiElementType(myFixture.getFile(), QinTokenTypes.METHOD_NAME);
        assertNotNull("Qin method name PSI was not built", methodName);
        new RenameProcessor(getProject(), methodName, "advance", false, false).run();

        String text = myFixture.getEditor().getDocument().getText();
        assertTrue("Qin method rename should update declaration: " + text, text.contains("advance()"));
        assertTrue("Qin method rename should update this usage: " + text, text.contains("this.advance()"));
        assertTrue("Qin method rename should update object usage: " + text, text.contains("Counter.advance()"));
        assertFalse("Qin method rename should remove old object usage: " + text, text.contains("Counter.next()"));
    }

    public void testQinObjectMethodRenameProcessorPreservesImportAliasQualifier() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  next() {
                    return 42
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C } from "./Counter.qin"

                const value = C.next()
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(
                myFixture.getEditor().getDocument().getText().indexOf("next"));
        assertNotNull("Aliased Qin object method reference was not registered for rename", reference);
        PsiElement methodName = assertInstanceOf(reference.resolve(), PsiElement.class);
        new RenameProcessor(getProject(), methodName, "advance", false, false).run();

        String text = myFixture.getEditor().getDocument().getText();
        assertTrue("Qin method rename should preserve alias qualifier: " + text,
                text.contains("C.advance()"));
        assertTrue("Qin method rename should preserve import alias declaration: " + text,
                text.contains("import { Counter as C }"));
        assertFalse("Qin method rename should not rewrite alias qualifier to exported name: " + text,
                text.contains("Counter.advance()"));
    }

    public void testQinUnresolvedReferenceAnnotatorReportsMissingMethod() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next() {
                    return 42
                  }
                }

                const value = Counter.missing()
                """);

        List<HighlightInfo> errors = myFixture.doHighlighting(HighlightSeverity.ERROR);
        assertHighlightContains(errors, "Unresolved Qin object method Counter.missing");
    }

    public void testQinUnresolvedReferenceInspectionReportsMissingMethod() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next() {
                    return 42
                  }
                }

                const value = Counter.missing()
                """);

        String text = myFixture.getEditor().getDocument().getText();
        PsiElement missingMethod = myFixture.getFile().findElementAt(text.indexOf("missing"));
        assertNotNull("Missing method token should be present", missingMethod);

        ProblemsHolder holder = new ProblemsHolder(
                InspectionManager.getInstance(getProject()),
                myFixture.getFile(),
                true);
        new QinUnresolvedReferenceInspection()
                .buildVisitor(holder, true)
                .visitElement(missingMethod.getParent());

        ProblemDescriptor[] problems = holder.getResultsArray();
        assertEquals(1, problems.length);
        assertEquals("Unresolved Qin object method Counter.missing", problems[0].getDescriptionTemplate());
    }

    public void testQinUnresolvedReferenceInspectionReportsMissingJavaClass() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { MissingGreeter } from "java:demo"

                const message = MissingGreeter.greet("Qin")
                """);

        String text = myFixture.getEditor().getDocument().getText();
        PsiElement missingClass = myFixture.getFile().findElementAt(text.indexOf("MissingGreeter"));
        assertNotNull("Missing Java class token should be present", missingClass);

        ProblemsHolder holder = new ProblemsHolder(
                InspectionManager.getInstance(getProject()),
                myFixture.getFile(),
                true);
        new QinUnresolvedReferenceInspection()
                .buildVisitor(holder, true)
                .visitElement(missingClass.getParent());

        ProblemDescriptor[] problems = holder.getResultsArray();
        assertEquals(1, problems.length);
        assertEquals("Unresolved Java class demo.MissingGreeter", problems[0].getDescriptionTemplate());
    }

    public void testQinUnresolvedReferenceInspectionReportsMissingAliasedJavaClass() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { MissingGreeter as G } from "java:demo"

                const message = G.greet("Qin")
                """);

        String text = myFixture.getEditor().getDocument().getText();
        PsiElement missingClass = myFixture.getFile().findElementAt(text.indexOf("MissingGreeter"));
        assertNotNull("Missing aliased Java class export token should be present", missingClass);

        ProblemsHolder holder = new ProblemsHolder(
                InspectionManager.getInstance(getProject()),
                myFixture.getFile(),
                true);
        new QinUnresolvedReferenceInspection()
                .buildVisitor(holder, true)
                .visitElement(missingClass.getParent());

        ProblemDescriptor[] problems = holder.getResultsArray();
        assertEquals(1, problems.length);
        assertEquals("Unresolved Java class demo.MissingGreeter", problems[0].getDescriptionTemplate());
    }

    public void testQinUnresolvedReferenceInspectionReportsMissingJavaStaticMember() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { Greeter } from "java:demo"

                const message = Greeter.missing("Qin")
                """);

        String text = myFixture.getEditor().getDocument().getText();
        PsiElement missingMember = myFixture.getFile().findElementAt(text.indexOf("missing"));
        assertNotNull("Missing Java member token should be present", missingMember);

        ProblemsHolder holder = new ProblemsHolder(
                InspectionManager.getInstance(getProject()),
                myFixture.getFile(),
                true);
        new QinUnresolvedReferenceInspection()
                .buildVisitor(holder, true)
                .visitElement(missingMember.getParent());

        ProblemDescriptor[] problems = holder.getResultsArray();
        assertEquals(1, problems.length);
        assertEquals("Unresolved static Java member demo.Greeter.missing", problems[0].getDescriptionTemplate());
    }

    public void testQinUnresolvedReferenceInspectionReportsMissingAliasedJavaStaticMember() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { Greeter as G } from "java:demo"

                const message = G.missing("Qin")
                """);

        String text = myFixture.getEditor().getDocument().getText();
        PsiElement missingMember = myFixture.getFile().findElementAt(text.indexOf("missing"));
        assertNotNull("Missing aliased Java member token should be present", missingMember);

        ProblemsHolder holder = new ProblemsHolder(
                InspectionManager.getInstance(getProject()),
                myFixture.getFile(),
                true);
        new QinUnresolvedReferenceInspection()
                .buildVisitor(holder, true)
                .visitElement(missingMember.getParent());

        ProblemDescriptor[] problems = holder.getResultsArray();
        assertEquals(1, problems.length);
        assertEquals("Unresolved static Java member demo.Greeter.missing", problems[0].getDescriptionTemplate());
    }

    public void testQinUnresolvedReferenceAnnotatorKeepsResolvedMethodsClean() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  next() {
                    return 42
                  }
                }

                const value = Counter.next()
                """);

        List<HighlightInfo> errors = myFixture.doHighlighting(HighlightSeverity.ERROR);
        assertHighlightMissing(errors, "Unresolved Qin object method Counter.next");
    }

    public void testQinUnresolvedReferenceAnnotatorIgnoresMissingFieldAccess() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41
                }

                const value = Counter.missing
                """);

        List<HighlightInfo> errors = myFixture.doHighlighting(HighlightSeverity.ERROR);
        assertHighlightMissing(errors, "Unresolved Qin object method Counter.missing");
    }

    public void testQinObjectFieldReferenceResolvesToFieldName() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41
                }

                const value = Counter.val<caret>ue
                """);

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Qin object field reference was not registered", reference);
        PsiElement fieldName = assertInstanceOf(reference.resolve(), PsiElement.class);
        assertEquals(QinTokenTypes.FIELD_NAME, fieldName.getNode().getElementType());
        assertEquals("value", fieldName.getText());
        assertSingleQinObjectFieldReference(reference.getElement());
    }

    public void testQinObjectFieldGoToDeclarationTargetsSameFileFieldName() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41
                }

                const value = Counter.val<caret>ue
                """);

        PsiElement fieldName = assertInstanceOf(myFixture.getElementAtCaret(), PsiElement.class);

        assertEquals(QinTokenTypes.FIELD_NAME, fieldName.getNode().getElementType());
        assertEquals("value", fieldName.getText());
        assertSame(myFixture.getFile(), fieldName.getContainingFile());
    }

    public void testQinObjectFieldReferenceResolvesAcrossRelativeImport() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter } from "./Counter.qin"

                const value = Counter.val<caret>ue
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Imported Qin object field reference was not registered", reference);
        PsiElement fieldName = assertInstanceOf(reference.resolve(), PsiElement.class);
        assertEquals(QinTokenTypes.FIELD_NAME, fieldName.getNode().getElementType());
        assertEquals("value", fieldName.getText());
        assertEquals("Counter.qin", fieldName.getContainingFile().getName());
        assertSingleQinObjectFieldReference(reference.getElement());
    }

    public void testQinObjectFieldReferenceResolvesAcrossAliasedRelativeImport() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C } from "./Counter.qin"

                const value = C.val<caret>ue
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Aliased imported Qin object field reference was not registered", reference);
        PsiElement fieldName = assertInstanceOf(reference.resolve(), PsiElement.class);
        assertEquals(QinTokenTypes.FIELD_NAME, fieldName.getNode().getElementType());
        assertEquals("value", fieldName.getText());
        assertEquals("Counter.qin", fieldName.getContainingFile().getName());
        assertSingleQinObjectFieldReference(reference.getElement());
    }

    public void testQinObjectFieldGoToDeclarationTargetsImportedFieldName() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter } from "./Counter.qin"

                const value = Counter.val<caret>ue
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiElement fieldName = assertInstanceOf(myFixture.getElementAtCaret(), PsiElement.class);

        assertEquals(QinTokenTypes.FIELD_NAME, fieldName.getNode().getElementType());
        assertEquals("value", fieldName.getText());
        assertEquals("Counter.qin", fieldName.getContainingFile().getName());
    }

    public void testQinObjectFieldGoToDeclarationTargetsAliasedImportedFieldName() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C } from "./Counter.qin"

                const value = C.val<caret>ue
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiElement fieldName = assertInstanceOf(myFixture.getElementAtCaret(), PsiElement.class);

        assertEquals(QinTokenTypes.FIELD_NAME, fieldName.getNode().getElementType());
        assertEquals("value", fieldName.getText());
        assertEquals("Counter.qin", fieldName.getContainingFile().getName());
    }

    public void testQinThisFieldReferenceResolvesToCurrentObjectFieldName() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41

                  next() {
                    return this.val<caret>ue
                  }
                }
                """);

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Qin this field reference was not registered", reference);
        PsiElement fieldName = assertInstanceOf(reference.resolve(), PsiElement.class);
        assertEquals(QinTokenTypes.FIELD_NAME, fieldName.getNode().getElementType());
        assertEquals("value", fieldName.getText());
        assertSingleQinObjectFieldReference(reference.getElement());
    }

    public void testQinThisFieldGoToDeclarationTargetsCurrentObjectFieldName() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41

                  next() {
                    return this.val<caret>ue
                  }
                }
                """);

        PsiElement fieldName = assertInstanceOf(myFixture.getElementAtCaret(), PsiElement.class);

        assertEquals(QinTokenTypes.FIELD_NAME, fieldName.getNode().getElementType());
        assertEquals("value", fieldName.getText());
        assertSame(myFixture.getFile(), fieldName.getContainingFile());
    }
    public void testQinObjectFieldReferenceParticipatesInReferencesSearch() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }

                const value = Counter.value
                """);

        PsiElement fieldName = findPsiElementType(myFixture.getFile(), QinTokenTypes.FIELD_NAME);
        assertNotNull("Qin field name PSI was not built", fieldName);
        Collection<PsiReference> references = ReferencesSearch.search(
                fieldName,
                GlobalSearchScope.allScope(getProject())).findAll();
        assertTrue("Find Usages should include Qin object field references: " + describeReferences(references),
                references.stream().filter(item -> item.getElement().getContainingFile() instanceof QinPsiFile
                        && "value".equals(item.getElement().getText())
                        && item.getElement() != fieldName).count() >= 2);
    }

    public void testQinObjectFieldRenameProcessorUpdatesReferences() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }

                const value = Counter.value
                """);

        PsiElement fieldName = findPsiElementType(myFixture.getFile(), QinTokenTypes.FIELD_NAME);
        assertNotNull("Qin field name PSI was not built", fieldName);
        new RenameProcessor(getProject(), fieldName, "total", false, false).run();

        String text = myFixture.getEditor().getDocument().getText();
        assertTrue("Qin field rename should update declaration: " + text, text.contains("total = 41"));
        assertTrue("Qin field rename should update this usage: " + text, text.contains("this.total"));
        assertTrue("Qin field rename should update object usage: " + text, text.contains("Counter.total"));
        assertFalse("Qin field rename should remove old object usage: " + text, text.contains("Counter.value"));
    }

    public void testQinObjectFieldRenameProcessorPreservesImportAliasQualifier() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C } from "./Counter.qin"

                const value = C.value
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        String source = myFixture.getEditor().getDocument().getText();
        PsiReference reference = myFixture.getFile().findReferenceAt(
                source.indexOf("value", source.indexOf("C.value")));
        assertNotNull("Aliased Qin object field reference was not registered for rename", reference);
        PsiElement fieldName = assertInstanceOf(reference.resolve(), PsiElement.class);
        new RenameProcessor(getProject(), fieldName, "total", false, false).run();

        String text = myFixture.getEditor().getDocument().getText();
        assertTrue("Qin field rename should preserve alias qualifier: " + text,
                text.contains("C.total"));
        assertTrue("Qin field rename should preserve import alias declaration: " + text,
                text.contains("import { Counter as C }"));
        assertFalse("Qin field rename should not rewrite alias qualifier to exported name: " + text,
                text.contains("Counter.total"));
    }

    public void testQinUnresolvedReferenceAnnotatorReportsMissingField() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41
                }

                const value = Counter.missing
                """);

        List<HighlightInfo> errors = myFixture.doHighlighting(HighlightSeverity.ERROR);
        assertHighlightContains(errors, "Unresolved Qin object field Counter.missing");
    }

    public void testQinUnresolvedReferenceInspectionReportsMissingField() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41
                }

                const value = Counter.missing
                """);

        String text = myFixture.getEditor().getDocument().getText();
        PsiElement missingField = myFixture.getFile().findElementAt(text.indexOf("missing"));
        assertNotNull("Missing field token should be present", missingField);

        ProblemsHolder holder = new ProblemsHolder(
                InspectionManager.getInstance(getProject()),
                myFixture.getFile(),
                true);
        new QinUnresolvedReferenceInspection()
                .buildVisitor(holder, true)
                .visitElement(missingField.getParent());

        ProblemDescriptor[] problems = holder.getResultsArray();
        assertEquals(1, problems.length);
        assertEquals("Unresolved Qin object field Counter.missing", problems[0].getDescriptionTemplate());
    }

    public void testQinUnresolvedReferenceAnnotatorIgnoresMissingMethodCall() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41
                }

                const value = Counter.missing()
                """);

        List<HighlightInfo> errors = myFixture.doHighlighting(HighlightSeverity.ERROR);
        assertHighlightMissing(errors, "Unresolved Qin object field Counter.missing");
    }

    public void testQinUnresolvedReferenceAnnotatorKeepsResolvedFieldsClean() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41
                }

                const value = Counter.value
                """);

        List<HighlightInfo> errors = myFixture.doHighlighting(HighlightSeverity.ERROR);
        assertHighlightMissing(errors, "Unresolved Qin object field Counter.value");
    }

    public void testQinCompletionFromIdeaFixtureIncludesObjectSymbol() throws Exception {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41
                }

                const probeValue = Co<caret>
                """);

        LspTestUtilKt.waitUntilFileOpenedByLspServer(getProject(), myFixture.getFile().getVirtualFile());
        LookupElement[] elements = myFixture.completeBasic();
        assertNotNull("IDEA completion did not produce a lookup list for a .qin file", elements);

        boolean hasCounter = Arrays.stream(elements)
                .map(LookupElement::getLookupString)
                .filter(Objects::nonNull)
                .anyMatch("Counter"::equals);
        assertTrue("IDEA completion did not include Qin object symbol Counter: "
                + Arrays.toString(Arrays.stream(elements)
                        .map(LookupElement::getLookupString)
                        .limit(40)
                        .toArray(String[]::new)), hasCounter);
    }

    public void testQinCompletionFromIdeaFixtureIncludesThisMember() throws Exception {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41;

                  next() {
                    return this.va<caret>
                  }
                }
                """);

        LspTestUtilKt.waitUntilFileOpenedByLspServer(getProject(), myFixture.getFile().getVirtualFile());
        LookupElement[] elements = myFixture.completeBasic();
        if (elements == null) {
            assertTrue("IDEA completion neither produced a lookup list nor inserted Qin object member value: "
                            + myFixture.getEditor().getDocument().getText(),
                    myFixture.getEditor().getDocument().getText().contains("return this.value"));
            return;
        }

        boolean hasValue = Arrays.stream(elements)
                .map(LookupElement::getLookupString)
                .filter(Objects::nonNull)
                .anyMatch("value"::equals);
        assertTrue("IDEA completion did not include Qin object member value: "
                + Arrays.toString(Arrays.stream(elements)
                        .map(LookupElement::getLookupString)
                        .limit(40)
                        .toArray(String[]::new)), hasValue);
    }

    public void testQinNativeCompletionIncludesObjectMembersAfterObjectQualifier() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }

                const value = Counter.v<caret>
        """);

        LookupElement[] elements = myFixture.completeBasic();
        if (elements == null) {
            assertTrue("IDEA native completion neither produced a lookup list nor inserted object member value: "
                            + myFixture.getEditor().getDocument().getText(),
                    myFixture.getEditor().getDocument().getText().contains("Counter.value"));
            return;
        }
        assertLookupContains(elements, "value");
    }

    public void testQinNativeCompletionIncludesImportedObjectMembersAfterObjectQualifier() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter } from "./Counter.qin"

                const value = Counter.v<caret>
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        LookupElement[] elements = myFixture.completeBasic();
        if (elements == null) {
            assertTrue("IDEA native completion neither produced a lookup list nor inserted imported object member value: "
                            + myFixture.getEditor().getDocument().getText(),
                    myFixture.getEditor().getDocument().getText().contains("Counter.value"));
            return;
        }
        assertLookupContains(elements, "value");
    }

    public void testQinNativeCompletionIncludesAliasedImportedObjectMethodsAfterObjectQualifier() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C } from "./Counter.qin"

                const value = C.n<caret>
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        LookupElement[] elements = myFixture.completeBasic();
        if (elements == null) {
            assertTrue("IDEA native completion neither produced a lookup list nor inserted imported object method next: "
                            + myFixture.getEditor().getDocument().getText(),
                    myFixture.getEditor().getDocument().getText().contains("C.next"));
            return;
        }
        assertLookupPsiElement(elements, "next", QinTokenTypes.METHOD_NAME, "Counter.qin");
    }

    public void testQinNativeCompletionIncludesAliasedImportedObjectFieldsAfterObjectQualifier() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter as C } from "./Counter.qin"

                const value = C.v<caret>
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        LookupElement[] elements = myFixture.completeBasic();
        if (elements == null) {
            assertTrue("IDEA native completion neither produced a lookup list nor inserted imported object field value: "
                            + myFixture.getEditor().getDocument().getText(),
                    myFixture.getEditor().getDocument().getText().contains("C.value"));
            return;
        }
        assertLookupPsiElement(elements, "value", QinTokenTypes.FIELD_NAME, "Counter.qin");
    }

    public void testQinNativeCompletionItemsCarryImportedMemberPsiElements() {
        myFixture.addFileToProject("src/main/Counter.qin", """
                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Counter } from "./Counter.qin"

                const value = Counter.<caret>
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        LookupElement[] elements = myFixture.completeBasic();
        assertNotNull("IDEA native completion should produce a lookup list for imported Qin members", elements);
        assertLookupPsiElement(elements, "value", QinTokenTypes.FIELD_NAME, "Counter.qin");
        assertLookupPsiElement(elements, "next", QinTokenTypes.METHOD_NAME, "Counter.qin");
    }

    public void testQinNativeCompletionDoesNotHandleJavaMemberQualifiers() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { Greeter } from "java:demo"

                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }

                const message = Greeter.g<caret>
                """);

        LookupElement[] elements = myFixture.completeBasic();
        if (elements == null) {
            return;
        }

        assertLookupMissing(elements, "value");
        assertLookupMissing(elements, "next");
    }

    public void testQinNativeCompletionDoesNotHandleAliasedJavaMemberQualifiers() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { Greeter as G } from "java:demo"

                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }

                const message = G.g<caret>
                """);

        LookupElement[] elements = myFixture.completeBasic();
        if (elements == null) {
            return;
        }

        assertLookupMissing(elements, "value");
        assertLookupMissing(elements, "next");
    }

    public void testQinNativeCompletionDoesNotHandleUnknownMemberQualifiers() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41

                  next() {
                    return this.value
                  }
                }

                const value = unknown.v<caret>
                """);

        LookupElement[] elements = myFixture.completeBasic();
        if (elements == null) {
            return;
        }

        assertLookupMissing(elements, "value");
        assertLookupMissing(elements, "next");
    }

    public void testQinNativeCompletionIncludesThisObjectMembers() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41

                  next() {
                    return this.n<caret>
                  }
                }
        """);

        LookupElement[] elements = myFixture.completeBasic();
        if (elements == null) {
            assertTrue("IDEA native completion neither produced a lookup list nor inserted this member next: "
                            + myFixture.getEditor().getDocument().getText(),
                    myFixture.getEditor().getDocument().getText().contains("return this.next"));
            return;
        }
        assertLookupContains(elements, "next");
    }

    public void testQinNativeCompletionForThisUsesOnlyCurrentObjectMembers() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Other {
                  otherValue = 1
                }

                export object Counter {
                  value = 41

                  next() {
                    return this.<caret>
                  }
                }
        """);

        LookupElement[] elements = myFixture.completeBasic();
        assertNotNull("IDEA native completion should produce current-object members for this.", elements);
        assertLookupContains(elements, "value");
        assertLookupContains(elements, "next");
        assertLookupMissing(elements, "otherValue");
    }

    public void testQinAutoPopupTypedHandlerTriggersForQinIdentifierInput() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41;

                  next() {
                    return this.<caret>
                  }
                }
                """);

        QinLspAutoPopupTypedHandler handler = new QinLspAutoPopupTypedHandler();
        assertTrue(QinLspAutoPopupTypedHandler.isAfterMemberAccess(myFixture.getEditor()));
        assertSame(TypedHandlerDelegate.Result.CONTINUE, handler.checkAutoPopup(
                'v',
                getProject(),
                myFixture.getEditor(),
                myFixture.getFile()));
        assertSame(TypedHandlerDelegate.Result.CONTINUE, handler.checkAutoPopup(
                '.',
                getProject(),
                myFixture.getEditor(),
                myFixture.getFile()));
        assertSame(TypedHandlerDelegate.Result.CONTINUE, handler.charTyped(
                'a',
                getProject(),
                myFixture.getEditor(),
                myFixture.getFile()));
        assertSame(TypedHandlerDelegate.Result.CONTINUE, handler.checkAutoPopup(
                ' ',
                getProject(),
                myFixture.getEditor(),
                myFixture.getFile()));
    }

    public void testQinTypingDotKeepsTypedText() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41;

                  next() {
                    return this<caret>
                  }
                }
                """);

        myFixture.type(".");
        assertTrue("Typing . should keep this. in the document: " + myFixture.getEditor().getDocument().getText(),
                myFixture.getEditor().getDocument().getText().contains("return this."));
    }

    public void testQinTypingMemberPrefixShowsLookup() throws Exception {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41;

                  next() {
                    return this.<caret>
                  }
                }
                """);

        LspTestUtilKt.waitUntilFileOpenedByLspServer(getProject(), myFixture.getFile().getVirtualFile());
        myFixture.type("va");
        waitForLookup();
        LookupElement[] elements = myFixture.getLookupElements();
        assertNotNull("Typing this.va did not open an IDEA lookup", LookupManager.getActiveLookup(myFixture.getEditor()));
        assertNotNull("Typing this.va opened no lookup elements", elements);
        boolean hasValue = Arrays.stream(elements)
                .map(LookupElement::getLookupString)
                .filter(Objects::nonNull)
                .anyMatch("value"::equals);
        assertTrue("Typing this.va did not include Qin object member value: "
                + Arrays.toString(Arrays.stream(elements)
                        .map(LookupElement::getLookupString)
                        .limit(40)
                        .toArray(String[]::new)), hasValue);
    }

    public void testQinTypingObjectPrefixShowsLookup() throws Exception {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41;
                }

                <caret>
                """);

        LspTestUtilKt.waitUntilFileOpenedByLspServer(getProject(), myFixture.getFile().getVirtualFile());
        myFixture.type("Co");
        waitForLookup();
        LookupElement[] elements = myFixture.getLookupElements();
        assertNotNull("Typing Co did not open an IDEA lookup", LookupManager.getActiveLookup(myFixture.getEditor()));
        assertNotNull("Typing Co opened no lookup elements", elements);
        boolean hasCounter = Arrays.stream(elements)
                .map(LookupElement::getLookupString)
                .filter(Objects::nonNull)
                .anyMatch("Counter"::equals);
        assertTrue("Typing Co did not include Qin object symbol Counter: "
                + Arrays.toString(Arrays.stream(elements)
                        .map(LookupElement::getLookupString)
                        .limit(40)
                        .toArray(String[]::new)), hasCounter);
    }

    public void testQinTypingMemberPrefixEnterInsertsLookupItem() throws Exception {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                export object Counter {
                  value = 41;

                  next() {
                    return this.<caret>
                  }
                }
                """);

        LspTestUtilKt.waitUntilFileOpenedByLspServer(getProject(), myFixture.getFile().getVirtualFile());
        myFixture.type("va");
        waitForLookup();
        LookupImpl lookup = (LookupImpl) LookupManager.getActiveLookup(myFixture.getEditor());
        assertNotNull("Typing this.va did not open an IDEA lookup", lookup);
        LookupElement value = lookup.getItems().stream()
                .filter(item -> "value".equals(item.getLookupString()))
                .findFirst()
                .orElse(null);
        assertNotNull("Typing this.va did not include Qin object member value", value);
        lookup.setCurrentItem(value);
        lookup.markSelectionTouched();
        myFixture.type("\n");
        UIUtil.dispatchAllInvocationEvents();

        String text = myFixture.getEditor().getDocument().getText();
        assertTrue("Pressing Enter on lookup item should insert this.value: " + text, text.contains("return this.value"));
        assertFalse("Pressing Enter on lookup item should not insert a blank line after this.va: " + text,
                text.contains("return this.va\n"));
    }

    public void testQinJavaImportReferenceResolvesToPsiClass() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const message = Gre<caret>eter.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Qin Java class reference was not registered at Greeter. " + describeCaretElement(), reference);
        assertInstanceOf(reference.resolve(), PsiClass.class);
        assertEquals("demo.Greeter", ((PsiClass) reference.resolve()).getQualifiedName());
        assertSingleQinJavaReference(reference.getElement());
    }

    public void testQinJavaMemberReferenceResolvesToPsiMethod() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }

                  public String instanceOnly() {
                    return "not static";
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const message = Greeter.gr<caret>eet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Qin Java member reference was not registered at Greeter.greet. " + describeCaretElement(), reference);
        assertInstanceOf(reference.resolve(), PsiMethod.class);
        PsiMethod method = (PsiMethod) reference.resolve();
        assertEquals("greet", method.getName());
        assertEquals("demo.Greeter", method.getContainingClass().getQualifiedName());
        assertSingleQinJavaReference(reference.getElement());
    }

    public void testQinJavaMemberGoToDeclarationTargetsPsiMethod() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const message = Greeter.gr<caret>eet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiMethod method = assertInstanceOf(myFixture.getElementAtCaret(), PsiMethod.class);

        assertEquals("greet", method.getName());
        assertEquals("demo.Greeter", method.getContainingClass().getQualifiedName());
    }

    public void testQinJavaAliasedMemberGoToDeclarationTargetsPsiMethod() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const message = G.gr<caret>eet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiMethod method = assertInstanceOf(myFixture.getElementAtCaret(), PsiMethod.class);

        assertEquals("greet", method.getName());
        assertEquals("demo.Greeter", method.getContainingClass().getQualifiedName());
    }

    public void testQinJavaAliasedImportResolvesThroughStructuredPsiImportTable() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const message = G.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        String text = myFixture.getEditor().getDocument().getText();
        PsiReference classReference = myFixture.getFile().findReferenceAt(text.indexOf("G.greet"));
        assertNotNull("Qin Java aliased class reference was not registered", classReference);
        assertInstanceOf(classReference.resolve(), QinImportAliasNamePsiElement.class);
        assertSingleQinImportAliasReference(classReference.getElement());
        assertNoQinJavaReference(classReference.getElement());

        PsiReference methodReference = myFixture.getFile().findReferenceAt(text.indexOf("greet"));
        assertNotNull("Qin Java aliased member reference was not registered", methodReference);
        PsiMethod method = assertInstanceOf(methodReference.resolve(), PsiMethod.class);
        assertEquals("greet", method.getName());
        assertEquals("demo.Greeter", method.getContainingClass().getQualifiedName());
        assertSingleQinJavaReference(methodReference.getElement());
    }

    public void testQinJavaAliasedImportSpecifierResolvesExportedNameToPsiClass() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Gre<caret>eter as G } from "java:demo"

                const message = G.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Qin Java import specifier reference was not registered at Greeter. " + describeCaretElement(), reference);
        PsiClass psiClass = assertInstanceOf(reference.resolve(), PsiClass.class);
        assertEquals("demo.Greeter", psiClass.getQualifiedName());
        assertSingleQinJavaReference(reference.getElement());
    }

    public void testQinJavaAliasedImportSpecifierGoToDeclarationTargetsPsiClass() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Gre<caret>eter as G } from "java:demo"

                const message = G.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiClass psiClass = assertInstanceOf(myFixture.getElementAtCaret(), PsiClass.class);

        assertEquals("demo.Greeter", psiClass.getQualifiedName());
    }

    public void testQinJavaAliasedImportSpecifierDoesNotReferenceLocalAliasName() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G<caret> } from "java:demo"

                const message = G.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNull("Aliased Java local import name should be a local declaration, not a Java class reference",
                reference);
    }

    public void testQinImportAliasRenameProcessorUpdatesJavaAliasUsages() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const message = G.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiElement aliasName = findPsiElementType(myFixture.getFile(), QinTokenTypes.IMPORT_ALIAS_NAME);
        assertNotNull("Qin import alias name PSI was not built", aliasName);
        new RenameProcessor(getProject(), aliasName, "GreeterAlias", false, false).run();

        String text = myFixture.getEditor().getDocument().getText();
        assertTrue("Java alias rename should update import alias declaration: " + text,
                text.contains("import { Greeter as GreeterAlias }"));
        assertTrue("Java alias rename should update local alias usages: " + text,
                text.contains("GreeterAlias.greet(\"Qin\")"));
    }

    public void testQinJavaImportAliasUsageGoToDeclarationTargetsAliasName() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const message = <caret>G.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiElement aliasName = assertInstanceOf(myFixture.getElementAtCaret(), PsiElement.class);

        assertEquals(QinTokenTypes.IMPORT_ALIAS_NAME, aliasName.getNode().getElementType());
        assertEquals("G", aliasName.getText());
        assertSame(myFixture.getFile(), aliasName.getContainingFile());
    }

    public void testQinJavaClassRenameProcessorPreservesImportAliasUsages() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const message = G.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiClass psiClass = JavaPsiFacade.getInstance(getProject())
                .findClass("demo.Greeter", GlobalSearchScope.allScope(getProject()));
        assertNotNull("Java class demo.Greeter was not available for rename", psiClass);
        new RenameProcessor(getProject(), psiClass, "Welcomer", false, false).run();

        String qinText = myFixture.getEditor().getDocument().getText();
        assertTrue("Java class rename should update the imported exported name: " + qinText,
                qinText.contains("import { Welcomer as G } from \"java:demo\""));
        assertTrue("Java class rename should preserve local alias usages: " + qinText,
                qinText.contains("G.greet(\"Qin\")"));
        assertFalse("Java class rename should not rewrite alias usages to the exported name: " + qinText,
                qinText.contains("Welcomer.greet"));
    }

    public void testQinJavaMemberReferenceResolvesAcrossWhitespaceWithPsiTokens() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const message = Greeter
                  .gr<caret>eet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Qin Java whitespace-separated member reference was not registered", reference);
        PsiMethod method = assertInstanceOf(reference.resolve(), PsiMethod.class);
        assertEquals("greet", method.getName());
        assertEquals("demo.Greeter", method.getContainingClass().getQualifiedName());
        assertSingleQinJavaReference(reference.getElement());
    }

    public void testQinJavaClassReferenceParticipatesInReferencesSearch() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const message = Greeter.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(
                myFixture.getEditor().getDocument().getText().indexOf("Greeter.greet"));
        assertNotNull("Qin Java class reference was not registered", reference);
        PsiClass javaClass = assertInstanceOf(reference.resolve(), PsiClass.class);

        Collection<PsiReference> references = ReferencesSearch.search(
                javaClass,
                GlobalSearchScope.allScope(getProject())).findAll();
        assertTrue("Find Usages should include Qin Java class reference: " + describeReferences(references),
                references.stream().anyMatch(item -> item.getElement().getContainingFile() instanceof QinPsiFile
                        && "Greeter".equals(item.getElement().getText())));
    }

    public void testQinJavaClassReferencesSearchIncludesAliasedExportedImportName() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const message = G.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiClass javaClass = JavaPsiFacade.getInstance(getProject())
                .findClass("demo.Greeter", GlobalSearchScope.allScope(getProject()));
        assertNotNull("Java class demo.Greeter was not available for Find Usages", javaClass);
        Collection<PsiReference> references = ReferencesSearch.search(
                javaClass,
                GlobalSearchScope.allScope(getProject())).findAll();
        assertTrue("Find Usages should include aliased exported Java import name: " + describeReferences(references),
                references.stream().anyMatch(item -> item.getElement().getContainingFile() instanceof QinPsiFile
                        && "App.qin".equals(item.getElement().getContainingFile().getName())
                        && "Greeter".equals(item.getElement().getText())));
        assertReferencesMissingQinElement(
                references,
                "App.qin",
                "G",
                "Find Usages for the remote Java class must not include the local import alias");
    }

    public void testQinJavaMethodReferenceParticipatesInReferencesSearch() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const message = Greeter.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(
                myFixture.getEditor().getDocument().getText().indexOf("greet"));
        assertNotNull("Qin Java method reference was not registered", reference);
        PsiMethod javaMethod = assertInstanceOf(reference.resolve(), PsiMethod.class);

        Collection<PsiReference> references = ReferencesSearch.search(
                javaMethod,
                GlobalSearchScope.allScope(getProject())).findAll();
        assertTrue("Find Usages should include Qin Java method reference: " + describeReferences(references),
                references.stream().anyMatch(item -> item.getElement().getContainingFile() instanceof QinPsiFile
                        && "greet".equals(item.getElement().getText())));
    }

    public void testQinJavaAliasedMethodReferenceParticipatesInReferencesSearch() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const message = G.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(
                myFixture.getEditor().getDocument().getText().indexOf("greet"));
        assertNotNull("Qin Java aliased method reference was not registered", reference);
        PsiMethod javaMethod = assertInstanceOf(reference.resolve(), PsiMethod.class);

        Collection<PsiReference> references = ReferencesSearch.search(
                javaMethod,
                GlobalSearchScope.allScope(getProject())).findAll();
        assertTrue("Find Usages should include Qin Java aliased method reference: " + describeReferences(references),
                references.stream().anyMatch(item -> item.getElement().getContainingFile() instanceof QinPsiFile
                        && "greet".equals(item.getElement().getText())));
    }

    public void testQinJavaMethodRenameProcessorUpdatesReferences() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const message = Greeter.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(
                myFixture.getEditor().getDocument().getText().indexOf("greet"));
        assertNotNull("Qin Java method reference was not registered for rename", reference);
        PsiMethod javaMethod = assertInstanceOf(reference.resolve(), PsiMethod.class);
        new RenameProcessor(getProject(), javaMethod, "welcome", false, false).run();

        assertTrue("Java method rename should update Qin method reference: "
                        + myFixture.getEditor().getDocument().getText(),
                myFixture.getEditor().getDocument().getText().contains("Greeter.welcome(\"Qin\")"));
    }

    public void testQinJavaAliasedMethodRenameProcessorPreservesAliasQualifier() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const message = G.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(
                myFixture.getEditor().getDocument().getText().indexOf("greet"));
        assertNotNull("Qin Java aliased method reference was not registered for rename", reference);
        PsiMethod javaMethod = assertInstanceOf(reference.resolve(), PsiMethod.class);
        new RenameProcessor(getProject(), javaMethod, "welcome", false, false).run();

        String text = myFixture.getEditor().getDocument().getText();
        assertTrue("Java method rename should update Qin aliased method reference: " + text,
                text.contains("G.welcome(\"Qin\")"));
        assertFalse("Java method rename should preserve the local alias qualifier: " + text,
                text.contains("Greeter.welcome"));
    }

    public void testQinJavaFieldReferenceParticipatesInReferencesSearch() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static final String DEFAULT_NAME = "Qin";
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const name = Greeter.DEFAULT_NAME
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(
                myFixture.getEditor().getDocument().getText().indexOf("DEFAULT_NAME"));
        assertNotNull("Qin Java field reference was not registered", reference);
        PsiField javaField = assertInstanceOf(reference.resolve(), PsiField.class);

        Collection<PsiReference> references = ReferencesSearch.search(
                javaField,
                GlobalSearchScope.allScope(getProject())).findAll();
        assertTrue("Find Usages should include Qin Java field reference: " + describeReferences(references),
                references.stream().anyMatch(item -> item.getElement().getContainingFile() instanceof QinPsiFile
                        && "DEFAULT_NAME".equals(item.getElement().getText())));
    }

    public void testQinJavaAliasedFieldReferenceParticipatesInReferencesSearch() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static final String DEFAULT_NAME = "Qin";
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const name = G.DEFAULT_NAME
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(
                myFixture.getEditor().getDocument().getText().indexOf("DEFAULT_NAME"));
        assertNotNull("Qin Java aliased field reference was not registered", reference);
        PsiField javaField = assertInstanceOf(reference.resolve(), PsiField.class);

        Collection<PsiReference> references = ReferencesSearch.search(
                javaField,
                GlobalSearchScope.allScope(getProject())).findAll();
        assertTrue("Find Usages should include Qin Java aliased field reference: " + describeReferences(references),
                references.stream().anyMatch(item -> item.getElement().getContainingFile() instanceof QinPsiFile
                        && "DEFAULT_NAME".equals(item.getElement().getText())));
    }

    public void testQinJavaAliasedFieldReferenceResolvesToPsiField() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static final String DEFAULT_NAME = "Qin";
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const name = G.DEFAULT_NAME
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference aliasReference = myFixture.getFile().findReferenceAt(
                myFixture.getEditor().getDocument().getText().indexOf("G.DEFAULT_NAME"));
        assertNotNull("Qin Java alias reference was not registered for static field qualifier", aliasReference);
        assertInstanceOf(aliasReference.resolve(), QinImportAliasNamePsiElement.class);
        assertSingleQinImportAliasReference(aliasReference.getElement());
        assertNoQinJavaReference(aliasReference.getElement());

        PsiReference fieldReference = myFixture.getFile().findReferenceAt(
                myFixture.getEditor().getDocument().getText().indexOf("DEFAULT_NAME"));
        assertNotNull("Qin Java aliased field reference was not registered", fieldReference);
        PsiField javaField = assertInstanceOf(fieldReference.resolve(), PsiField.class);
        assertEquals("DEFAULT_NAME", javaField.getName());
        assertEquals("demo.Greeter", javaField.getContainingClass().getQualifiedName());
        assertSingleQinJavaReference(fieldReference.getElement());
    }

    public void testQinJavaFieldGoToDeclarationTargetsPsiField() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static final String DEFAULT_NAME = "Qin";
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const name = Greeter.DEFAULT<caret>_NAME
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiField field = assertInstanceOf(myFixture.getElementAtCaret(), PsiField.class);

        assertEquals("DEFAULT_NAME", field.getName());
        assertEquals("demo.Greeter", field.getContainingClass().getQualifiedName());
    }

    public void testQinJavaAliasedFieldGoToDeclarationTargetsPsiField() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static final String DEFAULT_NAME = "Qin";
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const name = G.DEFAULT<caret>_NAME
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiField field = assertInstanceOf(myFixture.getElementAtCaret(), PsiField.class);

        assertEquals("DEFAULT_NAME", field.getName());
        assertEquals("demo.Greeter", field.getContainingClass().getQualifiedName());
    }

    public void testQinJavaFieldRenameProcessorUpdatesReferences() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static final String DEFAULT_NAME = "Qin";
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const name = Greeter.DEFAULT_NAME
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(
                myFixture.getEditor().getDocument().getText().indexOf("DEFAULT_NAME"));
        assertNotNull("Qin Java field reference was not registered for rename", reference);
        PsiField javaField = assertInstanceOf(reference.resolve(), PsiField.class);
        new RenameProcessor(getProject(), javaField, "FALLBACK_NAME", false, false).run();

        assertTrue("Java field rename should update Qin field reference: "
                        + myFixture.getEditor().getDocument().getText(),
                myFixture.getEditor().getDocument().getText().contains("Greeter.FALLBACK_NAME"));
    }

    public void testQinJavaAliasedFieldRenameProcessorPreservesAliasQualifier() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static final String DEFAULT_NAME = "Qin";
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const name = G.DEFAULT_NAME
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(
                myFixture.getEditor().getDocument().getText().indexOf("DEFAULT_NAME"));
        assertNotNull("Qin Java aliased field reference was not registered for rename", reference);
        PsiField javaField = assertInstanceOf(reference.resolve(), PsiField.class);
        new RenameProcessor(getProject(), javaField, "FALLBACK_NAME", false, false).run();

        String text = myFixture.getEditor().getDocument().getText();
        assertTrue("Java field rename should update Qin aliased field reference: " + text,
                text.contains("G.FALLBACK_NAME"));
        assertFalse("Java field rename should preserve the local alias qualifier: " + text,
                text.contains("Greeter.FALLBACK_NAME"));
    }

    public void testQinJavaReferenceRenameUpdatesQinToken() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const message = Greeter.gr<caret>eet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        PsiReference reference = myFixture.getFile().findReferenceAt(myFixture.getCaretOffset());
        assertNotNull("Qin Java method reference was not registered for rename", reference);
        WriteCommandAction.runWriteCommandAction(
                getProject(),
                (Runnable) () -> reference.handleElementRename("welcome"));

        assertTrue("Rename should update Qin Java member reference: " + myFixture.getEditor().getDocument().getText(),
                myFixture.getEditor().getDocument().getText().contains("Greeter.welcome(\"Qin\")"));
    }

    public void testQinUnresolvedReferenceAnnotatorReportsMissingImportedClass() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { MissingGreeter } from "java:demo"

                const message = MissingGreeter.greet("Qin")
                """);

        List<HighlightInfo> errors = myFixture.doHighlighting(HighlightSeverity.ERROR);
        assertHighlightContains(errors, "Unresolved Java class demo.MissingGreeter");
        assertHighlightMissing(errors, "Unresolved static Java member demo.MissingGreeter.greet");
    }

    public void testQinUnresolvedReferenceAnnotatorReportsMissingAliasedImportedClass() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { MissingGreeter as G } from "java:demo"

                const message = G.greet("Qin")
                """);

        List<HighlightInfo> errors = myFixture.doHighlighting(HighlightSeverity.ERROR);
        assertHighlightContains(errors, "Unresolved Java class demo.MissingGreeter");
        assertHighlightMissing(errors, "Unresolved static Java member demo.MissingGreeter.greet");
    }

    public void testQinUnresolvedReferenceAnnotatorReportsMissingStaticMember() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const message = Greeter.missing("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        List<HighlightInfo> errors = myFixture.doHighlighting(HighlightSeverity.ERROR);
        assertHighlightContains(errors, "Unresolved static Java member demo.Greeter.missing");
    }

    public void testQinUnresolvedReferenceAnnotatorReportsMissingAliasedStaticMember() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter as G } from "java:demo"

                const message = G.missing("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        List<HighlightInfo> errors = myFixture.doHighlighting(HighlightSeverity.ERROR);
        assertHighlightContains(errors, "Unresolved static Java member demo.Greeter.missing");
    }

    public void testQinUnresolvedReferenceAnnotatorKeepsResolvedJavaReferencesClean() {
        myFixture.addFileToProject("src/main/demo/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const message = Greeter.greet("Qin")
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        List<HighlightInfo> errors = myFixture.doHighlighting(HighlightSeverity.ERROR);
        assertHighlightMissing(errors, "Unresolved Java class demo.Greeter");
        assertHighlightMissing(errors, "Unresolved static Java member demo.Greeter.greet");
    }

    private static void assertHighlightContains(List<HighlightInfo> highlights, String expectedDescription) {
        boolean found = highlights.stream()
                .map(info -> info.getDescription())
                .filter(Objects::nonNull)
                .anyMatch(expectedDescription::equals);
        assertTrue("Expected highlight " + expectedDescription + " but got " + describeHighlights(highlights), found);
    }

    private static void assertHighlightMissing(List<HighlightInfo> highlights, String unexpectedDescription) {
        boolean found = highlights.stream()
                .map(info -> info.getDescription())
                .filter(Objects::nonNull)
                .anyMatch(unexpectedDescription::equals);
        assertFalse("Unexpected highlight " + unexpectedDescription + " in " + describeHighlights(highlights), found);
    }

    private static List<String> describeHighlights(List<HighlightInfo> highlights) {
        return highlights.stream()
                .map(info -> info.getDescription() + "@" + info.getSeverity())
                .toList();
    }

    private static List<String> collectLexerTokenEntries(String source) {
        Lexer lexer = new QinLexer();
        lexer.start(source);
        List<String> tokens = new ArrayList<>();
        while (lexer.getTokenType() != null) {
            tokens.add(source.substring(lexer.getTokenStart(), lexer.getTokenEnd()) + ":" + lexer.getTokenType());
            lexer.advance();
        }
        return tokens;
    }

    private static void assertTokenHighlight(
            QinSyntaxHighlighter highlighter,
            IElementType tokenType,
            TextAttributesKey expectedKey) {
        TextAttributesKey[] keys = highlighter.getTokenHighlights(tokenType);
        assertTrue("Expected " + tokenType + " to include " + expectedKey + " but got " + Arrays.toString(keys),
                Arrays.asList(keys).contains(expectedKey));
    }

    private static boolean hasPsiElementType(PsiElement root, IElementType type) {
        if (root.getNode() != null && root.getNode().getElementType() == type) {
            return true;
        }
        for (PsiElement child : root.getChildren()) {
            if (hasPsiElementType(child, type)) {
                return true;
            }
        }
        return false;
    }

    private static int countPsiElementType(PsiElement root, IElementType type) {
        int count = root.getNode() != null && root.getNode().getElementType() == type ? 1 : 0;
        for (PsiElement child : root.getChildren()) {
            count += countPsiElementType(child, type);
        }
        return count;
    }

    private static List<PsiElement> descendantsOfType(PsiElement root, IElementType type) {
        List<PsiElement> elements = new ArrayList<>();
        collectDescendantsOfType(root, type, elements);
        return elements;
    }

    private static void collectDescendantsOfType(PsiElement root, IElementType type, List<PsiElement> elements) {
        if (root.getNode() != null && root.getNode().getElementType() == type) {
            elements.add(root);
            return;
        }
        for (PsiElement child : root.getChildren()) {
            collectDescendantsOfType(child, type, elements);
        }
    }

    private static void assertSingleQinJavaReference(PsiElement element) {
        long count = countQinReferences(element, QinJavaReference.class);
        assertEquals("Qin Java references should be provided only through QinJavaReferenceContributor", 1L, count);
    }

    private static void assertSingleQinImportAliasReference(PsiElement element) {
        long count = countQinReferences(element, QinImportAliasReference.class);
        assertEquals("Qin import alias references should be provided only through QinImportAliasReferenceContributor", 1L, count);
    }

    private static void assertNoQinJavaReference(PsiElement element) {
        long count = countQinReferences(element, QinJavaReference.class);
        assertEquals("Qin Java references should not be provided on local alias usage " + element.getText(), 0L, count);
    }

    private static void assertNoQinObjectReference(PsiElement element) {
        long count = countQinReferences(element, QinObjectReference.class);
        assertEquals("Qin object references should not be provided on local alias usage " + element.getText(), 0L, count);
    }

    private static void assertSingleQinObjectReference(PsiElement element) {
        long count = countQinReferences(element, QinObjectReference.class);
        assertEquals("Qin object references should be provided only through QinObjectReferenceContributor", 1L, count);
    }

    private static void assertSingleQinObjectMethodReference(PsiElement element) {
        long count = countQinReferences(element, QinObjectMethodReference.class);
        assertEquals("Qin object method references should be provided only through QinObjectMethodReferenceContributor", 1L, count);
    }

    private static void assertSingleQinObjectFieldReference(PsiElement element) {
        long count = countQinReferences(element, QinObjectFieldReference.class);
        assertEquals("Qin object field references should be provided only through QinObjectFieldReferenceContributor", 1L, count);
    }

    private static long countQinReferences(PsiElement element, Class<? extends PsiReference> referenceType) {
        return Arrays.stream(QinPsiReferences.references(element))
                .filter(referenceType::isInstance)
                .count();
    }

    private static void assertLookupContains(LookupElement[] elements, String expected) {
        boolean found = Arrays.stream(elements)
                .map(LookupElement::getLookupString)
                .filter(Objects::nonNull)
                .anyMatch(expected::equals);
        assertTrue("IDEA completion did not include " + expected + ": "
                + Arrays.toString(Arrays.stream(elements)
                .map(LookupElement::getLookupString)
                .limit(40)
                .toArray(String[]::new)), found);
    }

    private static void assertLookupMissing(LookupElement[] elements, String unexpected) {
        boolean found = Arrays.stream(elements)
                .map(LookupElement::getLookupString)
                .filter(Objects::nonNull)
                .anyMatch(unexpected::equals);
        assertFalse("IDEA native Qin completion should not include " + unexpected + ": "
                + Arrays.toString(Arrays.stream(elements)
                .map(LookupElement::getLookupString)
                .limit(40)
                .toArray(String[]::new)), found);
    }

    private static void assertLookupPsiElement(
            LookupElement[] elements,
            String expected,
            IElementType expectedType,
            String expectedFileName) {
        LookupElement element = Arrays.stream(elements)
                .filter(item -> expected.equals(item.getLookupString()))
                .findFirst()
                .orElse(null);
        assertNotNull("IDEA completion did not include " + expected + ": "
                + Arrays.toString(Arrays.stream(elements)
                .map(LookupElement::getLookupString)
                .limit(40)
                .toArray(String[]::new)), element);
        PsiElement psiElement = element.getPsiElement();
        assertNotNull("Lookup item " + expected + " should carry its Qin PSI element", psiElement);
        assertEquals(expectedType, psiElement.getNode().getElementType());
        assertEquals(expectedFileName, psiElement.getContainingFile().getName());
    }

    private static PsiElement findPsiElementType(PsiElement root, IElementType type) {
        if (root.getNode() != null && root.getNode().getElementType() == type) {
            return root;
        }
        for (PsiElement child : root.getChildren()) {
            PsiElement found = findPsiElementType(child, type);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private static PsiElement findImportSpecifierStartingWith(PsiElement root, String text) {
        if (root.getNode() != null
                && root.getNode().getElementType() == QinTokenTypes.IMPORT_SPECIFIER
                && root.getText().startsWith(text)) {
            return root;
        }
        for (PsiElement child : root.getChildren()) {
            PsiElement found = findImportSpecifierStartingWith(child, text);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private static PsiElement findFirstChildOfText(PsiElement root, IElementType type, String text) {
        assertNotNull("Root PSI element should be available", root);
        if (root.getNode() != null
                && root.getNode().getElementType() == type
                && text.equals(root.getText())) {
            return root;
        }
        for (PsiElement child : root.getChildren()) {
            PsiElement found = findFirstChildOfText(child, type, text);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private static List<String> describeReferences(Collection<PsiReference> references) {
        return references.stream()
                .map(reference -> reference.getElement().getText()
                        + "@"
                        + reference.getElement().getContainingFile().getName())
                .toList();
    }

    private static void assertReferencesMissingQinElement(
            Collection<PsiReference> references,
            String fileName,
            String text,
            String message) {
        assertFalse(message + ": " + describeReferences(references),
                references.stream().anyMatch(reference ->
                        reference.getElement().getContainingFile() instanceof QinPsiFile
                                && fileName.equals(reference.getElement().getContainingFile().getName())
                                && text.equals(reference.getElement().getText())));
    }

    private static List<String> describePsiFiles(Collection<? extends PsiFile> files) {
        return files.stream()
                .map(PsiFile::getName)
                .toList();
    }

    private String describeCaretElement() {
        PsiElement element = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        if (element == null) {
            return "caretElement=null";
        }
        IElementType elementType = element.getNode() == null ? null : element.getNode().getElementType();
        return "caretElementClass=" + element.getClass().getName()
                + ", text='" + element.getText() + "'"
                + ", elementType=" + elementType
                + ", language=" + element.getLanguage()
                + ", directReferences=" + element.getReferences().length;
    }

    private void waitForLookup() throws Exception {
        long deadline = System.currentTimeMillis() + 5000;
        while (LookupManager.getActiveLookup(myFixture.getEditor()) == null && System.currentTimeMillis() < deadline) {
            UIUtil.dispatchAllInvocationEvents();
            Thread.sleep(25);
        }
        UIUtil.dispatchAllInvocationEvents();
    }
}
