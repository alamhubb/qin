import com.qin.runtime.core.QinHttpApp;
import com.qin.runtime.core.QinHttpResponse;

public final class Main {
    private static final QinHttpApp APP = QinHttpApp.create()
            .get("/api/message", request -> QinHttpResponse.json("""
                    {
                      "message": "Hello from the Qin backend in the same JVM process."
                    }
                    """));

    private Main() {
    }

    public static Object run() {
        return "qin-fullstack-ready";
    }

    public static QinHttpApp app() {
        return APP;
    }
}
