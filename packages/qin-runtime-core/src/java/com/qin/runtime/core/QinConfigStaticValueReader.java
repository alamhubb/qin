package com.qin.runtime.core;

import com.qin.parser.QinParserFacade;
import com.slime.ast.AstNode;
import com.slime.ast.Expression;
import com.slime.ast.nodes.expressions.ArrayExpression;
import com.slime.ast.nodes.expressions.Identifier;
import com.slime.ast.nodes.expressions.Literal;
import com.slime.ast.nodes.expressions.ObjectExpression;
import com.slime.ast.nodes.misc.Program;
import com.slime.ast.nodes.misc.Property;
import com.slime.ast.nodes.modules.ExportDefaultDeclaration;

import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Reads static object-literal options from Qin's JavaScript configuration AST. */
final class QinConfigStaticValueReader {
    private static final int MAX_CACHE_ENTRIES = 16;
    private static final Map<String, Program> PROGRAM_CACHE = Collections.synchronizedMap(
            new LinkedHashMap<>(MAX_CACHE_ENTRIES, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, Program> eldest) {
                    return size() > MAX_CACHE_ENTRIES;
                }
            });

    private final Program program;
    private final ObjectExpression optionsObject;

    private QinConfigStaticValueReader(Program program) {
        this.program = program;
        this.optionsObject = findOptionsObject(program);
    }

    static QinConfigStaticValueReader parse(String source) {
        String input = source == null ? "" : source;
        if (input.isBlank()) return new QinConfigStaticValueReader(null);
        Program program;
        synchronized (PROGRAM_CACHE) {
            program = PROGRAM_CACHE.get(input);
        }
        if (program == null) {
            String parserSource = stripComments(input);
            String trimmed = parserSource.stripLeading();
            if (trimmed.startsWith("{")) parserSource = "export default " + parserSource;
            program = new QinParserFacade().parseProgram(parserSource);
            synchronized (PROGRAM_CACHE) {
                PROGRAM_CACHE.put(input, program);
            }
        }
        return new QinConfigStaticValueReader(program);
    }

    private static String stripComments(String source) {
        StringBuilder output = new StringBuilder(source.length());
        char quote = 0;
        boolean escaped = false;
        for (int index = 0; index < source.length(); index++) {
            char current = source.charAt(index);
            if (quote != 0) {
                output.append(current);
                if (escaped) escaped = false;
                else if (current == '\\') escaped = true;
                else if (current == quote) quote = 0;
                continue;
            }
            if (current == '\'' || current == '"' || current == '`') {
                quote = current;
                output.append(current);
                continue;
            }
            if (current == '/' && index + 1 < source.length() && source.charAt(index + 1) == '/') {
                output.append("  ");
                index += 2;
                while (index < source.length() && source.charAt(index) != '\n' && source.charAt(index) != '\r') {
                    output.append(' ');
                    index++;
                }
                if (index < source.length()) output.append(source.charAt(index));
                continue;
            }
            if (current == '/' && index + 1 < source.length() && source.charAt(index + 1) == '*') {
                output.append("  ");
                index += 2;
                while (index < source.length()) {
                    char commentChar = source.charAt(index);
                    if (commentChar == '*' && index + 1 < source.length() && source.charAt(index + 1) == '/') {
                        output.append("  ");
                        index++;
                        break;
                    }
                    output.append(commentChar == '\n' || commentChar == '\r' ? commentChar : ' ');
                    index++;
                }
                continue;
            }
            output.append(current);
        }
        return output.toString();
    }

    Expression firstProperty(String name) {
        return directProperty(optionsObject, name);
    }

    private static ObjectExpression findOptionsObject(Program program) {
        if (program == null) return null;
        ObjectExpression root = null;
        for (AstNode item : program.body()) {
            if (item instanceof ExportDefaultDeclaration exported
                    && exported.declaration() instanceof ObjectExpression object) {
                root = object;
                break;
            }
        }
        if (root == null) return null;

        Expression plugins = directProperty(root, "plugins");
        Expression pluginOptions = findPropertyValue(plugins, "cssts",
                Collections.newSetFromMap(new IdentityHashMap<>()));
        if (pluginOptions instanceof ObjectExpression object) return object;

        Expression directOptions = directProperty(root, "cssts");
        if (directOptions instanceof ObjectExpression object) return object;
        return root;
    }

    private static Expression directProperty(ObjectExpression object, String name) {
        if (object == null) return null;
        for (AstNode node : object.properties()) {
            if (!(node instanceof Property property) || property.method() || property.computed()) continue;
            if (name.equals(propertyName(property.key()))) return (Expression) property.value();
        }
        return null;
    }

    static Map<String, Expression> objectProperties(ObjectExpression object) {
        if (object == null) return Map.of();
        Map<String, Expression> values = new LinkedHashMap<>();
        for (AstNode node : object.properties()) {
            if (!(node instanceof Property property) || property.method() || property.computed()) continue;
            String name = propertyName(property.key());
            if (name != null) values.put(name, (Expression) property.value());
        }
        return values;
    }

    static String stringValue(Expression expression) {
        if (!(expression instanceof Literal literal) || !(literal.value() instanceof String value)) return null;
        if (value.length() < 2) return value;
        char quote = value.charAt(0);
        if ((quote != '\'' && quote != '"' && quote != '`') || value.charAt(value.length() - 1) != quote) return value;
        StringBuilder decoded = new StringBuilder(value.length() - 2);
        for (int index = 1; index < value.length() - 1; index++) {
            char current = value.charAt(index);
            if (current == '\\' && index + 1 < value.length() - 1) {
                char escaped = value.charAt(++index);
                decoded.append(switch (escaped) {
                    case 'n' -> '\n';
                    case 'r' -> '\r';
                    case 't' -> '\t';
                    default -> escaped;
                });
            } else {
                decoded.append(current);
            }
        }
        return decoded.toString();
    }

    static List<String> stringArray(Expression expression) {
        if (!(expression instanceof ArrayExpression array)) return List.of();
        List<String> values = new ArrayList<>();
        for (Expression item : array.elements()) {
            String value = stringValue(item);
            if (value != null) values.add(value);
        }
        return List.copyOf(values);
    }

    private static Expression findPropertyValue(Object value, String name, Set<AstNode> visited) {
        if (value instanceof Iterable<?> values) {
            for (Object item : values) {
                Expression found = findPropertyValue(item, name, visited);
                if (found != null) return found;
            }
            return null;
        }
        if (!(value instanceof AstNode node) || !visited.add(node)) return null;
        String typeName = node.type().name();
        if (typeName.contains("FUNCTION")) return null;
        if (node instanceof Property property && !property.method() && !property.computed()
                && name.equals(propertyName(property.key()))) {
            return (Expression) property.value();
        }
        if (!node.getClass().isRecord()) return null;
        for (RecordComponent component : node.getClass().getRecordComponents()) {
            Object child;
            try {
                child = component.getAccessor().invoke(node);
            } catch (ReflectiveOperationException exception) {
                throw new IllegalStateException("Cannot inspect Qin configuration AST component "
                        + component.getName(), exception);
            }
            Expression found = findPropertyValue(child, name, visited);
            if (found != null) return found;
        }
        return null;
    }

    private static String propertyName(AstNode key) {
        if (key instanceof Identifier identifier) return identifier.name();
        return key instanceof Literal literal && literal.value() instanceof String value ? value : null;
    }
}
