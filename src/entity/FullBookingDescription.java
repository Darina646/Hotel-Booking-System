package entity;

import java.time.LocalDate;

public class FullBookingDescription {

    private int bookingId;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private double totalPrice;
    private int guestId;
    private String guestName;
    private String guestEmail;
    private int roomId;
    private String roomNumber;
    private String pricePerNight;

    public FullBookingDescription(
            int bookingId,
            LocalDate arrivalDate,
            LocalDate departureDate,
            double totalPrice,
            int guestId,
            String guestName,
            String guestEmail,
            int roomId,
            String roomNumber,
            String pricePerNight
    ) {
        this.bookingId = bookingId;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.totalPrice = totalPrice;
        this.guestId = guestId;
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
    }

}
