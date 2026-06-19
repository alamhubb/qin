export function __qin_init_enum_value(value, name, ordinal) {
  Object.defineProperty(value, "__qinEnumName", {
    value: "" + name,
    configurable: true
  });
  Object.defineProperty(value, "__qinEnumOrdinal", {
    value: ordinal,
    configurable: true
  });
  return value;
}
