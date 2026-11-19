package ca.hccis.squash.bo;

import ca.hccis.squash.jpa.entity.Booking;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Business logic for a booking: calculates the prices of a booking from the
 * room type and the birthday add-on.
 * <p>
 * basePrice    = price of the room (Party Room 150.00, Gym 200.00, Small Rec Room 60.00)
 * birthdayCost = 75.00 if it is a birthday, otherwise 0.00
 * tax          = 15% of (basePrice + birthdayCost)
 * totalPrice   = basePrice + birthdayCost + tax
 * <p>
 * Equipment is included with the room and does not add to the cost. This
 * matches all of the sample bookings in db/mysql/createDatabase.sql.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 19/11/2026
 */
public class BookingBO {

    public static final String ROOM_TYPE_PARTY_ROOM = "Party Room";
    public static final String ROOM_TYPE_GYM = "Gym";
    public static final String ROOM_TYPE_SMALL_REC_ROOM = "Small Rec Room";

    //Room types in the order they are shown in the room type select list
    public static final List<String> ROOM_TYPES = List.of(ROOM_TYPE_PARTY_ROOM, ROOM_TYPE_GYM, ROOM_TYPE_SMALL_REC_ROOM);

    public static final double PRICE_PARTY_ROOM = 150.00;
    public static final double PRICE_GYM = 200.00;
    public static final double PRICE_SMALL_REC_ROOM = 60.00;
    public static final double BIRTHDAY_COST = 75.00;
    public static final double EQUIPMENT_COST = 0.00;
    public static final double TAX_RATE = 0.15;

    private static final int PRICE_DECIMAL_PLACES = 2;

    /**
     * Get the base price of a room type.
     *
     * @param roomType Room type (Party Room, Gym or Small Rec Room)
     * @return Base price of the room
     * @throws IllegalArgumentException if the room type is not one of the rooms
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    public static double getBasePrice(String roomType) {
        if (roomType == null) {
            throw new IllegalArgumentException("Room type is required");
        }
        switch (roomType) {
            case ROOM_TYPE_PARTY_ROOM:
                return PRICE_PARTY_ROOM;
            case ROOM_TYPE_GYM:
                return PRICE_GYM;
            case ROOM_TYPE_SMALL_REC_ROOM:
                return PRICE_SMALL_REC_ROOM;
            default:
                throw new IllegalArgumentException("Unknown room type: " + roomType);
        }
    }

    /**
     * Calculate the prices of a booking and set them on the booking (base
     * price, birthday cost, tax and total price). Called when a booking is
     * added or edited, before it is saved.
     *
     * @param booking Booking to calculate (room type and birthday must be set)
     * @return The same booking with the calculated prices set
     * @throws IllegalArgumentException if the room type is not one of the rooms
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    public static Booking calculatePrices(Booking booking) {

        //Input: room price and birthday add-on (equipment is free)
        double basePrice = getBasePrice(booking.getRoomType());
        double birthdayCost = booking.isBirthday() ? BIRTHDAY_COST : 0.00;
        double equipmentCost = booking.isEquipmentNeeded() ? EQUIPMENT_COST : 0.00;

        //Tax is charged on everything before tax, then rounded to the cent
        double subtotal = basePrice + birthdayCost + equipmentCost;
        double tax = roundToCents(subtotal * TAX_RATE);
        double totalPrice = roundToCents(subtotal + tax);

        //Output: set the calculated values on the booking
        booking.setBasePrice(roundToCents(basePrice));
        booking.setBirthdayCost(roundToCents(birthdayCost));
        booking.setTax(tax);
        booking.setTotalPrice(totalPrice);
        return booking;
    }

    /**
     * Round an amount to 2 decimal places (half up), avoiding floating point
     * results like 33.749999.
     *
     * @param amount Amount to round
     * @return Amount rounded to the cent
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    public static double roundToCents(double amount) {
        return BigDecimal.valueOf(amount).setScale(PRICE_DECIMAL_PLACES, RoundingMode.HALF_UP).doubleValue();
    }
}
