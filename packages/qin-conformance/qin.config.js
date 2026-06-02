export default {
    "name":  "com.qin:qin-conformance",
    "version":  "0.1.0",
    "description":  "Qin Chrome-strict conformance runner",
    "entry":  "src/java/com/qin/conformance/ConformancePackageMarker.java",
    "dependencies":  {
                         "com.qin:qin-runtime-core":  "0.1.0",
                         "com.google.code.gson@gson":  "2.10.1"
                     },
    "java":  {
                 "version":  "21",
                 "sourceDir":  "src/java",
                 "outputDir":  "build/classes",
                 "encoding":  "UTF-8",
                 "release":  "21",
                 "source":  "21",
                 "target":  "21"
             },
    "output":  {
                   "dir":  "build",
                   "jarName":  "qin-conformance.jar"
               }
}

