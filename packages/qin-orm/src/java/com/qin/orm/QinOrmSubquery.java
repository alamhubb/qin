package com.qin.orm;

import java.util.Objects;
import java.util.function.Function;

public final class QinOrmSubquery<R> {
    private final QinOrmSelect<?, R> select;

    QinOrmSubquery(QinOrmSelect<?, R> select) {
        this.select = Objects.requireNonNull(select, "select");
    }

    public QinOrmPredicate<Object> exists() {
        return QinOrmPredicate.exists(this);
    }

    String render(QinOrmJpqlContext context, Function<Class<?>, String> parentAliases) {
        return select.renderSubquery(context, parentAliases);
    }

    Class<R> resultType() {
        return select.subqueryResultType();
    }
}
