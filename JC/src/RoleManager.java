import java.util.*;

public class RoleManager implements Repository<Role> {
    private final Map<String, Role> rolesById = new HashMap<>();
    private final Map<String, Role> rolesByName = new HashMap<>();

    @Override
    public void add(Role role) {
        if (rolesByName.containsKey(role.getName())) throw new IllegalArgumentException("Role name must be unique");
        rolesById.put(role.getId(), role);
        rolesByName.put(role.getName(), role);
    }

    @Override
    public boolean remove(Role role) {
        rolesByName.remove(role.getName());
        return rolesById.remove(role.getId()) != null;
    }

    @Override public Optional<Role> findById(String id) { return Optional.ofNullable(rolesById.get(id)); }
    public Optional<Role> findByName(String name) { return Optional.ofNullable(rolesByName.get(name)); }
    @Override public List<Role> findAll() { return new ArrayList<>(rolesById.values()); }
    @Override public int count() { return rolesById.size(); }
    @Override public void clear() { rolesById.clear(); rolesByName.clear(); }

    public List<Role> findByFilter(RoleFilter filter) {
        return rolesById.values().stream().filter(filter::test).toList();
    }

    public void addPermissionToRole(String roleName, Permission p) {
        findByName(roleName).ifPresent(r -> r.addPermission(p));
    }

    public List<Role> findRolesWithPermission(String pName, String resource) {
        return rolesById.values().stream()
                .filter(r -> r.hasPermission(pName, resource))
                .toList();
    }
}