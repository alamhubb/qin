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
  Object.defineProperty(functional, "__qinJavaFunctional", { value: true });
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
  return functional;
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
          return {
            setAccessible() {},
            invoke(target, ...args) {
              return target[candidate](...args);
            }
          };
        }
      }
      prototype = Object.getPrototypeOf(prototype);
    }
    throw new Error("NoSuchMethod: " + className + "." + name);
  };
  return {
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
    case "WeakMap":
      return value instanceof Map || value instanceof WeakMap;
    case "Set":
    case "WeakSet":
      return value instanceof Set || value instanceof WeakSet;
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
if (Object.prototype.getClass == null) {
  Object.defineProperty(Object.prototype, "getClass", {
    value() { return __qin_java_class_info__(this == null ? Object : this.constructor); },
    configurable: true
  });
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
  switch (operator) {
    case "+": return left + right;
    case "-": return left - right;
    case "*": return left * right;
    case "/": return left / right;
    case "%": return left % right;
    case "==": return left == right;
    case "!=": return left != right;
    case "===": return left === right;
    case "!==": return left !== right;
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
export function __qin_logical__(operator, left, right) {
  switch (operator) {
    case "&&": return left && right;
    case "||": return left || right;
    default: throw new Error("Unsupported Qin logical operator: " + operator);
  }
}
