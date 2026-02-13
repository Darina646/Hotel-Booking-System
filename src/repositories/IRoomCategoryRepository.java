package repositories;

import entity.RoomCategory;
import java.util.List;
import java.util.Optional;

public interface IRoomCategoryRepository {
    boolean createCategory(RoomCategory category);
    Optional<RoomCategory> getCategoryById(int id);
    List<RoomCategory> getAllCategories();
    boolean updateCategory(RoomCategory category);
    boolean deleteCategory(int id);
}
