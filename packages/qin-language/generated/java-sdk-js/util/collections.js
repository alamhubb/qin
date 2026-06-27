import { __qin_builtin_constructor__ } from "../core/runtime.js";
import {
  __qin_java_hash_key__,
  __qin_java_hash_key_equals__,
  __qin_java_value_hash_code__,
  __qin_java_values_equal__
} from "./hash.js";
import { __QinJavaUtilStream } from "./stream.js";

export class __QinJavaUtilArrayList {
  constructor(initialValues) {
    this.__items = [];
    if (initialValues != null) {
      const values = initialValues instanceof __QinJavaUtilArrayList
        ? initialValues.__items
        : initialValues;
      for (const item of values) {
        this.__items.push(item);
      }
    }
  }
  add(value) {
    this.__items.push(value);
    return true;
  }
  addAll(values) {
    const source = values instanceof __QinJavaUtilArrayList
      ? values.__items
      : Array.from(values);
    for (const item of source) {
      this.__items.push(item);
    }
    return source.length > 0;
  }
  get(index) {
    return this.__items[index];
  }
  set(index, value) {
    const previous = this.__items[index];
    this.__items[index] = value;
    return previous;
  }
  remove(index) {
    return this.__items.splice(index, 1)[0];
  }
  size() {
    return this.__items.length;
  }
  indexOf(value) {
    for (let index = 0; index < this.__items.length; index++) {
      if (__qin_java_hash_key_equals__(this.__items[index], value)) return index;
    }
    return -1;
  }
  isEmpty() {
    return this.__items.length === 0;
  }
  clear() {
    this.__items.length = 0;
  }
  sort(comparator) {
    this.__items.sort(comparator);
  }
  subList(fromIndex, toIndex) {
    return new __QinJavaUtilUnmodifiableList(this.__items.slice(fromIndex, toIndex));
  }
  toArray() {
    return this.__items.slice();
  }
  stream() {
    return new __QinJavaUtilStream(this.__items);
  }
  [Symbol.iterator]() {
    return this.__items[Symbol.iterator]();
  }
}
export class __QinJavaUtilUnmodifiableList {
  constructor(source) {
    this.__source = source == null ? [] : source;
  }
  __values() {
    return this.__source instanceof __QinJavaUtilArrayList
      ? this.__source.__items
      : Array.from(this.__source);
  }
  add() {
    throw new TypeError("java.util.List is unmodifiable");
  }
  addAll() {
    throw new TypeError("java.util.List is unmodifiable");
  }
  get(index) {
    return this.__values()[index];
  }
  remove() {
    throw new TypeError("java.util.List is unmodifiable");
  }
  set() {
    throw new TypeError("java.util.List is unmodifiable");
  }
  subList(fromIndex, toIndex) {
    return new __QinJavaUtilUnmodifiableList(this.__values().slice(fromIndex, toIndex));
  }
  size() {
    return this.__values().length;
  }
  indexOf(value) {
    const values = this.__values();
    for (let index = 0; index < values.length; index++) {
      if (__qin_java_hash_key_equals__(values[index], value)) return index;
    }
    return -1;
  }
  isEmpty() {
    return this.__values().length === 0;
  }
  clear() {
    throw new TypeError("java.util.List is unmodifiable");
  }
  sort() {
    throw new TypeError("java.util.List is unmodifiable");
  }
  toArray() {
    return this.__values().slice();
  }
  stream() {
    return new __QinJavaUtilStream(this.__values());
  }
  [Symbol.iterator]() {
    return this.__values()[Symbol.iterator]();
  }
}
export const __QinJavaUtilList = {
  of(...values) {
    return new __QinJavaUtilUnmodifiableList(values);
  }
};
export class __QinJavaUtilHashSet {
  constructor(initialValues) {
    const __QinJsMap = __qin_builtin_constructor__("Map");
    this.__buckets = new __QinJsMap();
    this.__size = 0;
    if (initialValues != null) {
      for (const value of initialValues) {
        this.add(value);
      }
    }
  }
  __bucket(value, create) {
    const hash = __qin_java_hash_key__(value);
    let bucket = this.__buckets.get(hash);
    if (bucket == null && create) {
      bucket = [];
      this.__buckets.set(hash, bucket);
    }
    return bucket;
  }
  __findEntry(value) {
    const bucket = this.__bucket(value, false);
    if (bucket == null) {
      return null;
    }
    for (let index = 0; index < bucket.length; index++) {
      if (__qin_java_hash_key_equals__(bucket[index], value)) {
        return { bucket, index, value: bucket[index] };
      }
    }
    return null;
  }
  add(value) {
    if (this.__findEntry(value) != null) {
      return false;
    }
    this.__bucket(value, true).push(value);
    this.__size++;
    return true;
  }
  contains(value) {
    return this.__findEntry(value) != null;
  }
  remove(value) {
    const found = this.__findEntry(value);
    if (found == null) {
      return false;
    }
    found.bucket.splice(found.index, 1);
    this.__size--;
    return true;
  }
  size() {
    return this.__size;
  }
  isEmpty() {
    return this.__size === 0;
  }
  clear() {
    this.__buckets.clear();
    this.__size = 0;
  }
  toArray() {
    const values = [];
    for (const bucket of this.__buckets.values()) {
      for (const value of bucket) {
        values.push(value);
      }
    }
    return values;
  }
  [Symbol.iterator]() {
    return this.toArray()[Symbol.iterator]();
  }
}
export class __QinJavaUtilUnmodifiableSet {
  constructor(source) {
    this.__source = source == null ? [] : source;
  }
  __values() {
    const __QinJsSet = __qin_builtin_constructor__("Set");
    if (this.__source instanceof __QinJsSet) {
      return this.__source;
    }
    return new __QinJsSet(this.__source);
  }
  add() {
    throw new TypeError("java.util.Set is unmodifiable");
  }
  contains(value) {
    if (this.__source instanceof __QinJavaUtilHashSet) {
      return this.__source.contains(value);
    }
    return this.__values().has(value);
  }
  remove() {
    throw new TypeError("java.util.Set is unmodifiable");
  }
  size() {
    if (this.__source instanceof __QinJavaUtilHashSet) {
      return this.__source.size();
    }
    return this.__values().size;
  }
  isEmpty() {
    if (this.__source instanceof __QinJavaUtilHashSet) {
      return this.__source.isEmpty();
    }
    return this.__values().size === 0;
  }
  clear() {
    throw new TypeError("java.util.Set is unmodifiable");
  }
  toArray() {
    if (this.__source instanceof __QinJavaUtilHashSet) {
      return this.__source.toArray();
    }
    return Array.from(this.__values());
  }
  [Symbol.iterator]() {
    if (this.__source instanceof __QinJavaUtilHashSet) {
      return this.__source[Symbol.iterator]();
    }
    return this.__values()[Symbol.iterator]();
  }
}
export const __QinJavaUtilSet = {
  of(...values) {
    return new __QinJavaUtilUnmodifiableSet(values);
  }
};
export const __QinJavaUtilArrays = {
  asList(...values) {
    if (values.length === 1 && Array.isArray(values[0])) {
      return new __QinJavaUtilArrayList(values[0]);
    }
    return new __QinJavaUtilArrayList(values);
  },
  __items(value) {
    if (value == null) {
      return null;
    }
    if (value instanceof __QinJavaUtilArrayList) {
      return value.__items;
    }
    if (Array.isArray(value)) {
      return value;
    }
    if (typeof value[Symbol.iterator] === "function" && typeof value !== "string") {
      return Array.from(value);
    }
    return [value];
  },
  equals(left, right) {
    if (left === right) {
      return true;
    }
    const leftItems = this.__items(left);
    const rightItems = this.__items(right);
    if (leftItems == null || rightItems == null || leftItems.length !== rightItems.length) {
      return false;
    }
    for (let index = 0; index < leftItems.length; index++) {
      if (!__qin_java_values_equal__(leftItems[index], rightItems[index])) {
        return false;
      }
    }
    return true;
  },
  hashCode(value) {
    const items = this.__items(value);
    if (items == null) {
      return 0;
    }
    let result = 1;
    for (const item of items) {
      result = result * 31 + __qin_java_value_hash_code__(item);
    }
    return result;
  },
  deepToString(value) {
    const format = (item, seen) => {
      if (item == null) {
        return "null";
      }
      const arrayListItems = item instanceof __QinJavaUtilArrayList ? item.__items : null;
      if (Array.isArray(item) || arrayListItems != null
          || (item != null && typeof item[Symbol.iterator] === "function"
          && typeof item !== "string")) {
        if (seen.indexOf(item) >= 0) {
          return "[...]";
        }
        seen.push(item);
        const parts = [];
        const iterable = arrayListItems == null ? item : arrayListItems;
        for (const child of iterable) {
          parts.push(format(child, seen));
        }
        seen.pop();
        return "[" + parts.join(", ") + "]";
      }
      return String(item);
    };
    return format(value, []);
  }
};
export class __QinJavaUtilHashMap {
  constructor(initialEntries) {
    const __QinJsMap = __qin_builtin_constructor__("Map");
    this.__buckets = new __QinJsMap();
    this.__size = 0;
    if (initialEntries != null) {
      for (const entry of initialEntries) {
        this.put(entry[0], entry[1]);
      }
    }
  }
  __bucket(key, create) {
    const hash = __qin_java_hash_key__(key);
    let bucket = this.__buckets.get(hash);
    if (bucket == null && create) {
      bucket = [];
      this.__buckets.set(hash, bucket);
    }
    return bucket;
  }
  __findEntry(key) {
    const bucket = this.__bucket(key, false);
    if (bucket == null) {
      return null;
    }
    for (let index = 0; index < bucket.length; index++) {
      const entry = bucket[index];
      if (__qin_java_hash_key_equals__(entry.key, key)) {
        return { bucket, index, entry };
      }
    }
    return null;
  }
  put(key, value) {
    const found = this.__findEntry(key);
    if (found != null) {
      const previous = found.entry.value;
      found.entry.value = value;
      return previous;
    }
    this.__bucket(key, true).push({ key, value });
    this.__size++;
    return null;
  }
  get(key) {
    const found = this.__findEntry(key);
    return found == null ? null : found.entry.value;
  }
  getOrDefault(key, defaultValue) {
    const found = this.__findEntry(key);
    return found == null ? defaultValue : found.entry.value;
  }
  putIfAbsent(key, value) {
    const found = this.__findEntry(key);
    if (found == null) {
      this.__bucket(key, true).push({ key, value });
      this.__size++;
      return null;
    }
    const previous = found.entry.value;
    if (previous == null) {
      found.entry.value = value;
    }
    return previous;
  }
  values() {
    const values = [];
    for (const bucket of this.__buckets.values()) {
      for (const entry of bucket) {
        values.push(entry.value);
      }
    }
    return new __QinJavaUtilArrayList(values);
  }
  computeIfAbsent(key, mappingFunction) {
    const found = this.__findEntry(key);
    if (found == null || found.entry.value == null) {
      const value = mappingFunction(key);
      if (found == null) {
        this.__bucket(key, true).push({ key, value });
        this.__size++;
      } else {
        found.entry.value = value;
      }
      return value;
    }
    return found.entry.value;
  }
  merge(key, value, remappingFunction) {
    const found = this.__findEntry(key);
    if (found == null) {
      this.__bucket(key, true).push({ key, value });
      this.__size++;
      return value;
    }
    if (found.entry.value == null) {
      found.entry.value = value;
      return value;
    }
    const nextValue = remappingFunction(found.entry.value, value);
    if (nextValue == null) {
      found.bucket.splice(found.index, 1);
      this.__size--;
      return null;
    }
    found.entry.value = nextValue;
    return nextValue;
  }
  containsKey(key) {
    return this.__findEntry(key) != null;
  }
  remove(key) {
    const found = this.__findEntry(key);
    if (found == null) {
      return null;
    }
    const previous = found.entry.value;
    found.bucket.splice(found.index, 1);
    this.__size--;
    return previous;
  }
  size() {
    return this.__size;
  }
  isEmpty() {
    return this.__size === 0;
  }
  clear() {
    this.__buckets.clear();
    this.__size = 0;
  }
}
export class __QinJavaUtilUnmodifiableMap {
  constructor(source) {
    const __QinJsMap = __qin_builtin_constructor__("Map");
    this.__source = source == null ? new __QinJsMap() : source;
  }
  __values() {
    const __QinJsMap = __qin_builtin_constructor__("Map");
    if (this.__source instanceof __QinJsMap) {
      return this.__source;
    }
    const entries = new __QinJsMap();
    if (this.__source != null && typeof this.__source[Symbol.iterator] === "function") {
      for (const entry of this.__source) {
        entries.set(entry[0], entry[1]);
      }
    }
    return entries;
  }
  put() {
    throw new TypeError("java.util.Map is unmodifiable");
  }
  get(key) {
    if (this.__source instanceof __QinJavaUtilHashMap) {
      return this.__source.get(key);
    }
    const entries = this.__values();
    return entries.has(key) ? entries.get(key) : null;
  }
  getOrDefault(key, defaultValue) {
    if (this.__source instanceof __QinJavaUtilHashMap) {
      return this.__source.getOrDefault(key, defaultValue);
    }
    const entries = this.__values();
    return entries.has(key) ? entries.get(key) : defaultValue;
  }
  putIfAbsent() {
    throw new TypeError("java.util.Map is unmodifiable");
  }
  values() {
    if (this.__source instanceof __QinJavaUtilHashMap) {
      return new __QinJavaUtilUnmodifiableList(this.__source.values());
    }
    return new __QinJavaUtilUnmodifiableList(Array.from(this.__values().values()));
  }
  computeIfAbsent() {
    throw new TypeError("java.util.Map is unmodifiable");
  }
  merge() {
    throw new TypeError("java.util.Map is unmodifiable");
  }
  containsKey(key) {
    if (this.__source instanceof __QinJavaUtilHashMap) {
      return this.__source.containsKey(key);
    }
    return this.__values().has(key);
  }
  remove() {
    throw new TypeError("java.util.Map is unmodifiable");
  }
  size() {
    if (this.__source instanceof __QinJavaUtilHashMap) {
      return this.__source.size();
    }
    return this.__values().size;
  }
  isEmpty() {
    if (this.__source instanceof __QinJavaUtilHashMap) {
      return this.__source.isEmpty();
    }
    return this.__values().size === 0;
  }
  clear() {
    throw new TypeError("java.util.Map is unmodifiable");
  }
}
export const __QinJavaUtilCollections = {
  unmodifiableList(value) {
    return new __QinJavaUtilUnmodifiableList(value);
  },
  emptySet() {
    return new __QinJavaUtilUnmodifiableSet([]);
  },
  unmodifiableSet(value) {
    return new __QinJavaUtilUnmodifiableSet(value);
  },
  unmodifiableMap(value) {
    return new __QinJavaUtilUnmodifiableMap(value);
  }
};
export const __QinJavaUtilObjects = {
  equals(left, right) {
    return __qin_java_values_equal__(left, right);
  },
  hash(...values) {
    let result = 1;
    for (const value of values) {
      result = result * 31 + __qin_java_value_hash_code__(value);
    }
    return result;
  },
  hashCode(value) {
    return __qin_java_value_hash_code__(value);
  },
  requireNonNull(value, message) {
    if (value == null) {
      throw new TypeError(message == null ? "null" : String(message));
    }
    return value;
  },
  requireNonNullElse(value, defaultValue) {
    if (value != null) {
      return value;
    }
    if (defaultValue == null) {
      throw new TypeError("defaultObj");
    }
    return defaultValue;
  },
  toString(value, nullDefault) {
    if (value == null) {
      return arguments.length >= 2 ? nullDefault : "null";
    }
    return String(value);
  }
};
