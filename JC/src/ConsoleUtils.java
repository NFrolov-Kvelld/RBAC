import java.util.*;

public class ConsoleUtils {
    public static String promptString(Scanner scanner, String message, boolean required) {
        while (true) {
            System.out.print(message + ": ");
            String input = scanner.nextLine().trim();
            if (required && input.isEmpty()) {
                System.out.println("Ошибка: поле обязательно для заполнения.");
                continue;
            }
            return input;
        }
    }

    public static boolean promptYesNo(Scanner scanner, String message) {
        System.out.print(message + " (yes/no): ");
        return scanner.nextLine().trim().equalsIgnoreCase("yes");
    }

    public static <T> T promptChoice(Scanner scanner, String message, List<T> options) {
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i).toString());
        }
        int choice = promptInt(scanner, message, 1, options.size());
        return options.get(choice - 1);
    }

    public static int promptInt(Scanner scanner, String message, int min, int max) {
        while (true) {
            System.out.print(message + " (" + min + "-" + max + "): ");
            try {
                int val = Integer.parseInt(scanner.nextLine());
                if (val >= min && val <= max) return val;
            } catch (NumberFormatException ignored) {}
            System.out.println("Ошибка: введите число в диапазоне.");
        }
    }
}