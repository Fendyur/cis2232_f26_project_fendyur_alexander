package ca.hccis.cis2232.a2.bo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

/**
 * Validation rules for the values entered on a booking. Each method only
 * checks a value, so the rules can be reused by any user interface and
 * unit tested without prompting.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 2/10/2026
 */
public class BookingValidationBO {

    /** Format of a booking date, for example 2026-10-31. */
    public static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
    /** Format of a booking time on a 24 hour clock, for example 14:30. */
    public static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    /** A 10 digit phone number, optionally split with spaces, dashes, dots or brackets. */
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\(?\\d{3}\\)?[- .]?\\d{3}[- .]?\\d{4}$");
    /** Something before an @, a domain, a dot and an ending, with no spaces. */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    /**
     * Prevent creating instances; all methods are static.
     */
    private BookingValidationBO() {
    }

    /**
     * Check that a value has text in it.
     *
     * @param value the value entered
     * @return true if the value is not null and not only spaces
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    public static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * Check that a booking date is a real date in yyyy-mm-dd format and is
     * not before today.
     *
     * @param value the date entered
     * @param today the current date
     * @return true if the date is valid and today or later
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    public static boolean isValidBookingDate(String value, LocalDate today) {
        LocalDate date = parseDate(value);
        return date != null && !date.isBefore(today);
    }

    /**
     * Check that a time is in hh:mm format on a 24 hour clock.
     *
     * @param value the time entered
     * @return true if the time is valid
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    public static boolean isValidTime(String value) {
        return parseTime(value) != null;
    }

    /**
     * Check that an end time is a valid time after the start time.
     *
     * @param startTime the start time entered
     * @param endTime   the end time entered
     * @return true if both times are valid and the end is after the start
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    public static boolean isEndAfterStart(String startTime, String endTime) {
        LocalTime start = parseTime(startTime);
        LocalTime end = parseTime(endTime);
        return start != null && end != null && end.isAfter(start);
    }

    /**
     * Check that a phone number has 10 digits.
     *
     * @param value the phone number entered
     * @return true if the phone number is valid
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    public static boolean isValidPhone(String value) {
        return value != null && PHONE_PATTERN.matcher(value.trim()).matches();
    }

    /**
     * Check that an email address has a name, an @ and a domain.
     *
     * @param value the email entered
     * @return true if the email is valid
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    public static boolean isValidEmail(String value) {
        return value != null && EMAIL_PATTERN.matcher(value.trim()).matches();
    }

    /**
     * Parse a date, returning null if it is not a valid yyyy-mm-dd date.
     *
     * @param value the date entered
     * @return the date, or null if invalid
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    private static LocalDate parseDate(String value) {
        if (value == null) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim(), DATE_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Parse a time, returning null if it is not a valid hh:mm time.
     *
     * @param value the time entered
     * @return the time, or null if invalid
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    private static LocalTime parseTime(String value) {
        if (value == null) {
            return null;
        }
        try {
            return LocalTime.parse(value.trim(), TIME_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
