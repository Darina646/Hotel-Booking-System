package repositories;

import data.IDB;
import entity.RoomCategory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomCategoryRepository implements IRoomCategoryRepository {
    private final IDB db;

    public RoomCategoryRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createCategory(RoomCategory category) {
        String sql = "INSERT INTO room_categories (name, description, base_price) VALUES (?, ?, ?) RETURNING id";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, category.getName());
            st.setString(2, category.getDescription());
            st.setDouble(3, category.getBasePrice());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                category.setId(rs.getInt("id"));
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Error creating category: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<RoomCategory> getCategoryById(int id) {
        String sql = "SELECT * FROM room_categories WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCategory(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving category: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<RoomCategory> getAllCategories() {
        List<RoomCategory> categories = new ArrayList<>();
        String sql = "SELECT * FROM room_categories ORDER BY base_price";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving all categories: " + e.getMessage());
        }
        return categories;
    }

    @Override
    public boolean updateCategory(RoomCategory category) {
        String sql = "UPDATE room_categories SET name = ?, description = ?, base_price = ? WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, category.getName());
            st.setString(2, category.getDescription());
            st.setDouble(3, category.getBasePrice());
            st.setInt(4, category.getId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating category: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM room_categories WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting category: " + e.getMessage());
            return false;
        }
    }

    private RoomCategory mapResultSetToCategory(ResultSet rs) throws SQLException {
        RoomCategory category = new RoomCategory();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setBasePrice(rs.getDouble("base_price"));
        return category;
    }
}
