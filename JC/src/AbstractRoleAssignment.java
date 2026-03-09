import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;


public abstract class AbstractRoleAssignment implements RoleAssignment {
    protected final String assignmentId;
    protected final User user;
    protected final Role role;
    protected final AssignmentMetadata metadata;

    public AbstractRoleAssignment(User user, Role role, AssignmentMetadata metadata) {
        this.assignmentId = UUID.randomUUID().toString().substring(0, 8);
        this.user = user;
        this.role = role;
        this.metadata = metadata;
    }

    @Override public String assignmentId() { return assignmentId; }
    @Override public User user() { return user; }
    @Override public Role role() { return role; }
    @Override public AssignmentMetadata metadata() { return metadata; }

    public String summary() {
        return String.format("[%s] %s assigned to %s %s\nStatus: %s",
                assignmentType(), role.getName(), user.username(), metadata.format(), (isActive() ? "ACTIVE" : "INACTIVE"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractRoleAssignment that)) return false;
        return Objects.equals(assignmentId, that.assignmentId);
    }

    @Override
    public int hashCode() { return Objects.hash(assignmentId); }
}

// Постоянное назначение
class PermanentAssignment extends AbstractRoleAssignment {
    private boolean revoked = false;

    public PermanentAssignment(User user, Role role, AssignmentMetadata metadata) {
        super(user, role, metadata);
    }

    public void revoke() { this.revoked = true; }
    @Override public boolean isActive() { return !revoked; }
    @Override public String assignmentType() { return "PERMANENT"; }
}

// Временное назначение
class TemporaryAssignment extends AbstractRoleAssignment {
    private String expiresAt; // Формат "yyyy-MM-dd HH:mm"

    public TemporaryAssignment(User user, Role role, AssignmentMetadata metadata, String expiresAt) {
        super(user, role, metadata);
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean isActive() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return now.compareTo(expiresAt) <= 0;
    }

    @Override public String assignmentType() { return "TEMPORARY"; }
    public void extend(String newDate) { this.expiresAt = newDate; }

    @Override
    public String summary() {
        return super.summary() + "\nExpires at: " + expiresAt;
    }
}