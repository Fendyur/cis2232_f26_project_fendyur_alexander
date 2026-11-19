package ca.hccis.squash.bo;

import ca.hccis.squash.jpa.entity.Booking;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the booking business validation (real calendar date, date
 * today or later on add / any date on edit, end time after start time). A fixed
 * "today" is used so the tests do not depend on the date they are run.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 19/11/2026
 */
class BookingValidationBOTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 11, 19);
    private static final boolean ADD = true;
    private static final boolean EDIT = false;

    /**
     * Build a valid booking for the given date and times.
     *
     * @param bookingDate Booking date
     * @param startTime   Start time
     * @param endTime     End time
     * @return Booking
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    private Booking buildBooking(String bookingDate, String startTime, String endTime) {
        Booking booking = new Booking();
        booking.setBookingDate(bookingDate);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        return booking;
    }

    /**
     * On add, today and future dates are allowed.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void addAllowsTodayAndFutureDates() {
        assertNull(BookingValidationBO.validateBookingDate("2026-11-19", ADD, TODAY));
        assertNull(BookingValidationBO.validateBookingDate("2026-11-20", ADD, TODAY));
        assertNull(BookingValidationBO.validateBookingDate("2028-02-29", ADD, TODAY));
    }

    /**
     * On add, a past date is rejected.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void addRejectsPastDate() {
        assertEquals(BookingValidationBO.MESSAGE_DATE_IN_PAST,
                BookingValidationBO.validateBookingDate("2026-11-18", ADD, TODAY));
        assertEquals(BookingValidationBO.MESSAGE_DATE_IN_PAST,
                BookingValidationBO.validateBookingDate("2026-10-03", ADD, TODAY));
    }

    /**
     * On edit, any real date is allowed, including past dates (sample data).
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void editAllowsPastDate() {
        assertNull(BookingValidationBO.validateBookingDate("2026-10-03", EDIT, TODAY));
        assertNull(BookingValidationBO.validateBookingDate("2020-01-01", EDIT, TODAY));
        assertNull(BookingValidationBO.validateBookingDate("2027-06-15", EDIT, TODAY));
    }

    /**
     * Dates that are not on the calendar are rejected on add and on edit.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void invalidCalendarDateRejected() {
        assertEquals(BookingValidationBO.MESSAGE_DATE_NOT_REAL,
                BookingValidationBO.validateBookingDate("2027-02-30", ADD, TODAY));
        assertEquals(BookingValidationBO.MESSAGE_DATE_NOT_REAL,
                BookingValidationBO.validateBookingDate("2027-02-30", EDIT, TODAY));
        assertEquals(BookingValidationBO.MESSAGE_DATE_NOT_REAL,
                BookingValidationBO.validateBookingDate("2027-02-29", ADD, TODAY)); //not a leap year
        assertEquals(BookingValidationBO.MESSAGE_DATE_NOT_REAL,
                BookingValidationBO.validateBookingDate("2027-13-01", EDIT, TODAY));
        assertEquals(BookingValidationBO.MESSAGE_DATE_NOT_REAL,
                BookingValidationBO.validateBookingDate(null, EDIT, TODAY));
    }

    /**
     * The end time must be after the start time.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void endTimeMustBeAfterStartTime() {
        assertNull(BookingValidationBO.validateEndTime("13:00", "16:00"));
        assertNull(BookingValidationBO.validateEndTime("09:59", "10:00"));
        assertEquals(BookingValidationBO.MESSAGE_END_NOT_AFTER_START,
                BookingValidationBO.validateEndTime("16:00", "13:00"));
        assertEquals(BookingValidationBO.MESSAGE_END_NOT_AFTER_START,
                BookingValidationBO.validateEndTime("13:00", "13:00"));
        assertEquals(BookingValidationBO.MESSAGE_TIME_NOT_REAL,
                BookingValidationBO.validateEndTime("25:00", "13:00"));
    }

    /**
     * validateBooking adds the errors to the binding result on the right fields.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void validateBookingAddsFieldErrors() {
        Booking booking = buildBooking("2026-11-01", "18:00", "17:00");
        BindingResult bindingResult = new BeanPropertyBindingResult(booking, "booking");

        int errorCount = BookingValidationBO.validateBooking(booking, ADD, TODAY, bindingResult);

        assertEquals(2, errorCount);
        assertEquals(BookingValidationBO.MESSAGE_DATE_IN_PAST,
                bindingResult.getFieldError("bookingDate").getDefaultMessage());
        assertEquals(BookingValidationBO.MESSAGE_END_NOT_AFTER_START,
                bindingResult.getFieldError("endTime").getDefaultMessage());
    }

    /**
     * The same past booking is valid when it is edited.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void validateBookingEditOfPastBookingIsValid() {
        Booking booking = buildBooking("2026-10-03", "13:00", "16:00");
        BindingResult bindingResult = new BeanPropertyBindingResult(booking, "booking");

        int errorCount = BookingValidationBO.validateBooking(booking, EDIT, TODAY, bindingResult);

        assertEquals(0, errorCount);
        assertFalse(bindingResult.hasErrors());
    }

    /**
     * A business rule is skipped when its field already has an annotation error,
     * so only one message is shown for the field.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void validateBookingSkipsFieldsWithExistingErrors() {
        Booking booking = buildBooking("bad", "18:00", "17:00");
        BindingResult bindingResult = new BeanPropertyBindingResult(booking, "booking");
        bindingResult.rejectValue("bookingDate", "Pattern", "Booking date must be in the format yyyy-mm-dd");

        int errorCount = BookingValidationBO.validateBooking(booking, ADD, TODAY, bindingResult);

        assertEquals(1, errorCount); //only the end time error was added
        assertEquals(1, bindingResult.getFieldErrorCount("bookingDate"));
        assertTrue(bindingResult.hasFieldErrors("endTime"));
    }
}
