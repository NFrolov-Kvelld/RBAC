import java.util.*;

public class Role {
    private final String id;
    private String name;
    private String description;
    private final Set<Permission> permissions = new HashSet<>();
    private static int idCounter = 1;

    public Role(String name, String description) {
        this.id = "role_" + (idCounter++);
        this.name = name;
        this.description = description;
    }

    public void addPermission(Permission p) { permissions.add(p); }
    public void removePermission(Permission p) { permissions.remove(p); }

    public boolean hasPermission(Permission p) { return permissions.contains(p); }

    public boolean hasPermission(String name, String resource) {
        return permissions.stream().anyMatch(p -> p.name().equals(name) && p.resource().equals(resource));
    }

    public Set<Permission> getPermissions() { return Collections.unmodifiableSet(permissions); }

    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    public String format() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Role: %s [ID: %s]\n", name, id));
        sb.append(String.format("Description: %s\n", description));
        sb.append(String.format("Permissions (%d):", permissions.size()));
        permissions.forEach(p -> sb.append("\n - ").append(p.format()));
        return sb.toString();
    }
}