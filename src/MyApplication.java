// MyApplication.java
import java.time.LocalDate;
import java.util.Scanner;
import entity.RoomCategory;
import controllers.BookingController;

public class MyApplication {
    private final BookingController controller;
    private final Scanner scanner = new Scanner(System.in);

    public MyApplication(BookingController controller) {
        this.controller = controller;
    }

    public void start() {
        System.out.print("Enter Guest Name: ");
        String guestName = scanner.nextLine();

        if (guestName.equalsIgnoreCase("editor")) {
            System.out.println("Editor role detected.");
            showEditorMenu();
        } else {
            System.out.println("Guest role detected.");
            showGuestMenu(guestName);
        }
    }

    private void showGuestMenu(String guestName) {
        System.out.println("""
            1. Get all bookings
            2. Create booking
            3. View unavailable rooms
            0. Exit
        """);
        int option = scanner.nextInt();

        switch (option) {
            case 1 -> System.out.println(controller.getAllBookings());
            case 2 -> createBooking(guestName);
            case 3 -> viewUnavailableRooms();
            default -> System.out.println("Invalid option.");
        }
    }

    private void showEditorMenu() {
        System.out.println("""
            1. Get all bookings
            2. Create booking
            3. View unavailable rooms
            4. Delete booking (Editor only)
            0. Exit
        """);
        int option = scanner.nextInt();

        switch (option) {
            case 1 -> System.out.println(controller.getAllBookings());
            case 2 -> createBooking("editor");
            case 3 -> viewUnavailableRooms();
            case 4 -> deleteBooking();
            default -> System.out.println("Invalid option.");
        }
    }

    private void createBooking(String guestName) {
        System.out.print("Enter Guest Email: ");
        String guestEmail = scanner.next();
        System.out.print("Enter Room ID: ");
        int roomId = scanner.nextInt();
        System.out.print("Arrival Date (YYYY-MM-DD): ");
        LocalDate arrival = LocalDate.parse(scanner.next());
        System.out.print("Departure Date (YYYY-MM-DD): ");
        LocalDate departure = LocalDate.parse(scanner.next());
        System.out.print("Enter your Age: ");
        int age = scanner.nextInt();

        System.out.println("Select Room Category:");
        System.out.println("1. Standard\n2. Luxury\n3. Business");
        int categoryChoice = scanner.nextInt();
        RoomCategory category = switch (categoryChoice) {
            case 1 -> RoomCategory.STANDARD;
            case 2 -> RoomCategory.LUXURY;
            case 3 -> RoomCategory.BUSINESS;
            default -> RoomCategory.STANDARD;
        };

        System.out.println(controller.createBooking(guestName, guestEmail, roomId, arrival, departure, age, category));
    }

    private void viewUnavailableRooms() {
        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.next());
        System.out.print("Enter End Date (YYYY-MM-DD): ");
        LocalDate endDate = LocalDate.parse(scanner.next());

        System.out.println(controller.getUnavailableRooms(startDate, endDate));
    }

    private void deleteBooking() {
        System.out.print("Enter Booking ID to delete: ");
        int bookingId = scanner.nextInt();
        if (controller.deleteBooking(bookingId)) {
            System.out.println("Booking deleted successfully.");
        } else {
            System.out.println("Failed to delete booking.");
        }
    }
}
