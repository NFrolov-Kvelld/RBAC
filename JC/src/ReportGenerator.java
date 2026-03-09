import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ReportGenerator {

    public String generateUserReport(UserManager userManager, AssignmentManager assignmentManager) {
        StringBuilder sb = new StringBuilder();
        sb.append(FormatUtils.formatHeader("Отчёт по пользователям"));

        String[] headers = {"Username", "Full Name", "Active Roles"};
        List<String[]> rows = new ArrayList<>();

        for (User user : userManager.findAll()) {
            String roles = assignmentManager.findAll().stream()
                    .filter(a -> a.user().equals(user) && a.isActive())
                    .map(a -> a.role().getName())
                    .collect(Collectors.joining(", "));

            rows.add(new String[]{user.username(), user.fullName(), roles.isEmpty() ? "None" : roles});
        }

        sb.append(FormatUtils.formatTable(headers, rows));
        return sb.toString();
    }

    public String generatePermissionMatrix(UserManager userManager, AssignmentManager assignmentManager) {
        List<User> users = userManager.findAll();
        Set<String> resources = assignmentManager.findAll().stream()
                .flatMap(a -> a.role().getPermissions().stream())
                .map(Permission::resource)
                .collect(Collectors.toCollection(TreeSet::new));

        StringBuilder sb = new StringBuilder();
        sb.append(FormatUtils.formatHeader("Матрица прав доступа"));

        if (resources.isEmpty()) return sb.append("Нет данных для матрицы.").toString();

        // Заголовки: Username + список ресурсов
        List<String> headerList = new ArrayList<>();
        headerList.add("Username");
        headerList.addAll(resources);

        List<String[]> rows = new ArrayList<>();
        for (User user : users) {
            String[] row = new String[headerList.size()];
            row[0] = user.username();

            Set<Permission> userPerms = assignmentManager.getUserPermissions(user);
            for (int i = 1; i < headerList.size(); i++) {
                String resName = headerList.get(i);
                boolean hasAccess = userPerms.stream().anyMatch(p -> p.resource().equals(resName));
                row[i] = hasAccess ? "[ X ]" : "[   ]";
            }
            rows.add(row);
        }

        sb.append(FormatUtils.formatTable(headerList.toArray(new String[0]), rows));
        return sb.toString();
    }

    public void exportToFile(String report, String filename) {
        try (PrintWriter out = new PrintWriter(filename)) {
            out.println(report);
            System.out.println("Отчёт успешно сохранен в файл: " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении отчёта: " + e.getMessage());
        }
    }
}