import { describeNumbers, safeDivide } from "mini-flow-pkg";

const summary = describeNumbers([4, -1, 0, 9]);
const ok = safeDivide(84, 2);
const fail = safeDivide(7, 0);

console.log(summary);
console.log(ok);
console.log(fail);
({ summary, ok, fail });
