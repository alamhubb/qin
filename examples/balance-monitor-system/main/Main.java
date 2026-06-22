import com.qin.qono.Qono;
import com.qin.runtime.core.QinHttpApp;

public final class Main {
    private static final BalanceMonitorService SERVICE = new BalanceMonitorService();
    private static final QinHttpApp APP = Qono.create()
            .health()
            .get("/api/config", request -> Qono.jsonRaw(SERVICE.configJson()))
            .get("/api/balances", request -> Qono.jsonRaw(SERVICE.balanceReportJson()))
            .toHttpApp();

    private Main() {
    }

    public static Object run() {
        return "balance-monitor-system";
    }

    public static QinHttpApp app() {
        return APP;
    }
}
