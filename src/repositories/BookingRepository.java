package repositories;

import data.IDB;
import entity.Booking;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository implements IBookingRepository {

    private final IDB db;

    public BookingRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createBooking(Booking booking) {
        String sql = "INSERT INTO bookings (guest_id, room_id, arrival_date, departure_date, total_price) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, booking.getGuestId());
            st.setInt(2, booking.getRoomId());
            st.setDate(3, Date.valueOf(booking.getArrivalDate()));
            st.setDate(4, Date.valueOf(booking.getDepartureDate()));
            st.setDouble(5, booking.getTotalPrice());

            st.execute();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isRoomAvailable(int roomId, LocalDate arrival, LocalDate departure) {
        String sql = """
            SELECT COUNT(*) FROM bookings
            WHERE room_id = ?
            AND NOT (departure_date <= ? OR arrival_date >= ?)
        """;

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, roomId);
            st.setDate(2, Date.valueOf(arrival));
            st.setDate(3, Date.valueOf(departure));

            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getInt(1) == 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public double getRoomPrice(int roomId) {
        String sql = "SELECT price_per_night FROM rooms WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, roomId);
            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getDouble(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Booking b = new Booking(
                        rs.getInt("guest_id"),
                        rs.getInt("room_id"),
                        rs.getDate("arrival_date").toLocalDate(),
                        rs.getDate("departure_date").toLocalDate(),
                        rs.getDouble("total_price")
                );
                b.setId(rs.getInt("id"));
                list.add(b);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}
