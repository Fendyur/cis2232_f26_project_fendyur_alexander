package ca.hccis.cis2232.a1.bo;

import ca.hccis.cis2232.a1.data.Booking;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
