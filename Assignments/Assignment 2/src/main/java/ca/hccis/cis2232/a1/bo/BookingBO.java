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
        if (booking == null) {
            throw new IllegalArgumentException("Booking is required");
        }
        double basePrice;
        if ("Party Room".equals(booking.getRoomType())) {
            basePrice = 200;
        } else if ("Gym".equals(booking.getRoomType())) {
            basePrice = 150;
        } else {
            throw new IllegalArgumentException("Unknown room type: " + booking.getRoomType());
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
