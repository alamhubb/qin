package com.qin.debug.lsp;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.codeInsight.lookup.impl.LookupImpl;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.platform.lsp.tests.LspTestUtilKt;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
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
    }

    public void testQinJavaAliasedImportResolvesThroughPsiTokenImportTable() {
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
        PsiClass psiClass = assertInstanceOf(classReference.resolve(), PsiClass.class);
        assertEquals("demo.Greeter", psiClass.getQualifiedName());

        PsiReference methodReference = myFixture.getFile().findReferenceAt(text.indexOf("greet"));
        assertNotNull("Qin Java aliased member reference was not registered", methodReference);
        PsiMethod method = assertInstanceOf(methodReference.resolve(), PsiMethod.class);
        assertEquals("greet", method.getName());
        assertEquals("demo.Greeter", method.getContainingClass().getQualifiedName());
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

    public void testQinJavaInteropAnnotatorReportsMissingImportedClass() {
        myFixture.configureByText(QinLspFileType.INSTANCE, """
                import { MissingGreeter } from "java:demo"

                const message = MissingGreeter.greet("Qin")
                """);

        List<HighlightInfo> errors = myFixture.doHighlighting(HighlightSeverity.ERROR);
        assertHighlightContains(errors, "Unresolved Java class demo.MissingGreeter");
    }

    public void testQinJavaInteropAnnotatorReportsMissingStaticMember() {
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

    public void testQinJavaInteropAnnotatorKeepsResolvedJavaReferencesClean() {
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

    private static List<String> describeReferences(Collection<PsiReference> references) {
        return references.stream()
                .map(reference -> reference.getElement().getText()
                        + "@"
                        + reference.getElement().getContainingFile().getName())
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
