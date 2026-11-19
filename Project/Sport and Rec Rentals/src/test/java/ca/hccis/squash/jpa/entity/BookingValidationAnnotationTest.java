package ca.hccis.squash.jpa.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Jakarta Bean Validation annotations on the Booking entity
 * using a jakarta.validation.Validator (no Spring context or database needed).
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 19/11/2026
 */
class BookingValidationAnnotationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    /**
     * Create the validator once for all of the tests.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    /**
     * Close the validator factory.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    /**
     * Build a booking that passes all of the annotation validation.
     *
     * @return Valid booking
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    private Booking buildValidBooking() {
        Booking booking = new Booking();
        booking.setRoomType("Party Room");
        booking.setBookingDate("2027-01-15");
        booking.setStartTime("13:00");
        booking.setEndTime("16:00");
        booking.setGroupNum(12);
        booking.setBookingName("Jane Doe");
        booking.setPhone("902-555-1234");
        booking.setEmail("jane.doe@example.com");
        booking.setBirthday(true);
        return booking;
    }

    /**
     * Validate a booking and check that exactly one field has errors.
     *
     * @param booking   Booking to validate
     * @param fieldName Field expected to fail
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    private void assertOnlyFieldFails(Booking booking, String fieldName) {
        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);
        assertTrue(!violations.isEmpty(), "Expected a violation on " + fieldName);
        for (ConstraintViolation<Booking> violation : violations) {
            assertEquals(fieldName, violation.getPropertyPath().toString(), violation.getMessage());
        }
    }

    /**
     * A booking with valid values has no violations.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void validBookingPasses() {
        assertTrue(validator.validate(buildValidBooking()).isEmpty());
    }

    /**
     * Every room type and phone format accepted by the form passes.
     *
     * @param phone Phone number to check
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @ParameterizedTest
    @ValueSource(strings = {"902-555-1234", "9025551234", "(902) 555-1234", "902.555.1234", "902 555 1234"})
    void validPhoneFormatsPass(String phone) {
        Booking booking = buildValidBooking();
        booking.setPhone(phone);
        assertTrue(validator.validate(booking).isEmpty());
    }

    /**
     * Each of the three rooms is a valid room type.
     *
     * @param roomType Room type to check
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @ParameterizedTest
    @ValueSource(strings = {"Party Room", "Gym", "Small Rec Room"})
    void validRoomTypesPass(String roomType) {
        Booking booking = buildValidBooking();
        booking.setRoomType(roomType);
        assertTrue(validator.validate(booking).isEmpty());
    }

    /**
     * A blank booking name fails with only the "required" message.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void blankNameFails() {
        Booking booking = buildValidBooking();
        booking.setBookingName("  ");
        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);
        assertEquals(1, violations.size());
        assertEquals("Booking name is required", violations.iterator().next().getMessage());
    }

    /**
     * Badly formed emails fail.
     *
     * @param email Email to check
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @ParameterizedTest
    @ValueSource(strings = {"not-an-email", "jane@", "jane@example", "jane doe@example.com"})
    void badEmailFails(String email) {
        Booking booking = buildValidBooking();
        booking.setEmail(email);
        assertOnlyFieldFails(booking, "email");
    }

    /**
     * Phone numbers that are not 10 digits fail.
     *
     * @param phone Phone number to check
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @ParameterizedTest
    @ValueSource(strings = {"555-1234", "902-555-12345", "abc-def-ghij", "902/555/1234"})
    void badPhoneFails(String phone) {
        Booking booking = buildValidBooking();
        booking.setPhone(phone);
        assertOnlyFieldFails(booking, "phone");
    }

    /**
     * A group size of 0 (or missing, or too large) fails.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void badGroupNumFails() {
        Booking booking = buildValidBooking();
        booking.setGroupNum(0);
        assertOnlyFieldFails(booking, "groupNum");
        booking.setGroupNum(null);
        assertOnlyFieldFails(booking, "groupNum");
        booking.setGroupNum(Booking.MAX_GROUP_SIZE + 1);
        assertOnlyFieldFails(booking, "groupNum");
    }

    /**
     * A room that is not one of the three rooms fails.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void badRoomTypeFails() {
        Booking booking = buildValidBooking();
        booking.setRoomType("Swimming Pool");
        assertOnlyFieldFails(booking, "roomType");
        booking.setRoomType("");
        assertOnlyFieldFails(booking, "roomType");
    }

    /**
     * Badly formatted dates and times fail the patterns.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void badDateAndTimeFormatsFail() {
        Booking booking = buildValidBooking();
        booking.setBookingDate("15/01/2027");
        assertOnlyFieldFails(booking, "bookingDate");

        booking = buildValidBooking();
        booking.setStartTime("24:00");
        assertOnlyFieldFails(booking, "startTime");

        booking = buildValidBooking();
        booking.setEndTime("4pm");
        assertOnlyFieldFails(booking, "endTime");
    }
}
