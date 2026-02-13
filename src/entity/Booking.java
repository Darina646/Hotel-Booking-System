package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {
    private int id;
    private int guestId;
    private Guest guest;
    private int roomId;
    private Room room;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalPrice;
    private String status;
    private Integer createdBy;
    private LocalDateTime createdAt;

    public Booking() {}

    public Booking(int guestId, int roomId, LocalDate startDate, LocalDate endDate, double totalPrice) {
        this.guestId = guestId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.status = "CONFIRMED";
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

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Booking{id=" + id + ", guest=" + (guest != null ? guest.getName() : "N/A") +
                ", room=" + (room != null ? room.getRoomNumber() : "N/A") +
                ", dates=" + startDate + " to " + endDate + ", total=$" + totalPrice +
                ", status='" + status + "'}";
    }
}