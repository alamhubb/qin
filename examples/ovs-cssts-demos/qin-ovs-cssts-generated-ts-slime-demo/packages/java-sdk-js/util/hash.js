import { __qin_builtin_constructor__ } from "../core/runtime.js";

export const __qin_java_hash_identity_ids__ = new (__qin_builtin_constructor__("WeakMap"))();
export let __qin_java_hash_identity_next__ = 1;
export function __qin_java_string_hash_code__(value) {
  let hash = 0;
  for (let index = 0; index < value.length; index++) {
    hash = hash * 31 + value.charCodeAt(index);
  }
  return hash;
}
export function __qin_java_identity_hash_code__(value) {
  if (!__qin_java_hash_identity_ids__.has(value)) {
    __qin_java_hash_identity_ids__.set(value, __qin_java_hash_identity_next__++);
  }
  return __qin_java_hash_identity_ids__.get(value);
}
export function __qin_java_value_hash_code__(value) {
  if (value == null) {
    return 0;
  }
  const valueType = typeof value;
  if (valueType === "string") {
    return __qin_java_string_hash_code__(value);
  }
  if (valueType === "boolean") {
    return value ? 1231 : 1237;
  }
  if (valueType === "number") {
    return value;
  }
  if (valueType === "object" || valueType === "function") {
    if (typeof value.hashCode === "function") {
      return value.hashCode();
    }
    return __qin_java_identity_hash_code__(value);
  }
  return __qin_java_string_hash_code__(String(value));
}
export function __qin_java_values_equal__(left, right) {
  if (left === right || (left !== left && right !== right)) {
    return true;
  }
  if (left == null || right == null) {
    return false;
  }
  if ((typeof left === "object" || typeof left === "function")
      && typeof left.equals === "function") {
    return left.equals(right) === true;
  }
  return false;
}
export function __qin_java_hash_key__(key) {
  return "hash:" + String(__qin_java_value_hash_code__(key));
}
export function __qin_java_hash_key_equals__(left, right) {
  return __qin_java_values_equal__(left, right);
}
