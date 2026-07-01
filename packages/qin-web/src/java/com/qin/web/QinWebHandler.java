package com.qin.web;

import com.qin.runtime.core.QinHttpRequest;

@FunctionalInterface
public interface QinWebHandler {
    Object handle(QinHttpRequest request) throws Exception;
}
