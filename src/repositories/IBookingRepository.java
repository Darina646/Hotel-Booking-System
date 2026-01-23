package repositories;

import entity.Booking;
import java.time.LocalDate;
import java.util.List;

public interface IBookingRepository {
    boolean createBooking(Booking booking);
    boolean isRoomAvailable(int roomId, LocalDate arrival, LocalDate departure);
    double getRoomPrice(int roomId);
    List<Booking> getAllBookings();
}
