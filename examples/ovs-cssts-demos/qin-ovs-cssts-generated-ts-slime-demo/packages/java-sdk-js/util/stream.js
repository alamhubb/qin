import { __QinJavaUtilArrayList } from "./collections.js";
import { __QinJavaUtilOptional } from "./optional.js";

export class __QinJavaUtilStream {
  constructor(source) {
    this.__items = source == null ? [] : Array.from(source);
  }
  filter(predicate) {
    return new __QinJavaUtilStream(this.__items.filter(item => predicate(item)));
  }
  map(mapper) {
    return new __QinJavaUtilStream(this.__items.map(item => mapper(item)));
  }
  anyMatch(predicate) {
    return this.__items.some(item => predicate(item));
  }
  findFirst() {
    return this.__items.length === 0
      ? __QinJavaUtilOptional.empty()
      : __QinJavaUtilOptional.ofNullable(this.__items[0]);
  }
  collect(collector) {
    if (collector == null || typeof collector.__collect !== "function") {
      throw new TypeError("java.util.stream.Stream.collect requires a Qin collector");
    }
    return collector.__collect(this.__items);
  }
  toArray() {
    return this.__items.slice();
  }
  [Symbol.iterator]() {
    return this.__items[Symbol.iterator]();
  }
}
export const __QinJavaUtilStreamCollectors = {
  toList() {
    return {
      __collect(items) {
        return new __QinJavaUtilArrayList(items);
      }
    };
  },
  joining(delimiter = "") {
    return {
      __collect(items) {
        return items.map(item => String(item)).join(String(delimiter));
      }
    };
  }
};
