package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrTypeRef;
import com.slime.java.ast.JavaAstClassDeclaration;
import com.slime.java.ast.JavaAstFieldDeclaration;
import com.slime.java.ast.JavaAstImportDeclaration;
import com.slime.java.ast.JavaAstMethodDeclaration;
import com.slime.java.ast.JavaAstParameter;
import com.slime.java.ast.JavaAstProgram;
import com.slime.java.ast.JavaCstToAst;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class QinJavaAstIrLowerer {
    public QinIrProgram lowerSource(String source) {
        return lowerProgram(JavaCstToAst.parse(source));
    }

    public QinIrProgram lowerProgram(JavaAstProgram program) {
        Map<String, String> importedTypes = importedTypes(program.imports());
        List<QinIrClassDeclaration> classes = new ArrayList<>();
        for (JavaAstClassDeclaration classDeclaration : program.classes()) {
            classes.add(lowerClass(program.packageName(), importedTypes, classDeclaration));
        }
        return new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                classes);
    }

    private QinIrClassDeclaration lowerClass(
            String packageName,
            Map<String, String> importedTypes,
            JavaAstClassDeclaration classDeclaration) {
        List<QinIrFieldDeclaration> fields = new ArrayList<>();
        for (JavaAstFieldDeclaration field : classDeclaration.fields()) {
            fields.add(new QinIrFieldDeclaration(
                    field.name(),
                    lowerType(field.typeName(), packageName, importedTypes),
                    List.of(),
                    null));
        }

        List<QinIrMethodDeclaration> methods = new ArrayList<>();
        for (JavaAstMethodDeclaration method : classDeclaration.methods()) {
            methods.add(lowerMethod(method, packageName, importedTypes));
        }

        return new QinIrClassDeclaration(
                packageName,
                classDeclaration.name(),
                null,
                List.<QinIrAnnotation>of(),
                fields,
                methods);
    }

    private QinIrMethodDeclaration lowerMethod(
            JavaAstMethodDeclaration method,
            String packageName,
            Map<String, String> importedTypes) {
        List<QinIrParameter> parameters = new ArrayList<>();
        for (JavaAstParameter parameter : method.parameters()) {
            parameters.add(new QinIrParameter(
                    parameter.name(),
                    lowerType(parameter.typeName(), packageName, importedTypes),
                    List.of()));
        }
        return new QinIrMethodDeclaration(
                method.name(),
                lowerType(method.returnTypeName(), packageName, importedTypes),
                parameters,
                List.of(),
                null);
    }

    private QinIrTypeRef lowerType(
            String typeName,
            String packageName,
            Map<String, String> importedTypes) {
        return switch (typeName) {
            case "void" -> QinIrTypeRef.voidType();
            case "boolean" -> QinIrTypeRef.booleanType();
            case "byte", "short", "int", "long", "char" -> QinIrTypeRef.intType();
            case "float", "double" -> QinIrTypeRef.doubleType();
            case "String", "java.lang.String" -> QinIrTypeRef.stringType();
            default -> QinIrTypeRef.classType(resolveClassName(typeName, packageName, importedTypes));
        };
    }

    private String resolveClassName(
            String typeName,
            String packageName,
            Map<String, String> importedTypes) {
        String imported = importedTypes.get(typeName);
        if (imported != null) {
            return imported;
        }
        if (typeName.contains(".")) {
            return typeName;
        }
        if (packageName != null && !packageName.isBlank()) {
            return packageName + "." + typeName;
        }
        return typeName;
    }

    private Map<String, String> importedTypes(List<JavaAstImportDeclaration> imports) {
        Map<String, String> importedTypes = new LinkedHashMap<>();
        for (JavaAstImportDeclaration importDeclaration : imports) {
            if (importDeclaration.onDemand()) {
                continue;
            }
            String name = importDeclaration.name();
            int dot = name.lastIndexOf('.');
            if (dot >= 0 && dot < name.length() - 1) {
                importedTypes.put(name.substring(dot + 1), name);
            }
        }
        return importedTypes;
    }
}
