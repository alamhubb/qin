export default {
    "name":  "com.qin:qin-lang-pipeline-cfa",
    "version":  "0.1.0",
    "description":  "Unified Slime AST -\u003e ESM semantics -\u003e JVM Class-File API pipeline",
    "entry":  "src/java/com/qin/lang/pipeline/cfa/CfaPipelinePackageMarker.java",
    "dependencies":  {
                         "com.qin:qin-lang-ir":  "0.1.0",
                         "com.qin:qin-lang-frontend-adapter":  "0.1.0",
                         "com.qin:qin-lang-module-policy":  "0.1.0",
                         "com.qin:qin-lang-module-resolver":  "0.1.0",
                         "com.qin:qin-lang-sema-esm":  "0.1.0",
                         "com.qin:qin-lang-lowering-jvm":  "0.1.0"
                     },
    "java":  {
                 "version":  "25",
                 "sourceDir":  "src/java",
                 "outputDir":  "build/classes",
                 "encoding":  "UTF-8",
                 "release":  "25",
                 "source":  "25",
                 "target":  "25"
             },
    "output":  {
                   "dir":  "build",
                   "jarName":  "qin-lang-pipeline-cfa.jar"
               }
}

