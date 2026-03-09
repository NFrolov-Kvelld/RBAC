import java.util.*;

public class RBACSystem {
    private final UserManager userManager = new UserManager();
    private final RoleManager roleManager = new RoleManager();
    private final AssignmentManager assignmentManager = new AssignmentManager();

    private final AuditLog auditLog = new AuditLog();

    private String currentUser = "system_admin";

    public UserManager getUserManager() { return userManager; }
    public RoleManager getRoleManager() { return roleManager; }
    public AssignmentManager getAssignmentManager() { return assignmentManager; }
    public AuditLog getAuditLog() { return auditLog; }

    public void setCurrentUser(String username) { this.currentUser = username; }
    public String getCurrentUser() { return currentUser; }

    public void initialize() {
        Permission read = new Permission("READ", "system", "Просмотр системы");
        Permission write = new Permission("WRITE", "system", "Изменение системы");

        Role adminRole = new Role("Admin", "Полный доступ");
        adminRole.addPermission(read);
        adminRole.addPermission(write);

        Role viewerRole = new Role("Viewer", "Только чтение");
        viewerRole.addPermission(read);

        roleManager.add(adminRole);
        roleManager.add(viewerRole);

        User admin = User.create("admin", "System Administrator", "admin@system.local");
        userManager.add(admin);

        AssignmentMetadata meta = AssignmentMetadata.now(currentUser, "Начальная настройка");
        assignmentManager.add(new PermanentAssignment(admin, adminRole, meta));

        auditLog.log("SYSTEM_INIT", "SYSTEM", "ALL", "Начальные данные созданы");
    }

    public String generateStatistics() {
        return String.format("""
            | Пользователей: %-22d |
            | Ролей:         %-22d |
            | Назначений:    %-22d |
            """,
                userManager.count(), roleManager.count(), assignmentManager.count());
    }
}