package repositories;

import entity.Room;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IRoomRepository {
    boolean createRoom(Room room);
    Optional<Room> getRoomById(int id);
    List<Room> getAllRooms();
    List<Room> getAvailableRooms(LocalDate startDate, LocalDate endDate);
    List<Room> getRoomsByCategory(int categoryId);
    boolean updateRoom(Room room);
    boolean deleteRoom(int id);
}
