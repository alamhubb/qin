package com.qin.runtime.http;

@FunctionalInterface
public interface QinHttpHandler {
    QinHttpResponse handle(QinHttpContext context) throws Exception;
}
