import controllers.IBookingController;
import java.time.LocalDate;
import java.util.Scanner;

public class MyApplication {

    private final IBookingController controller;
    private final Scanner scanner = new Scanner(System.in);

    public MyApplication(IBookingController controller) {
        this.controller = controller;
    }

    public void start() {
        while (true) {
            System.out.println("""
                1. Get all bookings
                2. Create booking
                3. View unavailable rooms
                0. Exit
                """);

            int option = scanner.nextInt();

            if (option == 1) {
                System.out.println(controller.getAllBookings());
            } else if (option == 2) {
                System.out.print("Enter Guest Name: ");
                scanner.nextLine();  // consume newline
                String guestName = scanner.nextLine();

                System.out.print("Enter Guest Email: ");
                String guestEmail = scanner.nextLine();

                System.out.print("Enter Room ID: ");
                int roomId = scanner.nextInt();

                System.out.print("Arrival Date (YYYY-MM-DD): ");
                LocalDate arrival = LocalDate.parse(scanner.next());

                System.out.print("Departure Date (YYYY-MM-DD): ");
                LocalDate departure = LocalDate.parse(scanner.next());

                System.out.println(controller.createBooking(guestName, guestEmail, roomId, arrival, departure));
            } else if (option == 3) {
                System.out.print("Enter Start Date (YYYY-MM-DD): ");
                LocalDate startDate = LocalDate.parse(scanner.next());

                System.out.print("Enter End Date (YYYY-MM-DD): ");
                LocalDate endDate = LocalDate.parse(scanner.next());

                System.out.println(controller.getUnavailableRooms(startDate, endDate));
            } else {
                break;
            }
        }
    }
}