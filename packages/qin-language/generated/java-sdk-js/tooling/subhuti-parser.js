export function __qin_subhuti_parser_create(parserClass, ...args) {
  if (parserClass == null) {
    throw new Error("Subhuti parser class cannot be null");
  }
  if (typeof parserClass.getDeclaredConstructor === "function") {
    return parserClass.getDeclaredConstructor().newInstance(...args);
  }
  if (typeof parserClass === "function") {
    return new parserClass(...args);
  }
  throw new Error("Unsupported Subhuti parser class metadata");
}