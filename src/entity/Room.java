package entity;

public class Room {
    private int id;
    private int roomNumber;  // Ensure this field exists
    private int capacity;     // Ensure this field exists

    // Getter and Setter for 'id'
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // Getter and Setter for 'roomNumber'
    public int getRoomNumber() { return roomNumber; }  // Getter for roomNumber
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    // Getter and Setter for 'capacity'
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}
