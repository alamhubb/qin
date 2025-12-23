package com.qin.npm;

import java.io.*;

/**
 * 简单的 TAR 输入流实现
 * 用于解压 npm 包的 .tgz 文件
 */
public class TarInputStream extends FilterInputStream {
    private static final int BLOCK_SIZE = 512;
    private TarEntry currentEntry;
    private long remaining;

    public TarInputStream(InputStream in) {
        super(in);
    }

    public TarEntry getNextEntry() throws IOException {
        // 跳过当前条目剩余内容
        if (remaining > 0) {
            skip(remaining);
        }
        // 跳过填充
        if (currentEntry != null) {
            long padding = (BLOCK_SIZE - (currentEntry.size % BLOCK_SIZE)) % BLOCK_SIZE;
            skip(padding);
        }

        byte[] header = new byte[BLOCK_SIZE];
        int read = readFully(header);
        if (read < BLOCK_SIZE) {
            return null;
        }

        // 检查是否为空块（文件结束）
        boolean empty = true;
        for (byte b : header) {
            if (b != 0) {
                empty = false;
                break;
            }
        }
        if (empty) {
            return null;
        }

        currentEntry = parseHeader(header);
        remaining = currentEntry.size;
        return currentEntry;
    }

    private TarEntry parseHeader(byte[] header) {
        TarEntry entry = new TarEntry();
        
        // 文件名 (0-99)
        entry.name = parseString(header, 0, 100);
        
        // 文件大小 (124-135, 八进制)
        entry.size = parseOctal(header, 124, 12);
        
        // 类型标志 (156)
        byte typeFlag = header[156];
        entry.directory = (typeFlag == '5');
        
        // 前缀 (345-499) - 用于长文件名
        String prefix = parseString(header, 345, 155);
        if (!prefix.isEmpty()) {
            entry.name = prefix + "/" + entry.name;
        }

        return entry;
    }

    private String parseString(byte[] data, int offset, int length) {
        int end = offset;
        while (end < offset + length && data[end] != 0) {
            end++;
        }
        return new String(data, offset, end - offset).trim();
    }

    private long parseOctal(byte[] data, int offset, int length) {
        String str = parseString(data, offset, length).trim();
        if (str.isEmpty()) return 0;
        try {
            return Long.parseLong(str, 8);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int readFully(byte[] buffer) throws IOException {
        int total = 0;
        while (total < buffer.length) {
            int read = in.read(buffer, total, buffer.length - total);
            if (read < 0) break;
            total += read;
        }
        return total;
    }

    @Override
    public int read() throws IOException {
        if (remaining <= 0) return -1;
        int b = in.read();
        if (b >= 0) remaining--;
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (remaining <= 0) return -1;
        int toRead = (int) Math.min(len, remaining);
        int read = in.read(b, off, toRead);
        if (read > 0) remaining -= read;
        return read;
    }

    @Override
    public long skip(long n) throws IOException {
        long skipped = 0;
        byte[] buffer = new byte[8192];
        while (skipped < n) {
            int toSkip = (int) Math.min(buffer.length, n - skipped);
            int read = in.read(buffer, 0, toSkip);
            if (read < 0) break;
            skipped += read;
        }
        return skipped;
    }
}

/**
 * TAR 条目
 */
class TarEntry {
    String name;
    long size;
    boolean directory;

    public String getName() { return name; }
    public long getSize() { return size; }
    public boolean isDirectory() { return directory; }
}
