import controllers.BookingController;
import data.PostgresDB;
import repositories.BookingRepository;

public class Main {
    public static void main(String[] args) {

        PostgresDB db = new PostgresDB(
                "jdbc:postgresql://localhost:5432/hotel_booking_system", "postgres", "0000");

        BookingRepository repo = new BookingRepository(db);
        BookingController controller = new BookingController(repo);

        MyApplication app = new MyApplication(controller);
        app.start();
    }
}
