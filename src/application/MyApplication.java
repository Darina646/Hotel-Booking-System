package application;

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
                0. Exit
                """);

            int option = scanner.nextInt();

            if (option == 1) {
                System.out.println(controller.getAllBookings());
            } else if (option == 2) {
                System.out.print("Guest id: ");
                int guestId = scanner.nextInt();

                System.out.print("Room id: ");
                int roomId = scanner.nextInt();

                System.out.print("Arrival (YYYY-MM-DD): ");
                LocalDate arrival = LocalDate.parse(scanner.next());

                System.out.print("Departure (YYYY-MM-DD): ");
                LocalDate departure = LocalDate.parse(scanner.next());

                System.out.println(controller.createBooking(guestId, roomId, arrival, departure));
            } else {
                break;
            }
        }
    }
}
