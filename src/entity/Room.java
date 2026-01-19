package entity;

public class Room {
    private int id;
    private String number;
    private double pricePerNight;

    public Room(int id, String number, double pricePerNight){
        this.id=id;
        this.number=number;
        this.pricePerNight=pricePerNight;
    }
}
