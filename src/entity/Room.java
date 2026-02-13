package entity;

public class Room {
    private int id;
    private String roomNumber;
    private int categoryId;
    private RoomCategory category;
    private int capacity;
    private double price;
    private boolean isAvailable;

    public Room() {}

    public Room(String roomNumber, int categoryId, int capacity, double price) {
        this.roomNumber = roomNumber;
        this.categoryId = categoryId;
        this.capacity = capacity;
        this.price = price;
        this.isAvailable = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public RoomCategory getCategory() {
        return category;
    }

    public void setCategory(RoomCategory category) {
        this.category = category;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Room{id=" + id + ", number='" + roomNumber + "', category=" +
                (category != null ? category.getName() : "N/A") + ", capacity=" + capacity +
                ", price=$" + price + ", available=" + isAvailable + "}";
    }
}