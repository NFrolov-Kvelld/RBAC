import java.util.Scanner;
import java.util.List;

public class CommandRegistry {
    public static void registerAll(CommandParser parser) {

        // --- УПРАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯМИ ---

        parser.registerCommand("user-list", "Вывести список всех пользователей", (scanner, system) -> {
            List<User> users = system.getUserManager().findAll();
            String[] headers = {"Username", "Full Name", "Email"};
            List<String[]> rows = users.stream()
                    .map(u -> new String[]{u.username(), u.fullName(), u.email()})
                    .toList();
            System.out.println(FormatUtils.formatTable(headers, rows));
        });

        parser.registerCommand("user-create", "Создать нового пользователя", (scanner, system) -> {
            String un = ConsoleUtils.promptString(scanner, "Введите username", true);
            String fn = ConsoleUtils.promptString(scanner, "Введите полное имя", true);
            String em = ConsoleUtils.promptString(scanner, "Введите email", true);

            if (!ValidationUtils.isValidUsername(un)) {
                System.out.println("Ошибка: Некорректный формат username.");
                return;
            }

            try {
                system.getUserManager().add(User.create(un, fn, em));
                system.getAuditLog().log("USER_CREATE", system.getCurrentUser(), un, "Успешно");
                System.out.println("Пользователь успешно создан.");
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        });

        // --- УПРАВЛЕНИЕ РОЛЯМИ ---

        parser.registerCommand("role-list", "Список всех ролей", (scanner, system) -> {
            List<Role> roles = system.getRoleManager().findAll();
            roles.forEach(r -> System.out.println(r.format()));
        });

        // --- ОТЧЕТЫ И СТАТИСТИКА ---

        parser.registerCommand("stats", "Статистика системы", (scanner, system) -> {
            System.out.println(system.generateStatistics());
        });

        parser.registerCommand("report-matrix", "Матрица прав доступа", (scanner, system) -> {
            ReportGenerator gen = new ReportGenerator();
            String report = gen.generatePermissionMatrix(system.getUserManager(), system.getAssignmentManager());
            System.out.println(report);
        });

        parser.registerCommand("audit-log", "Просмотр журнала аудита", (scanner, system) -> {
            system.getAuditLog().printLog();
        });

        // --- СЛУЖЕБНЫЕ ---

        parser.registerCommand("help", "Справка по командам", (scanner, system) -> {
            parser.printHelp();
        });

        parser.registerCommand("clear", "Очистить экран", (scanner, system) -> {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        });
    }
}