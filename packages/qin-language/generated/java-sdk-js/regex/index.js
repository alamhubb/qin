import {
  __qin_java_regex_pattern_compile__,
  __qin_java_regex_matcher__,
  __qin_java_regex_matcher_region__,
  __qin_java_regex_matcher_reset__,
  __qin_java_regex_matcher_looking_at__,
  __qin_java_regex_matcher_matches__,
  __qin_java_regex_matcher_find__,
  __qin_java_regex_matcher_group__,
  __qin_java_regex_matcher_group_count__,
  __qin_java_regex_matcher_start__,
  __qin_java_regex_matcher_end__,
  __qin_java_regex_matcher_replace_all__,
  __qin_java_regex_matcher_append_replacement__,
  __qin_java_regex_matcher_append_tail__
} from "../core/runtime.js";
import { __QinJavaLangStringBuilder } from "../lang/string-builder.js";

const __QIN_REGEX_CASE_INSENSITIVE: number = 2;
const __QIN_REGEX_MULTILINE: number = 8;
const __QIN_REGEX_DOTALL: number = 32;

export class __QinJavaUtilRegexPattern {
  constructor(source: string, flags: number) {
    this.__source = String(source);
    this.__flags = flags;
    this.__nativePattern = __qin_java_regex_pattern_compile__(this.__source, this.__flags);
  }
  static compile(source: string, flags: number): __QinJavaUtilRegexPattern {
    return new __QinJavaUtilRegexPattern(source, flags);
  }
  static quote(literal: string): string {
    const text = String(literal);
    return "\\Q" + text.replace(/\\E/g, "\\E\\\\E\\Q") + "\\E";
  }
  matcher(input: string): __QinJavaUtilRegexMatcher {
    return new __QinJavaUtilRegexMatcher(this, String(input));
  }
  __nativePatternValue() {
    return this.__nativePattern;
  }
  pattern(): string {
    return this.__source;
  }
  flags(): number {
    return this.__flags;
  }
}
export class __QinJavaUtilRegexMatcher {
  constructor(pattern: __QinJavaUtilRegexPattern, input: string) {
    this.__pattern = pattern;
    this.__nativeMatcher = __qin_java_regex_matcher__(pattern.__nativePatternValue(), input);
  }
  region(start: number, end: number): __QinJavaUtilRegexMatcher {
    __qin_java_regex_matcher_region__(this.__nativeMatcher, start, end);
    return this;
  }
  reset(input: string): __QinJavaUtilRegexMatcher {
    __qin_java_regex_matcher_reset__(this.__nativeMatcher, input);
    return this;
  }
  lookingAt(): boolean {
    return __qin_java_regex_matcher_looking_at__(this.__nativeMatcher);
  }
  matches(): boolean {
    return __qin_java_regex_matcher_matches__(this.__nativeMatcher);
  }
  find(start: number): boolean {
    return __qin_java_regex_matcher_find__(this.__nativeMatcher, start);
  }
  group(index: number) {
    return __qin_java_regex_matcher_group__(this.__nativeMatcher, index);
  }
  groupCount(): number {
    return __qin_java_regex_matcher_group_count__(this.__nativeMatcher);
  }
  start(): number {
    return __qin_java_regex_matcher_start__(this.__nativeMatcher);
  }
  end(): number {
    return __qin_java_regex_matcher_end__(this.__nativeMatcher);
  }
  replaceAll(replacement: string): string {
    return __qin_java_regex_matcher_replace_all__(this.__nativeMatcher, replacement);
  }
  appendReplacement(buffer: __QinJavaLangStringBuilder, replacement: string): __QinJavaUtilRegexMatcher {
    __qin_java_regex_matcher_append_replacement__(this.__nativeMatcher, buffer, replacement);
    return this;
  }
  appendTail(buffer: __QinJavaLangStringBuilder): __QinJavaLangStringBuilder {
    return __qin_java_regex_matcher_append_tail__(this.__nativeMatcher, buffer);
  }
}
