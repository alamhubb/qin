package com.qin.runtime.core;

@FunctionalInterface
interface QinVueSpecifierRewriter {
    String rewrite(String specifier);
}
