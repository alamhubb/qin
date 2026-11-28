import metadata, { version, enabled, nested } from "../package.json";

if (metadata.version !== "0.45.4" || version !== metadata.version) {
  throw new Error("JSON module exports did not preserve package metadata");
}

if (!enabled || nested.count !== 3) {
  throw new Error("JSON module exports did not preserve nested JSON values");
}

console.log("Qin JSON module runtime OK");
