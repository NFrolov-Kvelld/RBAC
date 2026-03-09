import java.util.*;
import java.util.stream.Collectors;

public class AssignmentManager implements Repository<RoleAssignment> {
    private final Map<String, RoleAssignment> assignments = new HashMap<>();

    @Override
    public void add(RoleAssignment a) {
        boolean duplicate = assignments.values().stream()
                .anyMatch(ex -> ex.isActive() && ex.user().equals(a.user()) && ex.role().equals(a.role()));
        if (duplicate) throw new IllegalStateException("User already has this active role");
        assignments.put(a.assignmentId(), a);
    }

    @Override public boolean remove(RoleAssignment a) { return assignments.remove(a.assignmentId()) != null; }
    @Override public Optional<RoleAssignment> findById(String id) { return Optional.ofNullable(assignments.get(id)); }
    @Override public List<RoleAssignment> findAll() { return new ArrayList<>(assignments.values()); }
    @Override public int count() { return assignments.size(); }
    @Override public void clear() { assignments.clear(); }

    public Set<Permission> getUserPermissions(User user) {
        return assignments.values().stream()
                .filter(a -> a.user().equals(user) && a.isActive())
                .flatMap(a -> a.role().getPermissions().stream())
                .collect(Collectors.toSet());
    }

    public void revokeAssignment(String id) {
        RoleAssignment a = assignments.get(id);
        if (a instanceof PermanentAssignment pa) pa.revoke();
    }

    public List<RoleAssignment> findByFilter(AssignmentFilter f) {
        return assignments.values().stream().filter(f::test).toList();
    }
}