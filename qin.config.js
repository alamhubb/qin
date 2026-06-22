export default {
    "name":  "com.qin:qin",
    "version":  "0.1.0",
    "description":  "Qin - native fullstack build tool with zero XML configuration",
    "entry":  "src/com/qin/cli/QinCli.java",
    "dependencies":  {
                         "com.qin:qin-conformance":  "0.1.0",
                         "com.qin:qin-qono":  "0.1.0",
                         "com.google.code.gson@gson":  "2.10.1",
                         "io.get-coursier@coursier_2.13":  "2.1.10"
                     },
    "java":  {
                 "version":  "21",
                 "release":  "21",
                 "target":  "21",
                 "sourceDir":  "src",
                 "outputDir":  "build/classes",
                 "encoding":  "UTF-8"
             },
    "output":  {
                   "dir":  "dist",
                   "jarName":  "qin.jar",
                   "fatJar":  true
               },
    "repositories":  [
                         {
                             "id":  "aliyun",
                             "url":  "https://maven.aliyun.com/repository/public"
                         },
                         {
                             "id":  "central",
                             "url":  "https://repo1.maven.org/maven2"
                         }
                     ]
}

