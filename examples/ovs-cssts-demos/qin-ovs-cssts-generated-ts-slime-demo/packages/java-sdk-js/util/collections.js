import {
  __qin_builtin_constructor__,
  __qin_collection_contains__,
  __qin_collection_get__,
  __qin_collection_is_empty__,
  __qin_collection_size__,
  __qin_collection_to_array__,
  __qin_array_append__,
  __qin_array_prepend__,
  __qin_array_remove_at__,
  __qin_array_slice__
} from "../core/runtime.js";
import {
  __qin_java_hash_key__,
  __qin_java_hash_key_equals__,
  __qin_java_identity_hash_code__,
  __qin_java_value_hash_code__,
  __qin_java_values_equal__
} from "./hash.js";
import { __QinJavaUtilStream } from "./stream.js";

export class __QinJavaUtilArrayList {
  constructor(initialValues) {
    this.__items = [];
    if (initialValues != null) {
      if (typeof initialValues === "number") {
        return;
      }
      const values = __qin_collection_to_array__(initialValues);
      for (const item of values) {
        this.__items = __qin_array_append__(this.__items, item);
      }
    }
  }
  add(value) {
    this.__items = __qin_array_append__(this.__items, value);
    return true;
  }
  addAll(values) {
    const source = __qin_collection_to_array__(values);
    for (const item of source) {
      this.__items = __qin_array_append__(this.__items, item);
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
    const previous = this.__items[index];
    this.__items = __qin_array_remove_at__(this.__items, index);
    return previous;
  }
  size(): number {
    return this.__items.length;
  }
  indexOf(value): number {
    for (let index = 0; index < this.__items.length; index++) {
      if (__qin_java_hash_key_equals__(this.__items[index], value)) return index;
    }
    return -1;
  }
  contains(value): boolean {
    return this.indexOf(value) >= 0;
  }
  isEmpty(): boolean {
    return this.__items.length === 0;
  }
  clear() {
    this.__items = [];
  }
  sort(comparator) {
    this.__items.sort(comparator);
  }
  subList(fromIndex, toIndex) {
    return new __QinJavaUtilUnmodifiableList(__qin_array_slice__(this.__items, fromIndex, toIndex));
  }
  slice(start, end): any[] {
    const endIndex: number = end == null ? this.__items.length : Number(end);
    return __qin_array_slice__(this.__items, start, endIndex);
  }
  toArray() {
    return __qin_collection_to_array__(this.__items);
  }
  stream() {
    return new __QinJavaUtilStream(this.__items);
  }
  [Symbol.iterator]() {
    return this.__items[Symbol.iterator]();
  }
}
export class __QinJavaUtilArrayDeque {
  constructor(initialValues) {
    this.__items = [];
    if (initialValues != null) {
      if (typeof initialValues === "number") {
        return;
      }
      const values = __qin_collection_to_array__(initialValues);
      for (const item of values) {
        this.__items = __qin_array_append__(this.__items, item);
      }
    }
  }
  add(value) {
    return this.addLast(value);
  }
  addAll(values) {
    let changed = false;
    const source = __qin_collection_to_array__(values);
    for (const value of source) {
      this.addLast(value);
      changed = true;
    }
    return changed;
  }
  addFirst(value) {
    this.__items = __qin_array_prepend__(this.__items, value);
  }
  addLast(value) {
    this.__items = __qin_array_append__(this.__items, value);
    return true;
  }
  offer(value) {
    return this.offerLast(value);
  }
  offerFirst(value) {
    this.__items = __qin_array_prepend__(this.__items, value);
    return true;
  }
  offerLast(value) {
    this.__items = __qin_array_append__(this.__items, value);
    return true;
  }
  push(value) {
    this.addFirst(value);
  }
  pop() {
    return this.removeFirst();
  }
  remove(...__qin_args) {
    if (__qin_args.length === 0) {
      return this.removeFirst();
    }
    const value = __qin_args[0];
    const index = this.__findIndex(value);
    if (index < 0) {
      return false;
    }
    this.__items = __qin_array_remove_at__(this.__items, index);
    return true;
  }
  removeFirst() {
    this.__requireNotEmpty();
    const previous = this.__items[0];
    this.__items = __qin_array_slice__(this.__items, 1, this.__items.length);
    return previous;
  }
  removeLast() {
    this.__requireNotEmpty();
    const lastIndex: number = this.__items.length - 1;
    const previous = this.__items[lastIndex];
    this.__items = __qin_array_slice__(this.__items, 0, lastIndex);
    return previous;
  }
  poll() {
    return this.pollFirst();
  }
  pollFirst() {
    return this.__items.length === 0 ? null : this.removeFirst();
  }
  pollLast() {
    return this.__items.length === 0 ? null : this.removeLast();
  }
  element() {
    return this.getFirst();
  }
  getFirst() {
    this.__requireNotEmpty();
    return this.__items[0];
  }
  getLast() {
    this.__requireNotEmpty();
    return this.__items[this.__items.length - 1];
  }
  peek() {
    return this.peekFirst();
  }
  peekFirst() {
    return this.__items.length === 0 ? null : this.__items[0];
  }
  peekLast() {
    return this.__items.length === 0 ? null : this.__items[this.__items.length - 1];
  }
  contains(value): boolean {
    return this.__findIndex(value) >= 0;
  }
  size(): number {
    return this.__items.length;
  }
  isEmpty(): boolean {
    return this.__items.length === 0;
  }
  clear() {
    this.__items = [];
  }
  toArray() {
    return __qin_collection_to_array__(this.__items);
  }
  stream() {
    return new __QinJavaUtilStream(this.__items);
  }
  descendingIterator() {
    return this.__items.slice().reverse()[Symbol.iterator]();
  }
  __findIndex(value): number {
    for (let index = 0; index < this.__items.length; index++) {
      if (__qin_java_hash_key_equals__(this.__items[index], value)) return index;
    }
    return -1;
  }
  __requireNotEmpty() {
    if (this.__items.length === 0) {
      throw new Error("java.util.NoSuchElementException");
    }
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
    return __qin_collection_to_array__(this.__source);
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
    return new __QinJavaUtilUnmodifiableList(__qin_array_slice__(this.__values(), fromIndex, toIndex));
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
    return this.__values();
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
  },
  copyOf(values) {
    return new __QinJavaUtilUnmodifiableList(__qin_collection_to_array__(values));
  }
};
export class __QinJavaUtilHashSet {
  constructor(initialValues) {
    this.__bucketHashes = [];
    this.__bucketValues = [];
    this.__items = [];
    this.__size = 0;
    if (initialValues != null) {
      const values = __qin_collection_to_array__(initialValues);
      for (let index = 0; index < values.length; index++) {
        this.add(values[index]);
      }
    }
  }
  __bucketIndexByHash(hash: string): number {
    for (let index = 0; index < this.__bucketHashes.length; index++) {
      if (__qin_java_values_equal__(this.__bucketHashes[index], hash)) {
        return index;
      }
    }
    return -1;
  }
  __bucket(value, create: boolean): any[] {
    const hash = __qin_java_hash_key__(value);
    const bucketIndex = this.__bucketIndexByHash(hash);
    if (bucketIndex >= 0) {
      return this.__bucketValues[bucketIndex];
    }
    if (create) {
      this.__bucketHashes = __qin_array_append__(this.__bucketHashes, hash);
      this.__bucketValues = __qin_array_append__(this.__bucketValues, []);
      return [];
    }
    return null;
  }
  __setBucketByHash(hash: string, bucket): void {
    const bucketIndex = this.__bucketIndexByHash(hash);
    if (bucketIndex < 0) {
      this.__bucketHashes = __qin_array_append__(this.__bucketHashes, hash);
      this.__bucketValues = __qin_array_append__(this.__bucketValues, bucket);
      return;
    }
    let nextBuckets = [];
    for (let index = 0; index < this.__bucketValues.length; index++) {
      nextBuckets = __qin_array_append__(nextBuckets, index === bucketIndex ? bucket : this.__bucketValues[index]);
    }
    this.__bucketValues = nextBuckets;
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
  __findItemIndex(value): number {
    for (let index = 0; index < this.__items.length; index++) {
      if (__qin_java_hash_key_equals__(this.__items[index], value)) {
        return index;
      }
    }
    return -1;
  }
  add(value): boolean {
    if (this.__findEntry(value) != null) {
      return false;
    }
    const bucket = this.__bucket(value, true);
    this.__setBucketByHash(__qin_java_hash_key__(value), __qin_array_append__(bucket, value));
    this.__items = __qin_array_append__(this.__items, value);
    this.__size++;
    return true;
  }
  addAll(values): boolean {
    let changed = false;
    const source = __qin_collection_to_array__(values);
    for (let index = 0; index < source.length; index++) {
      if (this.add(source[index])) {
        changed = true;
      }
    }
    return changed;
  }
  retainAll(values): boolean {
    let changed = false;
    const snapshot = this.toArray();
    for (let index = 0; index < snapshot.length; index++) {
      const value = snapshot[index];
      if (!__qin_collection_contains__(values, value)) {
        this.remove(value);
        changed = true;
      }
    }
    return changed;
  }
  contains(value): boolean {
    return this.__findEntry(value) != null;
  }
  remove(value): boolean {
    const bucket = this.__bucket(value, false);
    if (bucket == null) {
      return false;
    }
    for (let index = 0; index < bucket.length; index++) {
      if (__qin_java_hash_key_equals__(bucket[index], value)) {
        const existing = bucket[index];
        this.__setBucketByHash(__qin_java_hash_key__(value), __qin_array_remove_at__(bucket, index));
        const itemIndex = this.__findItemIndex(existing);
        if (itemIndex >= 0) {
          this.__items = __qin_array_remove_at__(this.__items, itemIndex);
        }
        this.__size--;
        return true;
      }
    }
    return false;
  }
  size(): number {
    return this.__size;
  }
  isEmpty(): boolean {
    return this.__size === 0;
  }
  clear() {
    this.__bucketHashes = [];
    this.__bucketValues = [];
    this.__items = [];
    this.__size = 0;
  }
  toArray(): any[] {
    return __qin_array_slice__(this.__items, 0, this.__items.length);
  }
  [Symbol.iterator]() {
    return this.toArray()[Symbol.iterator]();
  }
}
function __qin_java_tree_set_compare_values__(left, right): number {
  if (__qin_java_values_equal__(left, right)) {
    return 0;
  }
  if (left == null) {
    return -1;
  }
  if (right == null) {
    return 1;
  }
  const leftText: string = String(left);
  const rightText: string = String(right);
  if (__qin_java_values_equal__(leftText, rightText)) {
    return 0;
  }
  return leftText.compareTo(rightText);
}
function __qin_java_tree_set_compare__(comparator, left, right): number {
  return __qin_java_tree_set_compare_values__(left, right);
}
function __qin_java_tree_set_is_comparator__(value): boolean {
  return false;
}
export class __QinJavaUtilTreeSet {
  constructor(initialValues) {
    this.__items = [];
    this.__comparator = null;
    if (initialValues != null) {
      const values = __qin_collection_to_array__(initialValues);
      for (let index = 0; index < values.length; index++) {
        this.add(values[index]);
      }
    }
  }
  __compare(left, right): number {
    if (__qin_java_values_equal__(left, right)) {
      return 0;
    }
    if (left == null) {
      return -1;
    }
    if (right == null) {
      return 1;
    }
    const leftText: string = String(left);
    const rightText: string = String(right);
    if (__qin_java_values_equal__(leftText, rightText)) {
      return 0;
    }
    return leftText.compareTo(rightText);
  }
  __findIndex(value): number {
    for (let index = 0; index < this.__items.length; index++) {
      if (this.__compare(this.__items[index], value) === 0) {
        return index;
      }
    }
    return -1;
  }
  __insertionIndex(value): number {
    let index = 0;
    while (index < this.__items.length && this.__compare(this.__items[index], value) < 0) {
      index++;
    }
    return index;
  }
  add(value): boolean {
    if (this.__findIndex(value) >= 0) {
      return false;
    }
    const insertionIndex = this.__insertionIndex(value);
    let nextItems = [];
    for (let index = 0; index < insertionIndex; index++) {
      nextItems = __qin_array_append__(nextItems, this.__items[index]);
    }
    nextItems = __qin_array_append__(nextItems, value);
    for (let index = insertionIndex; index < this.__items.length; index++) {
      nextItems = __qin_array_append__(nextItems, this.__items[index]);
    }
    this.__items = nextItems;
    return true;
  }
  addAll(values): boolean {
    let changed = false;
    const source = __qin_collection_to_array__(values);
    for (let index = 0; index < source.length; index++) {
      if (this.add(source[index])) {
        changed = true;
      }
    }
    return changed;
  }
  retainAll(values): boolean {
    let changed = false;
    const snapshot = this.toArray();
    for (let index = 0; index < snapshot.length; index++) {
      const value = snapshot[index];
      if (!__qin_collection_contains__(values, value)) {
        this.remove(value);
        changed = true;
      }
    }
    return changed;
  }
  contains(value): boolean {
    return this.__findIndex(value) >= 0;
  }
  remove(value): boolean {
    const index = this.__findIndex(value);
    if (index < 0) {
      return false;
    }
    this.__items = __qin_array_remove_at__(this.__items, index);
    return true;
  }
  first() {
    if (this.__items.length === 0) {
      throw new Error("java.util.NoSuchElementException");
    }
    return this.__items[0];
  }
  last() {
    if (this.__items.length === 0) {
      throw new Error("java.util.NoSuchElementException");
    }
    return this.__items[this.__items.length - 1];
  }
  size(): number {
    return this.__items.length;
  }
  isEmpty(): boolean {
    return this.__items.length === 0;
  }
  clear() {
    this.__items = [];
  }
  toArray(): any[] {
    return __qin_array_slice__(this.__items, 0, this.__items.length);
  }
  stream() {
    return new __QinJavaUtilStream(this.__items);
  }
  [Symbol.iterator]() {
    return this.__items[Symbol.iterator]();
  }
}
export class __QinJavaUtilUnmodifiableSet {
  constructor(source) {
    this.__source = source == null ? [] : source;
  }
  __values(): any[] {
    return __qin_collection_to_array__(this.__source);
  }
  add() {
    throw new TypeError("java.util.Set is unmodifiable");
  }
  addAll() {
    throw new TypeError("java.util.Set is unmodifiable");
  }
  retainAll() {
    throw new TypeError("java.util.Set is unmodifiable");
  }
  contains(value): boolean {
    return __qin_collection_contains__(this.__values(), value);
  }
  remove() {
    throw new TypeError("java.util.Set is unmodifiable");
  }
  size(): number {
    return this.__values().length;
  }
  isEmpty(): boolean {
    return this.__values().length === 0;
  }
  clear() {
    throw new TypeError("java.util.Set is unmodifiable");
  }
  toArray(): any[] {
    return this.__values();
  }
  [Symbol.iterator]() {
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
    const rawValues = __qin_collection_to_array__(values);
    if (rawValues.length === 1 && Array.isArray(rawValues[0])) {
      return new __QinJavaUtilArrayList(rawValues[0]);
    }
    return new __QinJavaUtilArrayList(rawValues);
  },
  asListArray(value) {
    return new __QinJavaUtilArrayList(__qin_collection_to_array__(value));
  },
  stream(value) {
    return new __QinJavaUtilStream(this.__items(value) ?? []);
  },
  copyOf(value, newLength) {
    const items = (this.__items(value) ?? []).slice(0, Number(newLength));
    while (items.length < Number(newLength)) {
      items.push(null);
    }
    return items;
  },
  fill(value, ...args) {
    const items = this.__items(value);
    if (items == null) {
      return;
    }
    if (args.length === 1) {
      items.fill(args[0]);
      return;
    }
    if (args.length === 3) {
      items.fill(args[2], Number(args[0]), Number(args[1]));
      return;
    }
    throw new TypeError("java.util.Arrays.fill expects (array, value) or (array, from, to, value)");
  },
  sort(value, fromIndex, toIndex) {
    const items = this.__items(value);
    if (items == null) {
      return;
    }
    if (fromIndex === undefined && toIndex === undefined) {
      items.sort();
      return;
    }
    const from = Number(fromIndex);
    const to = Number(toIndex);
    const sorted = items.slice(from, to).sort();
    items.splice(from, sorted.length, ...sorted);
  },
  toString(value) {
    const items = this.__items(value);
    if (items == null) {
      return "null";
    }
    return "[" + items.map(item => String(item)).join(", ") + "]";
  },
  __items(value) {
    if (value == null) {
      return null;
    }
    if (typeof value.size === "function" && typeof value.get === "function") {
      const result = [];
      const size = Number(value.size());
      for (let index = 0; index < size; index++) {
        result.push(value.get(index));
      }
      return result;
    }
    if (value instanceof __QinJavaUtilArrayList || Array.isArray(value.__items)) {
      return value.__items;
    }
    if (typeof value.__values === "function") {
      return value.__values();
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
      let listItems = null;
      if (typeof item.size === "function" && typeof item.get === "function") {
        listItems = [];
        const size = Number(item.size());
        for (let index = 0; index < size; index++) {
          listItems.push(item.get(index));
        }
      } else if (item instanceof __QinJavaUtilArrayList || Array.isArray(item.__items)) {
        listItems = item.__items;
      } else if (typeof item.__values === "function") {
        listItems = item.__values();
      }
      if (Array.isArray(item) || listItems != null
          || (item != null && typeof item[Symbol.iterator] === "function"
          && typeof item !== "string")) {
        if (seen.indexOf(item) >= 0) {
          return "[...]";
        }
        seen.push(item);
        const parts = [];
        const iterable = listItems != null ? listItems : item;
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
class __QinJavaUtilHashMapEntry {
  constructor(key, value) {
    this.key = key;
    this.value = value;
  }
  getKey() {
    return this.key;
  }
  getValue() {
    return this.value;
  }
  get(index) {
    return Number(index) === 0 ? this.key : this.value;
  }
}
class __QinJavaUtilHashMapFoundEntry {
  constructor(bucket: any[], index: number, entry: __QinJavaUtilHashMapEntry) {
    this.bucket = bucket;
    this.index = index;
    this.entry = entry;
  }
}
export class __QinJavaUtilHashMap {
  static copyOf(value) {
    return new __QinJavaUtilUnmodifiableMap(value);
  }
  constructor(initialEntries) {
    this.__bucketHashes = [];
    this.__bucketValues = [];
    this.__entries = [];
    this.__size = 0;
    if (initialEntries != null) {
      if (typeof initialEntries === "number") {
        return;
      }
      const entries = __qin_collection_to_array__(initialEntries);
      for (let index = 0; index < entries.length; index++) {
        const entry = entries[index];
        const key = __qin_collection_get__(entry, 0);
        const value = __qin_collection_get__(entry, 1);
        this.put(key, value);
      }
    }
  }
  __bucketIndexByHash(hash: string): number {
    for (let index = 0; index < this.__bucketHashes.length; index++) {
      if (__qin_java_values_equal__(this.__bucketHashes[index], hash)) {
        return index;
      }
    }
    return -1;
  }
  __bucket(key, create: boolean): any[] {
    const hash = __qin_java_hash_key__(key);
    const bucketIndex = this.__bucketIndexByHash(hash);
    if (bucketIndex >= 0) {
      return this.__bucketValues[bucketIndex];
    }
    if (create) {
      this.__bucketHashes = __qin_array_append__(this.__bucketHashes, hash);
      this.__bucketValues = __qin_array_append__(this.__bucketValues, []);
      return [];
    }
    return null;
  }
  __setBucketByHash(hash: string, bucket): void {
    const bucketIndex = this.__bucketIndexByHash(hash);
    if (bucketIndex < 0) {
      this.__bucketHashes = __qin_array_append__(this.__bucketHashes, hash);
      this.__bucketValues = __qin_array_append__(this.__bucketValues, bucket);
      return;
    }
    let nextBuckets = [];
    for (let index = 0; index < this.__bucketValues.length; index++) {
      nextBuckets = __qin_array_append__(nextBuckets, index === bucketIndex ? bucket : this.__bucketValues[index]);
    }
    this.__bucketValues = nextBuckets;
  }
  __findEntry(key): __QinJavaUtilHashMapFoundEntry {
    const bucket = this.__bucket(key, false);
    if (bucket == null) {
      return null;
    }
    for (let index = 0; index < bucket.length; index++) {
      const entry: __QinJavaUtilHashMapEntry = bucket[index];
      if (__qin_java_hash_key_equals__(entry.key, key)) {
        return new __QinJavaUtilHashMapFoundEntry(bucket, index, entry);
      }
    }
    return null;
  }
  __findEntryArrayIndex(key): number {
    for (let index = 0; index < this.__entries.length; index++) {
      const entry: __QinJavaUtilHashMapEntry = this.__entries[index];
      if (__qin_java_hash_key_equals__(entry.key, key)) {
        return index;
      }
    }
    return -1;
  }
  __putNewEntry(key, value): void {
    const hash = __qin_java_hash_key__(key);
    const bucket = this.__bucket(key, true);
    const entry = new __QinJavaUtilHashMapEntry(key, value);
    this.__setBucketByHash(hash, __qin_array_append__(bucket, entry));
    this.__entries = __qin_array_append__(this.__entries, entry);
    this.__size++;
  }
  put(key, value) {
    const found = this.__findEntry(key);
    if (found != null) {
      const previous = found.entry.value;
      found.entry.value = value;
      return previous;
    }
    this.__putNewEntry(key, value);
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
      this.__putNewEntry(key, value);
      return null;
    }
    const previous = found.entry.value;
    if (previous == null) {
      found.entry.value = value;
    }
    return previous;
  }
  values() {
    let values = [];
    for (let index = 0; index < this.__entries.length; index++) {
      const entry: __QinJavaUtilHashMapEntry = this.__entries[index];
      values = __qin_array_append__(values, entry.value);
    }
    return new __QinJavaUtilArrayList(values);
  }
  keys() {
    let keys = [];
    for (let index = 0; index < this.__entries.length; index++) {
      const entry: __QinJavaUtilHashMapEntry = this.__entries[index];
      keys = __qin_array_append__(keys, entry.key);
    }
    return new __QinJavaUtilArrayList(keys);
  }
  keySet() {
    return this.keys();
  }
  entrySet() {
    let entries = [];
    for (let index = 0; index < this.__entries.length; index++) {
      entries = __qin_array_append__(entries, this.__entries[index]);
    }
    return new __QinJavaUtilArrayList(entries);
  }
  forEach(action) {
    for (let index = 0; index < this.__entries.length; index++) {
      const entry: __QinJavaUtilHashMapEntry = this.__entries[index];
      action(entry.key, entry.value);
    }
    return null;
  }
  computeIfAbsent(key, mappingFunction) {
    const found = this.__findEntry(key);
    if (found == null || found.entry.value == null) {
      const value = mappingFunction(key);
      if (found == null) {
        this.__putNewEntry(key, value);
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
      this.__putNewEntry(key, value);
      return value;
    }
    if (found.entry.value == null) {
      found.entry.value = value;
      return value;
    }
    const nextValue = remappingFunction(found.entry.value, value);
    if (nextValue == null) {
      const entryIndex = this.__findEntryArrayIndex(key);
      this.__setBucketByHash(__qin_java_hash_key__(key), __qin_array_remove_at__(found.bucket, found.index));
      if (entryIndex >= 0) {
        this.__entries = __qin_array_remove_at__(this.__entries, entryIndex);
      }
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
    const entryIndex = this.__findEntryArrayIndex(key);
    this.__setBucketByHash(__qin_java_hash_key__(key), __qin_array_remove_at__(found.bucket, found.index));
    if (entryIndex >= 0) {
      this.__entries = __qin_array_remove_at__(this.__entries, entryIndex);
    }
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
    this.__bucketHashes = [];
    this.__bucketValues = [];
    this.__entries = [];
    this.__size = 0;
  }
  toArray() {
    return this.entrySet().toArray();
  }
}
export class __QinJavaUtilIdentityHashMap {
  constructor(initialEntries) {
    this.__keyHashes = [];
    this.__keys = [];
    this.__values = [];
    this.__size = 0;
    if (initialEntries != null) {
      const entries = __qin_collection_to_array__(initialEntries);
      for (let index = 0; index < entries.length; index++) {
        const entry = entries[index];
        this.put(__qin_collection_get__(entry, 0), __qin_collection_get__(entry, 1));
      }
    }
  }
  __findIndex(key): number {
    const hash = __qin_java_identity_hash_code__(key);
    for (let index = 0; index < this.__keyHashes.length; index++) {
      if (__qin_java_values_equal__(this.__keyHashes[index], hash)) {
        return index;
      }
    }
    return -1;
  }
  __setValueAt(targetIndex: number, value): void {
    let nextValues = [];
    for (let index = 0; index < this.__values.length; index++) {
      nextValues = __qin_array_append__(nextValues, index === targetIndex ? value : this.__values[index]);
    }
    this.__values = nextValues;
  }
  __putNewEntry(key, value): void {
    this.__keyHashes = __qin_array_append__(this.__keyHashes, __qin_java_identity_hash_code__(key));
    this.__keys = __qin_array_append__(this.__keys, key);
    this.__values = __qin_array_append__(this.__values, value);
    this.__size++;
  }
  put(key, value) {
    const index = this.__findIndex(key);
    if (index < 0) {
      this.__putNewEntry(key, value);
      return null;
    }
    const previous = this.__values[index];
    this.__setValueAt(index, value);
    return previous;
  }
  get(key) {
    const index = this.__findIndex(key);
    return index < 0 ? null : this.__values[index];
  }
  getOrDefault(key, defaultValue) {
    const index = this.__findIndex(key);
    return index < 0 ? defaultValue : this.__values[index];
  }
  putIfAbsent(key, value) {
    const index = this.__findIndex(key);
    if (index < 0) {
      this.__putNewEntry(key, value);
      return null;
    }
    const previous = this.__values[index];
    if (previous == null) {
      this.__setValueAt(index, value);
    }
    return previous;
  }
  values() {
    return new __QinJavaUtilArrayList(__qin_array_slice__(this.__values, 0, this.__values.length));
  }
  keys() {
    return new __QinJavaUtilArrayList(__qin_array_slice__(this.__keys, 0, this.__keys.length));
  }
  computeIfAbsent(key, mappingFunction) {
    const index = this.__findIndex(key);
    if (index < 0 || this.__values[index] == null) {
      const value = mappingFunction(key);
      if (index < 0) {
        this.__putNewEntry(key, value);
      } else {
        this.__setValueAt(index, value);
      }
      return value;
    }
    return this.__values[index];
  }
  merge(key, value, remappingFunction) {
    const index = this.__findIndex(key);
    if (index < 0) {
      this.__putNewEntry(key, value);
      return value;
    }
    const previous = this.__values[index];
    if (previous == null) {
      this.__setValueAt(index, value);
      return value;
    }
    const nextValue = remappingFunction(previous, value);
    if (nextValue == null) {
      this.__keyHashes = __qin_array_remove_at__(this.__keyHashes, index);
      this.__keys = __qin_array_remove_at__(this.__keys, index);
      this.__values = __qin_array_remove_at__(this.__values, index);
      this.__size--;
      return null;
    }
    this.__setValueAt(index, nextValue);
    return nextValue;
  }
  containsKey(key) {
    return this.__findIndex(key) >= 0;
  }
  remove(key) {
    const index = this.__findIndex(key);
    if (index < 0) {
      return null;
    }
    const previous = this.__values[index];
    this.__keyHashes = __qin_array_remove_at__(this.__keyHashes, index);
    this.__keys = __qin_array_remove_at__(this.__keys, index);
    this.__values = __qin_array_remove_at__(this.__values, index);
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
    this.__keyHashes = [];
    this.__keys = [];
    this.__values = [];
    this.__size = 0;
  }
}
export class __QinJavaUtilMapBackedSet {
  constructor(sourceMap: __QinJavaUtilHashMap) {
    if (sourceMap == null) {
      throw new TypeError("Collections.newSetFromMap requires a mutable map");
    }
    if (!sourceMap.isEmpty()) {
      throw new TypeError("Collections.newSetFromMap requires an empty map");
    }
    this.__map = sourceMap;
  }
  add(value): boolean {
    const hadValue = this.__map.containsKey(value);
    this.__map.put(value, true);
    return !hadValue;
  }
  addAll(values): boolean {
    let changed = false;
    const source = __qin_collection_to_array__(values);
    for (let index = 0; index < source.length; index++) {
      if (this.add(source[index])) {
        changed = true;
      }
    }
    return changed;
  }
  retainAll(values): boolean {
    let changed = false;
    const snapshot = this.toArray();
    for (let index = 0; index < snapshot.length; index++) {
      const value = snapshot[index];
      if (!__qin_collection_contains__(values, value)) {
        this.remove(value);
        changed = true;
      }
    }
    return changed;
  }
  contains(value): boolean {
    return this.__map.containsKey(value);
  }
  remove(value): boolean {
    if (!this.__map.containsKey(value)) {
      return false;
    }
    this.__map.remove(value);
    return true;
  }
  size(): number {
    return this.__map.size();
  }
  isEmpty(): boolean {
    return this.__map.isEmpty();
  }
  clear(): void {
    this.__map.clear();
  }
  toArray(): any[] {
    return this.__map.keys().toArray();
  }
  [Symbol.iterator]() {
    return this.toArray()[Symbol.iterator]();
  }
}
export class __QinJavaUtilUnmodifiableMap {
  constructor(source) {
    this.__source = source == null ? new __QinJavaUtilHashMap() : new __QinJavaUtilHashMap(source);
  }
  __values(): __QinJavaUtilHashMap {
    return this.__source;
  }
  put() {
    throw new TypeError("java.util.Map is unmodifiable");
  }
  get(key) {
    const entries = this.__values();
    return entries.containsKey(key) ? entries.get(key) : null;
  }
  getOrDefault(key, defaultValue) {
    const entries = this.__values();
    return entries.containsKey(key) ? entries.get(key) : defaultValue;
  }
  putIfAbsent() {
    throw new TypeError("java.util.Map is unmodifiable");
  }
  values() {
    return new __QinJavaUtilUnmodifiableList(this.__values().values());
  }
  forEach(action) {
    return this.__values().forEach(action);
  }
  computeIfAbsent() {
    throw new TypeError("java.util.Map is unmodifiable");
  }
  merge() {
    throw new TypeError("java.util.Map is unmodifiable");
  }
  containsKey(key) {
    return this.__values().containsKey(key);
  }
  remove() {
    throw new TypeError("java.util.Map is unmodifiable");
  }
  size() {
    return this.__values().size();
  }
  isEmpty() {
    return this.__values().isEmpty();
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
  },
  newSetFromMap(value: __QinJavaUtilHashMap) {
    return new __QinJavaUtilMapBackedSet(value);
  }
};
export const __QinJavaUtilObjects = {
  isNull(value) {
    return value == null;
  },
  nonNull(value) {
    return value != null;
  },
  equals(left, right) {
    return __qin_java_values_equal__(left, right);
  },
  hash(...values) {
    let result = 1;
    for (let index = 0; index < values.length; index++) {
      const value = values[index];
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
      return nullDefault === undefined ? "null" : nullDefault;
    }
    return String(value);
  }
};
