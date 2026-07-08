package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;

public final class QinJsMissingTrailingMethodArgsSmokeTestMain {
    private QinJsMissingTrailingMethodArgsSmokeTestMain() {
    }

    public static void main(String[] args) {
        OptionalArgTarget target = new OptionalArgTarget();
        Object result = JavaEsmGlobal.__qin_call_method__(
                target,
                "join",
                "left");
        if (!"left:null".equals(result)) {
            throw new IllegalStateException("Expected missing trailing arg to be null, got: " + result);
        }
        System.out.println("QinJsMissingTrailingMethodArgsSmokeTestMain OK");
    }

    public static final class OptionalArgTarget {
        public String join(Object first, Object second) {
            return first + ":" + second;
        }
    }
}
