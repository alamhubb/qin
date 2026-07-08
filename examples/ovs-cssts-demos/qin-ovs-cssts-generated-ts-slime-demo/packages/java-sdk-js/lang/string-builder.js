export class __QinJavaLangStringBuilder {
  constructor(initialValue) {
    this.__text = "";
    if (initialValue != null) {
      this.__text += String(initialValue);
    }
  }
  append(value) {
    this.__text += String(value);
    return this;
  }
  length() {
    return this.__text.length;
  }
  isEmpty() {
    return this.__text.length === 0;
  }
  charAt(index) {
    return this.__text.charAt(index);
  }
  setLength(length) {
    this.__text = this.__text.slice(0, length);
  }
  toString() {
    return this.__text;
  }
}
