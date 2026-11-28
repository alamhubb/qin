export default {
    "name":  "com.qin:qin-parser",
    "type": "library",
    "version":  "0.1.0",
    "description":  "Qin language parser frontend built on top of java-slime",
    "entry":  "src/java/com/qin/parser/QinParserFacade.java",
    "dependencies":  {
                         "com.qin:qin-lang-ir":  "0.1.0"
                     },
    "scripts": { "build": "python -X utf8 build-chevrotain-parser.py" },
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
                   "jarName":  "qin-parser.jar"
               }
}

