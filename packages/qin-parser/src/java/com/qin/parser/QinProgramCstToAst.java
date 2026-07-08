package com.qin.parser;

import com.slime.ast.AstNode;
import com.slime.ast.Expression;
import com.slime.ast.SyntaxToken;
import com.slime.ast.nodes.declarations.ClassDeclaration;
import com.slime.ast.nodes.declarations.VariableDeclaration;
import com.slime.ast.nodes.expressions.Identifier;
import com.slime.ast.nodes.expressions.NewExpression;
import com.slime.ast.nodes.misc.Program;
import com.slime.ast.nodes.misc.VariableDeclarator;
import com.slime.ast.nodes.modules.ExportDefaultDeclaration;
import com.slime.ast.nodes.modules.ExportNamedDeclaration;
import com.slime.parser.cstToAst.SlimeAstCreateUtils;
import com.slime.parser.cstToAst.SlimeCstToAstUtils;
import com.slime.parser.cstToAst.typescript.SlimeTSDecoratorCstToAst;
import com.subhuti.struct.SubhutiCst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

final class QinProgramCstToAst {
    private static final String OBJECT_INTERNAL_PREFIX = "__QinObject_";
    private final SlimeCstToAstUtils slimeTransformer = new SlimeCstToAstUtils();

    QinProgramCstToAst() {
    }

    Program createProgramAst(SubhutiCst cst) {
        List<AstNode> body = new ArrayList<>();
        collectBody(cst, body, identitySet());
        return SlimeAstCreateUtils.createProgram(
                body.toArray(new AstNode[0]),
                "module",
                SlimeAstCreateUtils.resolveSubhutiLocation(cst));
    }

    private void collectBody(SubhutiCst cst, List<AstNode> body, Set<SubhutiCst> visited) {
        if (cst == null || !visited.add(cst)) {
            return;
        }
        String name = cst.getName();
        if ("StatementListItem".equals(name) || "ModuleItem".equals(name)) {
            List<AstNode> nodes = createModuleItemAst(cst);
            if (!nodes.isEmpty()) {
                body.addAll(nodes);
            }
            return;
        }
        for (SubhutiCst child : safeChildren(cst)) {
            collectBody(child, body, visited);
        }
    }

    private List<AstNode> createModuleItemAst(SubhutiCst cst) {
        SubhutiCst exportDeclaration = directChildByName(cst, "ExportDeclaration");
        if (exportDeclaration != null && containsName(exportDeclaration, "QinObjectDeclarationBody")) {
            return createExportQinObjectNodes(exportDeclaration);
        }

        SubhutiCst qinObject = findFirstByName(cst, "QinObjectDeclaration");
        if (qinObject != null) {
            return createQinObjectNodes(qinObject, false, false);
        }

        AstNode node = slimeTransformer.toProgram(cst).body().stream().findFirst().orElse(null);
        return node == null ? List.of() : List.of(node);
    }

    private List<AstNode> createExportQinObjectNodes(SubhutiCst exportDeclaration) {
        boolean defaultExport = hasDirectToken(exportDeclaration, "Default", "default");
        SubhutiCst body = findFirstByName(exportDeclaration, "QinObjectDeclarationBody");
        return createQinObjectNodes(body, true, defaultExport, exportDeclaration);
    }

    private List<AstNode> createQinObjectNodes(
            SubhutiCst qinObject,
            boolean exported,
            boolean defaultExport) {
        return createQinObjectNodes(qinObject, exported, defaultExport, qinObject);
    }

    private List<AstNode> createQinObjectNodes(
            SubhutiCst qinObject,
            boolean exported,
            boolean defaultExport,
            SubhutiCst wrapper) {
        ObjectParts parts = createObjectParts(qinObject, wrapper);
        AstNode publicDeclaration = parts.singletonDeclaration();
        if (exported) {
            if (defaultExport) {
                AstNode defaultDeclaration = new ExportDefaultDeclaration(
                        parts.singletonIdentifier(),
                        token("Export", "export", findFirstByName(wrapper, "Export")),
                        token("Default", "default", findFirstByName(wrapper, "Default")),
                        SlimeAstCreateUtils.resolveSourceLocation(wrapper),
                        null);
                return List.of(parts.internalClass(), parts.singletonDeclaration(), defaultDeclaration);
            } else {
                publicDeclaration = new ExportNamedDeclaration(
                        parts.singletonDeclaration(),
                        List.of(),
                        null,
                        false,
                        token("Export", "export", findFirstByName(wrapper, "Export")),
                        null,
                        null,
                        null,
                        null,
                        null,
                        SlimeAstCreateUtils.resolveSourceLocation(wrapper));
            }
        }
        return List.of(parts.internalClass(), publicDeclaration);
    }

    private ObjectParts createObjectParts(SubhutiCst qinObject, SubhutiCst wrapper) {
        SubhutiCst body = "QinObjectDeclarationBody".equals(qinObject.getName())
                ? qinObject
                : findFirstByName(qinObject, "QinObjectDeclarationBody");
        if (body == null) {
            body = qinObject;
        }
        Identifier publicId = firstIdentifierAfterObjectKeyword(body);
        if (publicId == null || publicId.name() == null || publicId.name().isBlank()) {
            throw new IllegalArgumentException("Qin object declaration must have a binding identifier");
        }
        Identifier internalId = SlimeAstCreateUtils.createIdentifier(
                OBJECT_INTERNAL_PREFIX + publicId.name(),
                publicId.location());
        ClassDeclaration classDeclaration = createInternalClass(body, wrapper, internalId);
        NewExpression initializer = new NewExpression(
                internalId,
                List.of(),
                true,
                publicId.location());
        VariableDeclarator declarator = new VariableDeclarator(publicId, initializer, publicId.location());
        VariableDeclaration singleton = new VariableDeclaration(
                "const",
                List.of(declarator),
                SlimeAstCreateUtils.resolveSourceLocation(qinObject));
        return new ObjectParts(classDeclaration, singleton, publicId);
    }

    private ClassDeclaration createInternalClass(SubhutiCst body, SubhutiCst wrapper, Identifier internalId) {
        ClassDeclaration classDeclaration = (ClassDeclaration) slimeTransformer.createClassDeclarationAst(body);
        var decorators = classDeclaration.decorators();
        SubhutiCst decoratorNode = findFirstByName(wrapper, "TSDecorators");
        if (decoratorNode != null) {
            decorators = slimeTransformer.createDecoratorsAst(decoratorNode);
        }
        return SlimeAstCreateUtils.createClassDeclaration(
                internalId,
                classDeclaration.superClass(),
                classDeclaration.body(),
                decorators,
                classDeclaration.typeParameters(),
                classDeclaration.implementsTypes(),
                classDeclaration.location());
    }

    private Identifier firstIdentifierAfterObjectKeyword(SubhutiCst cst) {
        boolean[] seenObjectKeyword = new boolean[] { false };
        return firstIdentifierAfterObjectKeyword(cst, seenObjectKeyword, identitySet());
    }

    private Identifier firstIdentifierAfterObjectKeyword(
            SubhutiCst cst,
            boolean[] seenObjectKeyword,
            Set<SubhutiCst> visited) {
        if (cst == null || !visited.add(cst)) {
            return null;
        }
        if ("IdentifierName".equals(cst.getName()) && "object".equals(cst.getValue())) {
            seenObjectKeyword[0] = true;
            return null;
        }
        if (seenObjectKeyword[0] && "BindingIdentifier".equals(cst.getName())) {
            SubhutiCst identifier = findFirstByName(cst, "Identifier");
            return slimeTransformer.createIdentifierAst(identifier == null ? cst : identifier);
        }
        for (SubhutiCst child : safeChildren(cst)) {
            Identifier found = firstIdentifierAfterObjectKeyword(child, seenObjectKeyword, visited);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private static boolean containsName(SubhutiCst cst, String name) {
        return containsName(cst, name, identitySet());
    }

    private static boolean containsName(SubhutiCst cst, String name, Set<SubhutiCst> visited) {
        if (cst == null || !visited.add(cst)) {
            return false;
        }
        if (name.equals(cst.getName())) {
            return true;
        }
        for (SubhutiCst child : safeChildren(cst)) {
            if (containsName(child, name, visited)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasDirectToken(SubhutiCst cst, String tokenName, String value) {
        for (SubhutiCst child : safeChildren(cst)) {
            if (tokenName.equals(child.getName()) || value.equals(child.getValue())) {
                return true;
            }
        }
        return false;
    }

    private static SubhutiCst directChildByName(SubhutiCst cst, String name) {
        for (SubhutiCst child : safeChildren(cst)) {
            if (name.equals(child.getName())) {
                return child;
            }
        }
        return null;
    }

    private static SubhutiCst findFirstByName(SubhutiCst cst, String name) {
        return findFirstByName(cst, name, identitySet());
    }

    private static SubhutiCst findFirstByName(SubhutiCst cst, String name, Set<SubhutiCst> visited) {
        if (cst == null || name == null) {
            return null;
        }
        if (!visited.add(cst)) {
            return null;
        }
        if (name.equals(cst.getName())) {
            return cst;
        }
        for (SubhutiCst child : safeChildren(cst)) {
            SubhutiCst found = findFirstByName(child, name, visited);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private static SyntaxToken token(String type, String value, SubhutiCst cst) {
        return cst == null ? null : SlimeAstCreateUtils.createSyntaxToken(type, value, cst);
    }

    private static List<SubhutiCst> safeChildren(SubhutiCst cst) {
        List<SubhutiCst> children = cst == null ? null : cst.getChildren();
        return children == null ? List.of() : children;
    }

    private static Set<SubhutiCst> identitySet() {
        return Collections.newSetFromMap(new IdentityHashMap<>());
    }

    private record ObjectParts(
            ClassDeclaration internalClass,
            VariableDeclaration singletonDeclaration,
            Identifier singletonIdentifier) {
    }
}
