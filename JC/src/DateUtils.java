import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String getCurrentDate() {
        return LocalDate.now().format(ISO_DATE);
    }

    public static boolean isBefore(String date1, String date2) {
        return date1.compareTo(date2) < 0;
    }

    public static String addDays(String date, int days) {
        LocalDate d = LocalDate.parse(date, ISO_DATE);
        return d.plusDays(days).format(ISO_DATE);
    }
}