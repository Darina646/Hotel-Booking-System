package controllers;

import entity.Guest;
import entity.Room;
import entity.RoomCategory;
import entity.User;
import repositories.*;
import service.AuthenticationService;
import service.BookingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class HotelController {
    private final BookingService bookingService;
    private final IGuestRepository guestRepository;
    private final IRoomRepository roomRepository;
    private final IRoomCategoryRepository categoryRepository;
    private final IUserRepository userRepository;
    private final AuthenticationService authService;

    public HotelController(BookingService bookingService,
                           IGuestRepository guestRepository,
                           IRoomRepository roomRepository,
                           IRoomCategoryRepository categoryRepository,
                           IUserRepository userRepository,
                           AuthenticationService authService) {
        this.bookingService = bookingService;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public String login(String username, String password) {
        if (authService.login(username, password)) {
            User user = authService.getCurrentUser();
            return "Login successful! Welcome, " + user.getUsername() + " (" + user.getRole().getName() + ")";
        }
        return "Invalid username or password.";
    }

    /**
     * Registers a new user. Returns the result message.
     * Call isLastRegisterSuccess() to check whether it actually succeeded.
     */
    private AuthenticationService.RegistrationResult lastRegisterResult;

    public String register(String username, String password, String confirmPassword, String email) {
        lastRegisterResult = authService.register(username, password, confirmPassword, email);
        return lastRegisterResult.getMessage();
    }

    public boolean wasLastRegisterSuccessful() {
        return lastRegisterResult != null && lastRegisterResult.isSuccess();
    }

    public void logout() {
        authService.logout();
    }

    public User getCurrentUser() {
        return authService.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return authService.isLoggedIn();
    }

    public String createBooking(String guestName, String guestEmail, String guestPhone,
                                int roomId, LocalDate startDate, LocalDate endDate) {
        if (!authService.canCreateBooking()) {
            return "Access denied. Insufficient permissions.";
        }
        return bookingService.createBooking(guestName, guestEmail, guestPhone, roomId, startDate, endDate);
    }

    public String getBookingDetails(int bookingId) {
        if (!authService.isLoggedIn()) {
            return "Please login to view booking details.";
        }
        return bookingService.getBookingDetails(bookingId);
    }

    public String getAllBookings() {
        if (!authService.isLoggedIn()) {
            return "Please login to view bookings.";
        }

        List<entity.Booking> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            return "No bookings found.";
        }

        StringBuilder sb = new StringBuilder("\n========== ALL BOOKINGS ==========\n");
        bookings.forEach(booking -> sb.append(booking).append("\n"));
        return sb.toString();
    }

    public String getActiveBookings() {
        if (!authService.canViewReports()) {
            return "Access denied. Manager/Admin access required.";
        }

        List<entity.Booking> bookings = bookingService.getActiveBookings();
        if (bookings.isEmpty()) {
            return "No active bookings found.";
        }

        StringBuilder sb = new StringBuilder("\n========== ACTIVE BOOKINGS ==========\n");
        bookings.forEach(booking -> sb.append(booking).append("\n"));
        return sb.toString();
    }

    public String cancelBooking(int bookingId) {
        if (!authService.canCreateBooking()) {
            return "Access denied. Insufficient permissions.";
        }
        return bookingService.cancelBooking(bookingId);
    }

    public String getAvailableRooms(LocalDate startDate, LocalDate endDate) {
        List<Room> rooms = roomRepository.getAvailableRooms(startDate, endDate);
        if (rooms.isEmpty()) {
            return "No rooms available for the selected dates.";
        }

        StringBuilder sb = new StringBuilder("\n========== AVAILABLE ROOMS ==========\n");
        rooms.forEach(room -> sb.append(room).append("\n"));
        return sb.toString();
    }

    public String getAllRooms() {
        List<Room> rooms = roomRepository.getAllRooms();
        if (rooms.isEmpty()) {
            return "No rooms found.";
        }

        StringBuilder sb = new StringBuilder("\n========== ALL ROOMS ==========\n");
        rooms.forEach(room -> sb.append(room).append("\n"));
        return sb.toString();
    }

    public String createRoom(String roomNumber, int categoryId, int capacity, double price) {
        if (!authService.canManageRooms()) {
            return "Access denied. Manager/Admin access required.";
        }

        Room room = new Room(roomNumber, categoryId, capacity, price);
        if (roomRepository.createRoom(room)) {
            return "Room created successfully! ID: " + room.getId();
        }
        return "Failed to create room.";
    }

    public String getAllCategories() {
        List<RoomCategory> categories = categoryRepository.getAllCategories();
        if (categories.isEmpty()) {
            return "No categories found.";
        }

        StringBuilder sb = new StringBuilder("\n========== ROOM CATEGORIES ==========\n");
        categories.forEach(cat -> sb.append(cat).append("\n"));
        return sb.toString();
    }

    public String createGuest(String name, String email, String phone) {
        Guest guest = new Guest(name, email, phone);
        if (guestRepository.createGuest(guest)) {
            return "Guest created successfully! ID: " + guest.getId();
        }
        return "Failed to create guest.";
    }

    public String getAllGuests() {
        if (!authService.isLoggedIn()) {
            return "Please login to view guests.";
        }

        List<Guest> guests = guestRepository.getAllGuests();
        if (guests.isEmpty()) {
            return "No guests found.";
        }

        StringBuilder sb = new StringBuilder("\n========== ALL GUESTS ==========\n");
        guests.forEach(guest -> sb.append(guest).append("\n"));
        return sb.toString();
    }

    public String getRevenueSummary() {
        if (!authService.canViewReports()) {
            return "Access denied. Manager/Admin access required.";
        }

        double totalRevenue = bookingService.calculateTotalRevenue();
        long bookingCount = bookingService.getBookingCount();

        return String.format("\n========== REVENUE SUMMARY ==========\n" +
                        "Total Bookings: %d\n" +
                        "Total Revenue: $%.2f\n" +
                        "Average per Booking: $%.2f\n",
                bookingCount, totalRevenue,
                bookingCount > 0 ? totalRevenue / bookingCount : 0);
    }

    public String createUser(String username, String password, String email, int roleId) {
        if (!authService.canManageUsers()) {
            return "Access denied. Admin access required.";
        }

        // Basic duplicate check before attempting insert
        if (userRepository.existsByUsername(username)) {
            return "Failed: username '" + username + "' is already taken.";
        }
        if (email != null && !email.isBlank() && userRepository.existsByEmail(email)) {
            return "Failed: an account with this email already exists.";
        }

        User user = new User(username, password, email, roleId);
        if (userRepository.createUser(user)) {
            return "User created successfully! ID: " + user.getId();
        }
        return "Failed to create user.";
    }

    public String getAllUsers() {
        if (!authService.canManageUsers()) {
            return "Access denied. Admin access required.";
        }

        List<User> users = userRepository.getAllUsers();
        if (users.isEmpty()) {
            return "No users found.";
        }

        StringBuilder sb = new StringBuilder("\n========== ALL USERS ==========\n");
        users.forEach(user -> sb.append(user).append("\n"));
        return sb.toString();
    }
}