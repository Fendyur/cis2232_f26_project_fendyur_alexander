package ca.hccis.cis2232.a2.bo;

import ca.hccis.cis2232.a2.data.Booking;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for BookingBO.calculate, written test first.
 *
 * @author Alexander Fendyur
 * @since 2/10/2026
 */
class BookingBOTest {

    private static final double DELTA = 0.001;

    /**
     * A gym booking with no extras costs the gym price plus 15% tax.
     * <p>
     * Created following a Test Driven Development approach: test written
     * first (red), minimal code to pass (green), then refactored.
     */
    @Test
    void calculate_gymNoExtras_returnsBasePlusTax() {
        Booking booking = new Booking();
        booking.setRoomType("Gym");

        double actual = BookingBO.calculate(booking);

        assertEquals(172.50, actual, DELTA);
    }

    /**
     * A party room birthday booking with equipment adds both extras to the
     * room price before tax, and records the base price on the booking.
     * <p>
     * Created following a Test Driven Development approach: test written
     * first (red), minimal code to pass (green), then refactored.
     */
    @Test
    void calculate_partyRoomBirthdayWithEquipment_addsExtrasBeforeTax() {
        Booking booking = new Booking();
        booking.setRoomType("Party Room");
        booking.setBirthday(true);
        booking.setEquipmentNeeded(true);

        double actual = BookingBO.calculate(booking);

        assertEquals(316.25, actual, DELTA);
        assertEquals(200.00, booking.getBasePrice(), DELTA);
        assertTrue(actual > booking.getBasePrice());
    }

    /**
     * An unknown room type or a missing booking cannot be priced, so the
     * method rejects them instead of returning a wrong total.
     * <p>
     * Created following a Test Driven Development approach: test written
     * first (red), minimal code to pass (green), then refactored.
     */
    @Test
    void calculate_invalidInput_throwsIllegalArgumentException() {
        Booking booking = new Booking();
        booking.setRoomType("Hot Tub");

        assertThrows(IllegalArgumentException.class, () -> BookingBO.calculate(booking));
        assertThrows(IllegalArgumentException.class, () -> BookingBO.calculate(null));
    }
}
