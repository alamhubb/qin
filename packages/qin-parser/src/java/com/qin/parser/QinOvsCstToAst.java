package com.qin.parser;

import com.slime.ast.AstNode;
import com.qin.parser.OvsSourceScopes.RuntimeBinding;
import com.slime.ast.Expression;
import com.slime.ast.Statement;
import com.slime.ast.Pattern;
import com.slime.ast.SourceLocation;
import com.slime.ast.TypeOnlyDeclarations;
import com.slime.ast.nodes.expressions.ArrayExpression;
import com.slime.ast.nodes.expressions.CallExpression;
import com.slime.ast.nodes.expressions.Identifier;
import com.slime.ast.nodes.expressions.MemberExpression;
import com.slime.ast.nodes.expressions.Literal;
import com.slime.ast.nodes.expressions.AssignmentExpression;
import com.slime.ast.nodes.expressions.UpdateExpression;
import com.slime.ast.nodes.expressions.UnaryExpression;
import com.slime.ast.nodes.expressions.BinaryExpression;
import com.slime.ast.nodes.expressions.LogicalExpression;
import com.slime.ast.nodes.expressions.ConditionalExpression;
import com.slime.ast.nodes.expressions.ParenthesizedExpression;
import com.slime.ast.nodes.misc.Program;
import com.slime.ast.nodes.misc.SpreadElement;
import com.slime.ast.nodes.misc.VariableDeclarator;
import com.slime.ast.nodes.statements.ExpressionStatement;
import com.slime.ast.nodes.statements.BlockStatement;
import com.slime.ast.nodes.statements.IfStatement;
import com.slime.ast.nodes.statements.WhileStatement;
import com.slime.ast.nodes.statements.DoWhileStatement;
import com.slime.ast.nodes.statements.ForStatement;
import com.slime.ast.nodes.statements.ForInStatement;
import com.slime.ast.nodes.statements.ForOfStatement;
import com.slime.ast.nodes.statements.LabeledStatement;
import com.slime.ast.nodes.statements.SwitchStatement;
import com.slime.ast.nodes.statements.TryStatement;
import com.slime.ast.nodes.misc.SwitchCase;
import com.slime.ast.nodes.misc.CatchClause;
import com.slime.ast.nodes.declarations.ClassDeclaration;
import com.slime.ast.nodes.declarations.FunctionDeclaration;
import com.slime.ast.nodes.declarations.VariableDeclaration;
import com.slime.ast.nodes.expressions.ArrowFunctionExpression;
import com.slime.ast.nodes.modules.ExportAllDeclaration;
import com.slime.ast.nodes.modules.ExportDefaultDeclaration;
import com.slime.ast.nodes.modules.ExportNamedDeclaration;
import com.slime.ast.nodes.modules.ImportDeclaration;
import com.slime.ast.nodes.misc.ExportSpecifier;
import com.slime.ast.nodes.misc.ImportSpecifier;
import com.slime.ast.nodes.typescript.TSTypeAssertion;
import com.slime.ast.nodes.typescript.TSAsExpression;
import com.slime.parser.cstToAst.SlimeAstCreateUtils;
import com.slime.parser.cstToAst.SlimeCstToAstUtils;
import com.subhuti.struct.SubhutiCst;
import com.subhuti.struct.SubhutiSourceLocation;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.LinkedHashSet;

import static com.qin.parser.OvsSourceScopes.RuntimeBinding.*;

/** Java-native OVS CST-to-AST visitor. It reuses the Java Slime visitor for standard syntax. */
public final class QinOvsCstToAst extends SlimeCstToAstUtils {
    private boolean usesOvsSyntax;
    private boolean usesCsstsSyntax;
    private SubhutiCst sourceRoot;
    private OvsSourceScopes sourceScopes;
    private final EnumMap<RuntimeBinding, String> runtimeNames = new EnumMap<>(RuntimeBinding.class);
    private String renderChildrenName;
    private int protectedRenderDepth;
    private final Set<String> usedCsstsAtoms = new LinkedHashSet<>();
    private Set<String> configuredPseudoAtoms = Set.of();
    private Set<String> configuredClassGroups = Set.of();
    private static final Set<String> HTML_TAGS = Set.of(
            "a", "abbr", "address", "area", "article", "aside", "audio", "b", "base", "bdi", "bdo",
            "blockquote", "body", "br", "button", "canvas", "caption", "cite", "code", "col", "colgroup",
            "data", "datalist", "dd", "del", "details", "dfn", "dialog", "div", "dl", "dt", "em", "embed",
            "fieldset", "figcaption", "figure", "footer", "form", "h1", "h2", "h3", "h4", "h5", "h6",
            "head", "header", "hgroup", "hr", "html", "i", "iframe", "img", "input", "ins", "kbd", "label",
            "legend", "li", "link", "main", "map", "mark", "menu", "meta", "meter", "nav", "noscript",
            "object", "ol", "optgroup", "option", "output", "p", "picture", "pre", "progress", "q", "rp",
            "rt", "ruby", "s", "samp", "script", "section", "select", "slot", "small", "source", "span",
            "strong", "style", "sub", "summary", "sup", "svg", "table", "tbody", "td", "template",
            "textarea", "tfoot", "th", "thead", "time", "title", "tr", "track", "u", "ul", "var", "video", "wbr");

    public Program toOvsProgram(SubhutiCst cst) {
        resetState();
        usesOvsSyntax = false;
        usesCsstsSyntax = false;
        usedCsstsAtoms.clear();
        runtimeNames.clear();
        sourceRoot = cst;
        renderChildrenName = null;
        Object ast;
        try {
            ast = super.createProgramAst(cst);
        } finally {
            sourceRoot = null;
            sourceScopes = null;
        }
        if (!(ast instanceof Program program)) {
            throw new IllegalStateException("Java Slime visitor did not produce Program");
        }
        return program;
    }

    public Program toOvsFileAst(SubhutiCst cst) {
        Program program = eraseTypeOnlyModuleItems(toOvsProgram(cst));
        if (!usesOvsSyntax && !usesCsstsSyntax) return program;

        List<AstNode> imports = new ArrayList<>();
        List<AstNode> other = new ArrayList<>();
        boolean hasExport = false;
        boolean hasExpression = false;
        for (AstNode node : program.body()) {
            if (node instanceof ImportDeclaration) {
                imports.add(node);
            } else {
                other.add(node);
                hasExport |= node instanceof ExportDefaultDeclaration || node instanceof ExportNamedDeclaration
                        || node instanceof ExportAllDeclaration;
                hasExpression |= node instanceof ExpressionStatement;
            }
        }
        ensureRuntimeImports(imports);

        List<AstNode> output = new ArrayList<>(imports);
        if (usesOvsSyntax && !hasExport && hasExpression) {
            List<Statement> functionBody = implicitRenderBody(other, cst);
            BlockStatement block = SlimeAstCreateUtils.createBlockStatement(functionBody.toArray(Statement[]::new), (SubhutiSourceLocation) null);
            ArrowFunctionExpression factory = SlimeAstCreateUtils.createArrowFunctionExpression(
                    new com.slime.ast.Pattern[] { SlimeAstCreateUtils.createIdentifier("props", (SubhutiSourceLocation) null) },
                    block, false, false, (SubhutiSourceLocation) null);
            CallExpression define = SlimeAstCreateUtils.createCallExpression(
                    runtimeIdentifier(COMPONENT, null),
                    new Expression[] { factory }, (SubhutiSourceLocation) null);
            output.add(new ExportDefaultDeclaration(define, (SourceLocation) null));
        } else {
            output.addAll(other);
        }
        return SlimeAstCreateUtils.createProgram(output.toArray(), "module", location(cst));
    }

    private List<Statement> implicitRenderBody(List<AstNode> nodes, SubhutiCst root) {
        List<Statement> body = new ArrayList<>();
        List<Expression> tail = new ArrayList<>();
        Identifier results = null;
        for (AstNode node : nodes) {
            if (node instanceof ExpressionStatement expression
                    && !isSideEffectExpression(expression.expression())) {
                if (results == null) tail.add(expression.expression());
                else body.add(appendToChildren(results, expression.expression(), expression.location()));
                continue;
            }
            if (!(node instanceof Statement statement)) {
                throw new IllegalStateException("Top-level OVS component body contains a non-statement node: " + node.type());
            }
            if (!tail.isEmpty()) {
                results = renderChildrenIdentifier(root);
                var initial = SlimeAstCreateUtils.createArrayExpression(tail.toArray(Expression[]::new));
                var declaration = SlimeAstCreateUtils.createVariableDeclarator(results, initial, (SourceLocation) null);
                body.add(SlimeAstCreateUtils.createVariableDeclaration(
                        new VariableDeclarator[] { declaration }, "const", (SourceLocation) null));
                tail.clear();
            }
            body.add(statement);
        }
        Expression result = results != null ? results
                : tail.size() == 1 ? tail.getFirst()
                : SlimeAstCreateUtils.createArrayExpression(tail.toArray(Expression[]::new));
        body.add(SlimeAstCreateUtils.createReturnStatement(result, (SourceLocation) null));
        return body;
    }

    private static Program eraseTypeOnlyModuleItems(Program program) {
        List<AstNode> statements = program.body();
        List<AstNode> filtered = null;
        for (int index = 0; index < statements.size(); index++) {
            AstNode original = statements.get(index);
            AstNode runtime = switch (original) {
                case ImportDeclaration declaration -> runtimeImport(declaration);
                case ExportAllDeclaration declaration when declaration.typeOnly() -> null;
                case ExportNamedDeclaration declaration -> runtimeNamedExport(declaration);
                case ExportDefaultDeclaration declaration when TypeOnlyDeclarations.isTypeOnly(declaration.declaration()) -> null;
                default -> TypeOnlyDeclarations.isTypeOnly(original) ? null : original;
            };
            if (filtered == null && original != runtime) {
                filtered = new ArrayList<>(statements.subList(0, index));
            }
            if (filtered != null && runtime != null) filtered.add(runtime);
        }
        return filtered == null ? program : new Program(program.sourceType(), filtered, program.location());
    }

    private static ImportDeclaration runtimeImport(ImportDeclaration declaration) {
        if (declaration.typeOnly()) return null;
        List<AstNode> specifiers = declaration.specifiers();
        List<AstNode> runtime = null;
        for (int index = 0; index < specifiers.size(); index++) {
            AstNode specifier = specifiers.get(index);
            if (specifier instanceof ImportSpecifier named && named.typeOnly()) {
                if (runtime == null) runtime = new ArrayList<>(specifiers.subList(0, index));
            } else if (runtime != null) {
                runtime.add(specifier);
            }
        }
        if (runtime == null) return declaration;
        if (runtime.isEmpty()) return null;
        return new ImportDeclaration(declaration.source(), runtime, declaration.importToken(), declaration.fromToken(),
                declaration.lBraceToken(), declaration.rBraceToken(), declaration.semicolonToken(),
                declaration.attributesCode(), false, declaration.location());
    }

    private static ExportNamedDeclaration runtimeNamedExport(ExportNamedDeclaration declaration) {
        if (declaration.typeOnly() || TypeOnlyDeclarations.isTypeOnly(declaration.declaration())) return null;
        List<ExportSpecifier> specifiers = declaration.specifiers();
        List<ExportSpecifier> runtime = null;
        for (int index = 0; index < specifiers.size(); index++) {
            ExportSpecifier specifier = specifiers.get(index);
            if (specifier.typeOnly()) {
                if (runtime == null) runtime = new ArrayList<>(specifiers.subList(0, index));
            } else if (runtime != null) {
                runtime.add(specifier);
            }
        }
        if (runtime == null) return declaration;
        if (runtime.isEmpty()) return null;
        return new ExportNamedDeclaration(declaration.declaration(), runtime, declaration.source(), false,
                declaration.exportToken(), declaration.fromToken(), declaration.lBraceToken(), declaration.rBraceToken(),
                declaration.semicolonToken(), declaration.attributesCode(), declaration.location());
    }

    public Set<String> usedCsstsAtoms() {
        return Set.copyOf(usedCsstsAtoms);
    }

    public void configureCsstsAtoms(Set<String> pseudoAtoms, Set<String> classGroups) {
        configuredPseudoAtoms = pseudoAtoms == null ? Set.of() : Set.copyOf(pseudoAtoms);
        configuredClassGroups = classGroups == null ? Set.of() : Set.copyOf(classGroups);
    }

    private OvsSourceScopes sourceScopes() {
        if (sourceScopes == null && sourceRoot != null) {
            sourceScopes = new OvsSourceScopes(sourceRoot);
            for (var binding : RuntimeBinding.values()) runtimeNames.put(binding, sourceScopes.runtimeName(binding));
        }
        return sourceScopes;
    }

    private Identifier runtimeIdentifier(RuntimeBinding binding, SourceLocation location) {
        return new Identifier(runtimeName(binding), location);
    }

    private String runtimeName(RuntimeBinding binding) {
        sourceScopes();
        return runtimeNames.getOrDefault(binding, binding.name);
    }

    private ImportSpecifier runtimeSpecifier(RuntimeBinding binding) {
        return new ImportSpecifier(new Identifier(binding.name, null), runtimeIdentifier(binding, null),
                (SourceLocation) null);
    }

    private static ImportDeclaration createRuntimeImport(String moduleName, List<AstNode> specifiers) {
        var module = SlimeAstCreateUtils.createStringLiteral(moduleName, (SourceLocation) null, "\"" + moduleName + "\"");
        return new ImportDeclaration(specifiers, module, (SourceLocation) null);
    }

    private ImportDeclaration createCsstsCssImport() {
        var module = SlimeAstCreateUtils.createStringLiteral("virtual:cssts.css", (SourceLocation) null, "\"virtual:cssts.css\"");
        return new ImportDeclaration(List.of(), module, (SourceLocation) null);
    }

    private void ensureRuntimeImports(List<AstNode> imports) {
        if (usesOvsSyntax) {
            List<AstNode> missing = new ArrayList<>();
            for (var binding : OVS) {
                if (!hasRuntimeBinding(imports, binding)) missing.add(runtimeSpecifier(binding));
            }
            if (!missing.isEmpty()) imports.add(createRuntimeImport(HTML_TAG.module, missing));
        }
        if (!usesCsstsSyntax) return;
        if (!hasRuntimeBinding(imports, CSSTS)) imports.add(createRuntimeImport(CSSTS.module, List.of(runtimeSpecifier(CSSTS))));
        if (!hasImportFrom(imports, "virtual:cssts.css")) imports.add(createCsstsCssImport());
        if (!hasRuntimeBinding(imports, ATOM)) imports.add(createRuntimeImport(ATOM.module, List.of(runtimeSpecifier(ATOM))));
    }

    private static boolean hasImportFrom(List<AstNode> imports, String moduleName) {
        for (AstNode node : imports) {
            if (node instanceof ImportDeclaration declaration
                    && declaration.source() != null
                    && moduleName.equals(declaration.source().value())) return true;
        }
        return false;
    }

    private boolean hasRuntimeBinding(List<AstNode> imports, RuntimeBinding binding) {
        if (!binding.name.equals(runtimeNames.getOrDefault(binding, binding.name))) return false;
        for (AstNode node : imports) {
            if (!(node instanceof ImportDeclaration declaration)
                    || declaration.typeOnly()
                    || declaration.source() == null) continue;
            for (AstNode specifier : declaration.specifiers()) {
                if (specifier instanceof ImportSpecifier named
                        && !named.typeOnly()
                        && binding.matches(String.valueOf(declaration.source().value()), named)) return true;
            }
        }
        return false;
    }

    private VariableDeclaration createOvsViewDeclaration(SubhutiCst view) {
        usesOvsSyntax = true;
        List<SubhutiCst> parts = children(view);
        if (parts.size() < 2) throw new IllegalArgumentException("OVS view declaration is missing its name");
        String name = firstTokenValue(parts.get(1));
        if (name == null || name.isBlank()) throw new IllegalArgumentException("OVS view declaration name is empty");
        Identifier componentName = SlimeAstCreateUtils.createIdentifier(name, location(view));

        SubhutiCst formalParameters = findDirectChild(view, "ArrowFormalParameters");
        Pattern[] parameters = formalParameters == null ? new Pattern[0] : createFormalParametersAst(formalParameters);
        if (parameters.length == 0) {
            parameters = new Pattern[] { SlimeAstCreateUtils.createIdentifier("props", (SubhutiSourceLocation) null) };
        }
        List<Statement> body = new ArrayList<>();
        SubhutiCst statementList = findDirectChild(view, "StatementList");
        if (statementList != null) {
            for (SubhutiCst item : children(statementList)) {
                Object converted = createStatementListItemAst(item);
                if (converted instanceof Statement statement) body.add(statement);
                else if (converted instanceof List<?> list) {
                    for (Object node : list) if (node instanceof Statement statement) body.add(statement);
                }
            }
        }
        if (!body.isEmpty() && body.getLast() instanceof ExpressionStatement expressionStatement) {
            body.set(body.size() - 1, SlimeAstCreateUtils.createReturnStatement(
                    expressionStatement.expression(), location(view)));
        }
        BlockStatement functionBody = SlimeAstCreateUtils.createBlockStatement(body.toArray(Statement[]::new), location(view));
        ArrowFunctionExpression factory = SlimeAstCreateUtils.createArrowFunctionExpression(
                parameters, functionBody, false, false, location(view));
        CallExpression definition = SlimeAstCreateUtils.createCallExpression(
                runtimeIdentifier(COMPONENT, null),
                new Expression[] { factory }, location(view));
        VariableDeclarator declarator = SlimeAstCreateUtils.createVariableDeclarator(componentName, definition, location(view));
        return SlimeAstCreateUtils.createVariableDeclaration(new VariableDeclarator[] { declarator }, "const", location(view));
    }

    @Override
    public Object createStatementListItemAst(SubhutiCst cst) {
        SubhutiCst view = findViewDeclaration(cst);
        if (view != null) return createOvsViewDeclaration(view);
        SubhutiCst render = findStatementRender(cst);
        if (render != null) {
            return SlimeAstCreateUtils.createExpressionStatement(createRenderCall(render), location(render));
        }
        SubhutiCst statement = findRenderBodyStatement(cst);
        if (statement != null && "NoRenderBlock".equals(statement.getName())) {
            return createNoRenderBodyBlock(statement, NoRenderContext.OUTSIDE_RENDER);
        }
        return super.createStatementListItemAst(cst);
    }

    private enum NoRenderContext {
        OUTSIDE_RENDER, RENDER_BODY, REACTIVE_BODY
    }

    private BlockStatement createNoRenderBodyBlock(SubhutiCst blockCst, NoRenderContext context) {
        List<Statement> statements = new ArrayList<>();
        SubhutiCst statementList = findFirst(blockCst, "StatementList");
        if (statementList != null) {
            for (SubhutiCst item : children(statementList)) {
                Statement statement = createNoRenderStatement(item, context);
                if (statement != null) statements.add(statement);
            }
        }
        return SlimeAstCreateUtils.createBlockStatement(statements.toArray(Statement[]::new), location(blockCst));
    }

    private Statement createNoRenderStatement(SubhutiCst cst, NoRenderContext context) {
        if (cst == null) return null;
        String name = cst.getName();
        boolean insideRenderBody = context != NoRenderContext.OUTSIDE_RENDER;
        if (insideRenderBody && isStatementWrapper(name)) {
            SubhutiCst control = findRenderBodyStatement(cst);
            if (control != null) return createNoRenderStatement(control, context);
        }
        SubhutiCst renderStatement = findStatementRender(cst);
        if (renderStatement != null) {
            Expression render = createRenderCall(renderStatement, insideRenderBody && protectedRenderDepth > 0);
            return insideRenderBody
                    ? appendToChildren(render, renderStatement)
                    : SlimeAstCreateUtils.createExpressionStatement(render, location(renderStatement));
        }
        if ("ExpressionStatement".equals(name)) return super.createExpressionStatementAst(cst);
        if (insideRenderBody) {
            Statement control = switch (name) {
                case "IfStatement" -> createNoRenderIfStatement(cst);
                case "SwitchStatement" -> createNoRenderSwitchStatement(cst);
                case "TryStatement" -> createNoRenderTryStatement(cst);
                case "LabelledStatement" -> createRenderLabeledStatement(cst, true);
                default -> isLoopStatement(name) ? createNoRenderLoopStatement(cst) : null;
            };
            if (control != null) {
                return context == NoRenderContext.RENDER_BODY ? createReactiveStatement(control, cst) : control;
            }
        }
        if ("NoRenderBlock".equals(name) || "Block".equals(name)
                || "BlockStatement".equals(name) || "StatementList".equals(name)) {
            return createNoRenderBodyBlock(cst, context);
        }
        if (isStatementWrapper(name)) {
            for (SubhutiCst child : children(cst)) {
                Statement statement = createNoRenderStatement(child, context);
                if (statement != null) return statement;
            }
            return null;
        }
        if (name.endsWith("Statement") || "LexicalDeclaration".equals(name)
                || "VariableStatement".equals(name) || "Declaration".equals(name)) {
            return convertStatement(cst);
        }
        return null;
    }

    private static SubhutiCst findRenderBodyStatement(SubhutiCst node) {
        if (node == null) return null;
        String name = node.getName();
        if ("IfStatement".equals(name) || "SwitchStatement".equals(name) || "TryStatement".equals(name)
                || "NoRenderBlock".equals(name) || "LabelledStatement".equals(name) || isLoopStatement(name)) return node;
        if (!isStatementWrapper(name) && !"BreakableStatement".equals(name)
                && !"IterationStatement".equals(name)) return null;
        for (SubhutiCst child : children(node)) {
            SubhutiCst statement = findRenderBodyStatement(child);
            if (statement != null) return statement;
        }
        return null;
    }

    private Statement createNoRenderIfStatement(SubhutiCst conditional) {
        Expression test = null;
        Statement consequent = null;
        Statement alternate = null;
        boolean sawElse = false;
        for (SubhutiCst child : children(conditional)) {
            if ("Else".equals(child.getName()) || "else".equals(child.getValue())) {
                sawElse = true;
            } else if (test == null && "Expression".equals(child.getName())) {
                test = createExpressionAst(child);
            } else if (isIfBody(child.getName())) {
                Statement body = createNoRenderStatement(child, NoRenderContext.REACTIVE_BODY);
                if (sawElse) alternate = body;
                else consequent = body;
            }
        }
        if (test == null || consequent == null) {
            throw new IllegalArgumentException("OVS NoRender control flow is missing its condition or body");
        }
        return SlimeAstCreateUtils.createIfStatement(test, consequent, alternate, location(conditional));
    }

    private Statement createNoRenderLoopStatement(SubhutiCst loopCst) {
        Object value = super.createDeclarationAst(loopCst);
        if (!(value instanceof Statement loop)) {
            throw new IllegalStateException("Java Slime visitor did not create loop AST for NoRender body");
        }
        Statement body = createNoRenderStatement(findLoopBody(loopCst), NoRenderContext.REACTIVE_BODY);
        return replaceLoopBody(loop, body);
    }

    private Statement createNoRenderSwitchStatement(SubhutiCst switchCst) {
        Object value = super.createDeclarationAst(switchCst);
        if (!(value instanceof SwitchStatement standard)) {
            throw new IllegalStateException("Java Slime visitor did not create switch AST for NoRender body");
        }
        List<SubhutiCst> clauses = new ArrayList<>();
        collectSwitchClauses(switchCst, clauses);
        List<SwitchCase> cases = new ArrayList<>();
        for (int index = 0; index < standard.cases().size(); index++) {
            SwitchCase original = standard.cases().get(index);
            if (index >= clauses.size()) {
                cases.add(original);
                continue;
            }
            SubhutiCst statementList = findFirst(clauses.get(index), "StatementList");
            List<Statement> consequent = new ArrayList<>();
            if (statementList != null) {
                for (SubhutiCst item : children(statementList)) {
                    Statement statement = createNoRenderStatement(item, NoRenderContext.REACTIVE_BODY);
                    if (statement != null) consequent.add(statement);
                }
            }
            cases.add(new SwitchCase(original.test(), consequent, original.location()));
        }
        return new SwitchStatement(standard.discriminant(), cases, standard.location());
    }

    private Statement createNoRenderTryStatement(SubhutiCst tryCst) {
        Object value = super.createDeclarationAst(tryCst);
        if (!(value instanceof TryStatement standard)) {
            throw new IllegalStateException("Java Slime visitor did not create try AST for NoRender body");
        }
        SubhutiCst tryBlockCst = findDirectChild(tryCst, "Block");
        BlockStatement block = tryBlockCst == null ? standard.block()
                : transformProtectedRenderBlock(tryBlockCst, true);
        CatchClause handler = standard.handler();
        SubhutiCst catchCst = findDirectChild(tryCst, "Catch");
        if (handler != null && catchCst != null) {
            SubhutiCst catchBlock = findDirectChild(catchCst, "Block");
            if (catchBlock != null) {
                handler = new CatchClause(handler.param(), createNoRenderBodyBlock(catchBlock, NoRenderContext.REACTIVE_BODY),
                        handler.location());
            }
        }
        BlockStatement finalizer = standard.finalizer();
        SubhutiCst finallyCst = findDirectChild(tryCst, "Finally");
        if (finallyCst != null) {
            SubhutiCst finallyBlock = findDirectChild(finallyCst, "Block");
            if (finallyBlock != null) finalizer = createNoRenderBodyBlock(finallyBlock, NoRenderContext.REACTIVE_BODY);
        }
        return new TryStatement(block, handler, finalizer, standard.location());
    }

    private Statement convertStatement(SubhutiCst cst) {
        if (cst == null) return null;
        String name = cst.getName();
        if ("OvsRenderStatement".equals(name)) {
            usesOvsSyntax = true;
            return SlimeAstCreateUtils.createExpressionStatement(createRenderCall(cst), location(cst));
        }
        if ("IfStatement".equals(name)) return createIfStatementAst(cst);
        if ("BlockStatement".equals(name) || "Block".equals(name)) return createBlockStatementAst(cst);
        if (isStatementWrapper(name)) {
            for (SubhutiCst child : children(cst)) {
                Statement statement = convertStatement(child);
                if (statement != null) return statement;
            }
            return null;
        }
        if ("ExpressionStatement".equals(name)) return super.createExpressionStatementAst(cst);
        return (Statement) super.createDeclarationAst(cst);
    }

    @Override
    public BlockStatement createBlockStatementAst(SubhutiCst cst) {
        List<Statement> body = new ArrayList<>();
        collectOvsBlockStatements(cst, body);
        return SlimeAstCreateUtils.createBlockStatement(body.toArray(Statement[]::new), location(cst));
    }

    @Override
    public IfStatement createIfStatementAst(SubhutiCst cst) {
        Expression test = null;
        Statement consequent = null;
        Statement alternate = null;
        boolean sawElse = false;
        for (SubhutiCst child : children(cst)) {
            if ("Else".equals(child.getName()) || "else".equals(child.getValue())) {
                sawElse = true;
            } else if (test == null && "Expression".equals(child.getName())) {
                test = createExpressionAst(child);
            } else if (isIfBody(child.getName())) {
                Statement body = convertIfBody(child);
                if (sawElse) alternate = body;
                else consequent = body;
            }
        }
        if (consequent == null) throw new IllegalArgumentException("OVS if statement has no consequent body");
        return SlimeAstCreateUtils.createIfStatement(test, consequent, alternate, location(cst));
    }

    private Statement convertIfBody(SubhutiCst cst) {
        for (SubhutiCst child : children(cst)) {
            Statement statement = convertStatement(child);
            if (statement != null) return statement;
        }
        return null;
    }

    private void collectOvsBlockStatements(SubhutiCst cst, List<Statement> output) {
        if (cst == null) return;
        switch (cst.getName()) {
            case "BlockStatement", "Block", "FunctionBody", "GeneratorBody", "AsyncFunctionBody",
                    "AsyncGeneratorBody", "FunctionStatementList", "ClassStaticBlock",
                    "ClassStaticBlockBody", "ClassStaticBlockStatementList", "StatementList" -> {
                for (SubhutiCst child : children(cst)) collectOvsBlockStatements(child, output);
            }
            case "StatementListItem" -> {
                if (createStatementListItemAst(cst) instanceof Statement statement) output.add(statement);
            }
            default -> {
                // Braces and other block delimiters do not produce statements.
            }
        }
    }

    private static boolean isStatementWrapper(String name) {
        return "Statement".equals(name) || "StatementListItem".equals(name)
                || "ModuleItem".equals(name) || "ModuleStatementListItem".equals(name)
                || "LabelledItem".equals(name) || "IfStatementBody".equals(name);
    }

    private static boolean isIfBody(String name) {
        return "IfStatementBody".equals(name) || isStatementWrapper(name)
                || "Block".equals(name) || "BlockStatement".equals(name);
    }

    @Override
    public Expression createExtensionExpressionAst(SubhutiCst cst) {
        return switch (cst.getName()) {
            case "OvsRenderFunction" -> createRenderCall(cst);
            case "CssExpression" -> createCssExpression(cst);
            default -> null;
        };
    }

    @Override
    public Expression createExpressionAst(SubhutiCst cst) {
        if (cst != null) {
            Expression extension = createExtensionExpressionAst(cst);
            if (extension != null) return extension;
        }
        return super.createExpressionAst(cst);
    }

    private Expression createCssExpression(SubhutiCst css) {
        usesCsstsSyntax = true;
        OvsSourceScopes scopes = sourceScopes();
        OvsSourceScopes.Scope scope = scopes == null ? null : scopes.at(css);
        SubhutiCst styleObject = findDirectChild(css, "CssStyleObject");
        SubhutiCst atomList = styleObject == null ? null : findDirectChild(styleObject, "CssAtomList");
        List<Expression> atoms = new ArrayList<>();
        if (atomList != null) {
            for (SubhutiCst child : children(atomList)) {
                SubhutiCst expressionCst = "AssignmentExpression".equals(child.getName())
                        ? child : findFirst(child, "AssignmentExpression");
                if (expressionCst == null) continue;
                Expression expression = transformCsstsExpression(createExpressionAst(expressionCst), scope);
                // CSSTS merge expands class objects and arrays in argument order.
                atoms.add(expression);
            }
        }
        Identifier cssts = SlimeAstCreateUtils.createIdentifier(runtimeName(CSSTS), location(css));
        Identifier merge = SlimeAstCreateUtils.createIdentifier("merge", location(css));
        MemberExpression callee = SlimeAstCreateUtils.createMemberExpression(cssts, merge, false, false, location(css));
        return SlimeAstCreateUtils.createCallExpression(callee, atoms.toArray(Expression[]::new), location(css));
    }

    @Override
    public VariableDeclaration createVariableDeclarationAst(SubhutiCst cst) {
        var declaration = (VariableDeclaration) super.createVariableDeclarationAst(cst);
        if (!usesCsstsSyntax || declaration.declare()) return declaration;
        List<VariableDeclarator> updated = null;
        for (int index = 0; index < declaration.declarations().size(); index++) {
            var binding = declaration.declarations().get(index);
            if (!(binding.id() instanceof Identifier id)
                    || !(binding.init() instanceof CallExpression call)
                    || call.location() == null || !"CssExpression".equals(call.location().type())) continue;
            var variant = CsstsAtomNames.pseudoVariant(id.name());
            if (variant == null || variant.pseudos().stream()
                    .noneMatch(name -> CsstsAtomNames.isPseudoAtom(name, configuredPseudoAtoms))) continue;

            usedCsstsAtoms.add(id.name());
            var atom = new MemberExpression(runtimeIdentifier(ATOM, id.location()), id, false, false, id.location());
            List<Expression> arguments = new ArrayList<>(call.arguments().size() + 1);
            arguments.add(atom);
            arguments.addAll(call.arguments());
            var merged = new CallExpression(call.callee(), List.copyOf(arguments), call.optional(),
                    call.typeParameters(), call.location());
            if (updated == null) updated = new ArrayList<>(declaration.declarations());
            updated.set(index, new VariableDeclarator(id, merged, binding.typeAnnotation(), binding.location()));
        }
        return updated == null ? declaration : new VariableDeclaration(declaration.kind(), List.copyOf(updated),
                declaration.declare(), declaration.location());
    }

    private Expression transformCsstsExpression(Expression expression, OvsSourceScopes.Scope scope) {
        if (expression instanceof Identifier identifier) {
            String name = identifier.name();
            if (scope != null && scope.contains(name)) return expression;
            if (CsstsAtomNames.parseTsAtom(name) == null
                    && !CsstsAtomNames.isPseudoAtom(name, configuredPseudoAtoms)
                    && !CsstsAtomNames.isClassGroup(name, configuredClassGroups)) return expression;
            usedCsstsAtoms.add(name);
            Identifier object = runtimeIdentifier(ATOM, identifier.location());
            return new MemberExpression(object, identifier, false, false, identifier.location());
        }
        if (expression instanceof ParenthesizedExpression group) {
            return new ParenthesizedExpression(transformCsstsExpression(group.expression(), scope), group.location());
        }
        if (expression instanceof TSTypeAssertion assertion) {
            return new TSTypeAssertion(transformCsstsExpression(assertion.expression(), scope),
                    assertion.typeAnnotation(), assertion.location());
        }
        if (expression instanceof TSAsExpression assertion) {
            return new TSAsExpression(transformCsstsExpression(assertion.expression(), scope),
                    assertion.typeAnnotation(), assertion.location());
        }
        if (expression instanceof UnaryExpression unary) {
            return new UnaryExpression(unary.operator(), transformCsstsExpression(unary.argument(), scope),
                    unary.prefix(), unary.location());
        }
        if (expression instanceof BinaryExpression binary) {
            return new BinaryExpression(binary.operator(), transformCsstsExpression(binary.left(), scope),
                    transformCsstsExpression(binary.right(), scope), binary.location());
        }
        if (expression instanceof LogicalExpression logical) {
            return new LogicalExpression(logical.operator(), transformCsstsExpression(logical.left(), scope),
                    transformCsstsExpression(logical.right(), scope), logical.location());
        }
        if (expression instanceof ConditionalExpression conditional) {
            return new ConditionalExpression(transformCsstsExpression(conditional.test(), scope),
                    transformCsstsExpression(conditional.consequent(), scope),
                    transformCsstsExpression(conditional.alternate(), scope), conditional.location());
        }
        if (expression instanceof CallExpression call) {
            return new CallExpression(call.callee(), transformCsstsExpressions(call.arguments(), scope),
                    call.optional(), call.typeParameters(), call.location());
        }
        if (expression instanceof ArrayExpression array) {
            return new ArrayExpression(transformCsstsExpressions(array.elements(), scope), array.location());
        }
        if (expression instanceof SpreadElement spread) {
            return new SpreadElement(transformCsstsExpression(spread.argument(), scope), spread.location());
        }
        return expression;
    }

    private List<Expression> transformCsstsExpressions(List<Expression> expressions, OvsSourceScopes.Scope scope) {
        List<Expression> result = new ArrayList<>(expressions.size());
        for (Expression expression : expressions) result.add(transformCsstsExpression(expression, scope));
        return result;
    }

    private Expression createRenderCall(SubhutiCst render) {
        return createRenderCall(render, false);
    }

    private Expression createRenderCall(SubhutiCst render, boolean immediate) {
        usesOvsSyntax = true;
        sourceScopes();
        List<SubhutiCst> children = children(render);
        if (children.isEmpty()) throw new IllegalArgumentException("OVS render node is missing its tag name");
        String tagName = firstTokenValue(children.get(0));
        if (tagName == null || tagName.isBlank()) throw new IllegalArgumentException("OVS render tag is empty");

        SubhutiCst arguments = findDirectChild(render, "OvsArguments");
        Expression props = arguments == null
                ? SlimeAstCreateUtils.createObjectExpression(new AstNode[0], location(render))
                : createOvsArguments(arguments);
        SubhutiCst statementList = findDirectChild(render, "StatementList");
        List<Expression> childExpressions = new ArrayList<>();
        List<Object> orderedRenderItems = new ArrayList<>();
        if (statementList != null) {
            for (SubhutiCst item : children(statementList)) {
                SubhutiCst nestedRender = findStatementRender(item);
                if (nestedRender != null) {
                    Expression nestedCall = createRenderCall(nestedRender);
                    childExpressions.add(nestedCall);
                    orderedRenderItems.add(nestedCall);
                    continue;
                }
                SubhutiCst bodyStatement = findRenderBodyStatement(item);
                if (bodyStatement != null) {
                    orderedRenderItems.add(switch (bodyStatement.getName()) {
                        case "NoRenderBlock" -> createNoRenderBodyBlock(bodyStatement, NoRenderContext.RENDER_BODY);
                        case "IfStatement" -> createReactiveIfRenderStatement(bodyStatement);
                        default -> createReactiveStatement(createRenderControlFlowStatement(bodyStatement), bodyStatement);
                    });
                    continue;
                }
                Object converted = createStatementListItemAst(item);
                for (Object statement : asValues(converted)) {
                    if (statement instanceof ExpressionStatement expressionStatement) {
                        Expression expression = expressionStatement.expression();
                        if (isSideEffectExpression(expression)) {
                            orderedRenderItems.add(expressionStatement);
                        } else {
                            childExpressions.add(expression);
                            orderedRenderItems.add(expression);
                        }
                    } else if (statement instanceof Expression expression) {
                        if (isSideEffectExpression(expression)) {
                            orderedRenderItems.add(SlimeAstCreateUtils.createExpressionStatement(expression, location(item)));
                        } else {
                            childExpressions.add(expression);
                            orderedRenderItems.add(expression);
                        }
                    } else if (statement instanceof Statement nonRenderStatement) {
                        orderedRenderItems.add(nonRenderStatement);
                    } else if (statement instanceof AstNode astStatement) {
                        throw new IllegalArgumentException("Unsupported OVS render child node: " + astStatement.type());
                    }
                }
            }
        }
        if (orderedRenderItems.stream().anyMatch(Statement.class::isInstance)) {
            return createComplexRenderCall(render, tagName, props, orderedRenderItems, immediate);
        }
        Expression[] reactiveChildren = new Expression[childExpressions.size()];
        for (int index = 0; index < childExpressions.size(); index++) {
            reactiveChildren[index] = createReactiveChild(childExpressions.get(index), render);
        }
        ArrayExpression childArray = SlimeAstCreateUtils.createArrayExpression(reactiveChildren, location(render));
        Expression callee = createTagCallee(tagName, render);
        return SlimeAstCreateUtils.createCallExpression(callee, new Expression[] { props, childArray }, location(render));
    }

    private Expression createComplexRenderCall(
            SubhutiCst render,
            String tagName,
            Expression props,
            List<Object> orderedRenderItems,
            boolean immediate) {
        Identifier childrenName = renderChildrenIdentifier();
        VariableDeclarator childrenVariable = SlimeAstCreateUtils.createVariableDeclarator(
                childrenName,
                SlimeAstCreateUtils.createArrayExpression(new Expression[0], (SourceLocation) null),
                (SourceLocation) null);
        List<Statement> componentBody = new ArrayList<>();
        componentBody.add(SlimeAstCreateUtils.createVariableDeclaration(
                new VariableDeclarator[] { childrenVariable }, "const", (SourceLocation) null));
        for (Object item : orderedRenderItems) {
            if (item instanceof Statement statement) {
                componentBody.add(statement);
                continue;
            }
            Expression expression = createReactiveChild((Expression) item, render);
            MemberExpression push = SlimeAstCreateUtils.createMemberExpression(
                    renderChildrenIdentifier(),
                    SlimeAstCreateUtils.createIdentifier("push", (SourceLocation) null),
                    false, false, (SubhutiSourceLocation) null);
            CallExpression append = SlimeAstCreateUtils.createCallExpression(push, new Expression[] { expression }, location(render));
            componentBody.add(SlimeAstCreateUtils.createExpressionStatement(append, location(render)));
        }
        Expression renderCall = SlimeAstCreateUtils.createCallExpression(
                createTagCallee(tagName, render),
                new Expression[] {
                    props,
                    renderChildrenIdentifier()
                }, location(render));
        componentBody.add(SlimeAstCreateUtils.createReturnStatement(renderCall, location(render)));
        BlockStatement body = SlimeAstCreateUtils.createBlockStatement(
                componentBody.toArray(Statement[]::new), location(render));
        ArrowFunctionExpression factory = SlimeAstCreateUtils.createArrowFunctionExpression(
                new Pattern[0], body, false, false, location(render));
        // Evaluate the render statement in its enclosing try so its synchronous failures reach catch.
        if (immediate) {
            var group = new ParenthesizedExpression(factory, sourceLocation(render));
            return SlimeAstCreateUtils.createCallExpression(group, new Expression[0], location(render));
        }
        return SlimeAstCreateUtils.createCallExpression(
                runtimeIdentifier(REACTIVE, null), new Expression[] { factory }, location(render));
    }

    private Expression createReactiveChild(Expression expression, SubhutiCst render) {
        if (expression instanceof Literal) return expression;
        ArrowFunctionExpression getter = SlimeAstCreateUtils.createArrowFunctionExpression(
                new Pattern[0], expression, true, false, location(render));
        return SlimeAstCreateUtils.createCallExpression(
                runtimeIdentifier(REACTIVE, null),
                new Expression[] { getter }, location(render));
    }

    private Statement createReactiveIfRenderStatement(SubhutiCst conditional) {
        IfStatement ifStatement = createReactiveIfStatement(conditional);
        Identifier children = renderChildrenIdentifier();
        VariableDeclarator childrenVariable = SlimeAstCreateUtils.createVariableDeclarator(
                children,
                SlimeAstCreateUtils.createArrayExpression(new Expression[0]),
                (SourceLocation) null);
        BlockStatement effectBody = SlimeAstCreateUtils.createBlockStatement(new Statement[] {
                SlimeAstCreateUtils.createVariableDeclaration(
                        new VariableDeclarator[] { childrenVariable }, "const", (SourceLocation) null),
                ifStatement,
                SlimeAstCreateUtils.createReturnStatement(
                        renderChildrenIdentifier(),
                        (SourceLocation) null)
        }, location(conditional));
        ArrowFunctionExpression effect = SlimeAstCreateUtils.createArrowFunctionExpression(
                new Pattern[0], effectBody, false, false, location(conditional));
        CallExpression reactive = SlimeAstCreateUtils.createCallExpression(
                runtimeIdentifier(REACTIVE, null),
                new Expression[] { effect }, location(conditional));
        return appendToChildren(reactive, conditional);
    }

    private IfStatement createReactiveIfStatement(SubhutiCst conditional) {
        Expression test = null;
        Statement consequent = null;
        Statement alternate = null;
        boolean sawElse = false;
        for (SubhutiCst child : children(conditional)) {
            if ("Else".equals(child.getName()) || "else".equals(child.getValue())) {
                sawElse = true;
            } else if (test == null && "Expression".equals(child.getName())) {
                test = createExpressionAst(child);
            } else if (isIfBody(child.getName())) {
                Statement body = createReactiveIfBody(child);
                if (sawElse) alternate = body;
                else consequent = body;
            }
        }
        if (test == null || consequent == null) {
            throw new IllegalArgumentException("OVS render if statement is missing its test or consequent");
        }
        return SlimeAstCreateUtils.createIfStatement(test, consequent, alternate, location(conditional));
    }

    private BlockStatement createReactiveIfBody(SubhutiCst bodyCst) {
        Statement body = createControlFlowBody(bodyCst);
        return body instanceof BlockStatement block ? block
                : SlimeAstCreateUtils.createBlockStatement(new Statement[] { body }, location(bodyCst));
    }

    private static boolean isLoopStatement(String name) {
        return "ForStatement".equals(name) || "ForInStatement".equals(name)
                || "ForOfStatement".equals(name) || "ForInOfStatement".equals(name)
                || "WhileStatement".equals(name) || "DoWhileStatement".equals(name);
    }

    private Statement createRenderLoopStatement(SubhutiCst loopCst) {
        Object standardLoop = super.createDeclarationAst(loopCst);
        if (!(standardLoop instanceof Statement statement)) {
            throw new IllegalStateException("Java Slime visitor did not create loop statement for " + loopCst.getName());
        }
        return replaceLoopBody(statement, createControlFlowBody(findLoopBody(loopCst)));
    }

    private Statement replaceLoopBody(Statement loop, Statement body) {
        if (loop instanceof WhileStatement value) return new WhileStatement(value.test(), body, value.location());
        if (loop instanceof DoWhileStatement value) return new DoWhileStatement(body, value.test(), value.location());
        if (loop instanceof ForStatement value) return new ForStatement(value.init(), value.test(), value.update(), body, value.location());
        if (loop instanceof ForInStatement value) return new ForInStatement(value.left(), value.right(), body, value.location());
        if (loop instanceof ForOfStatement value) return new ForOfStatement(value.left(), value.right(), body, value.await(), value.location());
        throw new IllegalArgumentException("Unsupported Java Slime loop AST " + loop.type());
    }

    private SubhutiCst findLoopBody(SubhutiCst loop) {
        List<SubhutiCst> parts = children(loop);
        for (int index = parts.size() - 1; index >= 0; index--) {
            String name = parts.get(index).getName();
            if (Set.of("Statement", "StatementListItem", "StatementBody", "Block", "BlockStatement")
                    .contains(name)) return parts.get(index);
        }
        throw new IllegalArgumentException("OVS loop has no body CST: " + loop.getName());
    }

    private Statement createControlFlowBody(SubhutiCst bodyCst) {
        List<Statement> statements = new ArrayList<>();
        collectControlFlowStatements(bodyCst, statements);
        if (statements.isEmpty()) return SlimeAstCreateUtils.createEmptyStatement(location(bodyCst));
        if (statements.size() == 1) return statements.getFirst();
        return SlimeAstCreateUtils.createBlockStatement(statements.toArray(Statement[]::new), location(bodyCst));
    }

    private void collectControlFlowStatements(SubhutiCst cst, List<Statement> statements) {
        if (cst == null) return;
        String name = cst.getName();
        if (isStatementWrapper(name) || "StatementList".equals(name)
                || "BreakableStatement".equals(name) || "IterationStatement".equals(name)
                || "BlockStatement".equals(name)) {
            for (SubhutiCst child : children(cst)) collectControlFlowStatements(child, statements);
            return;
        }
        if ("LBrace".equals(name) || "RBrace".equals(name)) return;
        Statement statement = createRenderControlFlowStatement(cst);
        if (statement != null) statements.add(statement);
    }

    private Statement createRenderControlFlowStatement(SubhutiCst bodyCst) {
        String name = bodyCst.getName();
        if ("Block".equals(name)) return transformControlFlowBlock(bodyCst);
        if ("OvsRenderStatement".equals(name)) {
            return appendToChildren(createRenderCall(bodyCst, protectedRenderDepth > 0), bodyCst);
        }
        if ("IfStatement".equals(name)) return createReactiveIfStatement(bodyCst);
        if (isLoopStatement(name)) return createRenderLoopStatement(bodyCst);
        if ("SwitchStatement".equals(name)) return createRenderSwitchStatement(bodyCst);
        if ("TryStatement".equals(name)) return createRenderTryStatement(bodyCst);
        if ("LabelledStatement".equals(name)) return createRenderLabeledStatement(bodyCst, false);
        if ("NoRenderBlock".equals(name)) return createNoRenderBodyBlock(bodyCst, NoRenderContext.REACTIVE_BODY);
        if ("ExpressionStatement".equals(name)) {
            ExpressionStatement expressionStatement = super.createExpressionStatementAst(bodyCst);
            Expression expression = expressionStatement.expression();
            return isSideEffectExpression(expression)
                    ? expressionStatement
                    : appendToChildren(createReactiveChild(expression, bodyCst), bodyCst);
        }
        return convertStatement(bodyCst);
    }

    private LabeledStatement createRenderLabeledStatement(SubhutiCst cst, boolean noRender) {
        LabeledStatement standard = super.createLabeledStatementAst(cst);
        SubhutiCst item = findDirectChild(cst, "LabelledItem");
        Statement body = noRender ? createNoRenderStatement(item, NoRenderContext.REACTIVE_BODY)
                : createControlFlowBody(item);
        return new LabeledStatement(standard.label(), body, standard.location());
    }

    private Statement createReactiveStatement(Statement statement, SubhutiCst source) {
        VariableDeclarator declarator = SlimeAstCreateUtils.createVariableDeclarator(
                renderChildrenIdentifier(),
                SlimeAstCreateUtils.createArrayExpression(new Expression[0]),
                (SourceLocation) null);
        BlockStatement body = SlimeAstCreateUtils.createBlockStatement(new Statement[] {
                SlimeAstCreateUtils.createVariableDeclaration(
                        new VariableDeclarator[] { declarator }, "const", (SourceLocation) null),
                statement,
                SlimeAstCreateUtils.createReturnStatement(
                        renderChildrenIdentifier(),
                        (SourceLocation) null)
        }, location(source));
        ArrowFunctionExpression getter = SlimeAstCreateUtils.createArrowFunctionExpression(
                new Pattern[0], body, false, false, location(source));
        CallExpression reactive = SlimeAstCreateUtils.createCallExpression(
                runtimeIdentifier(REACTIVE, null),
                new Expression[] { getter }, location(source));
        return appendToChildren(reactive, source);
    }

    private Statement createRenderSwitchStatement(SubhutiCst switchCst) {
        Object value = super.createDeclarationAst(switchCst);
        if (!(value instanceof SwitchStatement standard)) {
            throw new IllegalStateException("Java Slime visitor did not create switch AST");
        }
        List<SubhutiCst> clauses = new ArrayList<>();
        collectSwitchClauses(switchCst, clauses);
        List<SwitchCase> cases = new ArrayList<>();
        for (int index = 0; index < standard.cases().size(); index++) {
            SwitchCase original = standard.cases().get(index);
            if (index >= clauses.size()) {
                cases.add(original);
                continue;
            }
            SubhutiCst clause = clauses.get(index);
            SubhutiCst statementList = findFirst(clause, "StatementList");
            List<Statement> consequent = new ArrayList<>();
            collectControlFlowStatements(statementList, consequent);
            cases.add(new SwitchCase(original.test(), consequent, original.location()));
        }
        return new SwitchStatement(standard.discriminant(), cases, standard.location());
    }

    private void collectSwitchClauses(SubhutiCst cst, List<SubhutiCst> clauses) {
        if (cst == null) return;
        String name = cst.getName();
        if ("CaseClause".equals(name) || "DefaultClause".equals(name)) {
            clauses.add(cst);
            return;
        }
        for (SubhutiCst child : children(cst)) collectSwitchClauses(child, clauses);
    }

    private Statement createRenderTryStatement(SubhutiCst tryCst) {
        Object value = super.createDeclarationAst(tryCst);
        if (!(value instanceof TryStatement standard)) {
            throw new IllegalStateException("Java Slime visitor did not create try AST");
        }
        SubhutiCst tryBlockCst = findDirectChild(tryCst, "Block");
        BlockStatement block = tryBlockCst == null ? standard.block() : transformProtectedRenderBlock(tryBlockCst, false);
        CatchClause handler = standard.handler();
        SubhutiCst catchCst = findDirectChild(tryCst, "Catch");
        if (handler != null && catchCst != null) {
            SubhutiCst catchBlock = findDirectChild(catchCst, "Block");
            if (catchBlock != null) {
                handler = new CatchClause(handler.param(), transformControlFlowBlock(catchBlock), handler.location());
            }
        }
        BlockStatement finalizer = standard.finalizer();
        SubhutiCst finallyCst = findDirectChild(tryCst, "Finally");
        if (finallyCst != null) {
            SubhutiCst finallyBlock = findDirectChild(finallyCst, "Block");
            if (finallyBlock != null) finalizer = transformControlFlowBlock(finallyBlock);
        }
        return new TryStatement(block, handler, finalizer, standard.location());
    }

    private BlockStatement transformControlFlowBlock(SubhutiCst blockCst) {
        List<Statement> body = new ArrayList<>();
        for (SubhutiCst item : children(blockCst)) collectControlFlowStatements(item, body);
        return SlimeAstCreateUtils.createBlockStatement(body.toArray(Statement[]::new), location(blockCst));
    }

    private BlockStatement transformProtectedRenderBlock(SubhutiCst blockCst, boolean noRender) {
        protectedRenderDepth++;
        try {
            return noRender ? createNoRenderBodyBlock(blockCst, NoRenderContext.REACTIVE_BODY)
                    : transformControlFlowBlock(blockCst);
        } finally {
            protectedRenderDepth--;
        }
    }

    private Statement appendToChildren(Expression expression, SubhutiCst source) {
        return appendToChildren(renderChildrenIdentifier(), expression, sourceLocation(source));
    }

    private static Statement appendToChildren(Identifier children, Expression expression, SourceLocation location) {
        MemberExpression push = new MemberExpression(
                children,
                SlimeAstCreateUtils.createIdentifier("push", (SourceLocation) null),
                false, false, location);
        CallExpression append = SlimeAstCreateUtils.createCallExpression(
                push, new Expression[] { expression }, location);
        return SlimeAstCreateUtils.createExpressionStatement(append, location);
    }

    private Identifier renderChildrenIdentifier() {
        return renderChildrenIdentifier(sourceRoot);
    }

    private Identifier renderChildrenIdentifier(SubhutiCst root) {
        if (renderChildrenName == null) {
            Set<String> sourceNames = new LinkedHashSet<>();
            collectRenderChildrenNames(root, sourceNames);
            renderChildrenName = "children";
            if (sourceNames.contains(renderChildrenName)) {
                renderChildrenName = "__qinOvsChildren";
                for (int suffix = 1; sourceNames.contains(renderChildrenName); suffix++) {
                    renderChildrenName = "__qinOvsChildren" + suffix;
                }
            }
        }
        return SlimeAstCreateUtils.createIdentifier(renderChildrenName, (SourceLocation) null);
    }

    private void collectRenderChildrenNames(SubhutiCst node, Set<String> names) {
        if (node == null) return;
        if (node.getKind() == SubhutiCst.Kind.TOKEN && "IdentifierName".equals(node.getName())) {
            String value = node.getValue();
            if (value != null && (value.indexOf('\\') >= 0 || value.equals("children") || value.startsWith("__qinOvsChildren"))) {
                names.add(createIdentifierAst(node).name());
            }
            return;
        }
        for (SubhutiCst child : children(node)) collectRenderChildrenNames(child, names);
    }

    private static boolean isSideEffectExpression(Expression expression) {
        return expression instanceof AssignmentExpression
                || expression instanceof UpdateExpression
                || expression instanceof UnaryExpression;
    }

    private Expression createTagCallee(String tagName, SubhutiCst render) {
        String normalized = tagName.trim();
        SubhutiSourceLocation sourceLocation = location(children(render).getFirst());
        if (HTML_TAGS.contains(normalized.toLowerCase(Locale.ROOT))) {
            MemberExpression member = SlimeAstCreateUtils.createMemberExpression(
                    runtimeIdentifier(HTML_TAG, null),
                    SlimeAstCreateUtils.createIdentifier(normalized, sourceLocation),
                    false, false, sourceLocation);
            return member;
        }
        return SlimeAstCreateUtils.createIdentifier(normalized, sourceLocation);
    }

    private Expression createOvsArguments(SubhutiCst arguments) {
        SubhutiCst list = findDirectChild(arguments, "OvsPropertyDefinitionList");
        List<AstNode> properties = new ArrayList<>();
        if (list != null) {
            for (SubhutiCst property : children(list)) {
                if ("OvsPropertyDefinition".equals(property.getName())) {
                    properties.add(createOvsProperty(property));
                }
            }
        }
        return SlimeAstCreateUtils.createObjectExpression(properties.toArray(AstNode[]::new), location(arguments));
    }

    private AstNode createOvsProperty(SubhutiCst property) {
        List<SubhutiCst> parts = children(property);
        if (parts.isEmpty()) throw new IllegalArgumentException("OVS property is empty");
        if ("MethodDefinition".equals(parts.get(0).getName())) {
            Object method = super.createMethodDefinitionAst(parts.get(0), false);
            if (!(method instanceof com.slime.ast.nodes.misc.MethodDefinition definition)) {
                throw new IllegalStateException("Java Slime visitor did not create an OVS property method");
            }
            boolean accessor = "get".equals(definition.kind()) || "set".equals(definition.kind());
            return SlimeAstCreateUtils.createProperty(
                    definition.key(), definition.value(), accessor ? definition.kind() : "init",
                    definition.computed(), false, !accessor, location(property));
        }

        SubhutiCst propertyName = findDirectChild(property, "PropertyName");
        int assignmentIndex = -1;
        for (int i = 0; i < parts.size(); i++) {
            String nodeName = parts.get(i).getName();
            String value = parts.get(i).getValue();
            if ("Assign".equals(nodeName) || "Colon".equals(nodeName) || "=".equals(value) || ":".equals(value)) {
                assignmentIndex = i;
                break;
            }
        }
        if (propertyName != null && assignmentIndex >= 0) {
            SubhutiCst valueCst = firstAfter(parts, assignmentIndex);
            if (valueCst == null) throw new IllegalArgumentException("OVS property value is missing");
            SubhutiCst computedName = findDirectChild(propertyName, "ComputedPropertyName");
            Expression key = computedName == null
                    ? createPropertyName(propertyName)
                    : createComputedPropertyName(computedName);
            Expression value = createExpressionAst(valueCst);
            return SlimeAstCreateUtils.createProperty(
                    key, value, "init", computedName != null, false, false, location(property));
        }

        SubhutiCst shorthand = findDirectChild(property, "IdentifierReference");
        if (shorthand != null) {
            Expression identifier = createExpressionAst(shorthand);
            return SlimeAstCreateUtils.createProperty(identifier, identifier, "init", false, true, false, location(property));
        }
        SubhutiCst spread = findDirectChild(property, "AssignmentExpression");
        if (spread != null && ("Ellipsis".equals(parts.get(0).getName()) || "...".equals(parts.get(0).getValue()))) {
            return new SpreadElement(createExpressionAst(spread), sourceLocation(property));
        }
        throw new IllegalArgumentException("Unsupported OVS property form: " + property.getName());
    }

    private Expression createPropertyName(SubhutiCst propertyName) {
        List<SubhutiCst> parts = children(propertyName);
        if (parts.isEmpty()) throw new IllegalArgumentException("OVS property name is empty");
        SubhutiCst name = parts.get(0);
        if ("LiteralPropertyName".equals(name.getName())) {
            String value = firstTokenValue(name);
            if (value == null) throw new IllegalArgumentException("OVS property name is missing a token value");
            if (value.length() >= 2 && ((value.startsWith("\"") && value.endsWith("\""))
                    || (value.startsWith("'") && value.endsWith("'")))) {
                return SlimeAstCreateUtils.createStringLiteral(value.substring(1, value.length() - 1), location(name), value);
            }
            return SlimeAstCreateUtils.createIdentifier(value, location(name));
        }
        return createExpressionAst(name);
    }

    private Expression createComputedPropertyName(SubhutiCst name) {
        SubhutiCst expression = findDirectChild(name, "AssignmentExpression");
        if (expression == null) throw new IllegalArgumentException("OVS computed property name is empty");
        return createExpressionAst(expression);
    }

    private static List<?> asValues(Object value) {
        if (value == null) return List.of();
        if (value instanceof List<?> list) return list;
        return List.of(value);
    }

    private static SubhutiCst firstAfter(List<SubhutiCst> children, int index) {
        return index + 1 < children.size() ? children.get(index + 1) : null;
    }

    private static SubhutiCst findDirectChild(SubhutiCst parent, String name) {
        for (SubhutiCst child : children(parent)) if (name.equals(child.getName())) return child;
        return null;
    }

    private static SubhutiCst findFirst(SubhutiCst root, String name) {
        if (root == null) return null;
        if (name.equals(root.getName())) return root;
        for (SubhutiCst child : children(root)) {
            SubhutiCst found = findFirst(child, name);
            if (found != null) return found;
        }
        return null;
    }

    private static SubhutiCst findStatementRender(SubhutiCst node) {
        if (node == null) return null;
        if ("OvsRenderStatement".equals(node.getName())) return node;
        if ("NoRenderBlock".equals(node.getName())) return null;
        if (!Set.of("StatementListItem", "ModuleItem", "ModuleStatementListItem", "Statement").contains(node.getName())) {
            return null;
        }
        for (SubhutiCst child : children(node)) {
            SubhutiCst render = findStatementRender(child);
            if (render != null) return render;
        }
        return null;
    }

    private static SubhutiCst findViewDeclaration(SubhutiCst node) {
        if (node == null) return null;
        if ("OvsViewDeclaration".equals(node.getName())) return node;
        if (!Set.of("StatementListItem", "ModuleItem", "ModuleStatementListItem", "Declaration").contains(node.getName())) {
            return null;
        }
        for (SubhutiCst child : children(node)) {
            SubhutiCst view = findViewDeclaration(child);
            if (view != null) return view;
        }
        return null;
    }

    private static String firstTokenValue(SubhutiCst node) {
        if (node == null) return null;
        if (node.getValue() != null) return node.getValue();
        for (SubhutiCst child : children(node)) {
            String value = firstTokenValue(child);
            if (value != null) return value;
        }
        return null;
    }

    private static List<SubhutiCst> children(SubhutiCst node) {
        return node == null || node.getChildren() == null ? List.of() : node.getChildren();
    }

    private static SubhutiSourceLocation location(SubhutiCst node) {
        return SlimeAstCreateUtils.resolveSubhutiLocation(node);
    }

    private static SourceLocation sourceLocation(SubhutiCst node) {
        return SlimeAstCreateUtils.resolveSourceLocation(node);
    }
}
