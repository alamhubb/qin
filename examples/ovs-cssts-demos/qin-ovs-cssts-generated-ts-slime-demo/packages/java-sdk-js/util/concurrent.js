export class __QinJavaUtilConcurrentAtomicLong {
  constructor(initialValue = 0) {
    this.__value = Number(initialValue);
  }
  get() {
    return this.__value;
  }
  set(value) {
    this.__value = Number(value);
  }
  incrementAndGet() {
    this.__value += 1;
    return this.__value;
  }
  getAndIncrement() {
    const previous = this.__value;
    this.__value += 1;
    return previous;
  }
  addAndGet(delta) {
    this.__value += Number(delta);
    return this.__value;
  }
  getAndAdd(delta) {
    const previous = this.__value;
    this.__value += Number(delta);
    return previous;
  }
  compareAndSet(expectedValue, newValue) {
    if (this.__value !== Number(expectedValue)) {
      return false;
    }
    this.__value = Number(newValue);
    return true;
  }
}
