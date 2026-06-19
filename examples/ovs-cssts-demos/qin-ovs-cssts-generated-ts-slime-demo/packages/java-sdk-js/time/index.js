export class __QinJavaTimeFormatDateTimeFormatter {
  constructor(pattern) {
    this.__pattern = String(pattern == null ? "" : pattern);
  }
  static ofPattern(pattern) {
    return new __QinJavaTimeFormatDateTimeFormatter(pattern);
  }
  format(value) {
    if (value != null && typeof value.format === "function") {
      return value.format(this);
    }
    return __QinJavaTimeLocalDateTime.__formatDate(new Date(), this.__pattern);
  }
}
export class __QinJavaTimeLocalDateTime {
  constructor(date) {
    this.__date = date instanceof Date ? date : new Date(date == null ? Date.now() : date);
  }
  static now() {
    const fixed = globalThis.__qinJavaFixedNow;
    return new __QinJavaTimeLocalDateTime(fixed == null ? new Date() : new Date(fixed));
  }
  format(formatter) {
    const pattern = formatter == null ? "" : formatter.__pattern;
    return __QinJavaTimeLocalDateTime.__formatDate(this.__date, pattern);
  }
  static __pad(value, width) {
    let text = String(value);
    while (text.length < width) {
      text = "0" + text;
    }
    return text;
  }
  static __formatDate(date, pattern) {
    const tokens = {
      yyyy: String(date.getFullYear()),
      MM: __QinJavaTimeLocalDateTime.__pad(date.getMonth() + 1, 2),
      dd: __QinJavaTimeLocalDateTime.__pad(date.getDate(), 2),
      HH: __QinJavaTimeLocalDateTime.__pad(date.getHours(), 2),
      mm: __QinJavaTimeLocalDateTime.__pad(date.getMinutes(), 2),
      ss: __QinJavaTimeLocalDateTime.__pad(date.getSeconds(), 2)
    };
    let out = String(pattern == null || pattern.length === 0 ? "yyyy-MM-ddTHH:mm:ss" : pattern);
    out = out.split("yyyy").join(tokens.yyyy);
    out = out.split("MM").join(tokens.MM);
    out = out.split("dd").join(tokens.dd);
    out = out.split("HH").join(tokens.HH);
    out = out.split("mm").join(tokens.mm);
    out = out.split("ss").join(tokens.ss);
    return out;
  }
}
