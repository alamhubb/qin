package com.qin.lang.ir;

/**
 * Canonical names for generated {@code @qin/java-sdk-js} facade classes.
 *
 * The source-visible generated TypeScript uses stable facade identifiers such
 * as {@code __QinJavaLangRuntimeException}. JVM declaration lowering and
 * emission must resolve those identifiers to the owning Java runtime classes
 * when they are used as static superclasses, constructors, throws, or casts.
 */
public final class QinJavaSdkAliasSupport {
    private QinJavaSdkAliasSupport() {
    }

    public static String canonicalBinaryName(String binaryName) {
        if (binaryName == null) {
            return null;
        }
        return switch (binaryName) {
            case "QinJavaLangInteger" -> "java.lang.Integer";
            case "QinJavaLangNumber" -> "java.lang.Number";
            case "QinJavaLangDouble" -> "java.lang.Double";
            case "QinJavaLangLong" -> "java.lang.Long";
            case "__QinJavaLangStringBuilder" -> "java.lang.StringBuilder";
            case "__QinJavaLangString" -> "java.lang.String";
            case "__QinJavaLangClass" -> "java.lang.Class";
            case "__QinJavaLangBoolean" -> "java.lang.Boolean";
            case "__QinJavaLangInteger" -> "java.lang.Integer";
            case "__QinJavaLangNumber" -> "java.lang.Number";
            case "__QinJavaLangDouble" -> "java.lang.Double";
            case "__QinJavaLangLong" -> "java.lang.Long";
            case "__QinJavaMathBigInteger" -> "java.math.BigInteger";
            case "__QinJavaLangEnum" -> "java.lang.Enum";
            case "__QinJavaLangThrowable" -> "java.lang.Throwable";
            case "__QinJavaLangException" -> "java.lang.Exception";
            case "__QinJavaLangRuntimeException" -> "java.lang.RuntimeException";
            case "__QinJavaLangReflectiveOperationException" -> "java.lang.ReflectiveOperationException";
            case "__QinJavaLangClassNotFoundException" -> "java.lang.ClassNotFoundException";
            case "__QinJavaLangClassCastException" -> "java.lang.ClassCastException";
            case "__QinJavaLangNoSuchMethodException" -> "java.lang.NoSuchMethodException";
            case "__QinJavaLangReflectMethod" -> "java.lang.reflect.Method";
            case "__QinJavaLangReflectInvocationTargetException" -> "java.lang.reflect.InvocationTargetException";
            case "__QinJavaLangNumberFormatException" -> "java.lang.NumberFormatException";
            case "__QinJavaLangUnsupportedOperationException" -> "java.lang.UnsupportedOperationException";
            case "__QinJavaLangError" -> "java.lang.Error";
            case "__QinJavaLangStackOverflowError" -> "java.lang.StackOverflowError";
            case "__QinJavaLangIllegalArgumentException" -> "java.lang.IllegalArgumentException";
            case "__QinJavaLangIllegalStateException" -> "java.lang.IllegalStateException";
            case "__QinJavaIoIOException" -> "java.io.IOException";
            case "__QinJavaLangSystem" -> "java.lang.System";
            case "__QinJavaLangCharacter" -> "java.lang.Character";
            case "__QinJavaIoFile" -> "java.io.File";
            case "__QinJavaIoByteArrayOutputStream" -> "java.io.ByteArrayOutputStream";
            case "__QinJavaIoByteArrayInputStream" -> "java.io.ByteArrayInputStream";
            case "__QinJavaIoDataOutputStream" -> "java.io.DataOutputStream";
            case "__QinJavaIoDataInputStream" -> "java.io.DataInputStream";
            case "__QinJavaIoFileWriter" -> "java.io.FileWriter";
            case "__QinJavaIoBufferedWriter" -> "java.io.BufferedWriter";
            case "__QinJavaNioFilePath" -> "java.nio.file.Path";
            case "__QinJavaNioFilePaths" -> "java.nio.file.Paths";
            case "__QinJavaNioFileFiles" -> "java.nio.file.Files";
            case "__QinJavaNioCharsetStandardCharsets" -> "java.nio.charset.StandardCharsets";
            case "__QinJavaTimeLocalDateTime" -> "java.time.LocalDateTime";
            case "__QinJavaTimeFormatDateTimeFormatter" -> "java.time.format.DateTimeFormatter";
            case "__QinJavaUtilList" -> "java.util.List";
            case "__QinJavaUtilSet" -> "java.util.Set";
            case "__QinJavaUtilArrays" -> "java.util.Arrays";
            case "__QinJavaUtilCollections" -> "java.util.Collections";
            case "__QinJavaUtilArrayList" -> "java.util.ArrayList";
            case "__QinJavaUtilArrayDeque" -> "java.util.ArrayDeque";
            case "__QinJavaUtilDeque" -> "java.util.Deque";
            case "__QinJavaUtilHashSet" -> "java.util.HashSet";
            case "__QinJavaUtilTreeSet" -> "java.util.TreeSet";
            case "__QinJavaUtilHashMap" -> "java.util.HashMap";
            case "__QinJavaUtilLinkedHashMap" -> "java.util.LinkedHashMap";
            case "__QinJavaUtilIdentityHashMap" -> "java.util.IdentityHashMap";
            case "__QinJavaUtilConcurrentAtomicLong" -> "java.util.concurrent.atomic.AtomicLong";
            case "__QinJavaUtilMap" -> "java.util.Map";
            case "__QinJavaUtilMapEntry" -> "java.util.Map$Entry";
            case "__QinJavaUtilObjects" -> "java.util.Objects";
            case "__QinJavaUtilOptional" -> "java.util.Optional";
            case "__QinJavaUtilComparator" -> "java.util.Comparator";
            case "__QinJavaUtilStream" -> "java.util.stream.Stream";
            case "__QinJavaUtilStreamCollectors" -> "java.util.stream.Collectors";
            case "__QinJavaUtilZipGZIPOutputStream" -> "java.util.zip.GZIPOutputStream";
            case "__QinJavaUtilZipGZIPInputStream" -> "java.util.zip.GZIPInputStream";
            case "__QinJavaUtilBase64" -> "java.util.Base64";
            case "__QinJavaSecurityMessageDigest" -> "java.security.MessageDigest";
            case "__QinJavaUtilHexFormat" -> "java.util.HexFormat";
            case "__QinSubhutiCompileOnlyDsl" -> "com.subhuti.parser.SubhutiCompileOnlyDsl";
            default -> binaryName;
        };
    }

    public static boolean isKnownAlias(String binaryName) {
        return binaryName != null && !binaryName.equals(canonicalBinaryName(binaryName));
    }
}
