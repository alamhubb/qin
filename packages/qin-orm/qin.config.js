export default {
    "name": "com.qin:qin-orm",
    "type": "library",
    "version": "0.1.0",
    "description": "Qin entity-first ORM query API backed by Jakarta Persistence",
    "entry": "src/java/com/qin/orm/QinOrm.java",
    "dependencies": {
        "jakarta.persistence:jakarta.persistence-api": "3.2.0"
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
        "jarName": "qin-orm.jar"
    }
}
