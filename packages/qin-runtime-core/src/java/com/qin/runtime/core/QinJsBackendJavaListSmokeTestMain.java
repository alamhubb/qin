package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaListSmokeTestMain {
    private QinJsBackendJavaListSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(new QinIrConstDeclaration(
                        "list",
                        new QinIrStaticMethodCallExpression(
                                "List",
                                "java.util.List",
                                "of",
                                List.of(new QinIrStringLiteral("alpha"), new QinIrStringLiteral("beta"))))),
                List.of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:java.util",
                        "List",
                        "List",
                        "java.util.List")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("const __QinJavaUtilList"), "List runtime facade");
        require(generated.contains("const List = __QinJavaUtilList;"), "List import alias");
        require(generated.contains("List.of(\"alpha\", \"beta\")"), "List.of call");
        require(generated.contains("new __QinJavaUtilUnmodifiableList(values)"), "List.of unmodifiable value");

        Path root = Files.createTempDirectory("qin-js-backend-list-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-list\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nlet mutationFailed = false;\n"
                        + "try { globalThis.__qinResult.add(\"gamma\"); } catch (error) { mutationFailed = true; }\n"
                        + "globalThis.__qinResult.get(1) + \":\" + globalThis.__qinResult.size() + \":\" + mutationFailed;\n",
                "js_backend_list");
        if (!"beta:2:true".equals(result)) {
            throw new IllegalStateException("Expected generated List.of result, got: " + result);
        }
        Object copyResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst source = new __QinJavaUtilArrayList();\n"
                        + "source.add(\"alpha\");\n"
                        + "const copy = new __QinJavaUtilArrayList(source);\n"
                        + "const changed = copy.addAll(List.of(\"beta\", \"gamma\"));\n"
                        + "const previous = copy.set(1, \"delta\");\n"
                        + "const tail = copy.subList(0, 1);\n"
                        + "copy.get(0) + \":\" + copy.get(2) + \":\" + copy.size() + \":\" + tail.get(0)"
                        + " + \":\" + tail.size() + \":\" + changed + \":\" + previous + \":\" + copy.get(1)"
                        + " + \":\" + copy.indexOf(\"gamma\") + \":\" + copy.indexOf(\"missing\");\n",
                "js_backend_array_list_copy");
        if (!"alpha:gamma:3:alpha:1:true:beta:delta:2:-1".equals(copyResult)) {
            throw new IllegalStateException("Expected ArrayList copy/addAll/set/subList/indexOf result alpha:gamma:3:alpha:1:true:beta:delta:2:-1, got: "
                    + copyResult);
        }
        Object capacityResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst capacityOnly = new __QinJavaUtilArrayList(10000);\n"
                        + "capacityOnly.size() + \":\" + capacityOnly.isEmpty();\n",
                "js_backend_array_list_capacity");
        if (!"0:true".equals(capacityResult)) {
            throw new IllegalStateException("Expected ArrayList capacity constructor result 0:true, got: "
                    + capacityResult);
        }
        Object arrayLikeResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst source = new __QinJavaUtilArrayList([\"alpha\", \"beta\", \"gamma\"]);\n"
                        + "let visited = \"\";\n"
                        + "source.forEach((item, index) => { visited += index + item[0]; });\n"
                        + "const sliced = source.slice(1).join(\",\");\n"
                        + "const mapped = source.map(item => item.toUpperCase()).join(\",\");\n"
                        + "const filtered = source.filter(item => item.includes(\"a\")).join(\",\");\n"
                        + "const found = source.find(item => item.startsWith(\"g\"));\n"
                        + "const foundIndex = source.findIndex(item => item.startsWith(\"b\"));\n"
                        + "const hasBeta = source.some(item => item === \"beta\");\n"
                        + "const allHaveA = source.every(item => item.includes(\"a\"));\n"
                        + "const flattened = new __QinJavaUtilArrayList([[\"x\"], [\"y\"]]).flat().join(\",\");\n"
                        + "visited + \":\" + sliced + \":\" + mapped + \":\" + filtered"
                        + " + \":\" + source[1] + \":\" + source.length + \":\" + found"
                        + " + \":\" + foundIndex + \":\" + hasBeta + \":\" + allHaveA + \":\" + flattened;\n",
                "js_backend_array_list_array_like");
        if (!"0a1b2g:beta,gamma:ALPHA,BETA,GAMMA:alpha,beta,gamma:beta:3:gamma:1:true:true:x,y"
                .equals(arrayLikeResult)) {
            throw new IllegalStateException("Expected ArrayList array-like read methods result, got: "
                    + arrayLikeResult);
        }
        Object listSubListResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst pair = globalThis.__qinResult.subList(0, 2);\n"
                        + "const sliced = globalThis.__qinResult.slice(0, 1).join(\",\");\n"
                        + "const mapped = pair.map(item => item.toUpperCase()).join(\",\");\n"
                        + "pair.get(0) + \":\" + pair.get(1) + \":\" + pair.size()"
                        + " + \":\" + pair.indexOf(\"beta\") + \":\" + pair.indexOf(\"missing\")"
                        + " + \":\" + sliced + \":\" + mapped + \":\" + pair[1] + \":\" + pair.length;\n",
                "js_backend_list_sub_list");
        if (!"alpha:beta:2:1:-1:alpha:ALPHA,BETA:beta:2".equals(listSubListResult)) {
            throw new IllegalStateException("Expected List.of subList/indexOf/slice/map/index/length result alpha:beta:2:1:-1:alpha:ALPHA,BETA:beta:2, got: "
                    + listSubListResult);
        }
        System.out.println("QinJsBackendJavaListSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
