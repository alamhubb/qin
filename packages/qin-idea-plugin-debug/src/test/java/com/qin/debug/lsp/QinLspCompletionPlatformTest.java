package com.qin.debug.lsp;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.codeInsight.lookup.impl.LookupImpl;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.platform.lsp.tests.LspTestUtilKt;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.util.ui.UIUtil;

import java.nio.file.Path;
import java.util.Arrays;
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

    public void testQinMainProjectCompletesImportedJavaSourceMethods() throws Exception {
        myFixture.addFileToProject("qin.config.js", """
                export default {
                  name: "qin-lsp-java-source-fixture",
                  type: "app",
                  entry: "src/main/App.qin"
                }
                """);
        myFixture.addFileToProject("src/main/Greeter.java", """
                package demo;

                public class Greeter {
                  public static String greet(String name) {
                    return "Hello " + name;
                  }

                  public static int count() {
                    return 1;
                  }
                }
                """);
        var qinFile = myFixture.addFileToProject("src/main/App.qin", """
                import { Greeter } from "java:demo"

                const message = Greeter.gr<caret>
                """);
        myFixture.configureFromExistingVirtualFile(qinFile.getVirtualFile());

        LspTestUtilKt.waitUntilFileOpenedByLspServer(getProject(), myFixture.getFile().getVirtualFile());
        LookupElement[] elements = myFixture.completeBasic();
        assertNotNull("IDEA completion produced no lookup list for imported Java source method", elements);

        boolean hasGreet = Arrays.stream(elements)
                .map(LookupElement::getLookupString)
                .filter(Objects::nonNull)
                .anyMatch("greet"::equals);
        assertTrue("IDEA completion did not include Java source method Greeter.greet: "
                + Arrays.toString(Arrays.stream(elements)
                        .map(LookupElement::getLookupString)
                        .limit(40)
                        .toArray(String[]::new)), hasGreet);
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
