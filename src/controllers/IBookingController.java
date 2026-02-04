package controllers;

import java.time.LocalDate;

public interface IBookingController {

    String createBooking(String guestName, String guestEmail, int roomId, LocalDate arrival, LocalDate departure, int age);
    String getAllBookings();
    String getUnavailableRooms(LocalDate startDate, LocalDate endDate);
    String getFullBookingDescription(int bookingId);

}

