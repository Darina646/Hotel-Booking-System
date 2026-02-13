package repositories;

import data.IDB;
import entity.Room;
import entity.RoomCategory;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomRepository implements IRoomRepository {
    private final IDB db;

    public RoomRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createRoom(Room room) {
        String sql = "INSERT INTO rooms (room_number, category_id, capacity, price, is_available) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, room.getRoomNumber());
            st.setInt(2, room.getCategoryId());
            st.setInt(3, room.getCapacity());
            st.setDouble(4, room.getPrice());
            st.setBoolean(5, room.isAvailable());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                room.setId(rs.getInt("id"));
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Error creating room: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Room> getRoomById(int id) {
        String sql = """
            SELECT r.*, c.name as category_name, c.description as category_description, c.base_price 
            FROM rooms r 
            JOIN room_categories c ON r.category_id = c.id 
            WHERE r.id = ?
        """;
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving room: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = """
            SELECT r.*, c.name as category_name, c.description as category_description, c.base_price 
            FROM rooms r 
            JOIN room_categories c ON r.category_id = c.id 
            ORDER BY r.id
        """;
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving all rooms: " + e.getMessage());
        }
        return rooms;
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate startDate, LocalDate endDate) {
        List<Room> rooms = new ArrayList<>();
        String sql = """
            SELECT r.*, c.name as category_name, c.description as category_description, c.base_price 
            FROM rooms r 
            JOIN room_categories c ON r.category_id = c.id 
            WHERE r.is_available = true 
            AND r.id NOT IN (
                SELECT room_id FROM bookings 
                WHERE status != 'CANCELLED' 
                AND NOT (end_date <= ? OR start_date >= ?)
            )
            ORDER BY r.price
        """;
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setDate(1, Date.valueOf(startDate));
            st.setDate(2, Date.valueOf(endDate));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving available rooms: " + e.getMessage());
        }
        return rooms;
    }

    @Override
    public List<Room> getRoomsByCategory(int categoryId) {
        List<Room> rooms = new ArrayList<>();
        String sql = """
            SELECT r.*, c.name as category_name, c.description as category_description, c.base_price 
            FROM rooms r 
            JOIN room_categories c ON r.category_id = c.id 
            WHERE r.category_id = ? 
            ORDER BY r.room_number
        """;
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, categoryId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving rooms by category: " + e.getMessage());
        }
        return rooms;
    }

    @Override
    public boolean updateRoom(Room room) {
        String sql = "UPDATE rooms SET room_number = ?, category_id = ?, capacity = ?, price = ?, is_available = ? WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, room.getRoomNumber());
            st.setInt(2, room.getCategoryId());
            st.setInt(3, room.getCapacity());
            st.setDouble(4, room.getPrice());
            st.setBoolean(5, room.isAvailable());
            st.setInt(6, room.getId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating room: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteRoom(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting room: " + e.getMessage());
            return false;
        }
    }

    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getInt("id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setCategoryId(rs.getInt("category_id"));
        room.setCapacity(rs.getInt("capacity"));
        room.setPrice(rs.getDouble("price"));
        room.setAvailable(rs.getBoolean("is_available"));

        RoomCategory category = new RoomCategory();
        category.setId(rs.getInt("category_id"));
        category.setName(rs.getString("category_name"));
        category.setDescription(rs.getString("category_description"));
        category.setBasePrice(rs.getDouble("base_price"));
        room.setCategory(category);

        return room;
    }
}
