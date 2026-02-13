package factory;

import data.IDB;
import repositories.*;

public class RepositoryFactory {
    private final IDB database;

    public RepositoryFactory(IDB database) {
        this.database = database;
    }

    public IBookingRepository createBookingRepository() {
        return new BookingRepository(database);
    }

    public IGuestRepository createGuestRepository() {
        return new GuestRepository(database);
    }

    public IRoomRepository createRoomRepository() {
        return new RoomRepository(database);
    }

    public IUserRepository createUserRepository() {
        return new UserRepository(database);
    }

    public IRoomCategoryRepository createRoomCategoryRepository() {
        return new RoomCategoryRepository(database);
    }
}
