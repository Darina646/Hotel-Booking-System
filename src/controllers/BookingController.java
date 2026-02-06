package controllers;

import entity.Booking;
import entity.RoomCategory;
import repositories.IBookingRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingController implements IBookingController {

    private final IBookingRepository repo;

    public BookingController(IBookingRepository repo) {
        this.repo = repo;
    }

    @Override
    public String createBooking(String guestName, String guestEmail, int roomId, LocalDate arrival, LocalDate departure, int age, RoomCategory category) {
        if (age < 18) {
            return "Booking failed. You must be 18 or older.";
        }

        double pricePerDay = getPricePerDay(category); // Get price based on category
        long days = ChronoUnit.DAYS.between(arrival, departure);
        double totalPrice = pricePerDay * days;

        Booking booking = new Booking(1, roomId, arrival, departure, totalPrice, category); // Assuming id is auto-generated
        return "Booking successfully created. Total price: $" + totalPrice;
    }

    @Override
    public List<Booking> getAllBookings() {
        return repo.getAllBookings();
    }

    @Override
    public List<Booking> getUnavailableRooms(LocalDate startDate, LocalDate endDate) {
        return repo.getUnavailableRooms(startDate, endDate);
    }

    @Override
    public boolean deleteBooking(int bookingId) {
        return repo.deleteBooking(bookingId);
    }

    // Helper method to return the price based on category
    private double getPricePerDay(RoomCategory category) {
        switch (category) {
            case STANDARD:
                return 20; // Standard price is 20 per day
            case LUXURY:
                return 50; // Luxury price is 50 per day
            case BUSINESS:
                return 100; // Business price is 100 per day
            default:
                return 20; // Default to standard if something goes wrong
        }
    }
}
