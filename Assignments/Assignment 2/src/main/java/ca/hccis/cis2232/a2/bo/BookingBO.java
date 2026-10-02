package ca.hccis.cis2232.a2.bo;

import ca.hccis.cis2232.a2.data.Booking;

/**
 * Business logic for Sport and Rec Rentals bookings.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 2/10/2026
 */
public class BookingBO {

    /** Room type name for the party room. */
    public static final String ROOM_PARTY = "Party Room";
    /** Room type name for the gym. */
    public static final String ROOM_GYM = "Gym";
    /** Room type name for the small rec room. */
    public static final String ROOM_SMALL_REC = "Small Rec Room";

    /** Base price of the party room. */
    public static final double PRICE_PARTY_ROOM = 200.00;
    /** Base price of the gym. */
    public static final double PRICE_GYM = 150.00;
    /** Base price of the small rec room. */
    public static final double PRICE_SMALL_REC_ROOM = 100.00;

    /** Flat cost added when the booking is a birthday (decorations and cake). */
    public static final double BIRTHDAY_COST = 50.00;
    /** Flat cost added when equipment is rented. */
    public static final double EQUIPMENT_COST = 25.00;
    /** Standard tax rate applied to the subtotal. */
    public static final double TAX_RATE = 0.15;

    /**
     * Calculate the total cost of a booking. The room's base price is looked
     * up from its room type, the birthday and equipment costs are added if
     * requested, then tax is applied. The base price, birthday cost and
     * total are also stored on the booking.
     *
     * @param booking the booking to price
     * @return the total cost including tax
     * @throws IllegalArgumentException if the booking is null or its room type is unknown
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    public static double calculate(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking is required");
        }

        double basePrice = getRoomPrice(booking.getRoomType());
        booking.setBasePrice(basePrice);

        double birthdayCost = booking.isBirthday() ? BIRTHDAY_COST : 0.0;
        booking.setBirthdayCost(birthdayCost);

        double subTotal = basePrice + birthdayCost;
        if (booking.isEquipmentNeeded()) {
            subTotal += EQUIPMENT_COST;
        }

        double total = subTotal * (1 + TAX_RATE);
        booking.setTotalPrice(total);
        return total;
    }

    /**
     * Look up the base price for a room type. Matching ignores case and
     * surrounding spaces.
     *
     * @param roomType the room type name
     * @return the base price of the room
     * @throws IllegalArgumentException if the room type is unknown
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    private static double getRoomPrice(String roomType) {
        String room = roomType == null ? "" : roomType.trim();
        if (ROOM_PARTY.equalsIgnoreCase(room)) {
            return PRICE_PARTY_ROOM;
        } else if (ROOM_GYM.equalsIgnoreCase(room)) {
            return PRICE_GYM;
        } else if (ROOM_SMALL_REC.equalsIgnoreCase(room)) {
            return PRICE_SMALL_REC_ROOM;
        }
        throw new IllegalArgumentException("Unknown room type: " + roomType);
    }
}
