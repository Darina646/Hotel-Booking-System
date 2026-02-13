package repositories;

import dto.BookingDetailsDTO;
import entity.Booking;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IBookingRepository {
    boolean createBooking(Booking booking);
    Optional<Booking> getBookingById(int id);
    Optional<BookingDetailsDTO> getFullBookingDetails(int bookingId);
    List<Booking> getAllBookings();
    List<Booking> getBookingsByGuest(int guestId);
    List<Booking> getBookingsByRoom(int roomId);
    boolean updateBooking(Booking booking);
    boolean cancelBooking(int id);
    boolean isRoomAvailable(int roomId, LocalDate startDate, LocalDate endDate);
}
