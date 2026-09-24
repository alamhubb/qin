export function __qin_init_enum_value(value, name, ordinal) {
  const hostInit = globalThis.__qin_init_enum_value;
  if (typeof hostInit === "function" && hostInit !== __qin_init_enum_value) {
    return hostInit(value, name, ordinal);
  }
  if (value == null) {
    return value;
  }
  value.__qinEnumName = "" + name;
  value.__qinEnumOrdinal = ordinal;
  let registry = globalThis.__qinGeneratedJavaEnumValues;
  if (registry == null) {
    registry = new Map();
    globalThis.__qinGeneratedJavaEnumValues = registry;
  }
  const owner = __qin_generated_java_enum_class_owner__(value);
  const key = "" + name + "::" + ordinal;
  let ownerValues = registry.get(owner);
  if (ownerValues == null) {
    ownerValues = new Map();
    registry.set(owner, ownerValues);
  }
  if (ownerValues.has(key)) {
    return ownerValues.get(key);
  }
  ownerValues.set(key, value);
  return value;
}

function __qin_generated_java_enum_class_owner__(value) {
  if (value != null && typeof value.getClass === "function") {
    const runtimeClass = value.getClass();
    if (runtimeClass != null) return runtimeClass;
  }
  return value == null || value.constructor == null ? Object : value.constructor;
}
