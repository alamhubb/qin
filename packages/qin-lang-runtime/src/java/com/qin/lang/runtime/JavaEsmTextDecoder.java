package com.qin.lang.runtime;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Minimal TextDecoder builtin for npm packages that decode Uint8Array buffers.
 */
public final class JavaEsmTextDecoder {
    private final Charset charset;

    public JavaEsmTextDecoder() {
        this(StandardCharsets.UTF_8);
    }

    public JavaEsmTextDecoder(Object label) {
        this(resolveCharset(label));
    }

    private JavaEsmTextDecoder(Charset charset) {
        this.charset = charset;
    }

    public Object decode() {
        return "";
    }

    public Object decode(Object input) {
        byte[] bytes = toBytes(input);
        return charset.decode(ByteBuffer.wrap(bytes)).toString();
    }

    private static Charset resolveCharset(Object label) {
        if (label == null) {
            return StandardCharsets.UTF_8;
        }
        String normalized = String.valueOf(label).trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "", "utf-8", "utf8", "unicode-1-1-utf-8" -> StandardCharsets.UTF_8;
            case "utf-16", "utf-16le" -> StandardCharsets.UTF_16LE;
            case "utf-16be" -> StandardCharsets.UTF_16BE;
            case "iso-8859-1", "latin1", "latin-1" -> StandardCharsets.ISO_8859_1;
            case "us-ascii", "ascii" -> StandardCharsets.US_ASCII;
            default -> Charset.forName(String.valueOf(label));
        };
    }

    private static byte[] toBytes(Object input) {
        input = JavaEsmGlobal.__qin_value__(input);
        if (input == null) {
            return new byte[0];
        }
        if (input instanceof byte[] bytes) {
            return bytes;
        }
        if (input instanceof CharSequence text) {
            return text.toString().getBytes(StandardCharsets.UTF_8);
        }
        if (input instanceof Iterable<?> iterable) {
            List<Byte> bytes = new ArrayList<>();
            for (Object value : iterable) {
                bytes.add((byte) toUnsignedByte(value));
            }
            byte[] result = new byte[bytes.size()];
            for (int i = 0; i < bytes.size(); i++) {
                result[i] = bytes.get(i);
            }
            return result;
        }
        if (input.getClass().isArray()) {
            int length = Array.getLength(input);
            byte[] result = new byte[length];
            for (int i = 0; i < length; i++) {
                result[i] = (byte) toUnsignedByte(Array.get(input, i));
            }
            return result;
        }
        throw new IllegalArgumentException("Unsupported TextDecoder input: " + input.getClass().getName());
    }

    private static int toUnsignedByte(Object value) {
        if (value instanceof Number number) {
            return number.intValue() & 0xff;
        }
        if (value instanceof Character character) {
            return character & 0xff;
        }
        if (value instanceof CharSequence text) {
            try {
                return (int) Double.parseDouble(text.toString()) & 0xff;
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }
        return 0;
    }
}
