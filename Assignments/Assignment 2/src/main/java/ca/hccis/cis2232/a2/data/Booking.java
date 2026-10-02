package ca.hccis.cis2232.a2.data;

/*
 * Room booking.
 *
 * Alexander Fendyur
 * 27/9/2026
 */
public class Booking {
    private int id;
    private int groupNum;
    private boolean isBirthday;
    private boolean equipmentNeeded;
    private String roomType;
    private String bookingDate;
    private String startTime;
    private String endTime;
    private String bookingName;
    private String phone;
    private String email;
    private double basePrice;
    private double totalPrice;
    private double birthdayCost;

    public Booking(){}

    /*
     * Getters and setters
     *
     * Alexander Fendyur
     * 27/9/2026
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    public boolean isBirthday() {
        return isBirthday;
    }

    public void setBirthday(boolean birthday) {
        isBirthday = birthday;
    }

    public boolean isEquipmentNeeded() {
        return equipmentNeeded;
    }

    public void setEquipmentNeeded(boolean equipmentNeeded) {
        this.equipmentNeeded = equipmentNeeded;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBookingName() {
        return bookingName;
    }

    public void setBookingName(String bookingName) {
        this.bookingName = bookingName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getBirthdayCost() {
        return birthdayCost;
    }

    public void setBirthdayCost(double birthdayCost) {
        this.birthdayCost = birthdayCost;
    }

    /*
     * toString generator.
     *
     * Alexander Fendyur
     * 27/9/2026
     */
    @Override
    public String toString(){
        return "Booking Number: " + id + System.lineSeparator() +
                "Booking Name: " + bookingName + System.lineSeparator() +
                "Phone Number: " + phone + System.lineSeparator() +
                "Email: " + email + System.lineSeparator() +
                "Room Type: " + roomType + System.lineSeparator() +
                "Booking Date: " + bookingDate + System.lineSeparator() +
                "Group Size: " + groupNum + System.lineSeparator() +
                "Birthday Booking?: " + isBirthday + System.lineSeparator() +
                "Booked Equipment: " + equipmentNeeded + System.lineSeparator() +
                String.format("Base Cost: $%.2f%n", basePrice) +
                String.format("Total: $%.2f%n", totalPrice);
    }
}
