package controllers;

import entity.Booking;
import entity.RoomCategory;
import java.time.LocalDate;
import java.util.List;

public interface IBookingController {
    String createBooking(String guestName, String guestEmail, int roomId, LocalDate arrival, LocalDate departure, int age, RoomCategory category);
    List<Booking> getAllBookings();
    List<Booking> getUnavailableRooms(LocalDate startDate, LocalDate endDate);
    boolean deleteBooking(int bookingId);
}
