import { __qin_java_pattern_regexp__ } from "../core/runtime.js";

export class __QinJavaUtilRegexPattern {
  constructor(source, flags) {
    this.__source = String(source);
    this.__flags = flags == null ? 0 : (flags | 0);
  }
  static compile(source, flags = 0) {
    return new __QinJavaUtilRegexPattern(source, flags);
  }
  static quote(literal) {
    const text = String(literal);
    return "\\Q" + text.replace(/\\E/g, "\\E\\\\E\\Q") + "\\E";
  }
  matcher(input) {
    return new __QinJavaUtilRegexMatcher(this, String(input));
  }
  pattern() {
    return this.__source;
  }
  flags() {
    return this.__flags;
  }
  __jsFlags(extraFlags = "") {
    let flags = "";
    if ((this.__flags & __QinJavaUtilRegexPattern.CASE_INSENSITIVE) !== 0) flags += "i";
    if ((this.__flags & __QinJavaUtilRegexPattern.MULTILINE) !== 0) flags += "m";
    if ((this.__flags & __QinJavaUtilRegexPattern.DOTALL) !== 0) flags += "s";
    for (const ch of String(extraFlags)) {
      if (flags.indexOf(ch) < 0) flags += ch;
    }
    return flags;
  }
  __regexp(extraFlags = "") {
    return __qin_java_pattern_regexp__(this.__source, this.__jsFlags(extraFlags));
  }
}
__QinJavaUtilRegexPattern.UNIX_LINES = 1;
__QinJavaUtilRegexPattern.CASE_INSENSITIVE = 2;
__QinJavaUtilRegexPattern.COMMENTS = 4;
__QinJavaUtilRegexPattern.MULTILINE = 8;
__QinJavaUtilRegexPattern.LITERAL = 16;
__QinJavaUtilRegexPattern.DOTALL = 32;
__QinJavaUtilRegexPattern.UNICODE_CASE = 64;
__QinJavaUtilRegexPattern.CANON_EQ = 128;
__QinJavaUtilRegexPattern.UNICODE_CHARACTER_CLASS = 256;
export class __QinJavaUtilRegexMatcher {
  constructor(pattern, input) {
    this.__pattern = pattern;
    this.__input = String(input);
    this.__regionStart = 0;
    this.__regionEnd = this.__input.length;
    this.__searchIndex = 0;
    this.__lastMatch = null;
    this.__appendPosition = 0;
  }
  static quoteReplacement(text) {
    return String(text).replace(/\\/g, "\\\\").replace(/\$/g, "\\\$");
  }
  region(start, end) {
    this.__regionStart = Math.max(0, start | 0);
    this.__regionEnd = Math.min(this.__input.length, Math.max(this.__regionStart, end | 0));
    this.__searchIndex = this.__regionStart;
    this.__lastMatch = null;
    return this;
  }
  lookingAt() {
    return this.__matchAtRegionStart(false);
  }
  matches() {
    return this.__matchAtRegionStart(true);
  }
  find(start) {
    const from = arguments.length > 0 ? Math.max(this.__regionStart, start | 0) : this.__searchIndex;
    const boundedFrom = Math.min(Math.max(from, this.__regionStart), this.__regionEnd);
    const re = this.__pattern.__regexp();
    const text = this.__input.slice(boundedFrom, this.__regionEnd);
    const match = re.exec(text);
    if (match == null) {
      this.__lastMatch = null;
      this.__searchIndex = this.__regionEnd;
      return false;
    }
    this.__storeMatch(match, boundedFrom + (match.index == null ? 0 : match.index));
    this.__searchIndex = this.__lastMatch.end === this.__lastMatch.start
      ? Math.min(this.__lastMatch.end + 1, this.__regionEnd)
      : this.__lastMatch.end;
    return true;
  }
  group(index = 0) {
    if (this.__lastMatch == null) {
      throw new Error("No match available");
    }
    const value = this.__lastMatch.groups[index | 0];
    return value == null ? null : value;
  }
  groupCount() {
    return this.__lastMatch == null ? 0 : Math.max(0, this.__lastMatch.groups.length - 1);
  }
  start() {
    if (this.__lastMatch == null) throw new Error("No match available");
    return this.__lastMatch.start;
  }
  end() {
    if (this.__lastMatch == null) throw new Error("No match available");
    return this.__lastMatch.end;
  }
  replaceAll(replacement) {
    return this.__input.replace(this.__pattern.__regexp("g"), String(replacement));
  }
  appendReplacement(buffer, replacement) {
    if (this.__lastMatch == null) {
      throw new Error("No match available");
    }
    const text = this.__input.slice(this.__appendPosition, this.__lastMatch.start) + String(replacement);
    this.__append(buffer, text);
    this.__appendPosition = this.__lastMatch.end;
    return this;
  }
  appendTail(buffer) {
    this.__append(buffer, this.__input.slice(this.__appendPosition));
    this.__appendPosition = this.__input.length;
    return buffer;
  }
  __matchAtRegionStart(requireFullRegion) {
    const re = this.__pattern.__regexp("y");
    const text = this.__input.slice(this.__regionStart, this.__regionEnd);
    const match = re.exec(text);
    if (match == null) {
      this.__lastMatch = null;
      return false;
    }
    this.__storeMatch(match, this.__regionStart);
    return !requireFullRegion || this.__lastMatch.end === this.__regionEnd;
  }
  __storeMatch(match, absoluteStart) {
    const groups = [];
    for (let index = 0; index < match.length; index++) {
      groups.push(match[index] == null ? null : match[index]);
    }
    this.__lastMatch = {
      groups,
      start: absoluteStart,
      end: absoluteStart + String(match[0] == null ? "" : match[0]).length
    };
  }
  __append(buffer, text) {
    if (buffer != null && typeof buffer.append === "function") {
      buffer.append(text);
      return;
    }
    throw new TypeError("Matcher append target must support append(value)");
  }
}
