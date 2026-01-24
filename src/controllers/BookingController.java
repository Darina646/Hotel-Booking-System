package controllers;

import repositories.IBookingRepository;
import entity.Booking;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingController implements IBookingController {

    private final IBookingRepository repo;

    public BookingController(IBookingRepository repo) {
        this.repo = repo;
    }

    @Override
    public String createBooking(int guestId, int roomId, LocalDate arrival, LocalDate departure) {
        // Check if the room is available during the requested dates
        if (!repo.isRoomAvailable(roomId, arrival, departure)) {
            return "Room is not available for these dates.";
        }

        // Calculate the total price based on the number of nights and room price
        long days = ChronoUnit.DAYS.between(arrival, departure);
        double pricePerNight = repo.getRoomPrice(roomId);
        double totalPrice = days * pricePerNight;

        // Create a new booking object
        Booking booking = new Booking(guestId, roomId, arrival, departure, totalPrice);

        // Add the booking to the repository and return result
        return repo.createBooking(booking)
                ? "Booking successfully created. Total price: " + totalPrice
                : "Booking creation failed.";
    }

    @Override
    public String getAllBookings() {
        List<Booking> list = repo.getAllBookings();
        if (list.isEmpty()) return "No bookings found.";
        StringBuilder sb = new StringBuilder();
        for (Booking b : list) sb.append(b).append("\n");
        return sb.toString();
    }
}
