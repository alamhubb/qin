export default {
    "name":  "com.qin.demo:vue-minimal",
    "version":  "0.1.0",
    "description":  "Minimal Vue SFC app served by Qin without Vite or npm commands",
    "entry":  "main/main.qin",
    "dependencies":  {
                         "com.qin:qin-runtime-core":  "0.1.0",
                         "@vue/compiler-sfc":  "latest",
                         "vue":  "latest",
                         "cssts-compiler":  "0.2.87",
                         "cssts-ts":  "0.2.87"
                     },
    "java":  {
                 "version":  "21",
                 "sourceDir":  "main",
                 "outputDir":  "build/classes",
                 "encoding":  "UTF-8"
             }
}

