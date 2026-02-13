package service;

import dto.BookingDetailsDTO;
import entity.Booking;
import entity.Guest;
import entity.Room;
import repositories.IBookingRepository;
import repositories.IGuestRepository;
import repositories.IRoomRepository;
import util.ValidationUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookingService {
    private final IBookingRepository bookingRepository;
    private final IGuestRepository guestRepository;
    private final IRoomRepository roomRepository;
    private final AuthenticationService authService;

    public BookingService(IBookingRepository bookingRepository,
                          IGuestRepository guestRepository,
                          IRoomRepository roomRepository,
                          AuthenticationService authService) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.authService = authService;
    }

    public String createBooking(String guestName, String guestEmail, String guestPhone,
                                int roomId, LocalDate startDate, LocalDate endDate) {
        if (!ValidationUtil.isValidName(guestName)) {
            return "Invalid guest name. Must be at least 2 characters.";
        }
        if (!ValidationUtil.isValidEmail(guestEmail)) {
            return "Invalid email format.";
        }
        if (!ValidationUtil.isValidPhone(guestPhone)) {
            return "Invalid phone number format.";
        }
        if (!ValidationUtil.isValidDateRange(startDate, endDate)) {
            return "Invalid date range. Ensure dates are in the future and start is before end.";
        }

        Optional<Room> roomOpt = roomRepository.getRoomById(roomId);
        if (roomOpt.isEmpty()) {
            return "Room not found.";
        }

        Room room = roomOpt.get();
        if (!room.isAvailable()) {
            return "Room is currently unavailable.";
        }

        if (!bookingRepository.isRoomAvailable(roomId, startDate, endDate)) {
            return "Room is already booked for the selected dates.";
        }

        Optional<Guest> existingGuest = guestRepository.getGuestByEmail(guestEmail);
        Guest guest;

        if (existingGuest.isPresent()) {
            guest = existingGuest.get();
        } else {
            guest = new Guest(guestName, guestEmail, guestPhone);
            if (!guestRepository.createGuest(guest)) {
                return "Failed to create guest record.";
            }
        }

        long nights = ChronoUnit.DAYS.between(startDate, endDate);
        double totalPrice = nights * room.getPrice();

        Booking booking = new Booking(guest.getId(), roomId, startDate, endDate, totalPrice);
        if (authService.isLoggedIn()) {
            booking.setCreatedBy(authService.getCurrentUser().getId());
        }

        if (bookingRepository.createBooking(booking)) {
            return String.format("Booking created successfully! ID: %d, Total: $%.2f for %d nights",
                    booking.getId(), totalPrice, nights);
        }
        return "Failed to create booking.";
    }

    public String getBookingDetails(int bookingId) {
        Optional<BookingDetailsDTO> details = bookingRepository.getFullBookingDetails(bookingId);
        return details.map(BookingDetailsDTO::toString)
                .orElse("Booking not found.");
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.getAllBookings();
    }

    public List<Booking> getActiveBookings() {
        return bookingRepository.getAllBookings().stream()
                .filter(booking -> !booking.getStatus().equals("CANCELLED"))
                .filter(booking -> !booking.getEndDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    public List<Booking> getUpcomingBookings() {
        LocalDate today = LocalDate.now();
        return bookingRepository.getAllBookings().stream()
                .filter(booking -> booking.getStartDate().isAfter(today))
                .filter(booking -> !booking.getStatus().equals("CANCELLED"))
                .sorted((b1, b2) -> b1.getStartDate().compareTo(b2.getStartDate()))
                .collect(Collectors.toList());
    }

    public String cancelBooking(int bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.getBookingById(bookingId);
        if (bookingOpt.isEmpty()) {
            return "Booking not found.";
        }

        Booking booking = bookingOpt.get();
        if (booking.getStatus().equals("CANCELLED")) {
            return "Booking is already cancelled.";
        }

        if (booking.getStartDate().isBefore(LocalDate.now())) {
            return "Cannot cancel a booking that has already started.";
        }

        if (bookingRepository.cancelBooking(bookingId)) {
            return "Booking cancelled successfully.";
        }
        return "Failed to cancel booking.";
    }

    public double calculateTotalRevenue() {
        return bookingRepository.getAllBookings().stream()
                .filter(booking -> !booking.getStatus().equals("CANCELLED"))
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }

    public long getBookingCount() {
        return bookingRepository.getAllBookings().stream()
                .filter(booking -> !booking.getStatus().equals("CANCELLED"))
                .count();
    }
}