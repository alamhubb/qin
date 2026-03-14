import profile, { age as profileAge } from "../shared/default-and-named.js";
import * as ns from "../shared/namespace-source.js";
import { valueFromReExport } from "../shared/re-export-named.js";
import { starValue } from "../shared/re-export-star.js";
import { allNs } from "../shared/re-export-namespace.js";

const result = { age: 1 };
console.log(result.age);

