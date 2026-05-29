package com.qin.lang.runtime;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

/**
 * Java-backed Date builtin subset for Qin.
 */
public final class JavaEsmDate {
    private final double epochMillis;

    private JavaEsmDate(double epochMillis) {
        this.epochMillis = epochMillis;
    }

    public static Object now() {
        return (double) System.currentTimeMillis();
    }

    public static JavaEsmDate create(Object... args) {
        if (args == null || args.length == 0 || args[0] == null) {
            return new JavaEsmDate(System.currentTimeMillis());
        }
        Object first = args[0];
        if (first instanceof JavaEsmDate date) {
            return new JavaEsmDate(date.epochMillis);
        }
        if (first instanceof Number number) {
            return new JavaEsmDate(number.doubleValue());
        }
        String text = String.valueOf(first);
        try {
            return new JavaEsmDate(Instant.parse(text).toEpochMilli());
        } catch (DateTimeParseException ignored) {
            throw new IllegalArgumentException("Unsupported Date constructor argument: " + text);
        }
    }

    public Object getTime() {
        return epochMillis;
    }

    public Object valueOf() {
        return epochMillis;
    }

    public Object getFullYear() {
        return (double) local().getYear();
    }

    public Object getMonth() {
        return (double) (local().getMonthValue() - 1);
    }

    public Object getDate() {
        return (double) local().getDayOfMonth();
    }

    public Object getDay() {
        return (double) (local().getDayOfWeek().getValue() % 7);
    }

    public Object getHours() {
        return (double) local().getHour();
    }

    public Object getMinutes() {
        return (double) local().getMinute();
    }

    public Object getSeconds() {
        return (double) local().getSecond();
    }

    public Object getMilliseconds() {
        return (double) (local().getNano() / 1_000_000);
    }

    public Object getTimezoneOffset() {
        return (double) (-local().getOffset().getTotalSeconds() / 60);
    }

    public Object getUTCFullYear() {
        return (double) utc().getYear();
    }

    public Object getUTCMonth() {
        return (double) (utc().getMonthValue() - 1);
    }

    public Object getUTCDate() {
        return (double) utc().getDayOfMonth();
    }

    public Object getUTCDay() {
        return (double) (utc().getDayOfWeek().getValue() % 7);
    }

    public Object getUTCHours() {
        return (double) utc().getHour();
    }

    public Object getUTCMinutes() {
        return (double) utc().getMinute();
    }

    public Object getUTCSeconds() {
        return (double) utc().getSecond();
    }

    public Object getUTCMilliseconds() {
        return (double) (utc().getNano() / 1_000_000);
    }

    public Object toISOString() {
        return instant().toString();
    }

    @Override
    public String toString() {
        return local().toString();
    }

    private Instant instant() {
        return Instant.ofEpochMilli((long) epochMillis);
    }

    private ZonedDateTime local() {
        return instant().atZone(ZoneId.systemDefault());
    }

    private ZonedDateTime utc() {
        return instant().atZone(ZoneId.of("UTC"));
    }
}
