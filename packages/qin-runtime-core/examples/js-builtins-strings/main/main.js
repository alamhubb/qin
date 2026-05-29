const text = "  qin esm  ";
const trimmed = text.trim();

console.log(trimmed);
console.log(trimmed.toUpperCase());
console.log(text.includes("qin"));
console.log(trimmed.startsWith("qin"));
console.log(trimmed.endsWith("esm"));
console.log(trimmed.slice(0, 3));
console.log(trimmed.substring(4));
console.log(JSON.stringify(trimmed.split(" ")));

const names = ["qin", "esm", "java"];
console.log(names.join("-"));
console.log(names.includes("esm"));
console.log(names.indexOf("java"));
console.log(JSON.stringify(names.filter(item => item.includes("a"))));
console.log(names.find(item => item.startsWith("e")));
console.log(names.some(item => item.endsWith("a")));
console.log(names.every(item => item.length >= 3));
console.log(names.at(-1));
