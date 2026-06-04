package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrProgram;

import java.util.Set;
import java.util.stream.Collectors;

public final class QinGeneratedJavaClassNameSmokeTestMain {
    private QinGeneratedJavaClassNameSmokeTestMain() {
    }

    public static void main(String[] args) {
        String source = """
                class com_example_First {
                  builder() {
                    return new com_example_First$Builder();
                  }
                }
                const First = com_example_First;
                class com_example_First$Builder {
                  build() {
                    return new com_example_First();
                  }
                }
                const First$Builder = com_example_First$Builder;
                class com_example_Second {
                  static ok() {
                    return "ok";
                  }
                }
                const Second = com_example_Second;
                class com_example_KeywordMethod {
                  in(patterns) {
                    return patterns;
                  }
                  same(...__qin_args) {
                    switch (__qin_args.length) {
                      case 1: return this.__qin_overload_same_1(...__qin_args);
                      default: throw new Error("bad");
                    }
                  }
                  __qin_overload_same_1(text) {
                    return text;
                  }
                  __qin_overload_same_1(pattern) {
                    return pattern;
                  }
                }
                const KeywordMethod = com_example_KeywordMethod;
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(source);
        Set<String> names = program.declarations().stream()
                .map(declaration -> declaration.name())
                .collect(Collectors.toSet());

        require(names.contains("com_example_First"), "first class runtime declaration");
        require(names.contains("com_example_First$Builder"), "builder class runtime declaration");
        require(names.contains("com_example_Second"), "second class runtime declaration");
        require(names.contains("com_example_KeywordMethod"), "keyword method class runtime declaration");

        System.out.println("QinGeneratedJavaClassNameSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
