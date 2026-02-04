package repositories;

import entity.FullBookingDescription;

public interface IBookingDetailsRepository {
    FullBookingDescription getFullBookingDescription(int bookingId);
}
