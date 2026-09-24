export class __QinJavaUtilConcurrentAtomicLong {
  constructor(initialValue: number = 0) {
    this.__value = initialValue;
  }
  get(): number {
    return this.__value;
  }
  set(value: number): void {
    this.__value = value;
  }
  incrementAndGet(): number {
    this.__value += 1;
    return this.__value;
  }
  getAndIncrement(): number {
    const previous = this.__value;
    this.__value += 1;
    return previous;
  }
  addAndGet(delta: number): number {
    this.__value += delta;
    return this.__value;
  }
  getAndAdd(delta: number): number {
    const previous = this.__value;
    this.__value += delta;
    return previous;
  }
  compareAndSet(expectedValue: number, newValue: number): boolean {
    if (this.__value !== expectedValue) {
      return false;
    }
    this.__value = newValue;
    return true;
  }
}
