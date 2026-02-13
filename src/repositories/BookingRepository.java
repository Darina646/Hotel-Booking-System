package repositories;

import data.IDB;
import dto.BookingDetailsDTO;
import entity.Booking;
import entity.Guest;
import entity.Room;
import entity.RoomCategory;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingRepository implements IBookingRepository {
    private final IDB db;

    public BookingRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createBooking(Booking booking) {
        String sql = "INSERT INTO bookings (guest_id, room_id, start_date, end_date, total_price, status, created_by) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, booking.getGuestId());
            st.setInt(2, booking.getRoomId());
            st.setDate(3, Date.valueOf(booking.getStartDate()));
            st.setDate(4, Date.valueOf(booking.getEndDate()));
            st.setDouble(5, booking.getTotalPrice());
            st.setString(6, booking.getStatus());
            if (booking.getCreatedBy() != null) {
                st.setInt(7, booking.getCreatedBy());
            } else {
                st.setNull(7, Types.INTEGER);
            }
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                booking.setId(rs.getInt("id"));
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Error creating booking: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Booking> getBookingById(int id) {
        String sql = """
            SELECT b.*, 
                   g.name as guest_name, g.email as guest_email, g.phone as guest_phone,
                   r.room_number, r.capacity, r.price as room_price,
                   c.name as category_name, c.description as category_description
            FROM bookings b
            JOIN guests g ON b.guest_id = g.id
            JOIN rooms r ON b.room_id = r.id
            JOIN room_categories c ON r.category_id = c.id
            WHERE b.id = ?
        """;
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving booking: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<BookingDetailsDTO> getFullBookingDetails(int bookingId) {
        String sql = """
            SELECT b.id as booking_id, b.start_date, b.end_date, b.total_price, b.status,
                   g.name as guest_name, g.email as guest_email, g.phone as guest_phone,
                   r.room_number, r.capacity,
                   c.name as category_name,
                   u.username as created_by_username
            FROM bookings b
            JOIN guests g ON b.guest_id = g.id
            JOIN rooms r ON b.room_id = r.id
            JOIN room_categories c ON r.category_id = c.id
            LEFT JOIN users u ON b.created_by = u.id
            WHERE b.id = ?
        """;
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, bookingId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                BookingDetailsDTO dto = new BookingDetailsDTO();
                dto.setBookingId(rs.getInt("booking_id"));
                dto.setGuestName(rs.getString("guest_name"));
                dto.setGuestEmail(rs.getString("guest_email"));
                dto.setGuestPhone(rs.getString("guest_phone"));
                dto.setRoomNumber(rs.getString("room_number"));
                dto.setCategoryName(rs.getString("category_name"));
                dto.setCapacity(rs.getInt("capacity"));
                dto.setStartDate(rs.getDate("start_date").toLocalDate());
                dto.setEndDate(rs.getDate("end_date").toLocalDate());
                dto.setTotalPrice(rs.getDouble("total_price"));
                dto.setStatus(rs.getString("status"));
                dto.setCreatedByUsername(rs.getString("created_by_username"));
                return Optional.of(dto);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving full booking details: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, 
                   g.name as guest_name, g.email as guest_email, g.phone as guest_phone,
                   r.room_number, r.capacity, r.price as room_price,
                   c.name as category_name, c.description as category_description
            FROM bookings b
            JOIN guests g ON b.guest_id = g.id
            JOIN rooms r ON b.room_id = r.id
            JOIN room_categories c ON r.category_id = c.id
            ORDER BY b.start_date DESC
        """;
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving all bookings: " + e.getMessage());
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByGuest(int guestId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, 
                   g.name as guest_name, g.email as guest_email, g.phone as guest_phone,
                   r.room_number, r.capacity, r.price as room_price,
                   c.name as category_name, c.description as category_description
            FROM bookings b
            JOIN guests g ON b.guest_id = g.id
            JOIN rooms r ON b.room_id = r.id
            JOIN room_categories c ON r.category_id = c.id
            WHERE b.guest_id = ?
            ORDER BY b.start_date DESC
        """;
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, guestId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving bookings by guest: " + e.getMessage());
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByRoom(int roomId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, 
                   g.name as guest_name, g.email as guest_email, g.phone as guest_phone,
                   r.room_number, r.capacity, r.price as room_price,
                   c.name as category_name, c.description as category_description
            FROM bookings b
            JOIN guests g ON b.guest_id = g.id
            JOIN rooms r ON b.room_id = r.id
            JOIN room_categories c ON r.category_id = c.id
            WHERE b.room_id = ?
            ORDER BY b.start_date DESC
        """;
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, roomId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving bookings by room: " + e.getMessage());
        }
        return bookings;
    }

    @Override
    public boolean updateBooking(Booking booking) {
        String sql = "UPDATE bookings SET guest_id = ?, room_id = ?, start_date = ?, end_date = ?, total_price = ?, status = ? WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, booking.getGuestId());
            st.setInt(2, booking.getRoomId());
            st.setDate(3, Date.valueOf(booking.getStartDate()));
            st.setDate(4, Date.valueOf(booking.getEndDate()));
            st.setDouble(5, booking.getTotalPrice());
            st.setString(6, booking.getStatus());
            st.setInt(7, booking.getId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating booking: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean cancelBooking(int id) {
        String sql = "UPDATE bookings SET status = 'CANCELLED' WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error cancelling booking: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isRoomAvailable(int roomId, LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT COUNT(*) FROM bookings 
            WHERE room_id = ? 
            AND status != 'CANCELLED' 
            AND NOT (end_date <= ? OR start_date >= ?)
        """;
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, roomId);
            st.setDate(2, Date.valueOf(startDate));
            st.setDate(3, Date.valueOf(endDate));
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking room availability: " + e.getMessage());
        }
        return false;
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setGuestId(rs.getInt("guest_id"));
        booking.setRoomId(rs.getInt("room_id"));
        booking.setStartDate(rs.getDate("start_date").toLocalDate());
        booking.setEndDate(rs.getDate("end_date").toLocalDate());
        booking.setTotalPrice(rs.getDouble("total_price"));
        booking.setStatus(rs.getString("status"));

        int createdBy = rs.getInt("created_by");
        if (!rs.wasNull()) {
            booking.setCreatedBy(createdBy);
        }

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            booking.setCreatedAt(createdAt.toLocalDateTime());
        }

        Guest guest = new Guest();
        guest.setId(rs.getInt("guest_id"));
        guest.setName(rs.getString("guest_name"));
        guest.setEmail(rs.getString("guest_email"));
        guest.setPhone(rs.getString("guest_phone"));
        booking.setGuest(guest);

        Room room = new Room();
        room.setId(rs.getInt("room_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setCapacity(rs.getInt("capacity"));
        room.setPrice(rs.getDouble("room_price"));

        RoomCategory category = new RoomCategory();
        category.setName(rs.getString("category_name"));
        category.setDescription(rs.getString("category_description"));
        room.setCategory(category);

        booking.setRoom(room);

        return booking;
    }
}
