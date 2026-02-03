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
        String sql = "INSERT INTO bookings (guest_id, room_id, total_price, arrival_date, departure_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = db.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, booking.getGuestId());
            st.setInt(2, booking.getRoomId());
            st.setDouble(3, booking.getTotalPrice());
            st.setDate(4, Date.valueOf(booking.getArrivalDate()));
            st.setDate(5, Date.valueOf(booking.getDepartureDate()));

            st.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error while creating booking: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isRoomAvailable(int roomId, LocalDate arrival, LocalDate departure) {
        String sql = """
            SELECT COUNT(*) FROM bookings
            WHERE room_id = ? AND NOT (departure_date <= ? OR arrival_date >= ?)
        """;

        try (Connection con = db.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, roomId);
            st.setDate(2, Date.valueOf(arrival));
            st.setDate(3, Date.valueOf(departure));

            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getInt(1) == 0;
        } catch (SQLException e) {
            System.out.println("Error while checking room availability: " + e.getMessage());
            return false;
        }
    }

    @Override
    public double getRoomPrice(int roomId) {
        return 20; // Fixed price per night for all rooms
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = """
            SELECT b.id, b.guest_id, g.name AS guest_name, b.room_id, r.room_number, r.category, 
                   b.arrival_date, b.departure_date, b.total_price
            FROM bookings b
            JOIN guests g ON b.guest_id = g.id
            JOIN rooms r ON b.room_id = r.id
        """;

        try (Connection con = db.getConnection(); PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
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
            System.out.println("Error while retrieving all bookings: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Booking> getUnavailableRooms(LocalDate startDate, LocalDate endDate) {
        List<Booking> list = new ArrayList<>();
        String sql = """
            SELECT * FROM bookings
            WHERE (arrival_date BETWEEN ? AND ?) 
            OR (departure_date BETWEEN ? AND ?)
        """;

        try (Connection con = db.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
            st.setDate(1, Date.valueOf(startDate));
            st.setDate(2, Date.valueOf(endDate));
            st.setDate(3, Date.valueOf(startDate));
            st.setDate(4, Date.valueOf(endDate));

            ResultSet rs = st.executeQuery();
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
            System.out.println("Error while retrieving unavailable rooms: " + e.getMessage());
        }
        return list;
    }
}
