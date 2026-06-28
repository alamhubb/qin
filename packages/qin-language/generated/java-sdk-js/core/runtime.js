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
  Object.defineProperty(fn, "__qinJavaFunctional", { value: true });
  fn.get = () => fn();
  fn.run = () => fn();
  fn.execute = () => fn();
  fn.apply = (...args) => fn(...args);
  fn.accept = (...args) => {
    fn(...args);
    return null;
  };
  fn.test = (...args) => !!fn(...args);
  fn.compare = (...args) => fn(...args);
  return fn;
}
export function __qin_java_class_info__(ctor) {
  const className = ctor && ctor.name ? ctor.name : "Object";
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
    case "instanceof": return left instanceof right;
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
