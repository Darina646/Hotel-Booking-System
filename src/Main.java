import controllers.BookingController;
import data.PostgresDB;
import repositories.BookingRepository;
import application.MyApplication;


public class Main {
    public static void main(String[] args) {

        PostgresDB db = new PostgresDB(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "0000"
        );

        BookingRepository repo = new BookingRepository(db);
        BookingController controller = new BookingController(repo);

        MyApplication app = new MyApplication(controller);
        app.start();
    }
}