package com.qin.runtime.core.db;

public final class QinDbColumn {
    private final QinDbTable table;
    private final String sqlName;
    private final String jsonName;
    private final String type;
    private boolean primaryKey;
    private boolean notNull;
    private boolean unique;
    private boolean defaultNow;

    QinDbColumn(QinDbTable table, String sqlName, String type) {
        this.table = table;
        this.sqlName = QinDb.normalizeIdentifier(sqlName, "column name");
        this.jsonName = QinDb.toJsonName(this.sqlName);
        this.type = type;
    }

    public QinDbColumn primaryKey() {
        primaryKey = true;
        return this;
    }

    public QinDbColumn notNull() {
        notNull = true;
        return this;
    }

    public QinDbColumn unique() {
        unique = true;
        return this;
    }

    public QinDbColumn defaultNow() {
        defaultNow = true;
        return this;
    }

    public QinDbTable table() {
        return table;
    }

    public String sqlName() {
        return sqlName;
    }

    public String jsonName() {
        return jsonName;
    }

    public boolean primaryKeyValue() {
        return primaryKey;
    }

    String definitionSql() {
        StringBuilder sql = new StringBuilder(sqlName).append(' ').append(type);
        if (primaryKey) {
            sql.append(" primary key");
        }
        if (notNull) {
            sql.append(" not null");
        }
        if (unique) {
            sql.append(" unique");
        }
        if (defaultNow) {
            sql.append(" default now()");
        }
        return sql.toString();
    }
}
