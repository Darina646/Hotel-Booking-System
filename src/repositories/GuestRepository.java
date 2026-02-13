package repositories;

import data.IDB;
import entity.Guest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuestRepository implements IGuestRepository {
    private final IDB db;

    public GuestRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createGuest(Guest guest) {
        String sql = "INSERT INTO guests (name, email, phone) VALUES (?, ?, ?) RETURNING id";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, guest.getName());
            st.setString(2, guest.getEmail());
            st.setString(3, guest.getPhone());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                guest.setId(rs.getInt("id"));
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Error creating guest: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Guest> getGuestById(int id) {
        String sql = "SELECT * FROM guests WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToGuest(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving guest: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Guest> getGuestByEmail(String email) {
        String sql = "SELECT * FROM guests WHERE email = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToGuest(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving guest by email: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests ORDER BY id";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                guests.add(mapResultSetToGuest(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving all guests: " + e.getMessage());
        }
        return guests;
    }

    @Override
    public boolean updateGuest(Guest guest) {
        String sql = "UPDATE guests SET name = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, guest.getName());
            st.setString(2, guest.getEmail());
            st.setString(3, guest.getPhone());
            st.setInt(4, guest.getId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating guest: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteGuest(int id) {
        String sql = "DELETE FROM guests WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting guest: " + e.getMessage());
            return false;
        }
    }

    private Guest mapResultSetToGuest(ResultSet rs) throws SQLException {
        Guest guest = new Guest();
        guest.setId(rs.getInt("id"));
        guest.setName(rs.getString("name"));
        guest.setEmail(rs.getString("email"));
        guest.setPhone(rs.getString("phone"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            guest.setCreatedAt(createdAt.toLocalDateTime());
        }
        return guest;
    }
}
