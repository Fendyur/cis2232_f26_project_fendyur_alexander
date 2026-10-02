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
 */
class BookingValidationBOTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 10, 2);

    @ParameterizedTest
    @ValueSource(strings = {"Smith Party", " a "})
    @DisplayName("Text with characters is not blank")
    void isNotBlank_text_true(String value) {
        assertTrue(BookingValidationBO.isNotBlank(value));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Null, empty and spaces only are blank")
    void isNotBlank_blank_false(String value) {
        assertFalse(BookingValidationBO.isNotBlank(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2026-10-02", "2026-12-31", "2028-02-29", " 2027-01-15 "})
    @DisplayName("Real dates from today onward are valid")
    void isValidBookingDate_todayOrLater_true(String value) {
        assertTrue(BookingValidationBO.isValidBookingDate(value, TODAY));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"2026-10-01", "2026-02-30", "2027-02-29", "2026-13-01",
            "2026 10 31", "31-10-2026", "2026-10-5", "tomorrow"})
    @DisplayName("Past, impossible and badly formatted dates are invalid")
    void isValidBookingDate_invalid_false(String value) {
        assertFalse(BookingValidationBO.isValidBookingDate(value, TODAY));
    }

    @ParameterizedTest
    @ValueSource(strings = {"00:00", "09:30", "14:05", "23:59"})
    @DisplayName("24 hour hh:mm times are valid")
    void isValidTime_valid_true(String value) {
        assertTrue(BookingValidationBO.isValidTime(value));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"24:00", "12:60", "9:30", "2pm", "14.30", "1430"})
    @DisplayName("Out of range or badly formatted times are invalid")
    void isValidTime_invalid_false(String value) {
        assertFalse(BookingValidationBO.isValidTime(value));
    }

    @Test
    @DisplayName("An end time after the start time is valid")
    void isEndAfterStart_later_true() {
        assertTrue(BookingValidationBO.isEndAfterStart("13:00", "15:30"));
    }

    @Test
    @DisplayName("An end time equal to or before the start time is invalid")
    void isEndAfterStart_notLater_false() {
        assertFalse(BookingValidationBO.isEndAfterStart("13:00", "13:00"));
        assertFalse(BookingValidationBO.isEndAfterStart("13:00", "12:59"));
    }

    @Test
    @DisplayName("An invalid start or end time fails the check")
    void isEndAfterStart_invalidTime_false() {
        assertFalse(BookingValidationBO.isEndAfterStart("13:00", "25:00"));
        assertFalse(BookingValidationBO.isEndAfterStart(null, "15:00"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"9025551234", "902-555-1234", "902 555 1234", "(902) 555-1234", "902.555.1234"})
    @DisplayName("10 digit phone numbers are valid")
    void isValidPhone_valid_true(String value) {
        assertTrue(BookingValidationBO.isValidPhone(value));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"555-1234", "902-555-12345", "902-555-abcd", "phone"})
    @DisplayName("Short, long and non numeric phone numbers are invalid")
    void isValidPhone_invalid_false(String value) {
        assertFalse(BookingValidationBO.isValidPhone(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"alex@example.com", "a.b+c@hollandcollege.ca", " name@mail.co "})
    @DisplayName("Email addresses with a name, @ and domain are valid")
    void isValidEmail_valid_true(String value) {
        assertTrue(BookingValidationBO.isValidEmail(value));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"alex", "alex@", "@example.com", "alex@example", "al ex@example.com"})
    @DisplayName("Email addresses missing a part are invalid")
    void isValidEmail_invalid_false(String value) {
        assertFalse(BookingValidationBO.isValidEmail(value));
    }
}
