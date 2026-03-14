import profile, { age as profileAge } from "../shared/default-and-named.js";
import * as ns from "../shared/namespace-source.js";
import { valueFromReExport } from "../shared/re-export-named.js";
import { starValue } from "../shared/re-export-star.js";
import { allNs } from "../shared/re-export-namespace.js";

const nsValue = ns.nsValue;
const allNsValue = allNs.nsValue;

console.log(profile.age);
console.log(profileAge.age);
console.log(nsValue.age);
console.log(valueFromReExport.age);
console.log(starValue.age);
console.log(allNsValue.age);

const result = { age: allNsValue.age };
