package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrProgram;

import java.lang.reflect.Array;
import java.lang.reflect.RecordComponent;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class QinJavaAstIrLowererCollectorLambdaOwnerSmokeTestMain {
    private QinJavaAstIrLowererCollectorLambdaOwnerSmokeTestMain() {
    }

    public static void main(String[] args) {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                package com.example;

                import java.util.ArrayList;
                import java.util.List;
                import java.util.Map;
                import java.util.stream.Collectors;

                public class Demo {
                    public record CallData(String name) {}
                    public record CallKey(String name) {}
                    public record Contributor(CallData call) {}

                    public static Map<String, List<Contributor>> group(List<Contributor> contributors) {
                        return contributors.stream()
                                .filter(contributor -> contributor.call() != null)
                                .collect(Collectors.groupingBy(contributor -> contributor.call().name()));
                    }

                    public static List<String> flatten(Map<CallKey, List<Contributor>> byCall) {
                        List<String> result = new ArrayList<>();
                        for (var entry : byCall.entrySet()) {
                            result.addAll(entry.getValue().stream()
                                    .map(contributor -> contributor.call().name())
                                    .toList());
                        }
                        return result;
                    }
                }
                """);
        CallOwnerProbe probe = new CallOwnerProbe();
        probe.visit(program, "$");
        if (probe.callCount == 0) {
            throw new IllegalStateException("Expected lowered collector lambda to contain call() accessors");
        }
        if (probe.missingOwnerCount != 0) {
            throw new IllegalStateException(
                    "Expected collector lambda call() accessors to keep static owner, missing="
                            + probe.missingOwnerCount
                            + "/"
                            + probe.callCount
                            + " owners="
                            + probe.owners
                            + " receivers="
                            + probe.receivers
                            + " paths="
                            + probe.paths);
        }
        System.out.println("QinJavaAstIrLowererCollectorLambdaOwnerSmokeTestMain OK");
    }

    private static final class CallOwnerProbe {
        private final Set<Object> seen = java.util.Collections.newSetFromMap(new IdentityHashMap<>());
        private final List<String> owners = new ArrayList<>();
        private final List<String> receivers = new ArrayList<>();
        private final List<String> paths = new ArrayList<>();
        private int callCount;
        private int missingOwnerCount;

        private void visit(Object node, String path) {
            if (node == null || isScalar(node) || !seen.add(node)) {
                return;
            }
            if (node instanceof QinIrInstanceMethodCallExpression call
                    && "call".equals(call.methodName())) {
                callCount++;
                owners.add(String.valueOf(call.ownerBinaryName()));
                receivers.add(call.receiver().getClass().getSimpleName() + ":" + call.receiver());
                paths.add(path);
                if (!"com.example.Demo$Contributor".equals(call.ownerBinaryName())) {
                    missingOwnerCount++;
                }
            }
            if (node instanceof Collection<?> collection) {
                int index = 0;
                for (Object item : collection) {
                    visit(item, path + "[" + index++ + "]");
                }
                return;
            }
            if (node instanceof Map<?, ?> map) {
                for (Object value : map.values()) {
                    visit(value, path + "{}");
                }
                return;
            }
            Class<?> type = node.getClass();
            if (type.isArray()) {
                int length = Array.getLength(node);
                for (int i = 0; i < length; i++) {
                    visit(Array.get(node, i), path + "[" + i + "]");
                }
                return;
            }
            if (!type.isRecord()) {
                return;
            }
            for (RecordComponent component : type.getRecordComponents()) {
                try {
                    visit(component.getAccessor().invoke(node), path + "." + component.getName());
                } catch (ReflectiveOperationException error) {
                    throw new IllegalStateException("Cannot inspect IR component " + component.getName(), error);
                }
            }
        }

        private static boolean isScalar(Object value) {
            return value instanceof String
                    || value instanceof Number
                    || value instanceof Boolean
                    || value instanceof Character
                    || value instanceof Enum<?>;
        }
    }
}
