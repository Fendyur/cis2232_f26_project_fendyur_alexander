package ca.hccis.squash.bo;

import ca.hccis.squash.jpa.entity.Booking;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the booking price calculation. Expected values are the same
 * as the sample bookings in db/mysql/createDatabase.sql. No database is needed.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 19/11/2026
 */
class BookingBOTest {

    private static final double DELTA = 0.0001;

    /**
     * Build a booking with the values that affect the price.
     *
     * @param roomType        Room type
     * @param birthday        Birthday add-on
     * @param equipmentNeeded Equipment add-on
     * @return Booking to calculate
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    private Booking buildBooking(String roomType, boolean birthday, boolean equipmentNeeded) {
        Booking booking = new Booking();
        booking.setRoomType(roomType);
        booking.setBirthday(birthday);
        booking.setEquipmentNeeded(equipmentNeeded);
        return booking;
    }

    /**
     * Every room x birthday x equipment combination (12 variations) gives the
     * expected base price, birthday cost, tax and total.
     *
     * @param roomType        Room type
     * @param birthday        Birthday add-on
     * @param equipmentNeeded Equipment add-on
     * @param basePrice       Expected base price
     * @param birthdayCost    Expected birthday cost
     * @param tax             Expected tax
     * @param totalPrice      Expected total price
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @ParameterizedTest(name = "{0} birthday={1} equipment={2} -> total {6}")
    @CsvSource({
            "Party Room,     false, false, 150.00,  0.00, 22.50, 172.50",
            "Party Room,     false, true,  150.00,  0.00, 22.50, 172.50",
            "Party Room,     true,  false, 150.00, 75.00, 33.75, 258.75",
            "Party Room,     true,  true,  150.00, 75.00, 33.75, 258.75",
            "Gym,            false, false, 200.00,  0.00, 30.00, 230.00",
            "Gym,            false, true,  200.00,  0.00, 30.00, 230.00",
            "Gym,            true,  false, 200.00, 75.00, 41.25, 316.25",
            "Gym,            true,  true,  200.00, 75.00, 41.25, 316.25",
            "Small Rec Room, false, false,  60.00,  0.00,  9.00,  69.00",
            "Small Rec Room, false, true,   60.00,  0.00,  9.00,  69.00",
            "Small Rec Room, true,  false,  60.00, 75.00, 20.25, 155.25",
            "Small Rec Room, true,  true,   60.00, 75.00, 20.25, 155.25"
    })
    void calculatePricesForEachVariation(String roomType, boolean birthday, boolean equipmentNeeded,
                                         double basePrice, double birthdayCost, double tax, double totalPrice) {
        Booking booking = BookingBO.calculatePrices(buildBooking(roomType, birthday, equipmentNeeded));
        assertEquals(basePrice, booking.getBasePrice(), DELTA);
        assertEquals(birthdayCost, booking.getBirthdayCost(), DELTA);
        assertEquals(tax, booking.getTax(), DELTA);
        assertEquals(totalPrice, booking.getTotalPrice(), DELTA);
    }

    /**
     * Sample row 1 (Jane Doe, Party Room, birthday, no equipment):
     * 150.00 / 75.00 / 33.75 / 258.75.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void calculatePricesMatchesSampleRowJaneDoe() {
        Booking booking = BookingBO.calculatePrices(buildBooking("Party Room", true, false));
        assertEquals(150.00, booking.getBasePrice(), DELTA);
        assertEquals(75.00, booking.getBirthdayCost(), DELTA);
        assertEquals(33.75, booking.getTax(), DELTA);
        assertEquals(258.75, booking.getTotalPrice(), DELTA);
    }

    /**
     * Sample row 4 (Sam Lee, Gym, birthday and equipment):
     * 200.00 / 75.00 / 41.25 / 316.25.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void calculatePricesMatchesSampleRowSamLee() {
        Booking booking = BookingBO.calculatePrices(buildBooking("Gym", true, true));
        assertEquals(200.00, booking.getBasePrice(), DELTA);
        assertEquals(75.00, booking.getBirthdayCost(), DELTA);
        assertEquals(41.25, booking.getTax(), DELTA);
        assertEquals(316.25, booking.getTotalPrice(), DELTA);
    }

    /**
     * Recalculating after the room is changed (edit) replaces the old prices.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void calculatePricesRecalculatesAfterRoomChange() {
        Booking booking = BookingBO.calculatePrices(buildBooking("Gym", false, true));
        assertEquals(230.00, booking.getTotalPrice(), DELTA);
        booking.setRoomType("Small Rec Room");
        Booking recalculated = BookingBO.calculatePrices(booking);
        assertSame(booking, recalculated);
        assertEquals(60.00, booking.getBasePrice(), DELTA);
        assertEquals(9.00, booking.getTax(), DELTA);
        assertEquals(69.00, booking.getTotalPrice(), DELTA);
    }

    /**
     * An unknown or missing room type can not be priced.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void getBasePriceRejectsUnknownRoom() {
        assertThrows(IllegalArgumentException.class, () -> BookingBO.getBasePrice("Pool"));
        assertThrows(IllegalArgumentException.class, () -> BookingBO.getBasePrice(null));
    }

    /**
     * Amounts are rounded half up to the cent.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Test
    void roundToCentsRoundsHalfUp() {
        assertEquals(33.75, BookingBO.roundToCents(33.749999999), DELTA);
        assertEquals(0.13, BookingBO.roundToCents(0.125), DELTA);
        assertEquals(10.12, BookingBO.roundToCents(10.124), DELTA);
    }
}
