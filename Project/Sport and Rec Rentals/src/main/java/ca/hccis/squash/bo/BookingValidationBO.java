package ca.hccis.squash.bo;

import ca.hccis.squash.jpa.entity.Booking;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Business validation for a booking. These are the rules that the Bean
 * Validation annotations on the Booking entity can not express:
 * <ul>
 *     <li>the booking date must be a real calendar date (e.g. not 2027-02-30)</li>
 *     <li>a new booking (add) must be for today or later; an edited booking can
 *     have any real date because past bookings can be corrected</li>
 *     <li>the end time must be after the start time</li>
 * </ul>
 * Errors are added to the Spring Errors/BindingResult so they are shown next to
 * the field on the form like the annotation errors.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 19/11/2026
 */
public class BookingValidationBO {

    public static final String FIELD_BOOKING_DATE = "bookingDate";
    public static final String FIELD_END_TIME = "endTime";

    public static final String ERROR_CODE_BOOKING_DATE = "invalid.booking.bookingDate";
    public static final String ERROR_CODE_END_TIME = "invalid.booking.endTime";

    public static final String MESSAGE_DATE_NOT_REAL = "Booking date must be a real date (yyyy-mm-dd)";
    public static final String MESSAGE_DATE_IN_PAST = "Booking date can not be in the past for a new booking";
    public static final String MESSAGE_TIME_NOT_REAL = "Start and end times must be valid times (hh:mm)";
    public static final String MESSAGE_END_NOT_AFTER_START = "End time must be after the start time";

    //uuuu (not yyyy) so the STRICT resolver rejects dates like 2027-02-30
    private static final DateTimeFormatter BOOKING_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter BOOKING_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Validate the booking date: it must be a real calendar date and, for a new
     * booking, today or later.
     *
     * @param bookingDate Booking date entered (yyyy-mm-dd)
     * @param newBooking  true when adding a booking, false when editing one
     * @param today       Today's date (passed in so the rule can be tested)
     * @return Error message, or null if the date is valid
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    public static String validateBookingDate(String bookingDate, boolean newBooking, LocalDate today) {
        LocalDate localBookingDate;
        try {
            localBookingDate = LocalDate.parse(bookingDate, BOOKING_DATE_FORMATTER);
        } catch (DateTimeParseException | NullPointerException e) {
            return MESSAGE_DATE_NOT_REAL;
        }

        //Only new bookings have to be in the future; past bookings can be edited
        if (newBooking && localBookingDate.isBefore(today)) {
            return MESSAGE_DATE_IN_PAST;
        }
        return null;
    }

    /**
     * Validate that the end time is after the start time.
     *
     * @param startTime Start time entered (hh:mm, 24 hour)
     * @param endTime   End time entered (hh:mm, 24 hour)
     * @return Error message, or null if the end time is after the start time
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    public static String validateEndTime(String startTime, String endTime) {
        LocalTime localStartTime;
        LocalTime localEndTime;
        try {
            localStartTime = LocalTime.parse(startTime, BOOKING_TIME_FORMATTER);
            localEndTime = LocalTime.parse(endTime, BOOKING_TIME_FORMATTER);
        } catch (DateTimeParseException | NullPointerException e) {
            return MESSAGE_TIME_NOT_REAL;
        }

        if (!localEndTime.isAfter(localStartTime)) {
            return MESSAGE_END_NOT_AFTER_START;
        }
        return null;
    }

    /**
     * Run the business validation for a booking and add any errors to the
     * binding result. A rule is only checked when the fields it uses passed
     * the annotation validation, so the user does not see two messages for
     * the same problem.
     *
     * @param booking    Booking entered on the form
     * @param newBooking true when adding a booking, false when editing one
     * @param today      Today's date
     * @param errors     Binding result to add the errors to
     * @return Number of business validation errors added
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    public static int validateBooking(Booking booking, boolean newBooking, LocalDate today, Errors errors) {
        int errorCount = 0;

        //Booking date rules
        if (!errors.hasFieldErrors(FIELD_BOOKING_DATE)) {
            String dateError = validateBookingDate(booking.getBookingDate(), newBooking, today);
            if (dateError != null) {
                errors.rejectValue(FIELD_BOOKING_DATE, ERROR_CODE_BOOKING_DATE, dateError);
                errorCount++;
            }
        }

        //End time after start time (shown on the end time field)
        if (!errors.hasFieldErrors("startTime") && !errors.hasFieldErrors(FIELD_END_TIME)) {
            String timeError = validateEndTime(booking.getStartTime(), booking.getEndTime());
            if (timeError != null) {
                errors.rejectValue(FIELD_END_TIME, ERROR_CODE_END_TIME, timeError);
                errorCount++;
            }
        }
        return errorCount;
    }
}
