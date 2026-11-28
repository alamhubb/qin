package com.qin.parser;

import com.slime.ast.nodes.misc.ImportDefaultSpecifier;
import com.slime.ast.nodes.misc.ImportNamespaceSpecifier;
import com.slime.ast.nodes.misc.ImportSpecifier;
import com.slime.ast.nodes.modules.ImportDeclaration;
import com.slime.parser.cstToAst.identifier.SlimeIdentifierCstToAst;
import com.slime.parser.cstToAst.module.SlimeImportCstToAst;
import com.subhuti.struct.SubhutiCst;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Source bindings for CSS atom lookup and generated OVS/CSSTS imports. */
final class OvsSourceScopes {
    enum RuntimeBinding {
        HTML_TAG("ovsjs", "$OvsHtmlTag", "__qinOvsHtmlTag"),
        COMPONENT("ovsjs", "defineOvsComponent", "__qinOvsDefineComponent"),
        REACTIVE("ovsjs", "defineReactiveExpression", "__qinOvsReactiveExpression"),
        CSSTS("cssts-ts", "cssts", "__qinCssts"),
        ATOM("virtual:csstsAtom", "csstsAtom", "__qinCsstsAtom");

        static final List<RuntimeBinding> OVS = List.of(HTML_TAG, COMPONENT, REACTIVE);

        final String module;
        final String name;
        final String alias;

        RuntimeBinding(String module, String name, String alias) {
            this.module = module;
            this.name = name;
            this.alias = alias;
        }

        boolean matches(String source, ImportSpecifier specifier) {
            return module.equals(source) && name.equals(specifier.local().name())
                    && name.equals(specifier.imported().name());
        }
    }

    private static final Map<String, RuntimeBinding> RUNTIME_BINDINGS = List.of(RuntimeBinding.values()).stream()
            .collect(Collectors.toUnmodifiableMap(binding -> binding.name, Function.identity()));
    private final IdentityHashMap<SubhutiCst, Scope> cssScopes = new IdentityHashMap<>();
    private final Set<String> runtimeBindings = new HashSet<>();
    private final Set<String> aliasReferences = new HashSet<>();

    OvsSourceScopes(SubhutiCst root) {
        visit(root, new Scope(null, true), "");
    }

    Scope at(SubhutiCst expression) {
        return cssScopes.get(expression);
    }

    String runtimeName(RuntimeBinding binding) {
        if (!runtimeBindings.contains(binding.name)) return binding.name;
        String candidate = binding.alias;
        for (int suffix = 1; aliasReferences.contains(candidate); suffix++) {
            candidate = binding.alias + suffix;
        }
        return candidate;
    }

    private void visit(SubhutiCst node, Scope enclosing, String parentRule) {
        if (node.getKind() != SubhutiCst.Kind.RULE) {
            if (node.getKind() == SubhutiCst.Kind.TOKEN && "IdentifierName".equals(node.getName())) {
                String value = node.getValue();
                if (value != null && (isInternalName(value) || value.indexOf('\\') >= 0)) {
                    aliasReferences.add(SlimeIdentifierCstToAst.createIdentifierAst(node).name());
                }
            }
            return;
        }
        String rule = node.getName();
        Scope scope = enclosing;
        switch (rule) {
            case "TSType", "TSTypeAnnotation", "TSTypeParameterDeclaration", "TSTypeParameterInstantiation",
                    "TSInterfaceDeclaration", "TSTypeAliasDeclaration" -> {
                return;
            }
            case "ImportDeclaration" -> {
                declareImports(node, scope);
                return;
            }
            case "Block", "CaseBlock", "Catch", "ForStatement", "ForInOfStatement", "NoRenderBlock" ->
                    scope = new Scope(enclosing, false);
            case "FunctionBody", "GeneratorBody", "AsyncFunctionBody", "AsyncGeneratorBody",
                    "OvsFunctionBody", "OvsAsyncFunctionBody", "ClassStaticBlock" ->
                    scope = new Scope(enclosing, true);
            case "FunctionDeclaration", "GeneratorDeclaration", "AsyncFunctionDeclaration",
                    "AsyncGeneratorDeclaration" -> {
                declareFunctionName(node, enclosing);
                scope = new Scope(enclosing, true);
            }
            case "FunctionExpression", "GeneratorExpression", "AsyncFunctionExpression", "AsyncGeneratorExpression" -> {
                scope = new Scope(enclosing, true);
                declareFunctionName(node, scope);
            }
            case "ArrowFunction", "AsyncArrowFunction", "MethodDefinition",
                    "GeneratorMethod", "AsyncMethod", "AsyncGeneratorMethod" ->
                    scope = new Scope(enclosing, true);
            case "ClassDeclaration" -> declareTargets(node, enclosing);
            case "ClassExpression" -> {
                scope = new Scope(enclosing, false);
                declareTargets(node, scope);
            }
            case "OvsViewDeclaration" -> {
                declareViewName(node, enclosing);
                scope = new Scope(enclosing, true);
            }
            case "TSEnumDeclaration" -> {
                for (SubhutiCst child : node.getChildren()) {
                    if ("Identifier".equals(child.getName())) declareIdentifier(child, enclosing);
                }
            }
            case "VariableDeclaration" -> declareTargets(node, scope.variables);
            case "ForBinding" -> declareTargets(node, "ForDeclaration".equals(parentRule) ? scope : scope.variables);
            case "LexicalBinding", "ImportedBinding", "FormalParameter", "FunctionRestParameter",
                    "ArrowParameters", "AsyncArrowBindingIdentifier", "CatchParameter" -> declareTargets(node, scope);
            case "CssExpression" -> cssScopes.put(node, scope);
            default -> { }
        }

        for (SubhutiCst child : node.getChildren()) {
            Scope childScope = scope;
            if ("ClassElementName".equals(child.getName()) || "PropertyName".equals(child.getName())) {
                childScope = enclosing;
            } else if ("StatementList".equals(child.getName()) && switch (rule) {
                case "OvsViewDeclaration", "OvsRenderFunction", "OvsRenderStatement" -> true;
                default -> false;
            }) {
                childScope = new Scope(scope, true);
            }
            visit(child, childScope, rule);
        }
    }

    private void declareImports(SubhutiCst node, Scope scope) {
        if (!(SlimeImportCstToAst.createImportDeclarationAst(node) instanceof ImportDeclaration declaration)
                || declaration.typeOnly()) return;
        String module = declaration.source() == null ? "" : String.valueOf(declaration.source().value());
        for (var specifier : declaration.specifiers()) {
            switch (specifier) {
                case ImportSpecifier named when !named.typeOnly() -> {
                    String local = named.local().name();
                    var binding = RUNTIME_BINDINGS.get(local);
                    boolean runtimeImport = binding != null && binding.matches(module, named);
                    declareName(local, scope, runtimeImport);
                }
                case ImportDefaultSpecifier imported -> declareName(imported.local().name(), scope, false);
                case ImportNamespaceSpecifier imported -> declareName(imported.local().name(), scope, false);
                default -> { }
            }
        }
    }

    private void declareFunctionName(SubhutiCst function, Scope scope) {
        for (SubhutiCst child : function.getChildren()) {
            switch (child.getName()) {
                case "RequiredFunctionBindingIdentifier", "OptionalFunctionBindingIdentifier" -> declareTargets(child, scope);
                case "BindingIdentifier" -> declareIdentifier(child, scope);
                default -> { }
            }
        }
    }

    private void declareViewName(SubhutiCst view, Scope scope) {
        boolean keyword = true;
        for (SubhutiCst child : view.getChildren()) {
            if (!"IdentifierName".equals(child.getName())) continue;
            if (!keyword) {
                declareIdentifier(child, scope);
                return;
            }
            keyword = false;
        }
    }

    private void declareTargets(SubhutiCst node, Scope scope) {
        for (SubhutiCst child : node.getChildren()) {
            switch (child.getName()) {
                case "BindingIdentifier" -> declareIdentifier(child, scope);
                case "BindingPattern", "ObjectBindingPattern", "ArrayBindingPattern", "BindingPropertyList",
                        "BindingElementList", "BindingProperty", "BindingElement", "BindingElisionElement",
                        "BindingRestElement", "BindingRestProperty", "SingleNameBinding" ->
                        declareTargets(child, scope);
                default -> { }
            }
        }
    }

    private void declareIdentifier(SubhutiCst node, Scope scope) {
        declareName(SlimeIdentifierCstToAst.createIdentifierAst(node).name(), scope, false);
    }

    private void declareName(String name, Scope scope, boolean runtimeImport) {
        scope.names.add(name);
        if (isInternalName(name)) aliasReferences.add(name);
        if (!runtimeImport && RUNTIME_BINDINGS.containsKey(name)) {
            runtimeBindings.add(name);
        }
    }

    private static boolean isInternalName(String name) {
        return name.startsWith("__qinCssts") || name.startsWith("__qinOvs");
    }

    static final class Scope {
        private final Scope enclosing;
        private final Scope variables;
        private final Set<String> names = new HashSet<>();

        private Scope(Scope enclosing, boolean variableBoundary) {
            this.enclosing = enclosing;
            this.variables = variableBoundary ? this : enclosing.variables;
        }

        boolean contains(String name) {
            for (Scope scope = this; scope != null; scope = scope.enclosing) {
                if (scope.names.contains(name)) return true;
            }
            return false;
        }
    }
}
