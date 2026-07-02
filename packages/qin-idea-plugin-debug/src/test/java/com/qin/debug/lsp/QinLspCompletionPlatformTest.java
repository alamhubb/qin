package com.qin.debug.lsp;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.platform.lsp.tests.LspTestUtilKt;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

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
        assertSame(TypedHandlerDelegate.Result.STOP, handler.checkAutoPopup(
                'v',
                getProject(),
                myFixture.getEditor(),
                myFixture.getFile()));
        assertSame(TypedHandlerDelegate.Result.STOP, handler.checkAutoPopup(
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
}
