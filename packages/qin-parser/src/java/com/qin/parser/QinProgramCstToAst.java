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
import com.slime.ast.nodes.modules.ImportDeclaration;
import com.slime.ast.nodes.modules.ExportDefaultDeclaration;
import com.slime.ast.nodes.modules.ExportNamedDeclaration;
import com.slime.parser.cstToAst.SlimeAstCreateUtils;
import com.slime.parser.cstToAst.SlimeCstToAstUtils;
import com.slime.parser.cstToAst.module.SlimeImportCstToAst;
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
        if ("ExportDeclaration".equals(name) && containsName(cst, "QinObjectDeclarationBody")) {
            body.addAll(createExportQinObjectNodes(cst));
            return;
        }
        if ("QinObjectDeclaration".equals(name)) {
            body.addAll(createQinObjectNodes(cst, false, false));
            return;
        }
        if ("ModuleBody".equals(name) || "ModuleItemList".equals(name)) {
            collectModuleBody(cst, body, visited);
            return;
        }
        if ("StatementListItem".equals(name) || "ModuleItem".equals(name)
                || "ImportDeclaration".equals(name) || "ExportDeclaration".equals(name)) {
            appendAstNodes(createModuleItemAst(cst), body);
            return;
        }
        if ("Declaration".equals(name) || "Statement".equals(name)
                || "ExpressionStatement".equals(name) || "LexicalDeclaration".equals(name)
                || "VariableDeclaration".equals(name)
                || "VariableStatement".equals(name)) {
            appendAstNodes(slimeTransformer.createStatementListItemAst(cst), body);
            return;
        }
        for (SubhutiCst child : safeChildren(cst)) {
            collectBody(child, body, visited);
        }
    }

    private void collectModuleBody(SubhutiCst moduleBody, List<AstNode> body, Set<SubhutiCst> visited) {
        List<SubhutiCst> children = safeChildren(moduleBody);
        for (int i = 0; i < children.size(); i++) {
            SubhutiCst child = children.get(i);
            if (isImportStart(child)) {
                int end = findImportStatementEnd(children, i);
                appendAstNodes(createImportDeclarationAst(children.subList(i, end + 1)), body);
                i = end;
                continue;
            }
            collectBody(child, body, visited);
        }
    }

    private List<AstNode> createModuleItemAst(SubhutiCst cst) {
        if ("ModuleItem".equals(cst.getName()) || "StatementListItem".equals(cst.getName())) {
            List<AstNode> nodes = new ArrayList<>();
            for (SubhutiCst child : safeChildren(cst)) {
                appendAstNodes(createModuleItemAst(child), nodes);
            }
            return nodes;
        }
        if ("ImportDeclaration".equals(cst.getName())) {
            return List.of((AstNode) SlimeImportCstToAst.createImportDeclarationAst(cst));
        }
        SubhutiCst exportDeclaration = directChildByName(cst, "ExportDeclaration");
        if (exportDeclaration != null && containsName(exportDeclaration, "QinObjectDeclarationBody")) {
            return createExportQinObjectNodes(exportDeclaration);
        }

        SubhutiCst qinObject = findFirstByName(cst, "QinObjectDeclaration");
        if (qinObject != null) {
            return createQinObjectNodes(qinObject, false, false);
        }

        AstNode node = asAstNode(slimeTransformer.createStatementListItemAst(cst));
        return node == null ? List.of() : List.of(node);
    }

    private List<AstNode> createImportDeclarationAst(List<SubhutiCst> moduleBodyChildren) {
        if (moduleBodyChildren == null || moduleBodyChildren.isEmpty()) {
            return List.of();
        }

        SubhutiCst importToken = moduleBodyChildren.get(0);
        SubhutiCst namedImports = null;
        SubhutiCst fromToken = null;
        SubhutiCst stringLiteral = null;
        SubhutiCst semicolon = null;
        List<SubhutiCst> importClauseChildren = new ArrayList<>();
        for (int i = 1; i < moduleBodyChildren.size(); i++) {
            SubhutiCst child = moduleBodyChildren.get(i);
            String childName = child.getName();
            String childValue = child.getValue();
            if ("IdentifierName".equals(childName) && "from".equals(childValue)) {
                fromToken = child;
                continue;
            }
            if ("StringLiteral".equals(childName)) {
                stringLiteral = child;
                continue;
            }
            if ("Semicolon".equals(childName) || "SemicolonASI".equals(childName)) {
                semicolon = child;
                continue;
            }
            if ("NamedImports".equals(childName)) {
                namedImports = child;
                continue;
            }
            importClauseChildren.add(child);
        }

        if (stringLiteral == null) {
            return List.of();
        }

        SubhutiCst moduleSpecifier = SubhutiCst.builder()
                .name("ModuleSpecifier")
                .addChild(stringLiteral)
                .build();
        SubhutiCst fromClause = SubhutiCst.builder()
                .name("FromClause")
                .addChild(fromToken == null ? SubhutiCst.builder().name("IdentifierName").value("from").build() : fromToken)
                .addChild(moduleSpecifier)
                .build();
        if (namedImports != null) {
            importClauseChildren.add(buildNamedImportsAst(namedImports));
        }
        SubhutiCst importClause = SubhutiCst.builder()
                .name("ImportClause")
                .children(importClauseChildren)
                .build();
        SubhutiCst importDeclaration = SubhutiCst.builder()
                .name("ImportDeclaration")
                .addChild(importToken)
                .addChild(importClause)
                .addChild(fromClause)
                .addChild(semicolon)
                .build();
        Object node = SlimeImportCstToAst.createImportDeclarationAst(importDeclaration);
        return node == null ? List.of() : List.of(asAstNode(node));
    }

    private SubhutiCst buildNamedImportsAst(SubhutiCst rawNamedImports) {
        SubhutiCst rawImportsList = rawNamedImports == null ? null : findFirstByName(rawNamedImports, "ImportsList");
        List<SubhutiCst> specifiers = new ArrayList<>();
        List<SubhutiCst> rawItems = safeChildren(rawImportsList);
        for (int i = 0; i < rawItems.size(); ) {
            SubhutiCst item = rawItems.get(i);
            String itemName = item.getName();
            String itemValue = item.getValue();
            if ("Comma".equals(itemName)) {
                i++;
                continue;
            }
            if (i + 2 < rawItems.size()
                    && "IdentifierName".equals(itemName)
                    && "IdentifierName".equals(rawItems.get(i + 1).getName())
                    && "as".equals(rawItems.get(i + 1).getValue())
                    && "ImportedBinding".equals(rawItems.get(i + 2).getName())) {
                SubhutiCst moduleExportName = SubhutiCst.builder()
                        .name("ModuleExportName")
                        .addChild(item)
                        .build();
                SubhutiCst importSpecifier = SubhutiCst.builder()
                        .name("ImportSpecifier")
                        .addChild(moduleExportName)
                        .addChild(rawItems.get(i + 2))
                        .addChild(rawItems.get(i + 1))
                        .build();
                specifiers.add(importSpecifier);
                i += 3;
                continue;
            }
            if ("ImportedBinding".equals(itemName)) {
                SubhutiCst importSpecifier = SubhutiCst.builder()
                        .name("ImportSpecifier")
                        .addChild(item)
                        .build();
                specifiers.add(importSpecifier);
                i++;
                continue;
            }
            if ("IdentifierName".equals(itemName) && "as".equals(itemValue)) {
                i++;
                continue;
            }
            i++;
        }
        SubhutiCst importsList = SubhutiCst.builder()
                .name("ImportsList")
                .children(specifiers)
                .build();
        return SubhutiCst.builder()
                .name("NamedImports")
                .addChild(SubhutiCst.builder().name("LBrace").value("{").build())
                .addChild(importsList)
                .addChild(SubhutiCst.builder().name("RBrace").value("}").build())
                .build();
    }

    private void appendAstNodes(Object value, List<AstNode> body) {
        if (value instanceof AstNode astNode) {
            body.add(astNode);
            return;
        }
        if (value instanceof List<?> nodes) {
            for (Object node : nodes) {
                appendAstNodes(node, body);
            }
            return;
        }
        if (value instanceof Object[] nodes) {
            for (Object node : nodes) {
                appendAstNodes(node, body);
            }
        }
    }

    private AstNode asAstNode(Object node) {
        if (node instanceof AstNode astNode) {
            return astNode;
        }
        return null;
    }

    private boolean isImportStart(SubhutiCst cst) {
        return cst != null && "Import".equals(cst.getName()) && "import".equals(cst.getValue());
    }

    private int findImportStatementEnd(List<SubhutiCst> children, int startIndex) {
        for (int i = startIndex + 1; i < children.size(); i++) {
            String name = children.get(i).getName();
            if ("Semicolon".equals(name) || "SemicolonASI".equals(name)) {
                return i;
            }
        }
        for (int i = startIndex + 1; i < children.size(); i++) {
            String name = children.get(i).getName();
            if ("Import".equals(name) || "StatementListItem".equals(name) || "ExportDeclaration".equals(name)) {
                return i - 1;
            }
        }
        return children.size() - 1;
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
        if (seenObjectKeyword[0] && "QinObjectName".equals(cst.getName())) {
            SubhutiCst identifier = findFirstByName(cst, "IdentifierName");
            return slimeTransformer.createIdentifierAst(identifier == null ? cst : identifier);
        }
        if (seenObjectKeyword[0] && "IdentifierName".equals(cst.getName())) {
            return slimeTransformer.createIdentifierAst(cst);
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
