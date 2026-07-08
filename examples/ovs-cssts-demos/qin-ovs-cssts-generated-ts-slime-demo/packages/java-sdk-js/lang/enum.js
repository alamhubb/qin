export class __QinJavaLangEnum {
  __qinEnumName = null;
  __qinEnumOrdinal = null;
  constructor() {
  }
  name() {
    return this.__qinEnumName == null ? "" : this.__qinEnumName;
  }
  ordinal() {
    return this.__qinEnumOrdinal == null ? -1 : this.__qinEnumOrdinal;
  }
  toString() {
    return this.name();
  }
}
