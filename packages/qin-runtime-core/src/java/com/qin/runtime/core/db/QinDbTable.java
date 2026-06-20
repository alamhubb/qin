package com.qin.runtime.core.db;

import java.util.ArrayList;
import java.util.List;

public final class QinDbTable {
    private final String name;
    private final List<QinDbColumn> columns = new ArrayList<>();

    QinDbTable(String name) {
        this.name = QinDb.normalizeIdentifier(name, "table name");
    }

    public QinDbColumn bigserial(String name) {
        return add(name, "bigserial");
    }

    public QinDbColumn text(String name) {
        return add(name, "text");
    }

    public QinDbColumn timestamptz(String name) {
        return add(name, "timestamptz");
    }

    public String name() {
        return name;
    }

    public List<QinDbColumn> columns() {
        return List.copyOf(columns);
    }

    public QinDbColumn column(String name) {
        String normalized = QinDb.normalizeIdentifier(name, "column name");
        for (QinDbColumn column : columns) {
            if (column.sqlName().equals(normalized) || column.jsonName().equals(normalized)) {
                return column;
            }
        }
        throw new IllegalArgumentException("Unknown column " + name + " on table " + this.name);
    }

    public QinDbColumn primaryKeyColumn() {
        for (QinDbColumn column : columns) {
            if (column.primaryKeyValue()) {
                return column;
            }
        }
        throw new IllegalStateException("Table has no primary key: " + name);
    }

    private QinDbColumn add(String name, String type) {
        QinDbColumn column = new QinDbColumn(this, name, type);
        columns.add(column);
        return column;
    }
}
