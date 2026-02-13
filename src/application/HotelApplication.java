package application;

import controllers.HotelController;
import entity.User;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class HotelApplication {
    private final HotelController controller;
    private final Scanner scanner;
    private boolean running;

    public HotelApplication(HotelController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    public void start() {
        printWelcome();

        while (running) {
            if (!controller.isLoggedIn()) {
                showAuthMenu();
            } else {
                showMainMenu();
            }
        }

        scanner.close();
    }

    // ─── Welcome banner ───────────────────────────────────────────────────────

    private void printWelcome() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   HOTEL BOOKING MANAGEMENT SYSTEM      ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    // ─── Auth menu (shown when not logged in) ─────────────────────────────────

    private void showAuthMenu() {
        System.out.println("\n========== WELCOME ==========");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        System.out.print("Choose option: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            switch (choice) {
                case 1 -> handleLogin();
                case 2 -> handleRegister();
                case 0 -> {
                    System.out.println("Thank you for using Hotel Booking System. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    // ─── Login ────────────────────────────────────────────────────────────────

    private void handleLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        String result = controller.login(username, password);
        System.out.println(result);
    }

    // ─── Registration ─────────────────────────────────────────────────────────

    private void handleRegister() {
        System.out.println("\n--- CREATE YOUR ACCOUNT ---");
        System.out.println("(Your account will be created with Guest access.)");
        System.out.println("(An Admin can later promote your account to a higher role.)");
        System.out.println();

        System.out.print("Choose a username (3–50 chars, letters/digits/underscore): ");
        String username = scanner.nextLine().trim();

        System.out.print("Email address: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password (min. 6 characters): ");
        String password = scanner.nextLine().trim();

        System.out.print("Confirm password: ");
        String confirmPassword = scanner.nextLine().trim();

        String result = controller.register(username, password, confirmPassword, email);
        System.out.println();

        if (controller.wasLastRegisterSuccessful()) {
            System.out.println("✓ " + result);
            System.out.println("  You can now log in using the Login option.");
        } else {
            System.out.println("✗ Registration failed: " + result);
        }
    }

    // ─── Main menu (shown when logged in) ────────────────────────────────────

    private void showMainMenu() {
        User currentUser = controller.getCurrentUser();
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("Logged in as: " + currentUser.getUsername()
                + " (" + currentUser.getRole().getName() + ")");

        System.out.println("\n--- BOOKING OPERATIONS ---");
        System.out.println("1. Create New Booking");
        System.out.println("2. View Booking Details");
        System.out.println("3. View All Bookings");
        System.out.println("4. View Active Bookings");
        System.out.println("5. Cancel Booking");

        System.out.println("\n--- ROOM OPERATIONS ---");
        System.out.println("6. View Available Rooms");
        System.out.println("7. View All Rooms");
        System.out.println("8. View Room Categories");
        System.out.println("9. Create New Room (Manager/Admin)");

        System.out.println("\n--- GUEST OPERATIONS ---");
        System.out.println("10. View All Guests");

        System.out.println("\n--- REPORTS (Manager/Admin) ---");
        System.out.println("11. Revenue Summary");

        System.out.println("\n--- USER MANAGEMENT (Admin) ---");
        System.out.println("12. View All Users");
        System.out.println("13. Create New User");

        System.out.println("\n14. Logout");
        System.out.println("0. Exit");

        System.out.print("\nChoose option: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            handleMainMenuChoice(choice);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1  -> createBooking();
            case 2  -> viewBookingDetails();
            case 3  -> System.out.println(controller.getAllBookings());
            case 4  -> System.out.println(controller.getActiveBookings());
            case 5  -> cancelBooking();
            case 6  -> viewAvailableRooms();
            case 7  -> System.out.println(controller.getAllRooms());
            case 8  -> System.out.println(controller.getAllCategories());
            case 9  -> createRoom();
            case 10 -> System.out.println(controller.getAllGuests());
            case 11 -> System.out.println(controller.getRevenueSummary());
            case 12 -> System.out.println(controller.getAllUsers());
            case 13 -> createUser();
            case 14 -> {
                controller.logout();
                System.out.println("Logged out successfully.");
            }
            case 0 -> {
                System.out.println("Thank you for using Hotel Booking System. Goodbye!");
                running = false;
            }
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    // ─── Booking helpers ──────────────────────────────────────────────────────

    private void createBooking() {
        System.out.println("\n--- CREATE NEW BOOKING ---");

        System.out.print("Guest Name: ");
        String guestName = scanner.nextLine().trim();

        System.out.print("Guest Email: ");
        String guestEmail = scanner.nextLine().trim();

        System.out.print("Guest Phone: ");
        String guestPhone = scanner.nextLine().trim();

        System.out.println("\nFirst, let's check available rooms.");
        System.out.print("Start Date (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine().trim();

        System.out.print("End Date (YYYY-MM-DD): ");
        String endDateStr = scanner.nextLine().trim();

        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate   = LocalDate.parse(endDateStr);

            System.out.println(controller.getAvailableRooms(startDate, endDate));

            System.out.print("\nEnter Room ID to book: ");
            int roomId = Integer.parseInt(scanner.nextLine().trim());

            String result = controller.createBooking(guestName, guestEmail, guestPhone,
                    roomId, startDate, endDate);
            System.out.println(result);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid room ID.");
        }
    }

    private void viewBookingDetails() {
        System.out.print("Enter Booking ID: ");
        try {
            int bookingId = Integer.parseInt(scanner.nextLine().trim());
            System.out.println(controller.getBookingDetails(bookingId));
        } catch (NumberFormatException e) {
            System.out.println("Invalid booking ID.");
        }
    }

    private void cancelBooking() {
        System.out.print("Enter Booking ID to cancel: ");
        try {
            int bookingId = Integer.parseInt(scanner.nextLine().trim());
            System.out.println(controller.cancelBooking(bookingId));
        } catch (NumberFormatException e) {
            System.out.println("Invalid booking ID.");
        }
    }

    private void viewAvailableRooms() {
        System.out.print("Start Date (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine().trim();

        System.out.print("End Date (YYYY-MM-DD): ");
        String endDateStr = scanner.nextLine().trim();

        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate   = LocalDate.parse(endDateStr);
            System.out.println(controller.getAvailableRooms(startDate, endDate));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    // ─── Room/User creation helpers ───────────────────────────────────────────

    private void createRoom() {
        System.out.println("\n--- CREATE NEW ROOM ---");
        System.out.println(controller.getAllCategories());

        System.out.print("Room Number: ");
        String roomNumber = scanner.nextLine().trim();

        System.out.print("Category ID: ");
        String categoryIdStr = scanner.nextLine().trim();

        System.out.print("Capacity: ");
        String capacityStr = scanner.nextLine().trim();

        System.out.print("Price per Night: ");
        String priceStr = scanner.nextLine().trim();

        try {
            int    categoryId = Integer.parseInt(categoryIdStr);
            int    capacity   = Integer.parseInt(capacityStr);
            double price      = Double.parseDouble(priceStr);
            System.out.println(controller.createRoom(roomNumber, categoryId, capacity, price));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format.");
        }
    }

    private void createUser() {
        System.out.println("\n--- CREATE NEW USER (Admin) ---");
        System.out.println("Available Roles:");
        System.out.println("1 - ADMIN");
        System.out.println("2 - MANAGER");
        System.out.println("3 - RECEPTIONIST");
        System.out.println("4 - GUEST");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Role ID (1-4): ");
        String roleIdStr = scanner.nextLine().trim();

        try {
            int roleId = Integer.parseInt(roleIdStr);
            System.out.println(controller.createUser(username, password, email, roleId));
        } catch (NumberFormatException e) {
            System.out.println("Invalid role ID.");
        }
    }
}