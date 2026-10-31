package ca.hccis.squash.entity;

/**
 * Entity class holding one variation of a room booking (room type plus the
 * equipment / birthday add-ons) and how many times it was booked. Used as one
 * row of the Monthly Room Bookings report.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 29/10/2026
 */
public class RoomBookingVariation {

    private String roomType;
    private boolean equipmentNeeded;
    private boolean birthday;
    private int bookingCount;

    /**
     * Default constructor needed for frameworks.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public RoomBookingVariation() {
    }

    /**
     * Constructor which sets all of the attributes of the variation.
     *
     * @param roomType        The room type (Party Room, Gym, Small Rec Room)
     * @param equipmentNeeded True if equipment was booked with the room
     * @param birthday        True if birthday decorations were booked with the room
     * @param bookingCount    Number of bookings for this variation
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public RoomBookingVariation(String roomType, boolean equipmentNeeded, boolean birthday, int bookingCount) {
        this.roomType = roomType;
        this.equipmentNeeded = equipmentNeeded;
        this.birthday = birthday;
        this.bookingCount = bookingCount;
    }

    /**
     * Build the description of the add-ons for this variation, for example
     * "Room + Equipment".
     *
     * @return Description of the add-ons
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public String getOptionDescription() {
        //Start with the room and append each add-on that applies
        StringBuilder description = new StringBuilder("Room");
        if (equipmentNeeded) {
            description.append(" + Equipment");
        }
        if (birthday) {
            description.append(" + Birthday decorations");
        }
        if (!equipmentNeeded && !birthday) {
            description.append(" only");
        }
        return description.toString();
    }

    /**
     * Build the full name of the variation, for example
     * "Gym - Room + Equipment". Used for chart labels and the report file.
     *
     * @return Room type and add-on description
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public String getVariationName() {
        return roomType + " - " + getOptionDescription();
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public boolean isEquipmentNeeded() {
        return equipmentNeeded;
    }

    public void setEquipmentNeeded(boolean equipmentNeeded) {
        this.equipmentNeeded = equipmentNeeded;
    }

    public boolean isBirthday() {
        return birthday;
    }

    public void setBirthday(boolean birthday) {
        this.birthday = birthday;
    }

    public int getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(int bookingCount) {
        this.bookingCount = bookingCount;
    }

    @Override
    public String toString() {
        return getVariationName() + ": " + bookingCount;
    }
}
