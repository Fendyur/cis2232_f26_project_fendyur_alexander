package ca.hccis.cis2232.a1.bo;

import ca.hccis.cis2232.a1.data.Booking;

/**
 * Business logic for Sport and Rec Rentals bookings.
 *
 * @author Alexander Fendyur
 * @since 2/10/2026
 */
public class BookingBO {

    /** Room type name for the party room. */
    public static final String ROOM_PARTY = "Party Room";
    /** Room type name for the gym. */
    public static final String ROOM_GYM = "Gym";

    /** Base price of the party room. */
    public static final double PRICE_PARTY_ROOM = 200.00;
    /** Base price of the gym. */
    public static final double PRICE_GYM = 150.00;

    /** Flat cost added when the booking is a birthday (decorations and cake). */
    public static final double BIRTHDAY_COST = 50.00;
    /** Flat cost added when equipment is rented. */
    public static final double EQUIPMENT_COST = 25.00;
    /** Standard tax rate applied to the subtotal. */
    public static final double TAX_RATE = 0.15;

    /**
     * Calculate the total cost of a booking. The room's base price is looked
     * up from its room type, the birthday and equipment costs are added if
     * requested, then tax is applied. The base price and total are also
     * stored on the booking.
     *
     * @param booking the booking to price
     * @return the total cost including tax
     * @throws IllegalArgumentException if the booking is null or its room type is unknown
     * @author Alexander Fendyur
     * @since 2/10/2026
     */
    public static double calculate(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking is required");
        }

        double basePrice = getRoomPrice(booking.getRoomType());
        booking.setBasePrice(basePrice);

        double subTotal = basePrice;
        if (booking.isBirthday()) {
            subTotal += BIRTHDAY_COST;
        }
        if (booking.isEquipmentNeeded()) {
            subTotal += EQUIPMENT_COST;
        }

        double total = subTotal * (1 + TAX_RATE);
        booking.setTotalPrice(total);
        return total;
    }

    /**
     * Look up the base price for a room type.
     *
     * @param roomType the room type name
     * @return the base price of the room
     * @throws IllegalArgumentException if the room type is unknown
     * @author Alexander Fendyur
     * @since 2/10/2026
     */
    private static double getRoomPrice(String roomType) {
        if (ROOM_PARTY.equals(roomType)) {
            return PRICE_PARTY_ROOM;
        } else if (ROOM_GYM.equals(roomType)) {
            return PRICE_GYM;
        }
        throw new IllegalArgumentException("Unknown room type: " + roomType);
    }
}
