package com.qin.lang.ir;

import java.util.Objects;

/**
 * Console log statement: console.log(<objectName>.<propertyName>)
 */
public record QinIrConsoleLogStatement(String objectName, String propertyName) {
    public QinIrConsoleLogStatement {
        Objects.requireNonNull(objectName, "objectName cannot be null");
        Objects.requireNonNull(propertyName, "propertyName cannot be null");
    }
}
