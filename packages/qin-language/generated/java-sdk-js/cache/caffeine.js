export const __QinCaffeineRemovalCause = {
  SIZE: {
    wasEvicted() {
      return true;
    }
  },
  EXPLICIT: {
    wasEvicted() {
      return false;
    }
  }
};
export class __QinCaffeineCache {
  constructor(maximumSize, removalListener) {
    this.__maximumSize = maximumSize == null ? Infinity : maximumSize;
    this.__removalListener = removalListener;
    this.__entries = new Map();
  }
  getIfPresent(key) {
    if (!this.__entries.has(key)) {
      return null;
    }
    const value = this.__entries.get(key);
    this.__entries.delete(key);
    this.__entries.set(key, value);
    return value;
  }
  put(key, value) {
    if (this.__entries.has(key)) {
      this.__entries.delete(key);
    }
    this.__entries.set(key, value);
    while (this.__entries.size > this.__maximumSize) {
      let oldestKey = null;
      for (const entryKey of this.__entries.keys()) {
        oldestKey = entryKey;
        break;
      }
      const oldestValue = this.__entries.get(oldestKey);
      this.__entries.delete(oldestKey);
      if (this.__removalListener != null) {
        this.__removalListener(oldestKey, oldestValue, __QinCaffeineRemovalCause.SIZE);
      }
    }
  }
  invalidate(key) {
    if (!this.__entries.has(key)) {
      return;
    }
    const value = this.__entries.get(key);
    this.__entries.delete(key);
    if (this.__removalListener != null) {
      this.__removalListener(key, value, __QinCaffeineRemovalCause.EXPLICIT);
    }
  }
  invalidateAll() {
    for (const [key, value] of Array.from(this.__entries.entries())) {
      this.__entries.delete(key);
      if (this.__removalListener != null) {
        this.__removalListener(key, value, __QinCaffeineRemovalCause.EXPLICIT);
      }
    }
  }
  estimatedSize() {
    return this.__entries.size;
  }
  stats() {
    return {};
  }
}
export class __QinCaffeineBuilder {
  constructor() {
    this.__maximumSize = Infinity;
    this.__removalListener = null;
  }
  maximumSize(value) {
    this.__maximumSize = value;
    return this;
  }
  expireAfterAccess() {
    return this;
  }
  expireAfterWrite() {
    return this;
  }
  removalListener(listener) {
    this.__removalListener = listener;
    return this;
  }
  recordStats() {
    return this;
  }
  build() {
    return new __QinCaffeineCache(this.__maximumSize, this.__removalListener);
  }
}
export const __QinCaffeine = {
  newBuilder() {
    return new __QinCaffeineBuilder();
  }
};
