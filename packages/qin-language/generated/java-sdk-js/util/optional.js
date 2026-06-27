export class __QinJavaUtilOptionalValue {
  constructor(present, value) {
    this.__present = present;
    this.__value = value;
  }
  isPresent() {
    return this.__present;
  }
  isEmpty() {
    return !this.__present;
  }
  get() {
    if (!this.__present) {
      throw new Error("No value present");
    }
    return this.__value;
  }
  orElse(other) {
    return this.__present ? this.__value : other;
  }
  orElseGet(supplier) {
    return this.__present ? this.__value : supplier();
  }
}
export const __QinJavaUtilOptional = {
  empty() {
    return new __QinJavaUtilOptionalValue(false, null);
  },
  of(value) {
    if (value == null) {
      throw new Error("Optional.of requires a non-null value");
    }
    return new __QinJavaUtilOptionalValue(true, value);
  },
  ofNullable(value) {
    return value == null
      ? new __QinJavaUtilOptionalValue(false, null)
      : new __QinJavaUtilOptionalValue(true, value);
  }
};
