package com.qin.runtime.core.vue;

@FunctionalInterface
public interface QinVueModuleImportRewriter {
    String rewrite(String specifier);
}
