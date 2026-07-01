export default {
  name: "qin-hello-web",
  version: "1.0.0",
  description: "Minimal QinWeb project that returns hello.",
  port: 19131,
  scripts: {
    dev: "..\\..\\..\\qin.bat run",
    start: "..\\..\\..\\qin.bat run",
    check: "..\\..\\..\\qin.bat build --build-only"
  },
  dependencies: {
    "com.qin:qin-runtime-core": "0.1.0",
    "com.qin:qin-web": "0.1.0"
  }
}
