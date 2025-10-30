package practicum.ru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatisticApp {
    static {
        System.out.println("=== STATIC INITIALIZER ===");
        System.out.println("Java: " + System.getProperty("java.version"));
        System.out.println("Classpath: " + System.getProperty("java.class.path"));
        System.out.println("User Dir: " + System.getProperty("user.dir"));
        System.out.println("=== END STATIC INITIALIZER ===");
    }

    public static void main(String[] args) {
        System.out.println("=== MAIN METHOD STARTED ===");
        try {
            SpringApplication.run(StatisticApp.class, args);
            System.out.println("=== SPRING STARTED SUCCESSFULLY ===");
        } catch (Exception e) {
            System.err.println("=== SPRING STARTUP FAILED ===");
            e.printStackTrace();
            throw e;
        }
    }
}
