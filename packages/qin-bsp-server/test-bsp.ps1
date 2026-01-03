$request = '{"jsonrpc":"2.0","id":1,"method":"build/initialize","params":{}}'
$classpath = "build\classes;C:\Users\qinky\.qin\libs\com.google.code.gson\gson\gson-2.10.1\gson-2.10.1.jar"
$request | java -cp $classpath com.qin.bsp.BspServer
