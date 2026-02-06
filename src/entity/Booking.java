package entity;

import java.time.LocalDate;

public class Booking {
    private int id;
    private final int guestId;
    private final int roomId;
    private final LocalDate arrivalDate;
    private final LocalDate departureDate;
    private final double totalPrice;
    private final RoomCategory category;

    public Booking(int guestId, int roomId, LocalDate arrivalDate, LocalDate departureDate, double totalPrice, RoomCategory category) {
        this.guestId = guestId;
        this.roomId = roomId;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.totalPrice = totalPrice;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuestId() {
        return guestId;
    }

    public int getRoomId() {
        return roomId;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public RoomCategory getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", guestId=" + guestId +
                ", roomId=" + roomId +
                ", arrivalDate=" + arrivalDate +
                ", departureDate=" + departureDate +
                ", totalPrice=" + totalPrice +
                ", category=" + category +
                '}';
    }
}
