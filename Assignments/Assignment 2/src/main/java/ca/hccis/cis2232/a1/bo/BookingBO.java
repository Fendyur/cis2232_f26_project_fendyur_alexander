package ca.hccis.cis2232.a1.bo;

import ca.hccis.cis2232.a1.data.Booking;

/**
 * Business logic for Sport and Rec Rentals bookings.
 *
 * @author Alexander Fendyur
 * @since 2/10/2026
 */
public class BookingBO {

    /**
     * Calculate the total cost of a booking.
     *
     * @param booking the booking to price
     * @return the total cost including tax
     */
    public static double calculate(Booking booking) {
        double total = 150 * 1.15;
        booking.setTotalPrice(total);
        return total;
    }
}
