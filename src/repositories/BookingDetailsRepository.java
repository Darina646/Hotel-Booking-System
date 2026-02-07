package repositories;

import data.IDB;
import entity.FullBookingDescription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingDetailsRepository implements IBookingDetailsRepository {

    private final IDB db;

    public BookingDetailsRepository(IDB db) {
        this.db = db;
    }

    @Override
    public FullBookingDescription getFullBookingDescription(int bookingId) {
        String sql = """
            SELECT
                b.id AS booking_id,
                b.arrival_date,
                b.departure_date,
                b.total_price,
                g.id AS guest_id,
                g.full_name AS guest_name,
                g.email AS guest_email,
                r.id AS room_id,
                r.number AS room_number,
                r.price_per_night
            FROM bookings b
            JOIN guests g ON b.guest_id = g.id
            JOIN rooms r ON b.room_id = r.id
            WHERE b.id = ?
        """;

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, bookingId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new FullBookingDescription(
                        rs.getInt("booking_id"),
                        rs.getDate("arrival_date").toLocalDate(),
                        rs.getDate("departure_date").toLocalDate(),
                        rs.getDouble("total_price"),
                        rs.getInt("guest_id"),
                        rs.getString("guest_name"),
                        rs.getString("guest_email"),
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getString("price_per_night")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
