package com.qin.lang.frontend.adapter;

/**
 * Smoke test for fail-fast decorator lowering in the static declaration subset.
 */
public final class QinDeclarationIrUnsupportedDecoratorSmokeTestMain {
    private QinDeclarationIrUnsupportedDecoratorSmokeTestMain() {
    }

    public static void main(String[] args) {
        String text = """
                function wrap(target, key, descriptor) {
                  return descriptor
                }

                class Tool {
                  @wrap
                  value(): number {
                    return 1
                  }
                }
                """;

        try {
            new QinFrontendLowerer().lowerSource(text);
            throw new IllegalStateException("Expected unsupported decorator to fail");
        } catch (IllegalArgumentException error) {
            String message = error.getMessage();
            if (message == null || !message.contains("QJS2013") || !message.contains("wrap")) {
                throw new IllegalStateException("Unexpected unsupported decorator error: " + message, error);
            }
        }

        System.out.println("QinDeclarationIrUnsupportedDecoratorSmokeTestMain passed.");
    }
}
