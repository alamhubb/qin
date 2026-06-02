export default {
    "name":  "com.qin:qin-lang-module-resolver",
    "version":  "0.1.0",
    "description":  "ESM module graph resolver and linker for Qin source files",
    "entry":  "src/java/com/qin/lang/module/resolver/ModuleResolverPackageMarker.java",
    "dependencies":  {
                         "com.qin:qin-lang-module-policy":  "0.1.0"
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
                   "jarName":  "qin-lang-module-resolver.jar"
               }
}

