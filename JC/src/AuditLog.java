import java.util.*;

public class AuditLog {
    public record AuditEntry(String timestamp, String action, String performer, String target, String details) {}

    private final List<AuditEntry> entries = new ArrayList<>();

    public void log(String action, String performer, String target, String details) {
        String now = java.time.LocalDateTime.now().toString();
        entries.add(new AuditEntry(now, action, performer, target, details));
    }

    public void printLog() {
        System.out.println(FormatUtils.formatHeader("ЖУРНАЛ АУДИТА"));
        entries.forEach(e -> System.out.printf("[%s] %s: %s выполнил %s над %s (%s)%n",
                e.timestamp, e.performer, e.action, e.target, e.details));
    }
}