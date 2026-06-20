package com.qin.runtime.core.qono;

import com.qin.runtime.core.QinHttpRequest;

@FunctionalInterface
public interface QonoHandler {
    Object handle(QinHttpRequest request) throws Exception;
}
