package controllers;

import entity.Booking;
import entity.FullBookingDescription;
import repositories.IBookingRepository;
import repositories.IBookingDetailsRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingController implements IBookingController {

    private final IBookingRepository bookingRepo;
    private final IBookingDetailsRepository detailsRepo;

    public BookingController(
            IBookingRepository bookingRepo,
            IBookingDetailsRepository detailsRepo
    ) {
        this.bookingRepo = bookingRepo;
        this.detailsRepo = detailsRepo;
    }

    @Override
    public String createBooking(
            String guestName,
            String guestEmail,
            int roomId,
            LocalDate arrival,
            LocalDate departure,
            int age
    ) {
        if (age < 18) {
            return "You must be 18 or older to make a booking.";
        }

        if (arrival.isAfter(departure)) {
            return "Departure date must be after arrival date.";
        }

        if (!bookingRepo.isRoomAvailable(roomId, arrival, departure)) {
            return "Room is not available for these dates.";
        }

        long days = ChronoUnit.DAYS.between(arrival, departure);
        double pricePerNight = 20;
        double totalPrice = days * pricePerNight;

        // guestId временно = 1 (или получаешь из БД)
        Booking booking =
                new Booking(1, roomId, arrival, departure, totalPrice);

        boolean success = bookingRepo.createBooking(booking);

        return success
                ? "Booking created. Total price: $" + totalPrice
                : "Booking creation failed.";
    }

    @Override
    public String getAllBookings() {
        List<Booking> list = bookingRepo.getAllBookings();
        if (list.isEmpty()) return "No bookings found.";

        StringBuilder sb = new StringBuilder();
        list.forEach(b -> sb.append(b).append("\n"));
        return sb.toString();
    }

    @Override
    public String getUnavailableRooms(LocalDate startDate, LocalDate endDate) {
        List<Booking> list =
                bookingRepo.getUnavailableRooms(startDate, endDate);

        if (list.isEmpty()) return "No unavailable rooms.";

        StringBuilder sb = new StringBuilder();
        for (Booking b : list) {
            sb.append("Room ID: ")
                    .append(b.getRoomId())
                    .append(" | ")
                    .append(b.getArrivalDate())
                    .append(" → ")
                    .append(b.getDepartureDate())
                    .append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getFullBookingDescription(int bookingId) {
        FullBookingDescription info =
                detailsRepo.getFullBookingDescription(bookingId);

        if (info == null) return "Booking not found.";

        return info.toString();
    }

}
