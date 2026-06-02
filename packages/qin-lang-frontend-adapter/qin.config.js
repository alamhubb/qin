export default {
    "name":  "com.qin:qin-lang-frontend-adapter",
    "version":  "0.1.0",
    "description":  "Adapter from source frontend to Qin language IR",
    "entry":  "src/java/com/qin/lang/frontend/adapter/QinFrontendLowerer.java",
    "dependencies":  {
                         "com.qin:qin-lang-ir":  "0.1.0",
                         "com.qin:qin-parser":  "0.1.0",
                         "com.slime:slime-parser":  "1.0.0",
                         "com.slime:slime-java":  "0.1.0"
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
                   "jarName":  "qin-lang-frontend-adapter.jar"
               }
}

