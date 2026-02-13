package dto;

import java.time.LocalDate;

public class BookingDetailsDTO {
    private int bookingId;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private String roomNumber;
    private String categoryName;
    private int capacity;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalPrice;
    private String status;
    private String createdByUsername;

    public BookingDetailsDTO() {}

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    @Override
    public String toString() {
        return "\n================================" +
                "\nBooking ID: " + bookingId +
                "\nGuest: " + guestName + " (" + guestEmail + ")" +
                "\nPhone: " + guestPhone +
                "\nRoom: " + roomNumber + " - " + categoryName +
                "\nCapacity: " + capacity + " persons" +
                "\nPeriod: " + startDate + " to " + endDate +
                "\nTotal Price: $" + totalPrice +
                "\nStatus: " + status +
                "\nBooked by: " + (createdByUsername != null ? createdByUsername : "N/A") +
                "\n================================";
    }
}
