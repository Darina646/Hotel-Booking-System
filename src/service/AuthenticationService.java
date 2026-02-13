package service;

import entity.User;
import repositories.IUserRepository;
import util.ValidationUtil;
import java.util.Optional;

public class AuthenticationService {
    private final IUserRepository userRepository;
    private User currentUser;

    // Password policy constants
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MAX_USERNAME_LENGTH = 50;

    public AuthenticationService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ─── Login ────────────────────────────────────────────────────────────────

    public boolean login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return false;
        }
        Optional<User> user = userRepository.authenticate(username.trim(), password);
        if (user.isPresent()) {
            this.currentUser = user.get();
            return true;
        }
        return false;
    }

    // ─── Registration ─────────────────────────────────────────────────────────

    /**
     * Registers a new user with GUEST role.
     * Returns a result object describing success or the specific validation failure.
     */
    public RegistrationResult register(String username, String password,
                                       String confirmPassword, String email) {
        // Trim inputs
        username = username == null ? "" : username.trim();
        email    = email    == null ? "" : email.trim();

        // Username validation
        if (username.isEmpty()) {
            return RegistrationResult.failure("Username cannot be empty.");
        }
        if (username.length() < MIN_USERNAME_LENGTH) {
            return RegistrationResult.failure(
                    "Username must be at least " + MIN_USERNAME_LENGTH + " characters.");
        }
        if (username.length() > MAX_USERNAME_LENGTH) {
            return RegistrationResult.failure(
                    "Username must not exceed " + MAX_USERNAME_LENGTH + " characters.");
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return RegistrationResult.failure(
                    "Username may only contain letters, digits, and underscores.");
        }

        // Password validation
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return RegistrationResult.failure(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
        }
        if (!password.equals(confirmPassword)) {
            return RegistrationResult.failure("Passwords do not match.");
        }

        // Email validation
        if (email.isEmpty()) {
            return RegistrationResult.failure("Email cannot be empty.");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            return RegistrationResult.failure("Invalid email address format.");
        }

        // Uniqueness checks
        if (userRepository.existsByUsername(username)) {
            return RegistrationResult.failure(
                    "Username '" + username + "' is already taken. Please choose another.");
        }
        if (userRepository.existsByEmail(email)) {
            return RegistrationResult.failure(
                    "An account with this email address already exists.");
        }

        // Persist — registerUser() always sets role_id = 4 (GUEST)
        User newUser = new User(username, password, email, 4);
        if (userRepository.registerUser(newUser)) {
            return RegistrationResult.success(
                    "Registration successful! You can now log in as '" + username + "'.");
        }
        return RegistrationResult.failure(
                "Registration failed due to a server error. Please try again.");
    }

    // ─── Session helpers ──────────────────────────────────────────────────────

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    // ─── Role-based permission checks ─────────────────────────────────────────

    public boolean hasRole(String roleName) {
        return currentUser != null &&
                currentUser.getRole() != null &&
                currentUser.getRole().getName().equalsIgnoreCase(roleName);
    }

    public boolean canCreateBooking() {
        return hasRole("ADMIN") || hasRole("MANAGER") || hasRole("RECEPTIONIST");
    }

    public boolean canManageRooms() {
        return hasRole("ADMIN") || hasRole("MANAGER");
    }

    public boolean canManageUsers() {
        return hasRole("ADMIN");
    }

    public boolean canViewReports() {
        return hasRole("ADMIN") || hasRole("MANAGER");
    }

    // ─── Inner result type ────────────────────────────────────────────────────

    public static class RegistrationResult {
        private final boolean success;
        private final String message;

        private RegistrationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public static RegistrationResult success(String message) {
            return new RegistrationResult(true, message);
        }

        public static RegistrationResult failure(String message) {
            return new RegistrationResult(false, message);
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}