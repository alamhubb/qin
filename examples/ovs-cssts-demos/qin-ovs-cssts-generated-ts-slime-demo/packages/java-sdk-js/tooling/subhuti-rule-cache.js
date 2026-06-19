import { __qin_builtin_constructor__ } from "../core/runtime.js";
import { __qin_java_hash_key__, __qin_java_hash_key_equals__ } from "../util/hash.js";

export let __qin_subhuti_next_rule_cache_id = 1;
export const __qin_subhuti_rule_cache_identity_ids = new (__qin_builtin_constructor__("WeakMap"))();
export const __qin_subhuti_rule_cache_value_buckets = new (__qin_builtin_constructor__("Map"))();
export function __qin_subhuti_identity_rule_cache_id(value) {
  if (!__qin_subhuti_rule_cache_identity_ids.has(value)) {
    __qin_subhuti_rule_cache_identity_ids.set(value, __qin_subhuti_next_rule_cache_id++);
  }
  return __qin_subhuti_rule_cache_identity_ids.get(value);
}
export function __qin_subhuti_value_rule_cache_id(value) {
  const hash = __qin_java_hash_key__(value);
  let bucket = __qin_subhuti_rule_cache_value_buckets.get(hash);
  if (bucket == null) {
    bucket = [];
    __qin_subhuti_rule_cache_value_buckets.set(hash, bucket);
  }
  for (const entry of bucket) {
    if (__qin_java_hash_key_equals__(entry.value, value)) {
      return entry.id;
    }
  }
  const id = __qin_subhuti_next_rule_cache_id++;
  bucket.push({ value, id });
  return id;
}
export function __qin_subhuti_rule_cache_key(args) {
  if (args == null || args.length === 0) return "";
  const format = (value) => {
    if (value == null) return "null";
    const type = typeof value;
    if (type === "string") return value;
    if (type === "number" || type === "boolean" || type === "bigint") return "" + value;
    if (Array.isArray(value)) return "[" + value.map(format).join(", ") + "]";
    if (type === "object" || type === "function") {
      if (typeof value.hashCode === "function" || typeof value.equals === "function") {
        return type + "#value:" + __qin_subhuti_value_rule_cache_id(value);
      }
      return type + "#identity:" + __qin_subhuti_identity_rule_cache_id(value);
    }
    return "" + value;
  };
  const parts = [];
  for (let i = 0; i < args.length; i++) {
    parts.push(format(args[i]));
  }
  return "[" + parts.join(", ") + "]";
}
