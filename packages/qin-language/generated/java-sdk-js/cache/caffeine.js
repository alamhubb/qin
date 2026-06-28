import { __qin_java_hash_key__, __qin_java_hash_key_equals__ } from "../util/hash.js";

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
    this.__buckets = new Map();
    this.__order = [];
    this.__size = 0;
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
  __touch(entry) {
    const index = this.__order.indexOf(entry);
    if (index >= 0) {
      this.__order.splice(index, 1);
    }
    this.__order.push(entry);
  }
  __removeEntry(bucket, index, entry, cause) {
    bucket.splice(index, 1);
    const orderIndex = this.__order.indexOf(entry);
    if (orderIndex >= 0) {
      this.__order.splice(orderIndex, 1);
    }
    this.__size--;
    if (this.__removalListener != null) {
      this.__removalListener(entry.key, entry.value, cause);
    }
  }
  getIfPresent(key) {
    const found = this.__findEntry(key);
    if (found == null) {
      return null;
    }
    this.__touch(found.entry);
    return found.entry.value;
  }
  put(key, value) {
    const found = this.__findEntry(key);
    if (found != null) {
      found.entry.value = value;
      this.__touch(found.entry);
    } else {
      const entry = { key, value };
      this.__bucket(key, true).push(entry);
      this.__order.push(entry);
      this.__size++;
    }
    while (this.__size > this.__maximumSize) {
      const oldest = this.__order[0];
      const oldestFound = this.__findEntry(oldest.key);
      if (oldestFound == null) {
        this.__order.shift();
      } else {
        this.__removeEntry(oldestFound.bucket, oldestFound.index, oldestFound.entry, __QinCaffeineRemovalCause.SIZE);
      }
    }
  }
  invalidate(key) {
    const found = this.__findEntry(key);
    if (found == null) {
      return;
    }
    this.__removeEntry(found.bucket, found.index, found.entry, __QinCaffeineRemovalCause.EXPLICIT);
  }
  invalidateAll() {
    for (const entry of Array.from(this.__order)) {
      if (this.__removalListener != null) {
        this.__removalListener(entry.key, entry.value, __QinCaffeineRemovalCause.EXPLICIT);
      }
    }
    this.__buckets.clear();
    this.__order = [];
    this.__size = 0;
  }
  estimatedSize() {
    return this.__size;
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
