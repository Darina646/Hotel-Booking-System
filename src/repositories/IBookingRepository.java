package repositories;
import entity.Booking;
import java.time.LocalDate;
import java.util.List;
public interface IBookingRepository {
    boolean createBooking(Booking booking);
    boolean isRoomAvailable(int roomId,LocalDate arrivalDate,LocalDate departureDate);
    List<Booking> getAllBookings();
}
