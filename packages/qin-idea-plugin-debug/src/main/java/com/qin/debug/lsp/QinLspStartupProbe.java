package com.qin.debug.lsp;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.qin.debug.QinLogger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

public final class QinLspStartupProbe {
    private static final Set<String> LSP_EXTENSIONS = Set.of("qin", "ovs", "cssts");
    private static final String FIXTURE_OPEN_FILE_PROPERTY = "qin.lsp.fixture.openFile";

    private QinLspStartupProbe() {
    }

    public static void log(Project project, Path projectRoot) {
        QinLogger.info("[LSP-PROBE] Starting editor/LSP probe");
        openFixtureFileIfConfigured(project);
        VirtualFile[] openFiles = FileEditorManager.getInstance(project).getOpenFiles();
        QinLogger.info("[LSP-PROBE] Open editor files: " + openFiles.length);
        for (VirtualFile openFile : openFiles) {
            logVirtualFile("[LSP-PROBE] Open", openFile);
        }

        if (projectRoot == null || !Files.isDirectory(projectRoot)) {
            QinLogger.info("[LSP-PROBE] Project root unavailable: " + projectRoot);
            return;
        }

        try (Stream<Path> walk = Files.walk(projectRoot, 2)) {
            walk.filter(Files::isRegularFile)
                    .filter(QinLspStartupProbe::isLspSource)
                    .sorted()
                    .limit(24)
                    .forEach(path -> {
                        VirtualFile file = LocalFileSystem.getInstance().findFileByNioFile(path);
                        if (file == null) {
                            QinLogger.info("[LSP-PROBE] Source " + projectRoot.relativize(path)
                                    + " virtualFile=null");
                        } else {
                            logVirtualFile("[LSP-PROBE] Source " + projectRoot.relativize(path), file);
                        }
                    });
        } catch (Exception e) {
            QinLogger.error("[LSP-PROBE] Failed to scan LSP source files", e);
        }
    }

    private static void openFixtureFileIfConfigured(Project project) {
        String configuredFile = System.getProperty(FIXTURE_OPEN_FILE_PROPERTY);
        if (configuredFile == null || configuredFile.isBlank()) {
            return;
        }

        Path path = Path.of(configuredFile).toAbsolutePath().normalize();
        VirtualFile file = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(path);
        if (file == null) {
            QinLogger.info("[LSP-PROBE] Fixture open file not found: " + path);
            return;
        }

        QinLogger.info("[LSP-PROBE] Opening fixture file: " + file.getPath());
        FileEditorManager.getInstance(project).openFile(file, true, true);
    }

    private static boolean isLspSource(Path path) {
        String fileName = path.getFileName().toString();
        int dot = fileName.lastIndexOf('.');
        return dot >= 0 && LSP_EXTENSIONS.contains(fileName.substring(dot + 1));
    }

    private static void logVirtualFile(String prefix, VirtualFile file) {
        FileType fileType = FileTypeManager.getInstance().getFileTypeByFile(file);
        String language = fileType instanceof LanguageFileType languageFileType
                ? languageFileType.getLanguage().getID()
                : "<none>";
        QinLogger.info(prefix
                + " path=" + file.getPath()
                + " extension=" + file.getExtension()
                + " fileType=" + fileType.getName()
                + " language=" + language);
    }
}
