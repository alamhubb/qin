import com.qin.runtime.core.QinApplication;

public final class Application {
    private Application() {
    }

    public static void main(String[] args) throws Exception {
        QinApplication.run(Application.class, args);
    }
}
