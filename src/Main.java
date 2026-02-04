import controllers.BookingController;
import data.PostgresDB;
import repositories.BookingRepository;

public class Main {
    public static void main(String[] args) {

        // Using Singleton to get the PostgresDB instance
        PostgresDB db = PostgresDB.getInstance();

        BookingRepository repo = new BookingRepository(db);
        BookingController controller = new BookingController(repo);

        MyApplication app = new MyApplication(controller);
        app.start();
    }
}
