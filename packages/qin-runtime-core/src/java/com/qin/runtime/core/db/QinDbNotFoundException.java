package com.qin.runtime.core.db;

public final class QinDbNotFoundException extends RuntimeException {
    QinDbNotFoundException(String message) {
        super(message);
    }
}
