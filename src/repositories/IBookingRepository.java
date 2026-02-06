package repositories;

import entity.Booking;
import entity.RoomCategory;

import java.time.LocalDate;
import java.util.List;

public interface IBookingRepository {

    // Method to check if a room is available for a specific date range
    boolean isRoomAvailable(int roomId, LocalDate arrival, LocalDate departure);

    // Method to get the price of a room by its ID
    double getRoomPrice(int roomId);

    // Method to retrieve all bookings
    List<Booking> getAllBookings();

    // Method to retrieve unavailable rooms between start and end dates
    List<Booking> getUnavailableRooms(LocalDate startDate, LocalDate endDate);

    // Method to delete a booking by its ID
    boolean deleteBooking(int bookingId);
}
