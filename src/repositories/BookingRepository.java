package repositories;
import data.IDB;
import entity.Booking;

import java.sql.Connection;

public class BookingRepository implements IBookingRepository {
    private final IDB db;
    public BookingRepository(IDB db) {
        this.db = db;
    }
    @Override
    public boolean createBooking(Booking booking) {
        Connection con = null;
}
