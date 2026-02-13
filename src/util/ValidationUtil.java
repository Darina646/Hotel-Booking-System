package util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone == null || phone.isEmpty() || PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() >= 2;
    }

    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return !startDate.isBefore(today) && startDate.isBefore(endDate);
    }

    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    public static boolean isValidCapacity(int capacity) {
        return capacity > 0 && capacity <= 10;
    }

    public static boolean isValidRoomNumber(String roomNumber) {
        return roomNumber != null && !roomNumber.trim().isEmpty();
    }
}