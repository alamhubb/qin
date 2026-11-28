package com.qin.orm;

import jakarta.persistence.Tuple;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class QinOrmRow {
    private final Tuple tuple;

    QinOrmRow(Tuple tuple) {
        this.tuple = Objects.requireNonNull(tuple, "tuple");
    }

    public Object get(String propertyName) {
        return tuple.get(propertyName);
    }

    public <V> V get(QinOrmColumn<?, V> column) {
        return tuple.get(column.propertyName(), column.propertyType());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new LinkedHashMap<>();
        tuple.getElements().forEach(element -> values.put(element.getAlias(), tuple.get(element)));
        return Collections.unmodifiableMap(values);
    }
}
