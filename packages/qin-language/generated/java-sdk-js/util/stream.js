import { __QinJavaUtilArrayList } from "./collections.js";
import { __QinJavaUtilOptional } from "./optional.js";
import { __qin_collection_add__, __qin_collection_to_array__, __qin_java_new_array__ } from "../core/runtime.js";
import { __qin_java_values_equal__ } from "./hash.js";

function __qin_java_compare_values(left, right): number {
  if (left === right) {
    return 0;
  }
  if (left == null) {
    return -1;
  }
  if (right == null) {
    return 1;
  }
  if (typeof left === "number" && typeof right === "number") {
    return left < right ? -1 : 1;
  }
  if (typeof left === "string" && typeof right === "string") {
    return left < right ? -1 : 1;
  }
  if (typeof left.compareTo === "function") {
    return Number(left.compareTo(right));
  }
  const leftText = String(left);
  const rightText = String(right);
  if (leftText === rightText) {
    return 0;
  }
  return leftText < rightText ? -1 : 1;
}

function __qin_java_comparator_compare(comparator: QinJavaComparator, left, right): number {
  if (comparator == null) {
    return __qin_java_compare_values(left, right);
  }
  return Number(comparator.compare(left, right));
}

function __qin_java_comparator(compare) {
  const comparator = (left, right) => Number(compare(left, right));
  comparator.compare = comparator;
  comparator.thenComparing = (next) => __qin_java_comparator((left, right) => {
    const first = comparator(left, right);
    return first !== 0 ? first : __qin_java_comparator_compare(next, left, right);
  });
  comparator.thenComparingInt = (extractor) => comparator.thenComparing(__QinJavaUtilComparator.comparingInt(extractor));
  comparator.reversed = () => __qin_java_comparator((left, right) => -comparator(left, right));
  return comparator;
}

export const __QinJavaUtilComparator = {
  comparing(extractor) {
    return __qin_java_comparator((left, right) => __qin_java_compare_values(extractor(left), extractor(right)));
  },
  comparingInt(extractor) {
    return __qin_java_comparator((left, right) => {
      const leftValue = Number(extractor(left));
      const rightValue = Number(extractor(right));
      if (leftValue === rightValue) {
        return 0;
      }
      return leftValue < rightValue ? -1 : 1;
    });
  }
};

class __QinJavaUtilCollector {
  __collect(items: any[]): any {
    return null;
  }
}

export class __QinJavaUtilStream {
  constructor(source) {
    this.__items = source == null ? [] : Array.from(source);
  }
  static __compareWithComparator(comparator: QinJavaComparator, left, right): number {
    if (comparator == null) {
      return 0;
    }
    return Number(comparator.compare(left, right));
  }
  filter(predicate: QinJavaPredicate) {
    const result = [];
    for (const item of this.__items) {
      if (predicate.test(item)) {
        result.push(item);
      }
    }
    return new __QinJavaUtilStream(result);
  }
  map(mapper: QinJavaFunction) {
    const result = [];
    for (const item of this.__items) {
      result.push(mapper.apply(item));
    }
    return new __QinJavaUtilStream(result);
  }
  distinct() {
    const result = [];
    for (const item of this.__items) {
      let exists = false;
      for (const existing of result) {
        if (__qin_java_values_equal__(existing, item)) {
          exists = true;
          break;
        }
      }
      if (!exists) {
        result.push(item);
      }
    }
    return new __QinJavaUtilStream(result);
  }
  sorted(comparator: QinJavaComparator) {
    const sorted = [];
    for (const item of this.__items) {
      sorted.push(item);
    }
    for (let index = 1; index < sorted.size(); index++) {
      const value = sorted.get(index);
      let insertion = index - 1;
      while (insertion >= 0) {
        if (__QinJavaUtilStream.__compareWithComparator(comparator, sorted.get(insertion), value) <= 0) {
          break;
        }
        sorted.set(insertion + 1, sorted.get(insertion));
        insertion--;
      }
      sorted.set(insertion + 1, value);
    }
    return new __QinJavaUtilStream(sorted);
  }
  anyMatch(predicate: QinJavaPredicate) {
    for (const item of this.__items) {
      if (predicate.test(item)) {
        return true;
      }
    }
    return false;
  }
  allMatch(predicate: QinJavaPredicate) {
    for (const item of this.__items) {
      if (!predicate.test(item)) {
        return false;
      }
    }
    return true;
  }
  noneMatch(predicate: QinJavaPredicate) {
    for (const item of this.__items) {
      if (predicate.test(item)) {
        return false;
      }
    }
    return true;
  }
  count() {
    return this.__items.length;
  }
  forEach(consumer: QinJavaConsumer) {
    for (const item of this.__items) {
      consumer.accept(item);
    }
  }
  flatMap(mapper: QinJavaFunction) {
    const result = [];
    for (const item of this.__items) {
      const mapped: any[] = __qin_collection_to_array__(mapper.apply(item));
      if (mapped != null) {
        for (const child of mapped) {
          result.push(child);
        }
      }
    }
    return new __QinJavaUtilStream(result);
  }
  mapToInt(mapper: QinJavaFunction) {
    const result = [];
    for (const item of this.__items) {
      result.push(Number(mapper.apply(item)));
    }
    return new __QinJavaUtilIntStream(result);
  }
  flatMapToInt(mapper: QinJavaFunction) {
    const result = [];
    for (const item of this.__items) {
      const mapped: any[] = __qin_collection_to_array__(mapper.apply(item));
      if (mapped != null) {
        for (const child of mapped) {
          result.push(Number(child));
        }
      }
    }
    return new __QinJavaUtilIntStream(result);
  }
  findFirst() {
    return this.__items.length === 0
      ? __QinJavaUtilOptional.empty()
      : __QinJavaUtilOptional.ofNullable(this.__items[0]);
  }
  collect(collector: __QinJavaUtilCollector) {
    if (collector == null) {
      throw new TypeError("java.util.stream.Stream.collect requires a Qin collector");
    }
    return collector.__collect(this.__items);
  }
  toArray() {
    const result = [];
    for (const item of this.__items) {
      result.push(item);
    }
    return result;
  }
  toList() {
    return new __QinJavaUtilArrayList(this.__items);
  }
  static of(...items) {
    return new __QinJavaUtilStream(items);
  }
  static concat(left, right) {
    const result = [];
    if (left != null) {
      const leftItems: any[] = __qin_collection_to_array__(left);
      for (const item of leftItems) {
        result.push(item);
      }
    }
    if (right != null) {
      const rightItems: any[] = __qin_collection_to_array__(right);
      for (const item of rightItems) {
        result.push(item);
      }
    }
    return new __QinJavaUtilStream(result);
  }
  [Symbol.iterator]() {
    return this.__items[Symbol.iterator]();
  }
}
class __QinJavaUtilOptionalNumber {
  __present: boolean = false;
  __value: number = 0;
  constructor(present: boolean, value: number) {
    this.__present = present;
    this.__value = value;
  }
  orElse(defaultValue) {
    return this.__present ? this.__value : defaultValue;
  }
  orElseThrow() {
    if (!this.__present) {
      throw new Error("No value present");
    }
    return this.__value;
  }
}
class __QinJavaUtilIntStream {
  __items: __QinJavaUtilArrayList = null as any;
  constructor(source) {
    this.__items = new __QinJavaUtilArrayList();
    const values: any[] = source == null ? [] : __qin_collection_to_array__(source);
    for (const item of values) {
      this.__items.add(Number(item));
    }
  }
  filter(predicate: QinJavaPredicate) {
    const result = new __QinJavaUtilArrayList();
    for (const item of this.__items) {
      if (predicate.test(item)) {
        result.add(item);
      }
    }
    return new __QinJavaUtilIntStream(result);
  }
  map(mapper: QinJavaFunction) {
    const result = new __QinJavaUtilArrayList();
    for (const item of this.__items) {
      result.add(Number(mapper.apply(item)));
    }
    return new __QinJavaUtilIntStream(result);
  }
  sum() {
    let result = 0;
    for (const item of this.__items) {
      result += Number(item);
    }
    return result;
  }
  min() {
    if (this.__items.size() === 0) {
      return new __QinJavaUtilOptionalNumber(false, 0);
    }
    let result = Number(this.__items.get(0));
    for (let index = 1; index < this.__items.size(); index++) {
      const value = Number(this.__items.get(index));
      if (value < result) {
        result = value;
      }
    }
    return new __QinJavaUtilOptionalNumber(true, result);
  }
  max() {
    if (this.__items.size() === 0) {
      return new __QinJavaUtilOptionalNumber(false, 0);
    }
    let result = Number(this.__items.get(0));
    for (let index = 1; index < this.__items.size(); index++) {
      const value = Number(this.__items.get(index));
      if (value > result) {
        result = value;
      }
    }
    return new __QinJavaUtilOptionalNumber(true, result);
  }
  toArray() {
    const length = this.__items.size();
    const result = __qin_java_new_array__("[I", length);
    for (let index = 0; index < length; index++) {
      result[index] = Number(this.__items.get(index)) | 0;
    }
    return result;
  }
  forEach(consumer: QinJavaConsumer) {
    for (const item of this.__items) {
      consumer.accept(item);
    }
  }
  [Symbol.iterator]() {
    return this.__items[Symbol.iterator]();
  }
}
class __QinJavaUtilToListCollector extends __QinJavaUtilCollector {
  constructor() {
    super();
  }
  __collect(items: any[]) {
    return new __QinJavaUtilArrayList(items);
  }
}
class __QinJavaUtilToCollectionCollector extends __QinJavaUtilCollector {
  constructor(supplier) {
    super();
    this.__supplier = supplier;
  }
  __collect(items: any[]) {
    const supplier = this.__supplier;
    if (supplier == null || typeof supplier !== "function") {
      throw new TypeError("Collectors.toCollection requires a supplier");
    }
    const collection = supplier();
    if (collection == null) {
      throw new TypeError("Collectors.toCollection supplier must return a mutable collection");
    }
    for (const item of items) {
      __qin_collection_add__(collection, item);
    }
    return collection;
  }
}
class __QinJavaUtilJoiningCollector extends __QinJavaUtilCollector {
  constructor(delimiter) {
    super();
    this.__delimiter = delimiter;
  }
  __collect(items: any[]) {
    let result: string = "";
    let first: boolean = true;
    const delimiter: string = String(this.__delimiter);
    for (const item of items) {
      const text: string = String(item);
      if (first) {
        first = false;
      } else {
        result = result + delimiter;
      }
      result = result + text;
    }
    return result;
  }
}
class __QinJavaUtilStreamCollectorsRuntime {
  toList() {
    return new __QinJavaUtilToListCollector();
  }
  toCollection(supplier) {
    return new __QinJavaUtilToCollectionCollector(supplier);
  }
  joining(delimiter = "") {
    return new __QinJavaUtilJoiningCollector(delimiter);
  }
}
export const __QinJavaUtilStreamCollectors = new __QinJavaUtilStreamCollectorsRuntime();
