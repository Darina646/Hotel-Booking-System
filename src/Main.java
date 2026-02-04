import controllers.BookingController;
import data.PostgresDB;
import repositories.BookingRepository;
import repositories.BookingDetailsRepository;
import repositories.IBookingRepository;
import repositories.IBookingDetailsRepository;

public class Main {
    public static void main(String[] args) {

        PostgresDB db = PostgresDB.getInstance();

        IBookingRepository bookingRepo =
                new BookingRepository(db);

        IBookingDetailsRepository detailsRepo =
                new BookingDetailsRepository(db);

        BookingController controller =
                new BookingController(bookingRepo, detailsRepo);

        MyApplication app = new MyApplication(controller);
        app.start();
    }
}
