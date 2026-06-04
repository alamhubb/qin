package com.qin.parser;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public final class QinParserGeneratedConsumerBisectProbeMain {
    private QinParserGeneratedConsumerBisectProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        check("small", """
                class A {
                  findMethod(clazz, name, params) {
                    return (() => {
                      let key = new B(clazz, name, params);
                      let cached = this.METHOD_CACHE.get(key);
                      let current = clazz;
                      (() => {
                      if (__qin_binary__("!=", cached, null)) {
                        return cached.orElse(null);
                      }
                      return null;
                    })();
                      (() => {
                      while (__qin_binary__("!=", current, null)) {
                      }
                      return null;
                    })();
                      this.METHOD_CACHE.putIfAbsent(key, Optional.empty());
                      return null;
                    })();
                  }
                }
                class B {}
                """);
        check("extends-constructor-super-next-class", """
                class A extends B {
                  constructor() {
                    super();
                    this.METHOD_CACHE = new ConcurrentHashMap();
                  }
                }
                class C {}
                """);
        check("exact-generated-constructor-next-class", """
                class SlimeJavascriptTokenConsumer extends SubhutiTokenConsumer {
                  constructor() {
                    super();
                    this.METHOD_CACHE = new ConcurrentHashMap();
                  }
                }
                class SlimeJavascriptTokenConsumer$MethodKey {}
                """);
        check("instanceof-comma-arrow-block", """
                class A {
                  equals(obj) {
                    return (() => { const value = obj; return value instanceof B && (obj = value, true); })();
                  }
                }
                class B {}
                """);
        check("nested-iife-if-equals-shape", """
                class A {
                  equals(obj) {
                    return (() => {
                      let other = null;
                      (() => {
                        if ((() => {
                          if ((() => { const value = obj; return value instanceof B && (other = value, true); })()) {
                            return false;
                          }
                          return true;
                        })()) {
                          return false;
                        }
                        return null;
                      })();
                      return true;
                    })();
                  }
                }
                class B {}
                """);
        check("two-classes-equals-first-guard-only", """
                class A {
                  constructor() {
                    super();
                    this.METHOD_CACHE = new ConcurrentHashMap();
                  }
                }
                class B {
                  equals(obj) {
                    return (() => {
                      let other = null;
                      (() => {
                        if (__qin_binary__("==", this, obj)) {
                          return true;
                        }
                        return null;
                      })();
                      return true;
                    })();
                  }
                }
                """);
        check("two-classes-equals-first-two-guards", """
                class A {
                  constructor() {
                    super();
                    this.METHOD_CACHE = new ConcurrentHashMap();
                  }
                }
                class B {
                  equals(obj) {
                    return (() => {
                      let other = null;
                      (() => {
                        if (__qin_binary__("==", this, obj)) {
                          return true;
                        }
                        return null;
                      })();
                      (() => {
                        if ((() => {
                          if ((() => { const __qin_pattern_value = obj; return __qin_pattern_value instanceof B && (other = __qin_pattern_value, true); })()) {
                            return false;
                          }
                          return true;
                        })()) {
                          return false;
                        }
                        return null;
                      })();
                      return true;
                    })();
                  }
                }
                """);
        check("two-classes-equals-final-return-only", """
                class A {
                  constructor() {
                    super();
                    this.METHOD_CACHE = new ConcurrentHashMap();
                  }
                }
                class B {
                  equals(obj) {
                    return (() => {
                      let other = obj;
                      return (() => {
                        if ((() => {
                          if (this.owner.equals(other.owner)) {
                            return this.name.equals(other.name);
                          }
                          return false;
                        })()) {
                          return Arrays.equals(this.params, other.params);
                        }
                        return false;
                      })();
                    })();
                  }
                }
                """);
        check("two-classes-equals-full", """
                class A {
                  constructor() {
                    super();
                    this.METHOD_CACHE = new ConcurrentHashMap();
                  }
                }
                class B {
                  equals(obj) {
                    return (() => {
                      let other = null;
                      (() => {
                        if (__qin_binary__("==", this, obj)) {
                          return true;
                        }
                        return null;
                      })();
                      (() => {
                        if ((() => {
                          if ((() => { const __qin_pattern_value = obj; return __qin_pattern_value instanceof B && (other = __qin_pattern_value, true); })()) {
                            return false;
                          }
                          return true;
                        })()) {
                          return false;
                        }
                        return null;
                      })();
                      return (() => {
                        if ((() => {
                          if (this.owner.equals(other.owner)) {
                            return this.name.equals(other.name);
                          }
                          return false;
                        })()) {
                          return Arrays.equals(this.params, other.params);
                        }
                        return false;
                      })();
                    })();
                  }
                }
                """);
        check("token-methods", """
                class A {
                  Await() { return this.consume("Await"); }
                  Break() { return this.consume("Break"); }
                  Case() { return this.consume("Case"); }
                  Catch() { return this.consume("Catch"); }
                  Class() { return this.consume("Class"); }
                  Const() { return this.consume("Const"); }
                  Continue() { return this.consume("Continue"); }
                  Debugger() { return this.consume("Debugger"); }
                  Default() { return this.consume("Default"); }
                  Do() { return this.consume("Do"); }
                  Else() { return this.consume("Else"); }
                  Enum() { return this.consume("Enum"); }
                  Export() { return this.consume("Export"); }
                  Extends() { return this.consume("Extends"); }
                  False() { return this.consume("False"); }
                  Finally() { return this.consume("Finally"); }
                  For() { return this.consume("For"); }
                  Function() { return this.consume("Function"); }
                  If() { return this.consume("If"); }
                  Import() { return this.consume("Import"); }
                  New() { return this.consume("New"); }
                  NullLiteral() { return this.consume("NullLiteral"); }
                  Return() { return this.consume("Return"); }
                  Super() { return this.consume("Super"); }
                  Switch() { return this.consume("Switch"); }
                  This() { return this.consume("This"); }
                  Throw() { return this.consume("Throw"); }
                  True() { return this.consume("True"); }
                  Try() { return this.consume("Try"); }
                  Var() { return this.consume("Var"); }
                  While() { return this.consume("While"); }
                  With() { return this.consume("With"); }
                  Yield() { return this.consume("Yield"); }
                  Void() { return this.consume("Void"); }
                  Typeof() { return this.consume("Typeof"); }
                  In() { return this.consume("In"); }
                  Instanceof() { return this.consume("Instanceof"); }
                  Delete() { return this.consume("Delete"); }
                  Let() { return this.consumeIdentifierValue("let"); }
                  Async() { return this.consumeIdentifierValue("async"); }
                  Static() { return this.consumeIdentifierValue("static"); }
                  As() { return this.consumeIdentifierValue("as"); }
                  Get() { return this.consumeIdentifierValue("get"); }
                  Set() { return this.consumeIdentifierValue("set"); }
                  Of() { return this.consumeIdentifierValue("of"); }
                  Target() { return this.consumeIdentifierValue("target"); }
                  Meta() { return this.consumeIdentifierValue("meta"); }
                  From() { return this.consumeIdentifierValue("from"); }
                  NumericLiteral() { return this.consume("NumericLiteral"); }
                  StringLiteral() { return this.consume("StringLiteral"); }
                  IdentifierName() { return this.consume("IdentifierName"); }
                  PrivateIdentifier() { return this.consume("PrivateIdentifier"); }
                  RegularExpressionLiteral() { return this.consume("RegularExpressionLiteral"); }
                  Template() { return this.consume("Template"); }
                  NoSubstitutionTemplate() { return this.consume("NoSubstitutionTemplate"); }
                  TemplateHead() { return this.consume("TemplateHead"); }
                  TemplateMiddle() { return this.consume("TemplateMiddle"); }
                  TemplateTail() { return this.consume("TemplateTail"); }
                  LBrace() { return this.consume("LBrace"); }
                  RBrace() { return this.consume("RBrace"); }
                  LParen() { return this.consume("LParen"); }
                  RParen() { return this.consume("RParen"); }
                  Assign() { return this.consume("Assign"); }
                }
                class B {}
                """);
        Path bundle = Path.of(".qin", "generated", "slime-parser", "slime-parser.bundle.js");
        if (Files.exists(bundle)) {
            String source = Files.readString(bundle, StandardCharsets.UTF_8);
            String firstClass = extractClass(source, "class SlimeJavascriptTokenConsumer ");
            String methodKeyClass = extractClass(source, "class SlimeJavascriptTokenConsumer$MethodKey ");
            bisectGeneratedConsumer(firstClass, methodKeyClass);
            bisectGeneratedMethodKey(firstClass, methodKeyClass);
            check("generated-token-consumer-and-method-key", firstClass + "\n" + methodKeyClass);
        }
        System.out.println("QinParserGeneratedConsumerBisectProbeMain OK");
    }

    private static void check(String label, String source) {
        try {
            new QinParserFacade().parseSource(source);
        } catch (RuntimeException e) {
            throw new IllegalStateException("Failed generated consumer probe: " + label, e);
        }
    }

    private static String extractClass(String source, String marker) {
        int start = source.indexOf(marker);
        if (start < 0) {
            throw new IllegalStateException("Missing marker: " + marker);
        }
        int bodyStart = source.indexOf('{', start);
        int depth = 0;
        for (int i = bodyStart; i < source.length(); i++) {
            char ch = source.charAt(i);
            if (ch == '{') {
                depth++;
            } else if (ch == '}') {
                depth--;
                if (depth == 0) {
                    return source.substring(start, i + 1);
                }
            }
        }
        throw new IllegalStateException("Unclosed class: " + marker);
    }

    private static void bisectGeneratedConsumer(String firstClass, String methodKeyClass) {
        int headerEnd = firstClass.indexOf('{') + 1;
        int classEnd = firstClass.lastIndexOf('}');
        String body = firstClass.substring(headerEnd, classEnd);
        java.util.List<String> members = splitMembers(body);
        int low = 0;
        int high = members.size();
        while (low < high) {
            int mid = (low + high) / 2;
            String candidate = firstClass.substring(0, headerEnd) + "\n"
                    + String.join("\n", members.subList(0, mid + 1))
                    + "\n}\n"
                    + methodKeyClass;
            if (parses(candidate)) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        if (low < members.size()) {
            String member = members.get(low).stripLeading();
            int nameEnd = member.indexOf('(');
            String name = nameEnd > 0 ? member.substring(0, nameEnd) : member.substring(0, Math.min(60, member.length()));
            System.out.println("first failing generated member index=" + low + " name=" + name);
        }
    }

    private static java.util.List<String> splitMembers(String body) {
        java.util.List<String> members = new java.util.ArrayList<>();
        int index = 0;
        while (index < body.length()) {
            while (index < body.length() && Character.isWhitespace(body.charAt(index))) {
                index++;
            }
            if (index >= body.length()) {
                break;
            }
            int memberStart = index;
            int brace = body.indexOf('{', memberStart);
            int depth = 0;
            for (int i = brace; i < body.length(); i++) {
                char ch = body.charAt(i);
                if (ch == '{') {
                    depth++;
                } else if (ch == '}') {
                    depth--;
                    if (depth == 0) {
                        members.add(body.substring(memberStart, i + 1));
                        index = i + 1;
                        break;
                    }
                }
            }
        }
        return members;
    }

    private static boolean parses(String source) {
        try {
            new QinParserFacade().parseSource(source);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private static void bisectGeneratedMethodKey(String firstClass, String methodKeyClass) {
        int firstHeaderEnd = firstClass.indexOf('{') + 1;
        java.util.List<String> firstMembers = splitMembers(firstClass.substring(firstHeaderEnd, firstClass.lastIndexOf('}')));
        String firstConstructorOnly = firstClass.substring(0, firstHeaderEnd) + "\n"
                + firstMembers.get(0)
                + "\n}";

        int headerEnd = methodKeyClass.indexOf('{') + 1;
        int classEnd = methodKeyClass.lastIndexOf('}');
        java.util.List<String> members = splitMembers(methodKeyClass.substring(headerEnd, classEnd));
        int low = 0;
        int high = members.size();
        while (low < high) {
            int mid = (low + high) / 2;
            String candidate = firstConstructorOnly + "\n"
                    + methodKeyClass.substring(0, headerEnd) + "\n"
                    + String.join("\n", members.subList(0, mid + 1))
                    + "\n}";
            if (parses(candidate)) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        if (low < members.size()) {
            String member = members.get(low).stripLeading();
            int nameEnd = member.indexOf('(');
            String name = nameEnd > 0 ? member.substring(0, nameEnd) : member.substring(0, Math.min(60, member.length()));
            System.out.println("first failing method-key member index=" + low + " name=" + name);
        }
    }

}
