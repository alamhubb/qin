const arr = [];
arr.push(1);
arr.push(2);
console.log(arr.length);
console.log(arr.pop());
console.log(JSON.stringify(arr));

const map = new Map();
map.set("name", "qin");
console.log(map.get("name"));
console.log(map.has("name"));
console.log(map.size);

const set = new Set();
set.add("a");
console.log(set.has("a"));
console.log(set.size);

console.log(JSON.stringify(Object.keys({ age: 1 })));

const parsed = JSON.parse("{\"age\":1}");
console.log(parsed.age);
