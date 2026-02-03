package controllers;

import repositories.IBookingRepository;
import entity.Booking;
import entity.Guest;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingController implements IBookingController {

    private final IBookingRepository repo;

    public BookingController(IBookingRepository repo) {
        this.repo = repo;
    }

    @Override
    public String createBooking(String guestName, String guestEmail, int roomId, LocalDate arrival, LocalDate departure, int age) {
        // Validate the email format
        if (!guestEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Invalid email address!";
        }

        // Ensure the departure date is after the arrival date
        if (arrival.isAfter(departure)) {
            return "Departure date must be after the arrival date.";
        }

        // Check if the arrival date is not in the past (must be today or in the future)
        if (arrival.isBefore(LocalDate.now())) {
            return "Arrival date cannot be in the past.";
        }

        // Check the user's age - If under 18, deny the booking
        if (age < 18) {
            return "You must be 18 or older to make a booking.";
        }

        // Check if the room is available during the requested dates
        if (!repo.isRoomAvailable(roomId, arrival, departure)) {
            return "Room is not available for these dates.";
        }

        // Create new guest (in a real system, you might want to check if the guest already exists)
        Guest guest = new Guest();
        guest.setName(guestName);
        guest.setEmail(guestEmail);

        // Calculate price based on the number of days
        long days = ChronoUnit.DAYS.between(arrival, departure);
        double pricePerNight = 20; // $20 per night
        double totalPrice = days * pricePerNight;

        // Create a new booking
        Booking booking = new Booking(guest.getId(), roomId, arrival, departure, totalPrice);

        // Save the booking
        return repo.createBooking(booking)
                ? "Booking successfully created. Total price: $" + totalPrice
                : "Booking creation failed.";
    }

    @Override
    public String getAllBookings() {
        List<Booking> list = repo.getAllBookings();
        if (list.isEmpty()) return "No bookings found.";

        // Use lambda expression for iteration
        StringBuilder sb = new StringBuilder();
        list.forEach(b -> sb.append(b).append("\n"));

        return sb.toString();
    }

    @Override
    public String getUnavailableRooms(LocalDate startDate, LocalDate endDate) {
        List<Booking> unavailableBookings = repo.getUnavailableRooms(startDate, endDate);
        if (unavailableBookings.isEmpty()) {
            return "No rooms are unavailable for the selected dates.";
        }

        // Use lambda expression for iteration
        StringBuilder sb = new StringBuilder();
        unavailableBookings.forEach(booking ->
                sb.append("Room ID: ").append(booking.getRoomId())
                        .append(" | Arrival: ").append(booking.getArrivalDate())
                        .append(" | Departure: ").append(booking.getDepartureDate())
                        .append("\n")
        );

        return sb.toString();
    }
}
