package com.qin.runtime.core;

import java.util.Map;

public final class QinOvsParserPipelineProbeSmokeTestMain {
    private QinOvsParserPipelineProbeSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const normalizeRootUrl = (value) => {
                  const text = String(value || "").trim()
                  if (!text) {
                    return ""
                  }
                  return text
                }

                div(onClick() { console.log(normalizeRootUrl("https://example.test")) }) {
                  "Open"
                }
                """;
        Map<?, ?> result = QinOvsParserPipelineProbeMain.probe(source);
        requireNumber(result, "arrowBodyLength", 3);
        requireNumber(result, "methodBodyLength", 1);
        Object generated = result.get("generated");
        String code = generated instanceof String text ? text : "";
        if (!code.contains("const text = String")
                || !code.contains("return text")
                || !code.contains("console.log(normalizeRootUrl")) {
            throw new IllegalStateException("OVS parser pipeline probe lost function body statements:\n"
                    + QinObjectJsonEncoder.toJson(result, 12000));
        }
        System.out.println("QinOvsParserPipelineProbeSmokeTestMain OK");
    }

    private static void requireNumber(Map<?, ?> map, String key, int expected) {
        Object value = map.get(key);
        if (!(value instanceof Number number) || number.intValue() != expected) {
            throw new IllegalStateException("Expected " + key + "=" + expected + ", got: "
                    + QinObjectJsonEncoder.toJson(map, 12000));
        }
    }
}
