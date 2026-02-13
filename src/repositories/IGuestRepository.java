package repositories;

import entity.Guest;
import java.util.List;
import java.util.Optional;

public interface IGuestRepository {
    boolean createGuest(Guest guest);
    Optional<Guest> getGuestById(int id);
    Optional<Guest> getGuestByEmail(String email);
    List<Guest> getAllGuests();
    boolean updateGuest(Guest guest);
    boolean deleteGuest(int id);
}
