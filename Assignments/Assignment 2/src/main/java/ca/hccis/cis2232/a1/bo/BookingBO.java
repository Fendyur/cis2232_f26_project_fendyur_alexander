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
        double basePrice;
        if (booking.getRoomType().equals("Party Room")) {
            basePrice = 200;
        } else {
            basePrice = 150;
        }
        booking.setBasePrice(basePrice);

        double subTotal = basePrice;
        if (booking.isBirthday()) {
            subTotal += 50;
        }
        if (booking.isEquipmentNeeded()) {
            subTotal += 25;
        }

        double total = subTotal * 1.15;
        booking.setTotalPrice(total);
        return total;
    }
}
