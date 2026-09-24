import { __qin_java_time_format__, __qin_java_time_from__, __qin_java_time_now__ } from "../core/runtime.js";

export class __QinJavaTimeFormatDateTimeFormatter {
  __pattern: string = "";
  constructor(pattern) {
    this.__pattern = String(pattern == null ? "" : pattern);
  }
  static ofPattern(pattern) {
    return new __QinJavaTimeFormatDateTimeFormatter(pattern);
  }
  format(value: __QinJavaTimeLocalDateTime) {
    return value == null
      ? __QinJavaTimeLocalDateTime.__formatDate(__qin_java_time_now__(), this.__pattern)
      : value.format(this);
  }
}
export class __QinJavaTimeLocalDateTime {
  __date: any = null;
  constructor(date) {
    this.__date = __qin_java_time_from__(date);
  }
  static now() {
    return new __QinJavaTimeLocalDateTime(__qin_java_time_now__());
  }
  format(formatter: __QinJavaTimeFormatDateTimeFormatter) {
    const pattern = formatter == null ? "" : formatter.__pattern;
    return __QinJavaTimeLocalDateTime.__formatDate(this.__date, pattern);
  }
  static __pad(value, width: number) {
    let text = String(value);
    while (text.length < width) {
      text = "0" + text;
    }
    return text;
  }
  static __formatDate(date: any, pattern: string) {
    return __qin_java_time_format__(date, pattern);
  }
}
