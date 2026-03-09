import java.util.*;

public class UserManager implements Repository<User> {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void add(User user) {
        if (users.containsKey(user.username())) throw new IllegalArgumentException("Username already exists");
        users.put(user.username(), user);
    }

    @Override public boolean remove(User user) { return users.remove(user.username()) != null; }
    @Override public Optional<User> findById(String id) { return Optional.ofNullable(users.get(id)); }
    @Override public List<User> findAll() { return new ArrayList<>(users.values()); }
    @Override public int count() { return users.size(); }
    @Override public void clear() { users.clear(); }

    public Optional<User> findByUsername(String username) { return findById(username); }
    public boolean exists(String username) { return users.containsKey(username); }

    public List<User> findByFilter(UserFilter filter) {
        return users.values().stream().filter(filter::test).toList();
    }

    public List<User> findAll(UserFilter filter, Comparator<User> sorter) {
        return users.values().stream().filter(filter::test).sorted(sorter).toList();
    }

    public void update(String username, String newFullName, String newEmail) {
        if (!users.containsKey(username)) throw new NoSuchElementException("User not found");
        users.put(username, User.create(username, newFullName, newEmail));
    }
}