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
export function __qin_java_number_hash_code__(value) {
  return value == null ? 0 : value;
}
export function __qin_java_structural_hash_code__(value) {
  if (value == null || typeof value !== "object") {
    return null;
  }
  if (Object.prototype.hasOwnProperty.call(value, "__qin_field_ruleName")
      && Object.prototype.hasOwnProperty.call(value, "__qin_field_cacheKeyExtra")
      && Object.prototype.hasOwnProperty.call(value, "__qin_field_tokenIndex")
      && Object.prototype.hasOwnProperty.call(value, "__qin_field_mode")
      && Object.prototype.hasOwnProperty.call(value, "__qin_field_lastTokenName")
      && Object.prototype.hasOwnProperty.call(value, "__qin_field_hashCode")) {
    let result = 1;
    result = result * 31 + __qin_java_value_hash_code__(value.__qin_field_ruleName);
    result = result * 31 + __qin_java_value_hash_code__(value.__qin_field_cacheKeyExtra);
    result = result * 31 + __qin_java_number_hash_code__(value.__qin_field_tokenIndex);
    result = result * 31 + __qin_java_value_hash_code__(value.__qin_field_mode);
    result = result * 31 + __qin_java_value_hash_code__(value.__qin_field_lastTokenName);
    return result;
  }
  if (Object.prototype.hasOwnProperty.call(value, "__qin_field_name")
      && typeof value.isDefault === "function"
      && typeof value.equals === "function"
      && typeof value.hashCode === "function") {
    let result = 1;
    result = result * 31 + __qin_java_value_hash_code__(value.__qin_field_name);
    return result;
  }
  return null;
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
    const structuralHash = __qin_java_structural_hash_code__(value);
    if (structuralHash != null) {
      return structuralHash;
    }
    if (typeof value.hashCode === "function") {
      return value.hashCode();
    }
    return __qin_java_identity_hash_code__(value);
  }
  return __qin_java_string_hash_code__(String(value));
}
export function __qin_java_structural_values_equal__(left, right) {
  if (left == null || right == null || typeof left !== "object" || typeof right !== "object") {
    return null;
  }
  if (Object.prototype.hasOwnProperty.call(left, "__qin_field_ruleName")
      && Object.prototype.hasOwnProperty.call(right, "__qin_field_ruleName")
      && Object.prototype.hasOwnProperty.call(left, "__qin_field_cacheKeyExtra")
      && Object.prototype.hasOwnProperty.call(right, "__qin_field_cacheKeyExtra")
      && Object.prototype.hasOwnProperty.call(left, "__qin_field_tokenIndex")
      && Object.prototype.hasOwnProperty.call(right, "__qin_field_tokenIndex")
      && Object.prototype.hasOwnProperty.call(left, "__qin_field_mode")
      && Object.prototype.hasOwnProperty.call(right, "__qin_field_mode")
      && Object.prototype.hasOwnProperty.call(left, "__qin_field_lastTokenName")
      && Object.prototype.hasOwnProperty.call(right, "__qin_field_lastTokenName")
      && Object.prototype.hasOwnProperty.call(left, "__qin_field_hashCode")
      && Object.prototype.hasOwnProperty.call(right, "__qin_field_hashCode")) {
    return left.__qin_field_tokenIndex === right.__qin_field_tokenIndex
      && __qin_java_values_equal__(left.__qin_field_ruleName, right.__qin_field_ruleName)
      && __qin_java_values_equal__(left.__qin_field_cacheKeyExtra, right.__qin_field_cacheKeyExtra)
      && left.__qin_field_mode === right.__qin_field_mode
      && __qin_java_values_equal__(left.__qin_field_lastTokenName, right.__qin_field_lastTokenName);
  }
  if (Object.prototype.hasOwnProperty.call(left, "__qin_field_name")
      && Object.prototype.hasOwnProperty.call(right, "__qin_field_name")
      && typeof left.isDefault === "function"
      && typeof right.isDefault === "function"
      && typeof left.equals === "function"
      && typeof right.equals === "function"
      && typeof left.hashCode === "function"
      && typeof right.hashCode === "function") {
    return __qin_java_values_equal__(left.__qin_field_name, right.__qin_field_name);
  }
  return null;
}
export function __qin_java_values_equal__(left, right) {
  if (left === right || (left !== left && right !== right)) {
    return true;
  }
  if (left == null || right == null) {
    return false;
  }
  const structuralEqual = __qin_java_structural_values_equal__(left, right);
  if (structuralEqual != null) {
    return structuralEqual === true;
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
