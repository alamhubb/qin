package server;

import com.qin.runtime.spring.QinSpringHostSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Spring Boot host shell. Controller business code is authored in .qin and loaded at startup.
 */
@SpringBootApplication
public class Main {
    private static final String CONTROLLER_SOURCE = "src/server/HelloController.qin";
    private static final String SERVICE_SOURCE = "src/server/HelloService.qin";

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context =
                QinSpringHostSupport.run(
                        Main.class,
                        args,
                        java.nio.file.Path.of(SERVICE_SOURCE),
                        java.nio.file.Path.of(CONTROLLER_SOURCE));
        int port = QinSpringHostSupport.resolvePort(context);

        System.out.println("Qin Spring Boot server started");
        System.out.println("  url: http://localhost:" + port);
        System.out.println("  api: http://localhost:" + port + "/api/hello");
    }
}
