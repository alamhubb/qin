export const __QinJavaLangBoolean = {
  TRUE: Object.freeze({
    equals(value) { return value === true || (value != null && typeof value.valueOf === "function" && value.valueOf() === true); },
    valueOf() { return true; },
    toString() { return "true"; }
  }),
  FALSE: Object.freeze({
    equals(value) { return value === false || (value != null && typeof value.valueOf === "function" && value.valueOf() === false); },
    valueOf() { return false; },
    toString() { return "false"; }
  }),
  valueOf(value) {
    return value === true || String(value).toLowerCase() === "true";
  },
  parseBoolean(value) {
    return String(value).toLowerCase() === "true";
  }
};
export const __QinJavaLangCharacter = {
  __char(value) { return typeof value === "number" ? String.fromCodePoint(value) : String(value).charAt(0); },
  isWhitespace(value) { return /\s/u.test(__QinJavaLangCharacter.__char(value)); },
  isLetter(value) { return /\p{L}/u.test(__QinJavaLangCharacter.__char(value)); },
  isLetterOrDigit(value) { return /[\p{L}\p{N}]/u.test(__QinJavaLangCharacter.__char(value)); },
  isJavaIdentifierStart(value) { return /[$_\p{L}]/u.test(__QinJavaLangCharacter.__char(value)); },
  toUpperCase(value) { return __QinJavaLangCharacter.__char(value).toUpperCase(); },
  toLowerCase(value) { return __QinJavaLangCharacter.__char(value).toLowerCase(); },
  charCount(value) { return Number(value) > 0xffff ? 2 : 1; }
};
export const __QinSlf4jLogger = {
  warn(_message, ..._args) { return null; },
  info(_message, ..._args) { return null; },
  debug(_message, ..._args) { return null; },
  error(_message, ..._args) { return null; },
  trace(_message, ..._args) { return null; }
};
export const __QinSlf4jLoggerFactory = {
  getLogger(_owner) { return __QinSlf4jLogger; }
};
export const __QinJavaLangInteger = {
  sum(a, b) { return (a | 0) + (b | 0); },
  compare(a, b) { return (a | 0) === (b | 0) ? 0 : ((a | 0) < (b | 0) ? -1 : 1); },
  valueOf(value) { return value | 0; },
  parseInt(value, radix = 10) { return Number.parseInt(String(value), radix); }
};
export const __QinJavaLangDouble = {
  compare(a, b) {
    const left = Number(a);
    const right = Number(b);
    if (Number.isNaN(left) && Number.isNaN(right)) {
      return 0;
    }
    if (Number.isNaN(left)) {
      return 1;
    }
    if (Number.isNaN(right)) {
      return -1;
    }
    return left === right ? 0 : (left < right ? -1 : 1);
  },
  parseDouble(value) { return Number.parseFloat(String(value)); },
  valueOf(value) { return Number(value); }
};
