package controllers;

import java.time.LocalDate;

public interface IBookingController {
    String createBooking(int guestId, int roomId, LocalDate arrival, LocalDate departure);
    String getAllBookings();
}
