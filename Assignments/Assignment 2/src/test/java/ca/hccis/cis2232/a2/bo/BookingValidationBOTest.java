package ca.hccis.cis2232.a2.bo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the booking input validation rules.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 2/10/2026
 *
 * @modifiedby Claude (AI) 2026-10-03 Renamed test methods to lowerCamelCase and added a
 * javadoc to each test, per the CIS Programming Standards.
 */
class BookingValidationBOTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 10, 2);

    /**
     * Text with characters is not blank.
     *
     * @param value the value to check
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    @ParameterizedTest
    @ValueSource(strings = {"Smith Party", " a "})
    @DisplayName("Text with characters is not blank")
    void isNotBlankTextTrue(String value) {
        assertTrue(BookingValidationBO.isNotBlank(value));
    }

    /**
     * Null, empty and spaces only are blank.
     *
     * @param value the value to check
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Null, empty and spaces only are blank")
    void isNotBlankBlankFalse(String value) {
        assertFalse(BookingValidationBO.isNotBlank(value));
    }

    /**
     * Real dates from today onward are valid.
     *
     * @param value the value to check
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    @ParameterizedTest
    @ValueSource(strings = {"2026-10-02", "2026-12-31", "2028-02-29", " 2027-01-15 "})
    @DisplayName("Real dates from today onward are valid")
    void isValidBookingDateTodayOrLaterTrue(String value) {
        assertTrue(BookingValidationBO.isValidBookingDate(value, TODAY));
    }

    /**
     * Past, impossible and badly formatted dates are invalid.
     *
     * @param value the value to check
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"2026-10-01", "2026-02-30", "2027-02-29", "2026-13-01",
            "2026 10 31", "31-10-2026", "2026-10-5", "tomorrow"})
    @DisplayName("Past, impossible and badly formatted dates are invalid")
    void isValidBookingDateInvalidFalse(String value) {
        assertFalse(BookingValidationBO.isValidBookingDate(value, TODAY));
    }

    /**
     * 24 hour hh:mm times are valid.
     *
     * @param value the value to check
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    @ParameterizedTest
    @ValueSource(strings = {"00:00", "09:30", "14:05", "23:59"})
    @DisplayName("24 hour hh:mm times are valid")
    void isValidTimeValidTrue(String value) {
        assertTrue(BookingValidationBO.isValidTime(value));
    }

    /**
     * Out of range or badly formatted times are invalid.
     *
     * @param value the value to check
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"24:00", "12:60", "9:30", "2pm", "14.30", "1430"})
    @DisplayName("Out of range or badly formatted times are invalid")
    void isValidTimeInvalidFalse(String value) {
        assertFalse(BookingValidationBO.isValidTime(value));
    }

    /**
     * An end time after the start time is valid.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    @Test
    @DisplayName("An end time after the start time is valid")
    void isEndAfterStartLaterTrue() {
        assertTrue(BookingValidationBO.isEndAfterStart("13:00", "15:30"));
    }

    /**
     * An end time equal to or before the start time is invalid.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    @Test
    @DisplayName("An end time equal to or before the start time is invalid")
    void isEndAfterStartNotLaterFalse() {
        assertFalse(BookingValidationBO.isEndAfterStart("13:00", "13:00"));
        assertFalse(BookingValidationBO.isEndAfterStart("13:00", "12:59"));
    }

    /**
     * An invalid start or end time fails the check.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    @Test
    @DisplayName("An invalid start or end time fails the check")
    void isEndAfterStartInvalidTimeFalse() {
        assertFalse(BookingValidationBO.isEndAfterStart("13:00", "25:00"));
        assertFalse(BookingValidationBO.isEndAfterStart(null, "15:00"));
    }

    /**
     * 10 digit phone numbers are valid.
     *
     * @param value the value to check
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    @ParameterizedTest
    @ValueSource(strings = {"9025551234", "902-555-1234", "902 555 1234", "(902) 555-1234", "902.555.1234"})
    @DisplayName("10 digit phone numbers are valid")
    void isValidPhoneValidTrue(String value) {
        assertTrue(BookingValidationBO.isValidPhone(value));
    }

    /**
     * Short, long and non numeric phone numbers are invalid.
     *
     * @param value the value to check
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"555-1234", "902-555-12345", "902-555-abcd", "phone"})
    @DisplayName("Short, long and non numeric phone numbers are invalid")
    void isValidPhoneInvalidFalse(String value) {
        assertFalse(BookingValidationBO.isValidPhone(value));
    }

    /**
     * Email addresses with a name, @ and domain are valid.
     *
     * @param value the value to check
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    @ParameterizedTest
    @ValueSource(strings = {"alex@example.com", "a.b+c@hollandcollege.ca", " name@mail.co "})
    @DisplayName("Email addresses with a name, @ and domain are valid")
    void isValidEmailValidTrue(String value) {
        assertTrue(BookingValidationBO.isValidEmail(value));
    }

    /**
     * Email addresses missing a part are invalid.
     *
     * @param value the value to check
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"alex", "alex@", "@example.com", "alex@example", "al ex@example.com"})
    @DisplayName("Email addresses missing a part are invalid")
    void isValidEmailInvalidFalse(String value) {
        assertFalse(BookingValidationBO.isValidEmail(value));
    }
}
