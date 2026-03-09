
@FunctionalInterface
interface UserFilter {
    boolean test(User user);

    // Метод для комбинирования (логическое И)
    default UserFilter and(UserFilter other) {
        return user -> this.test(user) && other.test(user);
    }

    // Метод для комбинирования (логическое ИЛИ)
    default UserFilter or(UserFilter other) {
        return user -> this.test(user) || other.test(user);
    }
}

class UserFilters {
    public static UserFilter byUsername(String username) {
        return user -> user.username().equals(username);
    }

    public static UserFilter byUsernameContains(String substring) {
        return user -> user.username().toLowerCase().contains(substring.toLowerCase());
    }

    public static UserFilter byEmail(String email) {
        return user -> user.email().equalsIgnoreCase(email);
    }

    public static UserFilter byEmailDomain(String domain) {
        return user -> user.email().endsWith(domain);
    }

    public static UserFilter byFullNameContains(String substring) {
        return user -> user.fullName().toLowerCase().contains(substring.toLowerCase());
    }
}


@FunctionalInterface
interface RoleFilter {
    boolean test(Role role);

    default RoleFilter and(RoleFilter other) {
        return role -> this.test(role) && other.test(role);
    }

    default RoleFilter or(RoleFilter other) {
        return role -> this.test(role) || other.test(role);
    }
}

class RoleFilters {
    public static RoleFilter byName(String name) {
        return role -> role.getName().equalsIgnoreCase(name);
    }

    public static RoleFilter byNameContains(String substring) {
        return role -> role.getName().toLowerCase().contains(substring.toLowerCase());
    }

    public static RoleFilter hasPermission(Permission permission) {
        return role -> role.hasPermission(permission);
    }

    public static RoleFilter hasPermission(String permissionName, String resource) {
        return role -> role.hasPermission(permissionName, resource);
    }

    public static RoleFilter hasAtLeastNPermissions(int n) {
        return role -> role.getPermissions().size() >= n;
    }
}

@FunctionalInterface
interface AssignmentFilter {
    boolean test(RoleAssignment assignment);

    default AssignmentFilter and(AssignmentFilter other) {
        return a -> this.test(a) && other.test(a);
    }

    default AssignmentFilter or(AssignmentFilter other) {
        return a -> this.test(a) || other.test(a);
    }
}

class AssignmentFilters {
    public static AssignmentFilter byUser(User user) {
        return a -> a.user().equals(user);
    }

    public static AssignmentFilter byUsername(String username) {
        return a -> a.user().username().equals(username);
    }

    public static AssignmentFilter byRole(Role role) {
        return a -> a.role().equals(role);
    }

    public static AssignmentFilter byRoleName(String roleName) {
        return a -> a.role().getName().equalsIgnoreCase(roleName);
    }

    public static AssignmentFilter activeOnly() {
        return RoleAssignment::isActive;
    }

    public static AssignmentFilter inactiveOnly() {
        return a -> !a.isActive();
    }

    public static AssignmentFilter byType(String type) {
        return a -> a.assignmentType().equals(type);
    }

    public static AssignmentFilter assignedBy(String username) {
        return a -> a.metadata().assignedBy().equals(username);
    }

    public static AssignmentFilter assignedAfter(String date) {
        // Лексикографическое сравнение строк дат (работает для ISO формата YYYY-MM-DD)
        return a -> a.metadata().assignedAt().compareTo(date) > 0;
    }

    public static AssignmentFilter expiringBefore(String date) {
        return a -> {
            if (a instanceof TemporaryAssignment temp) {
                return temp.isActive() && date.compareTo("TBD") > 0; // Логику даты можно уточнить
            }
            return false;
        };
    }
}