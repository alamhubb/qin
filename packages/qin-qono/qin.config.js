export default {
    "name": "com.qin:qin-qono",
    "version": "0.1.0",
    "description": "Qono - Qin-owned lightweight HTTP and RPC layer",
    "entry": "src/java/com/qin/qono/Qono.java",
    "dependencies": {
        "com.qin:qin-runtime-core": "0.1.0"
    },
    "java": {
        "version": "25",
        "sourceDir": "src/java",
        "outputDir": "build/classes",
        "encoding": "UTF-8",
        "release": "25",
        "source": "25",
        "target": "25"
    },
    "output": {
        "dir": "build",
        "jarName": "qin-qono.jar"
    }
}
