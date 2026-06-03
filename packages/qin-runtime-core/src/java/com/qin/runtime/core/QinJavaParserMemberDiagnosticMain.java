package com.qin.runtime.core;

import com.slime.java.ast.JavaCstToAst;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class QinJavaParserMemberDiagnosticMain {
    private QinJavaParserMemberDiagnosticMain() {
    }

    public static void main(String[] args) throws Exception {
        Path sourceFile = args.length == 0
                ? Path.of("..", "slime", "java-slime", "subhuti-java", "src", "main", "java",
                        "com", "subhuti", "parser", "SubhutiParserCore.java").toAbsolutePath().normalize()
                : Path.of(args[0]).toAbsolutePath().normalize();
        String source = Files.readString(sourceFile);
        int classKeyword = source.indexOf(" class ");
        if (classKeyword < 0) {
            classKeyword = source.indexOf("class ");
        }
        int classBodyStart = source.indexOf('{', classKeyword);
        int classBodyEnd = source.lastIndexOf('}');
        String header = source.substring(0, classBodyStart + 1);
        String body = source.substring(classBodyStart + 1, classBodyEnd);
        List<String> members = splitTopLevelMembers(body);
        StringBuilder candidateBody = new StringBuilder();
        for (int i = 0; i < members.size(); i++) {
            candidateBody.append(members.get(i)).append("\n");
            String candidate = header + "\n" + candidateBody + "\n}\n";
            try {
                JavaCstToAst.parse(candidate);
            } catch (RuntimeException e) {
                System.out.println("Failing member index: " + i);
                System.out.println(members.get(i));
                throw e;
            }
        }
        System.out.println("QinJavaParserMemberDiagnosticMain OK members=" + members.size());
    }

    private static List<String> splitTopLevelMembers(String body) {
        List<String> members = new ArrayList<>();
        int start = -1;
        int depth = 0;
        boolean inLineComment = false;
        boolean inBlockComment = false;
        boolean inString = false;
        boolean inChar = false;
        boolean escaped = false;
        for (int i = 0; i < body.length(); i++) {
            char current = body.charAt(i);
            char next = i + 1 < body.length() ? body.charAt(i + 1) : '\0';
            if (inLineComment) {
                if (current == '\n') {
                    inLineComment = false;
                }
                continue;
            }
            if (inBlockComment) {
                if (current == '*' && next == '/') {
                    inBlockComment = false;
                    i++;
                }
                continue;
            }
            if (inString) {
                if (escaped) {
                    escaped = false;
                } else if (current == '\\') {
                    escaped = true;
                } else if (current == '"') {
                    inString = false;
                }
                continue;
            }
            if (inChar) {
                if (escaped) {
                    escaped = false;
                } else if (current == '\\') {
                    escaped = true;
                } else if (current == '\'') {
                    inChar = false;
                }
                continue;
            }
            if (current == '/' && next == '/') {
                inLineComment = true;
                i++;
                continue;
            }
            if (current == '/' && next == '*') {
                inBlockComment = true;
                i++;
                continue;
            }
            if (current == '"') {
                inString = true;
                continue;
            }
            if (current == '\'') {
                inChar = true;
                continue;
            }
            if (start < 0 && !Character.isWhitespace(current)) {
                start = i;
            }
            if (current == '{') {
                depth++;
                continue;
            }
            if (current == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    members.add(body.substring(start, i + 1));
                    start = -1;
                }
                continue;
            }
            if (current == ';' && depth == 0 && start >= 0) {
                members.add(body.substring(start, i + 1));
                start = -1;
            }
        }
        return members;
    }
}
