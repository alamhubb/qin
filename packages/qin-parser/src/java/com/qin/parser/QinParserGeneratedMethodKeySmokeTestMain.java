package com.qin.parser;

public final class QinParserGeneratedMethodKeySmokeTestMain {
    private QinParserGeneratedMethodKeySmokeTestMain() {
    }

    public static void main(String[] args) {
        String source = """
                class SlimeJavascriptTokenConsumer$MethodKey {
                  constructor() {
                    this.owner = null;
                    this.name = null;
                    this.params = null;
                    (() => {
                      this.params = __qin_binary__("==", this.params, null);
                      return null;
                    })();
                  }
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
                      if ((() => { const __qin_pattern_value = obj; return __qin_pattern_value instanceof SlimeJavascriptTokenConsumer$MethodKey && (other = __qin_pattern_value, true); })()) {
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
                  hashCode() {
                    return (() => {
                      let result = this.owner.hashCode();
                      result = __qin_binary__("+", __qin_binary__("*", 31.0, result), this.name.hashCode());
                      result = __qin_binary__("+", __qin_binary__("*", 31.0, result), Arrays.hashCode(this.params));
                      return result;
                    })();
                  }
                }
                """;

        new QinParserFacade().parseSource(source);
        System.out.println("QinParserGeneratedMethodKeySmokeTestMain OK");
    }
}
