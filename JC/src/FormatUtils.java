import java.util.*;

public class FormatUtils {
    public static String formatHeader(String text) {
        return "\n=== " + text.toUpperCase() + " ===\n";
    }

    public static String formatTable(String[] headers, List<String[]> rows) {
        if (rows.isEmpty()) return "Нет данных для отображения.";

        StringBuilder sb = new StringBuilder();
        int[] widths = new int[headers.length];

        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
            for (String[] row : rows) {
                widths[i] = Math.max(widths[i], row[i].length());
            }
        }

        String separator = buildSeparator(widths);
        sb.append(separator).append("\n|");
        for (int i = 0; i < headers.length; i++) {
            sb.append(" ").append(padRight(headers[i], widths[i])).append(" |");
        }
        sb.append("\n").append(separator).append("\n");

        for (String[] row : rows) {
            sb.append("|");
            for (int i = 0; i < row.length; i++) {
                sb.append(" ").append(padRight(row[i], widths[i])).append(" |");
            }
            sb.append("\n");
        }
        sb.append(separator);
        return sb.toString();
    }

    private static String buildSeparator(int[] widths) {
        StringBuilder sb = new StringBuilder("+");
        for (int w : widths) sb.append("-".repeat(w + 2)).append("+");
        return sb.toString();
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
}