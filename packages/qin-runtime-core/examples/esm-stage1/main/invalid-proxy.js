const proxy = new Proxy({}, {
  get(_, prop) {
    return prop;
  }
});
console.log(proxy.demo);
