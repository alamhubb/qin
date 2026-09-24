import { __qin_java_hash_key__, __qin_java_hash_key_equals__, __qin_java_values_equal__ } from "../util/hash.js";
import { __qin_array_append__, __qin_array_remove_at__, __qin_array_slice__ } from "../core/runtime.js";

class __QinCaffeineRemovalCauseValue {
  constructor(evicted) {
    this.__evicted = evicted;
  }
  wasEvicted() {
    return this.__evicted;
  }
}
export class __QinCaffeineRemovalCause {
  static SIZE(): __QinCaffeineRemovalCauseValue {
    return new __QinCaffeineRemovalCauseValue(true);
  }
  static EXPLICIT(): __QinCaffeineRemovalCauseValue {
    return new __QinCaffeineRemovalCauseValue(false);
  }
}
class __QinCaffeineCacheEntry {
  constructor(key, value) {
    this.key = key;
    this.value = value;
  }
}
class __QinCaffeineCacheFoundEntry {
  constructor(bucket: any[], index: number, entry: __QinCaffeineCacheEntry) {
    this.bucket = bucket;
    this.index = index;
    this.entry = entry;
  }
}
export class __QinCaffeineCache {
  constructor(maximumSize: number, removalListener) {
    this.__maximumSize = maximumSize;
    this.__removalListener = removalListener;
    this.__bucketHashes = [];
    this.__bucketValues = [];
    this.__order = [];
    this.__size = 0;
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
  __findEntry(key): __QinCaffeineCacheFoundEntry {
    const bucket = this.__bucket(key, false);
    if (bucket == null) {
      return null;
    }
    for (let index = 0; index < bucket.length; index++) {
      const entry: __QinCaffeineCacheEntry = bucket[index];
      if (__qin_java_hash_key_equals__(entry.key, key)) {
        return new __QinCaffeineCacheFoundEntry(bucket, index, entry);
      }
    }
    return null;
  }
  __findOrderIndex(entry: __QinCaffeineCacheEntry): number {
    for (let index = 0; index < this.__order.length; index++) {
      const ordered: __QinCaffeineCacheEntry = this.__order[index];
      if (__qin_java_hash_key_equals__(ordered.key, entry.key)) {
        return index;
      }
    }
    return -1;
  }
  __touch(entry: __QinCaffeineCacheEntry): void {
    const index = this.__findOrderIndex(entry);
    if (index >= 0) {
      this.__order = __qin_array_remove_at__(this.__order, index);
    }
    this.__order = __qin_array_append__(this.__order, entry);
  }
  __removeEntry(bucket: any[], index: number, entry: __QinCaffeineCacheEntry, cause): void {
    this.__setBucketByHash(__qin_java_hash_key__(entry.key), __qin_array_remove_at__(bucket, index));
    const orderIndex = this.__findOrderIndex(entry);
    if (orderIndex >= 0) {
      this.__order = __qin_array_remove_at__(this.__order, orderIndex);
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
      const entry = new __QinCaffeineCacheEntry(key, value);
      const bucket = this.__bucket(key, true);
      this.__setBucketByHash(__qin_java_hash_key__(key), __qin_array_append__(bucket, entry));
      this.__order = __qin_array_append__(this.__order, entry);
      this.__size++;
    }
    while (this.__size > this.__maximumSize) {
      const oldest: __QinCaffeineCacheEntry = this.__order[0];
      const oldestFound = this.__findEntry(oldest.key);
      if (oldestFound == null) {
        this.__order = __qin_array_remove_at__(this.__order, 0);
      } else {
        this.__removeEntry(oldestFound.bucket, oldestFound.index, oldestFound.entry, __QinCaffeineRemovalCause.SIZE());
      }
    }
  }
  invalidate(key) {
    const found = this.__findEntry(key);
    if (found == null) {
      return;
    }
    this.__removeEntry(found.bucket, found.index, found.entry, __QinCaffeineRemovalCause.EXPLICIT());
  }
  invalidateAll() {
    const entries: any[] = __qin_array_slice__(this.__order, 0, this.__order.length);
    for (let index = 0; index < entries.length; index++) {
      const entry: __QinCaffeineCacheEntry = entries[index];
      if (this.__removalListener != null) {
        this.__removalListener(entry.key, entry.value, __QinCaffeineRemovalCause.EXPLICIT());
      }
    }
    this.__bucketHashes = [];
    this.__bucketValues = [];
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
    this.__maximumSize = 9007199254740991;
    this.__removalListener = null;
  }
  maximumSize(value: number): __QinCaffeineBuilder {
    this.__maximumSize = value;
    return this;
  }
  expireAfterAccess(): __QinCaffeineBuilder {
    return this;
  }
  expireAfterWrite(): __QinCaffeineBuilder {
    return this;
  }
  removalListener(listener): __QinCaffeineBuilder {
    this.__removalListener = listener;
    return this;
  }
  recordStats(): __QinCaffeineBuilder {
    return this;
  }
  build(): __QinCaffeineCache {
    return new __QinCaffeineCache(this.__maximumSize, this.__removalListener);
  }
}
export const __QinCaffeine = {
  newBuilder() {
    return new __QinCaffeineBuilder();
  }
};
