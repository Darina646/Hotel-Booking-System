import application.HotelApplication;
import controllers.HotelController;
import data.IDB;
import factory.RepositoryFactory;
import repositories.*;
import service.AuthenticationService;
import service.BookingService;
import util.DatabaseConnectionManager;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/hotel_booking_system";
        String username = "postgres";
        String password = "0000";

        IDB database = DatabaseConnectionManager.getInstance(url, username, password);

        RepositoryFactory factory = new RepositoryFactory(database);

        IBookingRepository bookingRepository = factory.createBookingRepository();
        IGuestRepository guestRepository = factory.createGuestRepository();
        IRoomRepository roomRepository = factory.createRoomRepository();
        IRoomCategoryRepository categoryRepository = factory.createRoomCategoryRepository();
        IUserRepository userRepository = factory.createUserRepository();

        AuthenticationService authService = new AuthenticationService(userRepository);

        BookingService bookingService = new BookingService(
                bookingRepository,
                guestRepository,
                roomRepository,
                authService
        );

        HotelController controller = new HotelController(
                bookingService,
                guestRepository,
                roomRepository,
                categoryRepository,
                userRepository,
                authService
        );

        HotelApplication app = new HotelApplication(controller);
        app.start();
    }
}
