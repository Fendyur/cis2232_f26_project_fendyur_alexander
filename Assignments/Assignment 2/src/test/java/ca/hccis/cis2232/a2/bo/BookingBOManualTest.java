package ca.hccis.cis2232.a2.bo;

import ca.hccis.cis2232.a2.data.Booking;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *A series of tests to check app functionality
 *
 * @author Alexander Fendyur
 * @since 2/10/2027
 */
class BookingBOTest{
    private static final double DELTA = 0.01;

    /**
     * Party Room + equipment costs, with tax added onto the final cost.
     */
    @Test
    void calcPartyRoomEquipmentPlusTax(){
        Booking booking = new Booking();
        booking.setRoomType("Party Room");
        booking.setEquipmentNeeded(true);

        double actual = BookingBO.calculate(booking);

        assertEquals(258.75, actual, DELTA);
    }

    /**
     * The gym rat special!
     * Gym + Birthday costs + equipment costs, with tax added on to the final cost.
     * Compares against the base price of a Gym rental to ensure extras and taxes increase the total cost.
     */
    @Test
    void calcGymEquipmentBirthdayPlusTax(){
        Booking booking = new Booking();
        booking.setRoomType("Gym");
        booking.setBirthday(true);
        booking.setEquipmentNeeded(true);

        double actual = BookingBO.calculate(booking);

        assertEquals(258.75, actual, DELTA);
        assertEquals(150.00, booking.getBasePrice(), DELTA);
        assertTrue(actual > booking.getBasePrice());
    }

    /**
     * Ensuring an error is thrown when no room type is selected.
     */
    @Test
    void noRoomType(){
        Booking booking = new Booking();
        booking.setRoomType(null);

        assertThrows(IllegalArgumentException.class, () -> BookingBO.calculate(booking));
    }
}
