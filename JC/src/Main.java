import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RBACSystem system = new RBACSystem();
        CommandParser parser = new CommandParser();

        system.initialize(); // Создаем админа и роли по умолчанию
        CommandRegistry.registerAll(parser); // Регистрируем все команды (включая отчеты и поиск)

        System.out.println(FormatUtils.formatHeader("Система управления доступом (RBAC)"));

        while (true) {
            System.out.print(system.getCurrentUser() + "@rbac> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;

            parser.parseAndExecute(input, scanner, system);
        }
    }
}