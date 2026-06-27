// Virtual path shims used by Qin tooling-generated JS, not by the public Web SDK.
export class __QinJavaIoFile {
  constructor(pathOrParent, child) {
    if (arguments.length >= 2) {
      const parentPath = pathOrParent instanceof __QinJavaIoFile
        ? pathOrParent.getPath()
        : String(pathOrParent == null ? "" : pathOrParent);
      this.__path = __QinJavaIoFile.__join(parentPath, child);
    } else {
      this.__path = String(pathOrParent == null ? "" : pathOrParent);
    }
    this.__path = __QinJavaIoFile.__normalize(this.__path);
  }
  static __separator() {
    return globalThis.__qinJavaFileSeparator == null
      ? "/"
      : String(globalThis.__qinJavaFileSeparator);
  }
  static __separatorCharCode() {
    const sep = __QinJavaIoFile.__separator();
    return sep.length === 0 ? 47 : sep.charCodeAt(0);
  }
  static __isSeparatorCode(code) {
    return code === 47 || code === 92;
  }
  static __normalize(path) {
    const text = String(path == null ? "" : path);
    if (text.length === 0) {
      return text;
    }
    const sep = __QinJavaIoFile.__separator();
    const sepCode = __QinJavaIoFile.__separatorCharCode();
    let normalized = "";
    for (let i = 0; i < text.length; i++) {
      const code = text.charCodeAt(i);
      normalized += __QinJavaIoFile.__isSeparatorCode(code)
        ? String.fromCharCode(sepCode)
        : text.charAt(i);
    }
    const prefix = __QinJavaIoFile.__drivePrefix(normalized);
    let rest = prefix.length === 0 ? normalized : normalized.slice(prefix.length);
    const absolute = rest.startsWith(sep);
    const parts = [];
    let part = "";
    for (let i = 0; i <= rest.length; i++) {
      const atEnd = i === rest.length;
      const ch = atEnd ? "" : rest.charAt(i);
      if (!atEnd && ch !== sep) {
        part += ch;
        continue;
      }
      if (part.length === 0 || part === ".") {
        part = "";
        continue;
      }
      if (part === "..") {
        if (parts.length > 0 && parts[parts.length - 1] !== "..") {
          parts.pop();
        } else if (!absolute) {
          parts.push(part);
        }
        part = "";
        continue;
      }
      parts.push(part);
      part = "";
    }
    const body = parts.join(sep);
    return prefix + (absolute ? sep : "") + body;
  }
  static __drivePrefix(path) {
    const text = String(path == null ? "" : path);
    if (text.length < 2 || text.charAt(1) !== ":") {
      return "";
    }
    const code = text.charCodeAt(0);
    const isUpper = code >= 65 && code <= 90;
    const isLower = code >= 97 && code <= 122;
    return isUpper || isLower ? text.slice(0, 2) : "";
  }
  static __join(parent, child) {
    const sep = __QinJavaIoFile.__separator();
    const left = String(parent == null ? "" : parent);
    const right = String(child == null ? "" : child);
    if (left.length === 0) {
      return right;
    }
    if (right.length === 0) {
      return left;
    }
    const lastCode = left.charCodeAt(left.length - 1);
    return __QinJavaIoFile.__isSeparatorCode(lastCode) ? left + right : left + sep + right;
  }
  static __configuredExists(path) {
    const normalized = __QinJavaIoFile.__normalize(path);
    const hook = globalThis.__qinJavaFileExists;
    if (typeof hook === "function") {
      return !!hook(normalized);
    }
    const files = globalThis.__qinJavaExistingFiles;
    if (files == null) {
      return false;
    }
    if (typeof files.has === "function") {
      return !!(files.has(normalized) || files.has(String(path)));
    }
    if (Array.isArray(files)) {
      return files.indexOf(normalized) >= 0 || files.indexOf(String(path)) >= 0;
    }
    if (typeof files === "object") {
      return !!(files[normalized] || files[String(path)]);
    }
    return false;
  }
  getPath() {
    return this.__path;
  }
  getAbsolutePath() {
    return this.__path;
  }
  getParentFile() {
    const sep = __QinJavaIoFile.__separator();
    const path = this.__path;
    if (path == null || path.length === 0) {
      return null;
    }
    let end = path.length;
    while (end > 1 && __QinJavaIoFile.__isSeparatorCode(path.charCodeAt(end - 1))) {
      end--;
    }
    const trimmed = path.slice(0, end);
    let index = -1;
    for (let i = trimmed.length - 1; i >= 0; i--) {
      if (__QinJavaIoFile.__isSeparatorCode(trimmed.charCodeAt(i))) {
        index = i;
        break;
      }
    }
    if (index < 0) {
      return null;
    }
    if (index === 0) {
      return new __QinJavaIoFile(trimmed.charAt(0));
    }
    if (index === 2 && __QinJavaIoFile.__drivePrefix(trimmed).length > 0) {
      return new __QinJavaIoFile(trimmed.slice(0, 3));
    }
    return new __QinJavaIoFile(trimmed.slice(0, index));
  }
  exists() {
    return __QinJavaIoFile.__configuredExists(this.__path);
  }
  equals(other) {
    return other instanceof __QinJavaIoFile && other.getPath() === this.__path;
  }
  toString() {
    return this.__path;
  }
}
__QinJavaIoFile.separator = __QinJavaIoFile.__separator();
export class __QinJavaNioFilePath {
  constructor(path) {
    this.__path = path == null ? "" : String(path);
  }
  getParent() {
    const parent = new __QinJavaIoFile(this.__path).getParentFile();
    return parent == null ? null : new __QinJavaNioFilePath(parent.getPath());
  }
  toString() {
    return this.__path;
  }
}
export class __QinJavaNioFilePaths {
  static get(first, ...more) {
    let path = first == null ? "" : String(first);
    for (const part of more) {
      path = __QinJavaIoFile.__join(path, part == null ? "" : String(part));
    }
    return new __QinJavaNioFilePath(path);
  }
}
export class __QinJavaNioFileFiles {
  static exists(path) {
    return false;
  }
  static createDirectories(path) {
    return path;
  }
}
export class __QinJavaIoFileWriter {
  constructor(filePath, append = false) {
    this.filePath = filePath;
    this.append = append;
    this.buffer = "";
  }
  write(message) {
    this.buffer += message == null ? "null" : String(message);
  }
  flush() {}
  close() {}
}
export class __QinJavaIoBufferedWriter {
  constructor(writer) {
    this.writer = writer;
  }
  write(message) {
    if (this.writer && typeof this.writer.write === "function") {
      this.writer.write(message);
    }
  }
  newLine() {
    this.write("\n");
  }
  flush() {
    if (this.writer && typeof this.writer.flush === "function") {
      this.writer.flush();
    }
  }
  close() {
    if (this.writer && typeof this.writer.close === "function") {
      this.writer.close();
    }
  }
}
