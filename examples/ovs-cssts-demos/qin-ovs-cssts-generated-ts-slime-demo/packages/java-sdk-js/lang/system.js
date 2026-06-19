import { __qin_builtin_constructor__ } from "../core/runtime.js";

export const __QinJavaLangSystem = (() => {
  let nextIdentityHashCode = 1;
  const __QinJsMap = __qin_builtin_constructor__("Map");
  const objectIdentityHashCodes = new __QinJsMap();
  const primitiveIdentityHashCodes = new __QinJsMap();
  function identityHashCode(value) {
    if ((typeof value === "object" && value !== null) || typeof value === "function") {
      if (!objectIdentityHashCodes.has(value)) {
        objectIdentityHashCodes.set(value, nextIdentityHashCode++);
      }
      return objectIdentityHashCodes.get(value);
    }
    const key = typeof value + ":" + String(value);
    if (!primitiveIdentityHashCodes.has(key)) {
      primitiveIdentityHashCodes.set(key, nextIdentityHashCode++);
    }
    return primitiveIdentityHashCodes.get(key);
  }
  function property(name) {
    const key = String(name);
    if (key === "user.dir") {
      return globalThis.__qinJavaUserDir == null ? "" : String(globalThis.__qinJavaUserDir);
    }
    if (key === "java.version") {
      return globalThis.__qinJavaVersion == null ? "25" : String(globalThis.__qinJavaVersion);
    }
    return null;
  }
  function printTo(methodName, value) {
    const target = typeof console === "undefined" ? null : console;
    if (target != null && typeof target[methodName] === "function") {
      target[methodName](value == null ? "null" : value);
    }
  }
  return {
    out: { println(value) { printTo("log", value); } },
    err: { println(value) { printTo("error", value); } },
    currentTimeMillis() { return Date.now(); },
    nanoTime() {
      const now = typeof performance !== "undefined" && performance != null
        && typeof performance.now === "function"
        ? performance.now()
        : Date.now();
      return Math.floor(now * 1000000);
    },
    getProperty: property,
    identityHashCode
  };
})();
