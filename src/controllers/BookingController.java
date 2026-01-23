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

        if (!repo.isRoomAvailable(roomId, arrival, departure)) {
            return "Room is not available";
        }

        long days = ChronoUnit.DAYS.between(arrival, departure);
        double pricePerNight = repo.getRoomPrice(roomId);
        double totalPrice = days * pricePerNight;

        Booking booking = new Booking(
                guestId, roomId, arrival, departure, totalPrice
        );

        return repo.createBooking(booking)
                ? "Booking created. Total price: " + totalPrice
                : "Booking failed";
    }

    @Override
    public String getAllBookings() {
        List<Booking> list = repo.getAllBookings();
        if (list.isEmpty()) return "No bookings";
        StringBuilder sb = new StringBuilder();
        for (Booking b : list) sb.append(b).append("\n");
        return sb.toString();
    }
}
