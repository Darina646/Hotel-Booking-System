package repositories;

import data.IDB;
import entity.Booking;
import entity.RoomCategory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class BookingRepository implements IBookingRepository {
    private final IDB db;

    public BookingRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean isRoomAvailable(int roomId, LocalDate arrival, LocalDate departure) {
        String sql = "SELECT * FROM bookings WHERE room_id = ? AND ((arrival_date BETWEEN ? AND ?) OR (departure_date BETWEEN ? AND ?))";

        try (Connection con = db.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, roomId);
            st.setDate(2, Date.valueOf(arrival));
            st.setDate(3, Date.valueOf(departure));
            st.setDate(4, Date.valueOf(arrival));
            st.setDate(5, Date.valueOf(departure));
            ResultSet rs = st.executeQuery();
            return !rs.next(); // If there are no bookings, room is available
        } catch (SQLException e) {
            System.out.println("Error while checking room availability: " + e.getMessage());
            return false;
        }
    }

    @Override
    public double getRoomPrice(int roomId) {
        double pricePerDay = 0;
        String sql = "SELECT price FROM rooms WHERE id = ?"; // Query the correct table for room price

        try (Connection con = db.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, roomId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                pricePerDay = rs.getDouble("price"); // Ensure the column name is correct
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving room price: " + e.getMessage());
        }
        return pricePerDay;
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings";

        try (Connection con = db.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Booking b = new Booking(
                        rs.getInt("guest_id"),
                        rs.getInt("room_id"),
                        rs.getDate("arrival_date").toLocalDate(),
                        rs.getDate("departure_date").toLocalDate(),
                        rs.getDouble("total_price"),
                        RoomCategory.valueOf(rs.getString("category"))
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
        String sql = "SELECT * FROM bookings WHERE (arrival_date BETWEEN ? AND ?) OR (departure_date BETWEEN ? AND ?)";

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
                        rs.getDouble("total_price"),
                        RoomCategory.valueOf(rs.getString("category"))
                );
                b.setId(rs.getInt("id"));
                list.add(b);
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving unavailable rooms: " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean deleteBooking(int bookingId) {
        String sql = "DELETE FROM bookings WHERE id = ?";

        try (Connection con = db.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, bookingId);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error while deleting booking: " + e.getMessage());
            return false;
        }
    }
}
