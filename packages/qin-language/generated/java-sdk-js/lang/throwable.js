export class __QinJavaLangThrowable {
  constructor(message, cause) {
    this.name = this.constructor.name;
    this.message = message == null ? null : String(message);
    this.__cause = cause == null ? null : cause;
  }
  getMessage() {
    return this.message;
  }
  getCause() {
    return this.__cause;
  }
  toString() {
    return this.message == null || this.message === ""
      ? this.name
      : this.name + ": " + this.message;
  }
}
export class __QinJavaLangException extends __QinJavaLangThrowable {
}
export class __QinJavaLangRuntimeException extends __QinJavaLangException {
}
export class __QinJavaLangReflectiveOperationException extends __QinJavaLangException {
}
export class __QinJavaLangClassNotFoundException extends __QinJavaLangException {
}
export class __QinJavaLangNoSuchMethodException extends __QinJavaLangReflectiveOperationException {
}
export class __QinJavaLangReflectInvocationTargetException extends __QinJavaLangReflectiveOperationException {
}
export class __QinJavaLangError extends __QinJavaLangThrowable {
}
export class __QinJavaLangStackOverflowError extends __QinJavaLangError {
}
export class __QinJavaLangIllegalArgumentException extends __QinJavaLangRuntimeException {
}
export class __QinJavaLangNumberFormatException extends __QinJavaLangIllegalArgumentException {
}
export class __QinJavaLangIllegalStateException extends __QinJavaLangRuntimeException {
}
export class __QinJavaLangUnsupportedOperationException extends __QinJavaLangRuntimeException {
}
export class __QinJavaIoIOException extends __QinJavaLangException {
}
