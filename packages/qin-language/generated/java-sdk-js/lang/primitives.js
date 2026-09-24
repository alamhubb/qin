import { __QinJavaLangSystem } from "./system.js";

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
  },
  getBoolean(name) {
    return __QinJavaLangBoolean.parseBoolean(__QinJavaLangSystem.getProperty(name));
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
class __QinJavaLangIntegerRuntime {
  isInstance(value) {
    return typeof value === "number" && Number.isInteger(value);
  }
  sum(a, b) { return a + b; }
  hashCode(value) { return Number(value) | 0; }
  compare(a, b) {
    const left = a;
    const right = b;
    if (left === right) {
      return 0;
    }
    if (left < right) {
      return -1;
    }
    return 1;
  }
  valueOf(value) { return value; }
  parseInt(value, radix) {
    return parseInt(value, radix === undefined ? 10 : radix);
  }
}
export const __QinJavaLangInteger = new __QinJavaLangIntegerRuntime();

class __QinJavaLangNumberRuntime {
  isInstance(value) {
    return typeof value === "number";
  }
  intValue(value) { return Number(value) | 0; }
  longValue(value) { return Math.trunc(Number(value)); }
  floatValue(value) { return Number(value); }
  doubleValue(value) { return Number(value); }
  byteValue(value) { return (Number(value) << 24) >> 24; }
  shortValue(value) { return (Number(value) << 16) >> 16; }
  valueOf(value) { return Number(value); }
}
export const __QinJavaLangNumber = new __QinJavaLangNumberRuntime();
for (const [name, bridge] of [
  ["intValue", (value) => __QinJavaLangNumber.intValue(value)],
  ["longValue", (value) => __QinJavaLangNumber.longValue(value)],
  ["floatValue", (value) => __QinJavaLangNumber.floatValue(value)],
  ["doubleValue", (value) => __QinJavaLangNumber.doubleValue(value)],
  ["byteValue", (value) => __QinJavaLangNumber.byteValue(value)],
  ["shortValue", (value) => __QinJavaLangNumber.shortValue(value)]
] as const) {
  const numberPrototype = Number.prototype as any;
  if (numberPrototype != null && typeof numberPrototype[name] !== "function") {
    numberPrototype[name] = function(this: Number) {
      return bridge(this.valueOf());
    };
  }
}

class __QinJavaLangDoubleRuntime {
  compare(a, b) {
    const left = a;
    const right = b;
    if (Number.isNaN(left) && Number.isNaN(right)) {
      return 0;
    }
    if (Number.isNaN(left)) {
      return 1;
    }
    if (Number.isNaN(right)) {
      return -1;
    }
    if (left === right) {
      return 0;
    }
    if (left < right) {
      return -1;
    }
    return 1;
  }
  parseDouble(value) { return parseFloat(value); }
  valueOf(value) { return value; }
}
export const __QinJavaLangDouble = new __QinJavaLangDoubleRuntime();

class __QinJavaLangLongRuntime {
  sum(a, b) { return a + b; }
  hashCode(value) {
    const longValue = typeof value === "bigint" ? value : BigInt(Math.trunc(Number(value)));
    return Number(longValue ^ (longValue >> 32n)) | 0;
  }
  compare(a, b) {
    const left = a;
    const right = b;
    if (left === right) {
      return 0;
    }
    if (left < right) {
      return -1;
    }
    return 1;
  }
  valueOf(value) { return value; }
  parseLong(value, radix) {
    return parseInt(value, radix === undefined ? 10 : radix);
  }
  toString(value, radix) {
    return value.toString(radix === undefined ? 10 : radix);
  }
}
export const __QinJavaLangLong = new __QinJavaLangLongRuntime();
