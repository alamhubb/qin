export const __qin_builtin_constructor__ = globalThis.__qin_builtin_constructor__ || ((name) => {
  const ctor = globalThis[name];
  if (typeof ctor !== "function") {
    throw new Error("Missing host constructor for Qin generated JS: " + name);
  }
  return ctor;
});
export const __qin_java_pattern_regexp__ = globalThis.__qin_java_pattern_regexp__ || ((source, flags = "") => {
  const jsSource = String(source).replace(/\\Q([\s\S]*?)\\E/g, (_match, literal) => {
    return literal.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
  });
  let jsFlags = String(flags);
  if (/\\[pP]\{/.test(jsSource) && !jsFlags.includes("u")) {
    jsFlags += "u";
  }
  return new RegExp(jsSource, jsFlags);
});
export const __qin_java_regex_pattern_compile__ = globalThis.__qin_java_regex_pattern_compile__ || ((source, flags = 0) => {
  return __qin_java_pattern_regexp__(source, flags);
});
export const __qin_java_regex_pattern_exec__ = globalThis.__qin_java_regex_pattern_exec__ || ((pattern, input) => {
  const re = pattern instanceof RegExp ? pattern : __qin_java_pattern_regexp__(pattern, "");
  return RegExp.prototype.exec.call(re, String(input));
});
export const __qin_java_new_array__ = globalThis.__qin_java_new_array__ || ((_arrayType, length) => {
  return Array.from({ length: Math.max(0, Number(length) | 0) }, () => null);
});
function __qin_java_regex_matcher_quote_replacement(text) {
  return String(text).replace(/\\/g, "\\\\").replace(/\$/g, "\\\$");
}
function __qin_java_regex_matcher_store_match(matcher, match, absoluteStart) {
  const groups = [];
  for (let index = 0; index < match.length; index++) {
    groups.push(match[index] == null ? null : match[index]);
  }
  matcher.__lastMatchGroups = groups;
  matcher.__lastMatchStart = absoluteStart;
  matcher.__lastMatchEnd = absoluteStart + String(match[0] == null ? "" : match[0]).length;
}
function __qin_java_regex_matcher_require_last_match(matcher) {
  if (matcher.__lastMatchGroups == null) {
    throw new Error("No match available");
  }
  return matcher.__lastMatchGroups;
}
function __qin_java_regex_matcher_match_at_region_start(matcher, requireFullRegion) {
  const re = matcher.__pattern;
  re.lastIndex = matcher.__regionStart;
  const text = matcher.__input.slice(matcher.__regionStart, matcher.__regionEnd);
  const match = __qin_java_regex_pattern_exec__(re, text);
  if (match == null || match.index !== 0) {
    matcher.__lastMatchGroups = null;
    return false;
  }
  __qin_java_regex_matcher_store_match(matcher, match, matcher.__regionStart);
  return !requireFullRegion || matcher.__lastMatchEnd === matcher.__regionEnd;
}
export const __qin_java_regex_matcher__ = globalThis.__qin_java_regex_matcher__ || ((pattern, input) => {
  const nativePattern = pattern instanceof RegExp ? pattern : __qin_java_pattern_regexp__(pattern, "");
  return {
    __pattern: nativePattern,
    __input: String(input),
    __regionStart: 0,
    __regionEnd: String(input).length,
    __searchIndex: 0,
    __lastMatchGroups: null,
    __lastMatchStart: 0,
    __lastMatchEnd: 0,
    __appendPosition: 0
  };
});
export const __qin_java_regex_matcher_region__ = globalThis.__qin_java_regex_matcher_region__ || ((matcher, start, end) => {
  matcher.__regionStart = Math.max(0, start | 0);
  matcher.__regionEnd = Math.min(matcher.__input.length, Math.max(matcher.__regionStart, end | 0));
  matcher.__searchIndex = matcher.__regionStart;
  matcher.__lastMatchGroups = null;
  return matcher;
});
export const __qin_java_regex_matcher_reset__ = globalThis.__qin_java_regex_matcher_reset__ || ((...__qin_args) => {
  const matcher = __qin_args[0];
  const input = __qin_args.length > 1 ? __qin_args[1] : null;
  if (__qin_args.length > 1) {
    matcher.__input = String(input);
  }
  matcher.__regionStart = 0;
  matcher.__regionEnd = matcher.__input.length;
  matcher.__searchIndex = 0;
  matcher.__lastMatchGroups = null;
  matcher.__appendPosition = 0;
  return matcher;
});
export const __qin_java_regex_matcher_looking_at__ = globalThis.__qin_java_regex_matcher_looking_at__ || ((matcher) => {
  return __qin_java_regex_matcher_match_at_region_start(matcher, false);
});
export const __qin_java_regex_matcher_matches__ = globalThis.__qin_java_regex_matcher_matches__ || ((matcher) => {
  return __qin_java_regex_matcher_match_at_region_start(matcher, true);
});
export const __qin_java_regex_matcher_find__ = globalThis.__qin_java_regex_matcher_find__ || ((matcher, start) => {
  const from = start == null ? matcher.__searchIndex : Math.max(matcher.__regionStart, start | 0);
  const boundedFrom = Math.min(Math.max(from, matcher.__regionStart), matcher.__regionEnd);
  const re = matcher.__pattern;
  re.lastIndex = boundedFrom;
  const text = matcher.__input.slice(boundedFrom, matcher.__regionEnd);
  const match = __qin_java_regex_pattern_exec__(re, text);
  if (match == null || match.index != null && match.index !== 0) {
    matcher.__lastMatchGroups = null;
    matcher.__searchIndex = matcher.__regionEnd;
    return false;
  }
  __qin_java_regex_matcher_store_match(matcher, match, boundedFrom + String(match[0] == null ? "" : match[0]).length === 0 ? boundedFrom : boundedFrom + match.index);
  const lastStart = matcher.__lastMatchStart;
  const lastEnd = matcher.__lastMatchEnd;
  matcher.__searchIndex = lastEnd === lastStart ? Math.min(lastEnd + 1, matcher.__regionEnd) : lastEnd;
  return true;
});
export const __qin_java_regex_matcher_group__ = globalThis.__qin_java_regex_matcher_group__ || ((matcher, index) => {
  const groups = __qin_java_regex_matcher_require_last_match(matcher);
  const value = groups[index | 0];
  return value == null ? null : value;
});
export const __qin_java_regex_matcher_group_count__ = globalThis.__qin_java_regex_matcher_group_count__ || ((matcher) => {
  return matcher.__lastMatchGroups == null ? 0 : Math.max(0, matcher.__lastMatchGroups.length - 1);
});
export const __qin_java_regex_matcher_start__ = globalThis.__qin_java_regex_matcher_start__ || ((matcher) => {
  __qin_java_regex_matcher_require_last_match(matcher);
  return matcher.__lastMatchStart;
});
export const __qin_java_regex_matcher_end__ = globalThis.__qin_java_regex_matcher_end__ || ((matcher) => {
  __qin_java_regex_matcher_require_last_match(matcher);
  return matcher.__lastMatchEnd;
});
export const __qin_java_regex_matcher_replace_all__ = globalThis.__qin_java_regex_matcher_replace_all__ || ((matcher, replacement) => {
  return matcher.__input.replace(matcher.__pattern, String(replacement));
});
export const __qin_java_regex_matcher_append_replacement__ = globalThis.__qin_java_regex_matcher_append_replacement__ || ((matcher, buffer, replacement) => {
  __qin_java_regex_matcher_require_last_match(matcher);
  const text = matcher.__input.slice(matcher.__appendPosition, matcher.__lastMatchStart) + String(replacement);
  if (buffer != null) {
    buffer.append(text);
  }
  matcher.__appendPosition = matcher.__lastMatchEnd;
  return matcher;
});
export const __qin_java_regex_matcher_append_tail__ = globalThis.__qin_java_regex_matcher_append_tail__ || ((matcher, buffer) => {
  if (buffer != null) {
    buffer.append(matcher.__input.slice(matcher.__appendPosition));
  }
  matcher.__appendPosition = matcher.__input.length;
  return buffer;
});
const arrayPrototype = Array.prototype as any;
if (arrayPrototype != null && typeof arrayPrototype.clone !== "function") {
  arrayPrototype.clone = function clone(this: unknown[]) {
    return this.slice();
  };
}
function __qin_collection_values_equal__(left, right) {
  if (left === right) return true;
  if (left == null || right == null) return false;
  const equals = (typeof left === "object" || typeof left === "function") && typeof left.equals === "function"
    ? left.equals.bind(left)
    : null;
  return equals == null ? false : equals(right) === true;
}
export function __qin_collection_size__(collection) {
  if (collection == null) return 0;
  if (Array.isArray(collection)) return collection.length;
  if (typeof collection.size === "function") return collection.size();
  if (typeof collection.size === "number") return collection.size;
  if (typeof collection.length === "number") return collection.length;
  throw new Error("Unsupported generated collection size target");
}
export function __qin_collection_is_empty__(collection) {
  if (collection == null) return true;
  if (Array.isArray(collection)) return collection.length === 0;
  if (typeof collection.isEmpty === "function") return collection.isEmpty();
  if (typeof collection.size === "function") return collection.size() === 0;
  if (typeof collection.size === "number") return collection.size === 0;
  if (typeof collection.length === "number") return collection.length === 0;
  throw new Error("Unsupported generated collection empty target");
}
export function __qin_collection_get__(collection, index) {
  if (collection == null) throw new Error("Generated collection get target cannot be null");
  if (Array.isArray(collection)) return collection[Number(index)];
  if (typeof collection.get === "function") return collection.get(index);
  return collection[Number(index)];
}
export function __qin_collection_add__(collection, value) {
  if (collection == null) throw new Error("Generated collection add target cannot be null");
  if (Array.isArray(collection)) {
    collection.push(value);
    return true;
  }
  if (typeof collection.add === "function") return collection.add(value);
  if (typeof collection.push === "function") {
    collection.push(value);
    return true;
  }
  throw new Error("Unsupported generated collection add target");
}
export function __qin_array_append__(collection, value) {
  const source = __qin_collection_to_array__(collection);
  const result = source.slice();
  result.push(value);
  return result;
}
export function __qin_array_prepend__(collection, value) {
  const source = __qin_collection_to_array__(collection);
  const result = [value];
  for (const item of source) result.push(item);
  return result;
}
export function __qin_array_remove_at__(collection, index) {
  const source = __qin_collection_to_array__(collection);
  const numericIndex = Number(index);
  const result = [];
  for (let sourceIndex = 0; sourceIndex < source.length; sourceIndex++) {
    if (sourceIndex !== numericIndex) result.push(source[sourceIndex]);
  }
  return result;
}
export function __qin_array_slice__(collection, start, end) {
  const source = __qin_collection_to_array__(collection);
  const numericStart = Math.max(0, Number(start));
  const numericEnd = Math.min(source.length, Number(end));
  const result = [];
  for (let sourceIndex = numericStart; sourceIndex < numericEnd; sourceIndex++) {
    result.push(source[sourceIndex]);
  }
  return result;
}
export function __qin_collection_contains__(collection, value) {
  if (collection == null) return false;
  if (Array.isArray(collection)) return collection.some((item) => __qin_collection_values_equal__(item, value));
  if (typeof collection.contains === "function") return collection.contains(value);
  if (typeof collection.has === "function") return collection.has(value);
  return false;
}
export function __qin_collection_to_array__(collection) {
  if (collection == null) return [];
  if (Array.isArray(collection)) return collection.slice();
  if (typeof collection.toArray === "function") return collection.toArray();
  if (typeof collection[Symbol.iterator] === "function") return Array.from(collection);
  throw new Error("Unsupported generated collection toArray target");
}
export function __qin_java_utf8_decode__(bytes) {
  const encoded = __qin_collection_to_array__(bytes);
  if (typeof TextDecoder !== "undefined") return new TextDecoder().decode(Uint8Array.from(encoded));
  let binary = "";
  for (let index = 0; index < encoded.length; index++) binary += String.fromCharCode(Number(encoded[index]) & 0xff);
  return decodeURIComponent(escape(binary));
}
export function __qin_java_time_now__() {
  const fixed = globalThis.__qinJavaFixedNow;
  return fixed == null ? new Date() : new Date(fixed);
}
export function __qin_java_time_from__(value) {
  return value instanceof Date ? value : new Date(value == null ? Date.now() : value);
}
export function __qin_java_time_format__(value, pattern) {
  const date = __qin_java_time_from__(value);
  const pad = (input, width) => {
    let text = String(input);
    while (text.length < width) text = "0" + text;
    return text;
  };
  let out = String(pattern == null || pattern.length === 0 ? "yyyy-MM-ddTHH:mm:ss" : pattern);
  out = out.split("yyyy").join(String(date.getFullYear()));
  out = out.split("MM").join(pad(date.getMonth() + 1, 2));
  out = out.split("dd").join(pad(date.getDate(), 2));
  out = out.split("HH").join(pad(date.getHours(), 2));
  out = out.split("mm").join(pad(date.getMinutes(), 2));
  out = out.split("ss").join(pad(date.getSeconds(), 2));
  return out;
}
export const __QinJavaLangString = {
  __hashCode(value) {
    const text = String(value);
    let hash = 0;
    for (let index = 0; index < text.length; index++) {
      hash = ((hash * 31) + text.charCodeAt(index)) | 0;
    }
    return hash;
  },
  __objectMethod(value, methodName) {
    if (value == null) {
      throw new Error("NullPointerException: " + methodName + "()");
    }
    const method = value[methodName];
    if ((typeof value === "object" || typeof value === "function") && typeof method === "function") {
      return method.bind(value);
    }
    return null;
  },
  length(value) {
    if (value == null) {
      throw new Error("NullPointerException: length()");
    }
    if (typeof value.length === "function") {
      return value.length();
    }
    if (typeof value.length === "number") {
      return value.length;
    }
    return String(value).length;
  },
  equals(left, right) {
    const method = this.__objectMethod(left, "equals");
    if (method != null) {
      return method(right) === true;
    }
    return String(left) === String(right);
  },
  contains(value, part) {
    const method = this.__objectMethod(value, "contains");
    if (method != null) {
      return method(part) === true;
    }
    return String(value).includes(String(part));
  },
  isEmpty(value) {
    const method = this.__objectMethod(value, "isEmpty");
    if (method != null) {
      return method() === true;
    }
    return String(value).length === 0;
  },
  isBlank(value) {
    const method = this.__objectMethod(value, "isBlank");
    if (method != null) {
      return method() === true;
    }
    return String(value).trim().length === 0;
  },
  hashCode(value) {
    const method = this.__objectMethod(value, "hashCode");
    if (method != null) {
      return method();
    }
    return this.__hashCode(value);
  },
  regionMatches(value, ...args) {
    const ignoreCase = args.length === 5 ? args[0] === true : false;
    const offsetBase = args.length === 5 ? 1 : 0;
    const toffset = Number(args[offsetBase]);
    const other = String(args[offsetBase + 1]);
    const ooffset = Number(args[offsetBase + 2]);
    const length = Number(args[offsetBase + 3]);
    const text = String(value);
    if (args.length !== 4 && args.length !== 5) {
      return false;
    }
    if (toffset < 0 || ooffset < 0 || length < 0) {
      return false;
    }
    if (toffset + length > text.length || ooffset + length > other.length) {
      return false;
    }
    let left = text.slice(toffset, toffset + length);
    let right = other.slice(ooffset, ooffset + length);
    if (ignoreCase) {
      left = left.toLowerCase();
      right = right.toLowerCase();
    }
    return left === right;
  },
  startsWith(value, prefix) {
    return String(value).startsWith(String(prefix));
  },
  endsWith(value, suffix) {
    return String(value).endsWith(String(suffix));
  },
  charAt(value, index) {
    return String(value).charAt(Number(index));
  },
  substring(value, start, end) {
    return String(value).substring(Number(start), end == null ? undefined : Number(end));
  },
  getBytes(value, _charset) {
    const text = String(value);
    if (typeof TextEncoder !== "undefined") {
      return Array.from(new TextEncoder().encode(text));
    }
    const bufferCtor = globalThis.Buffer;
    if (typeof bufferCtor !== "undefined" && typeof bufferCtor.from === "function") {
      return Array.from(bufferCtor.from(text, "utf8"));
    }
    return Array.from(text).map((ch) => ch.charCodeAt(0) & 0xff);
  },
  join(delimiter, elements) {
    const separator = String(delimiter);
    const values = elements == null
      ? []
      : (typeof elements[Symbol.iterator] === "function"
        ? Array.from(elements)
        : Array.from(elements.__items || elements));
    return values.map((value) => String(value)).join(separator);
  },
  format(formatText, ...values) {
    let valueIndex = 0;
    return ("" + formatText).replace(/%([csd])/g, (_match, kind) => {
      const value = values[valueIndex++];
      if (kind === "c") {
        if (typeof value === "number") {
          return String.fromCharCode(value);
        }
        return ("" + value).charAt(0);
      }
      if (kind === "d") {
        return "" + Math.trunc(value - 0);
      }
      return "" + value;
    });
  }
};
export function __qin_java_functional(fn) {
  if (fn == null || fn.__qinJavaFunctional) return fn;
  const functional = (...args) => fn(...args);
  functional.__qinJavaFunctional = true;
  functional.get = () => fn();
  functional.run = () => fn();
  functional.execute = () => fn();
  functional.apply = (...args) => fn(...args);
  functional.accept = (...args) => {
    fn(...args);
    return null;
  };
  functional.test = (...args) => !!fn(...args);
  functional.compare = (...args) => fn(...args);
  __qin_copy_subhuti_rule_metadata(functional, fn);
  if (fn.__originalFunction__ && fn.__originalFunction__ !== fn) {
    __qin_copy_subhuti_rule_metadata(functional, fn.__originalFunction__);
  }
  if (functional.method == null) {
    const nestedRuleName = fn.__qinSubhutiRuleName || fn.name || "";
    if (nestedRuleName) {
      const nestedMethod = (...args) => fn(...args);
      __qin_copy_subhuti_rule_metadata(nestedMethod, fn, nestedRuleName);
      nestedMethod.method = functional;
      functional.method = nestedMethod;
      if (functional.value == null) {
        functional.value = nestedMethod;
      }
    }
  }
  return functional;
}

function __qin_clone_subhuti_rule(method, ruleName = "") {
  if (method == null || typeof method !== "function") return method;
  const cloned = (...args) => method(...args);
  __qin_define_rule_metadata(cloned, "__isSubhutiRule__", true);
  __qin_define_rule_metadata(cloned, "__qinSubhutiRuleWrapped", true);
  if (ruleName) {
    __qin_define_rule_metadata(cloned, "__qinSubhutiRuleName", ruleName);
    try {
      Object.defineProperty(cloned, "name", {
        value: ruleName,
        writable: false,
        enumerable: false,
        configurable: true
      });
    } catch (error) {
    }
  }
  return cloned;
}

function __qin_subhuti_rule_name(source, fallbackRuleName = "") {
  if (source == null) return String(fallbackRuleName || "");
  const directRuleName = source.__qinSubhutiRuleName || source.name || "";
  if (directRuleName) return String(directRuleName);
  const nestedMethod = typeof source.method === "function" ? source.method : null;
  if (nestedMethod) {
    const nestedMethodRuleName = nestedMethod.__qinSubhutiRuleName || nestedMethod.name || "";
    if (nestedMethodRuleName) return String(nestedMethodRuleName);
  }
  const nestedValue = typeof source.value === "function" ? source.value : null;
  if (nestedValue) {
    const nestedValueRuleName = nestedValue.__qinSubhutiRuleName || nestedValue.name || "";
    if (nestedValueRuleName) return String(nestedValueRuleName);
  }
  return String(fallbackRuleName || "");
}

function __qin_copy_subhuti_rule_metadata(target, source, fallbackRuleName = "") {
  if (target == null || source == null) return target;
  const ruleName = __qin_subhuti_rule_name(source, fallbackRuleName);
  if (!(source.__isSubhutiRule__ === true || source.__qinSubhutiRuleWrapped === true || ruleName || typeof source.method === "function" || typeof source.value === "function")) {
    return target;
  }
  target.__isSubhutiRule__ = true;
  target.__qinSubhutiRuleWrapped = true;
  if (ruleName) {
    target.__qinSubhutiRuleName = ruleName;
    try {
      Object.defineProperty(target, "name", {
        value: ruleName,
        writable: false,
        enumerable: false,
        configurable: true
      });
    } catch (error) {
    }
  }
  if (typeof source.method === "function") {
    const nestedSource = source.method;
    const nestedRuleName = nestedSource.__qinSubhutiRuleName || nestedSource.name || ruleName;
    const nestedTarget = __qin_clone_subhuti_rule(nestedSource, nestedRuleName);
    nestedTarget.method = target;
    target.method = nestedTarget;
  }
  if (typeof source.value === "function") {
    const nestedValueSource = source.value;
    const nestedValueRuleName = nestedValueSource.__qinSubhutiRuleName || nestedValueSource.name || ruleName;
    const nestedValueTarget = nestedValueSource === source || nestedValueSource.method === nestedValueSource
      ? (...args) => nestedValueSource(...args)
      : nestedValueSource;
    __qin_copy_subhuti_rule_metadata(nestedValueTarget, nestedValueSource, nestedValueRuleName);
    target.value = nestedValueTarget;
  }
  return target;
}
export class __QinJavaLangReflectMethod {
  constructor(candidate = null) {
    this.__candidate = candidate;
  }
  setAccessible(_flag = true) {}
  invoke(target, ...args) {
    if (typeof this.__candidate !== "function") {
      throw new Error("NoSuchMethod");
    }
    return this.__candidate(target, ...args);
  }
}
export function __qin_java_class_info__(ctor, meta = null) {
  if (ctor != null && typeof ctor.isInstance === "function" && typeof ctor.getName === "function") {
    return ctor;
  }
  const className = meta && meta.name ? meta.name : (ctor && ctor.name ? ctor.name : "Object");
  const simpleName = className.split(".").pop().split("_").pop() || className;
  let hash = 0;
  for (let index = 0; index < className.length; index++) {
    hash = ((hash * 31) + className.charCodeAt(index)) | 0;
  }
  const findMethod = (name) => {
    const candidates = name === "_markParseFail" ? ["_markParseFail", "setParseFail"] : [name];
    let prototype = ctor == null ? null : ctor.prototype;
    while (prototype != null) {
      for (const candidate of candidates) {
        if (typeof prototype[candidate] === "function") {
          return new __QinJavaLangReflectMethod((target, ...args) => target[candidate](...args));
        }
      }
      prototype = Object.getPrototypeOf(prototype);
    }
    throw new Error("NoSuchMethod: " + className + "." + name);
  };
  return {
    __qin_ctor: ctor,
    __qin_meta: meta,
    getName() { return className; },
    getSimpleName() { return simpleName; },
    isInstance(value) {
      if (value == null) return false;
      if (meta && meta.interfaceName) return __qin_java_implements(value, meta.interfaceName);
      if (ctor == null || ctor === Object) return typeof value === "object" || typeof value === "function";
      if (typeof ctor === "function" && value instanceof ctor) return true;
      const targetRecord = ctor.__qinJavaRecordClass;
      if (targetRecord != null && value.__qinJavaRecordClass === targetRecord) return true;
      const interfaces = ctor.__qin_java_interfaces || [];
      for (const interfaceName of interfaces) {
        if (__qin_java_implements(value, interfaceName)) return true;
      }
      return false;
    },
    isAssignableFrom(other) {
      if (other == null || typeof other.getName !== "function") return false;
      if (className === "java.lang.Object" || className === other.getName()) return true;
      const otherMeta = other.__qin_meta;
      if (meta && meta.interfaceName) {
        if (otherMeta && otherMeta.interfaceName === meta.interfaceName) return true;
        const otherCtor = other.__qin_ctor;
        const interfaces = otherCtor == null ? [] : (otherCtor.__qin_java_interfaces || []);
        return interfaces.includes(meta.interfaceName);
      }
      if (typeof ctor !== "function" || typeof other.__qin_ctor !== "function") return false;
      let prototype = other.__qin_ctor.prototype;
      while (prototype != null) {
        if (prototype.constructor === ctor) return true;
        prototype = Object.getPrototypeOf(prototype);
      }
      return false;
    },
    cast(value) {
      if (value == null || this.isInstance(value)) return value;
      throw new Error("ClassCastException: cannot cast to " + className);
    },
    getDeclaredConstructor(...__qin_types) {
      const __qin_ctor = ctor == null ? Object : ctor;
      return {
        newInstance(...__qin_args) {
          return new __qin_ctor(...__qin_args);
        }
      };
    },
    getConstructor(...__qin_types) {
      return this.getDeclaredConstructor(...__qin_types);
    },
    getMethod(name, ...params) { return findMethod(name); },
    getDeclaredMethod(name, ...params) { return findMethod(name); },
    getSuperclass() {
      const parent = ctor == null || ctor.prototype == null ? null : Object.getPrototypeOf(ctor.prototype);
      return parent != null && parent.constructor != null && parent.constructor !== Object
        ? __qin_java_class_info__(parent.constructor)
        : null;
    },
    getField(name) {
      return {
        get(target) {
          const qinField = "__qin_field_" + name;
          if (target != null && qinField in target) return target[qinField];
          if (target != null && name in target && typeof target[name] !== "function") return target[name];
          throw new Error("NoSuchField: " + className + "." + name);
        }
      };
    },
    equals(other) { return other != null && typeof other.getName === "function" && other.getName() === className; },
    hashCode() { return hash; },
    toString() { return "class " + className; }
  };
}
export function __qin_java_implements(value, interfaceName) {
  if (value == null || interfaceName == null) return false;
  let ctor = value.constructor;
  while (ctor != null && ctor !== Object) {
    const interfaces = ctor.__qin_java_interfaces || [];
    if (interfaces.includes(interfaceName)) return true;
    const prototype = ctor.prototype == null ? null : Object.getPrototypeOf(ctor.prototype);
    ctor = prototype == null ? null : prototype.constructor;
  }
  return false;
}
export function __qin_instanceof__(value, ctor) {
  if (ctor == null) {
    throw new TypeError("Right-hand side of 'instanceof' is not callable");
  }
  if (__qin_native_mirror_instanceof__(value, ctor)) {
    return true;
  }
  if (typeof ctor === "string") {
    return __qin_builtin_instanceof__(value, ctor);
  }
  if (typeof ctor.isInstance === "function") {
    return !!ctor.isInstance(value);
  }
  if (typeof ctor.getName === "function" || typeof ctor.getSimpleName === "function") {
    return false;
  }
  if (typeof ctor[Symbol.hasInstance] === "function") {
    return !!ctor[Symbol.hasInstance](value);
  }
  if (typeof ctor !== "function") {
    throw new TypeError("Right-hand side of 'instanceof' is not callable");
  }
  return value instanceof ctor;
}
function __qin_builtin_instanceof__(value, ctorName) {
  switch (ctorName) {
    case "Object":
      return value !== null && (typeof value === "object" || typeof value === "function");
    case "Array":
      return Array.isArray(value);
    case "Map":
      return value instanceof Map;
    case "Set":
      return value instanceof Set;
    case "RegExp":
      return value instanceof RegExp;
    case "Date":
      return value instanceof Date;
    case "URLSearchParams":
      return typeof URLSearchParams !== "undefined" && value instanceof URLSearchParams;
    case "Uint8Array":
    case "Uint16Array":
    case "Uint32Array":
      return typeof ArrayBuffer !== "undefined" && ArrayBuffer.isView(value);
    case "Error":
    case "TypeError":
    case "RangeError":
    case "ReferenceError":
    case "SyntaxError":
      return value instanceof Error
        && (ctorName === "Error" || (value.name === ctorName || value.constructor?.name === ctorName));
    default:
      return false;
  }
}
function __qin_native_mirror_instanceof__(value, ctor) {
  if (value == null || ctor == null) return false;
  const ctorName = typeof ctor.name === "string"
    ? ctor.name
    : (typeof ctor.getName === "function"
      ? ctor.getName()
      : (typeof ctor.getSimpleName === "function" ? ctor.getSimpleName() : ""));
  if (ctorName === "com_subhuti_struct_SubhutiPosition"
      || ctorName === "SubhutiPosition"
      || ctorName.endsWith(".SubhutiPosition")) {
    return (typeof value.getLine === "function" || typeof value.line === "function")
      && (typeof value.getColumn === "function" || typeof value.column === "function")
      && (typeof value.getIndex === "function" || typeof value.index === "function");
  }
  return false;
}
const objectPrototype = Object.prototype as any;
if (objectPrototype != null && objectPrototype.getClass == null) {
  objectPrototype.getClass = function getClass() {
    return __qin_java_class_info__(this == null ? Object : this.constructor);
  };
}
export class __QinJavaMathBigInteger {
  constructor(value, radix = 10) {
    this.__value = BigInt(Number.parseInt(String(value), Number(radix)));
  }
  doubleValue() {
    return Number(this.__value);
  }
}
export function __qin_binary__(operator, left, right) {
  const hostBinary = globalThis.__qin_binary__;
  if (typeof hostBinary === "function" && hostBinary !== __qin_binary__) {
    return hostBinary(operator, left, right);
  }
  switch (operator) {
    case "+": return left + right;
    case "-": return left - right;
    case "*": return left * right;
    case "/": return left / right;
    case "%": return left % right;
    case "==": return __qin_generated_java_enum_equal__(left, right) || left == right;
    case "!=": return !__qin_generated_java_enum_equal__(left, right) && left != right;
    case "===": return __qin_generated_java_enum_equal__(left, right) || left === right;
    case "!==": return !__qin_generated_java_enum_equal__(left, right) && left !== right;
    case "<": return left < right;
    case "<=": return left <= right;
    case ">": return left > right;
    case ">=": return left >= right;
    case "instanceof": return __qin_instanceof__(left, right);
    case "&&": return left && right;
    case "||": return left || right;
    default: throw new Error("Unsupported Qin binary operator: " + operator);
  }
}
function __qin_generated_java_enum_equal__(left, right) {
  if (left === right) return true;
  if (left == null || right == null) return false;
  const leftName = left.__qinEnumName;
  const rightName = right.__qinEnumName;
  const leftOrdinal = left.__qinEnumOrdinal;
  const rightOrdinal = right.__qinEnumOrdinal;
  if (leftName == null || rightName == null || leftOrdinal == null || rightOrdinal == null) {
    return false;
  }
  if (String(leftName) !== String(rightName) || Number(leftOrdinal) !== Number(rightOrdinal)) {
    return false;
  }
  return true;
}
function __qin_generated_java_enum_class_name__(value) {
  if (value == null) return null;
  if (typeof value.getClass === "function") {
    const runtimeClass = value.getClass();
    if (runtimeClass != null && typeof runtimeClass.getName === "function") {
      return String(runtimeClass.getName());
    }
    if (runtimeClass != null && typeof runtimeClass.getSimpleName === "function") {
      return String(runtimeClass.getSimpleName());
    }
  }
  return value.constructor == null ? null : String(value.constructor.name || value.constructor);
}
export function __qin_logical__(operator, left, right) {
  switch (operator) {
    case "&&": return left && right;
    case "||": return left || right;
    default: throw new Error("Unsupported Qin logical operator: " + operator);
  }
}
