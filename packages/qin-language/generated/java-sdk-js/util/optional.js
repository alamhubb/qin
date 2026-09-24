export class __QinJavaUtilOptionalValue {
  __present: boolean = false;
  __value: any = null;
  constructor(present: boolean, value) {
    this.__present = present;
    this.__value = value;
  }
  isPresent(): boolean {
    return this.__present;
  }
  isEmpty(): boolean {
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
class __QinJavaUtilOptionalRuntime {
  empty() {
    return new __QinJavaUtilOptionalValue(false, null);
  }
  of(value) {
    if (value == null) {
      throw new Error("Optional.of requires a non-null value");
    }
    return new __QinJavaUtilOptionalValue(true, value);
  }
  ofNullable(value) {
    return value == null
      ? new __QinJavaUtilOptionalValue(false, null)
      : new __QinJavaUtilOptionalValue(true, value);
  }
}
export const __QinJavaUtilOptional = new __QinJavaUtilOptionalRuntime();
