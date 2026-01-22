package entity;

import java.time.LocalDate;

public class Booking {
    private int id;
    private int guestId;
    private int roomId;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private double totalPrice;
    public Booking(int guestId,int roomId,LocalDate arrivalDate,LocalDate departureDate,double totalPrice) {
       this.guestId=guestId;
       this.roomId=roomId;
       this.arrivalDate=arrivalDate;
       this.departureDate=departureDate;
       this.totalPrice=totalPrice;
    }
    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuestIdId() {
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
    @Override
    public String toString() {
        return "Booking{" + "id=" + id + ", roomId=" + roomId + ", guestId=" + guestId + ", arrivalDate=" +
                arrivalDate + ", departureDate=" + departureDate + ", totalPrice=" + totalPrice + '}';
    }
}
