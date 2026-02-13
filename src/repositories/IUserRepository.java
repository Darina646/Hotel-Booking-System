package repositories;

import entity.User;
import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    boolean createUser(User user);
    boolean registerUser(User user);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> getUserById(int id);
    Optional<User> getUserByUsername(String username);
    Optional<User> authenticate(String username, String password);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(int id);
}
