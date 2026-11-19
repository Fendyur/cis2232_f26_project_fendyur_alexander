package ca.hccis.squash.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * JPA entity for a room booking at the Sport and Rec Rentals facility. Maps to
 * the existing booking table (see db/mysql/createDatabase.sql). The Jakarta Bean
 * Validation annotations check the values entered on the add/edit form; rules
 * that need more than one field or today's date are in BookingValidationBO. The
 * price fields are not entered by the user, they are calculated by BookingBO.
 * <p>
 * The patterns allow an empty value so that a blank field only shows the
 * "required" message from @NotBlank and not a second format message.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 19/11/2026
 */
@Entity
@Table(name = "booking")
public class Booking {

    public static final int MAX_GROUP_SIZE = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull(message = "Group size is required")
    @Min(value = 1, message = "Group size must be at least 1")
    @Max(value = MAX_GROUP_SIZE, message = "Group size can not be more than " + MAX_GROUP_SIZE)
    @Column(name = "groupNum", nullable = false)
    private Integer groupNum;

    //Java field is birthday so the getter is isBirthday() and not isIsBirthday()
    @Column(name = "isBirthday", nullable = false)
    private boolean birthday;

    @Column(name = "equipmentNeeded", nullable = false)
    private boolean equipmentNeeded;

    @NotBlank(message = "Room type is required")
    @Pattern(regexp = "^$|^(Party Room|Gym|Small Rec Room)$",
            message = "Room type must be Party Room, Gym or Small Rec Room")
    @Column(name = "roomType", nullable = false, length = 50)
    private String roomType;

    @NotBlank(message = "Booking date is required")
    @Pattern(regexp = "^$|^\\d{4}-\\d{2}-\\d{2}$", message = "Booking date must be in the format yyyy-mm-dd")
    @Column(name = "bookingDate", nullable = false, length = 10)
    private String bookingDate;

    @NotBlank(message = "Start time is required")
    @Pattern(regexp = "^$|^([01]\\d|2[0-3]):[0-5]\\d$", message = "Start time must be in the format hh:mm (24 hour)")
    @Column(name = "startTime", nullable = false, length = 10)
    private String startTime;

    @NotBlank(message = "End time is required")
    @Pattern(regexp = "^$|^([01]\\d|2[0-3]):[0-5]\\d$", message = "End time must be in the format hh:mm (24 hour)")
    @Column(name = "endTime", nullable = false, length = 10)
    private String endTime;

    @NotBlank(message = "Booking name is required")
    @Size(max = 100, message = "Booking name can not be more than 100 characters")
    @Column(name = "bookingName", nullable = false, length = 100)
    private String bookingName;

    //10 digits, optionally with separators e.g. 9025551234, 902-555-1234, (902) 555-1234, 902.555.1234
    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number can not be more than 20 characters")
    @Pattern(regexp = "^$|^\\(?\\d{3}\\)?[-. ]?\\d{3}[-. ]?\\d{4}$",
            message = "Phone number must be 10 digits, e.g. 902-555-1234")
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email can not be more than 100 characters")
    @Email(regexp = "^$|^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "Email must be a valid email address, e.g. name@example.com")
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    //Calculated by BookingBO (not entered by the user)
    @Column(name = "basePrice", nullable = false)
    private Double basePrice = 0.0;

    @Column(name = "tax", nullable = false)
    private Double tax = 0.0;

    @Column(name = "totalPrice", nullable = false)
    private Double totalPrice = 0.0;

    @Column(name = "birthdayCost", nullable = false)
    private Double birthdayCost = 0.0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(Integer groupNum) {
        this.groupNum = groupNum;
    }

    public boolean isBirthday() {
        return birthday;
    }

    public void setBirthday(boolean birthday) {
        this.birthday = birthday;
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

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getBirthdayCost() {
        return birthdayCost;
    }

    public void setBirthdayCost(Double birthdayCost) {
        this.birthdayCost = birthdayCost;
    }

    @Override
    public String toString() {
        return "Booking\n" +
                "    id              = " + id + ",\n" +
                "    bookingName     = '" + bookingName + "',\n" +
                "    roomType        = '" + roomType + "',\n" +
                "    bookingDate     = '" + bookingDate + "',\n" +
                "    startTime       = '" + startTime + "',\n" +
                "    endTime         = '" + endTime + "',\n" +
                "    groupNum        = " + groupNum + ",\n" +
                "    equipmentNeeded = " + equipmentNeeded + ",\n" +
                "    birthday        = " + birthday + ",\n" +
                "    phone           = '" + phone + "',\n" +
                "    email           = '" + email + "',\n" +
                "    basePrice       = " + basePrice + ",\n" +
                "    birthdayCost    = " + birthdayCost + ",\n" +
                "    tax             = " + tax + ",\n" +
                "    totalPrice      = " + totalPrice + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Booking)) return false;
        Booking that = (Booking) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
