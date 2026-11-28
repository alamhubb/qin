package com.qin.orm;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public final class QinOrmSubquery<R> {
    private final QinOrmSelect<?, R> select;
    private final Set<Class<?>> outerTypes;

    QinOrmSubquery(QinOrmSelect<?, R> select, Set<Class<?>> outerTypes) {
        this.select = Objects.requireNonNull(select, "select");
        this.outerTypes = Set.copyOf(outerTypes);
    }

    public QinOrmPredicate<Object> exists() {
        return QinOrmPredicate.exists(this);
    }

    String render(QinOrmJpqlContext context, Function<Class<?>, String> parentAliases) {
        return select.renderSubquery(context, type -> {
            if (!outerTypes.contains(type)) {
                throw new IllegalArgumentException("Outer subquery entity must be declared explicitly: "
                        + type.getName());
            }
            return parentAliases.apply(type);
        });
    }

    Class<R> resultType() {
        return select.subqueryResultType();
    }
}
