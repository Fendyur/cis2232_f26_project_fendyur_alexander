package ca.hccis.cis2232.a2;

import ca.hccis.cis2232.a2.bo.BookingBO;
import ca.hccis.cis2232.a2.bo.BookingValidationBO;
import ca.hccis.cis2232.a2.data.Booking;
import ca.hccis.cis2232.a2.util.BookingDataOptions;
import ca.hccis.cis2232.a2.util.MenuOptions;

import java.time.LocalDate;
import java.util.List;


/**
 * An app to book rooms at a Sport and Rec facility
 *
 * @author Alexander Fendyur
 * @since 27/9/2026
 */
public class Menu {
    private static final String[] ROOM_TYPES = {BookingBO.ROOM_PARTY, BookingBO.ROOM_GYM, BookingBO.ROOM_SMALL_REC};

    public static void main(String[] args) {
        IO.println("Welcome to the Sport and Rec Reservation app!");
        IO.println("");

        List<Booking> reservations;

        /**
         * Searches for the desired directory and creates it in needed.
         * In case of a related error, warns user and prints said error.
         *
         * @author Alexander Fendyur
         * @since 27/9/2026
         */
        try{
            BookingDataOptions.ensureDir();
            reservations = BookingDataOptions.load();
        } catch(Exception e){
            IO.println("Can't access " + BookingDataOptions.getFilePath());
            IO.println(e.getMessage());
            return;
        }

        String selection;
        do {
            IO.println("A) Add");
            IO.println("V) View");
            IO.println("X) Exit");

            selection = MenuOptions.getString("Selection: ").toLowerCase();

            switch(selection){
                case "a":
                    addBooking(reservations);
                    break;
                case "v":
                    viewBookings(reservations);
                    break;
                case "x":
                    IO.println("See you next time!");
                    break;
                default:
                    IO.println("Enter a valid selection.");
                    break;
            }
        } while (!selection.equals("x"));
    }

    /**
     * Adds a booking to the existing list.
     *
     * @author Alexander Fendyur
     * @since 27/9/2026
     */
    public static void addBooking(List<Booking> reservations){
        Booking res = new Booking();
        res.setId(bookingID(reservations));

        res.setRoomType(resRoomType());
        res.setBookingDate(MenuOptions.getValidString("Date of Booking (yyyy-mm-dd): ",
                input -> BookingValidationBO.isValidBookingDate(input, LocalDate.now()),
                "Enter a real date in yyyy-mm-dd format that is today or later!"));
        res.setStartTime(MenuOptions.getValidString("Enter the Start Time (hh:mm): ",
                BookingValidationBO::isValidTime,
                "Enter a time in 24 hour hh:mm format, for example 14:30!"));
        String startTime = res.getStartTime();
        res.setEndTime(MenuOptions.getValidString("Enter the End Time (hh:mm): ",
                input -> BookingValidationBO.isEndAfterStart(startTime, input),
                "Enter a time in hh:mm format that is after " + startTime + "!"));
        res.setGroupNum(MenuOptions.getInt("Enter the Number of People Attending: "));
        res.setBookingName(MenuOptions.getValidString("Enter the Booking's Name: ",
                BookingValidationBO::isNotBlank,
                "A booking name is required!"));
        res.setPhone(MenuOptions.getValidString("Enter the Primary Booker's Phone Number: ",
                BookingValidationBO::isValidPhone,
                "Enter a 10 digit phone number, for example 902-555-1234!"));
        res.setEmail(MenuOptions.getValidString("Enter the Primary Booker's Email: ",
                BookingValidationBO::isValidEmail,
                "Enter an email address, for example name@example.com!"));
        res.setEquipmentNeeded(MenuOptions.getBool("Are you Booking Equipment?"));
        res.setBirthday(MenuOptions.getBool("Is this a Birthday party?"));

        BookingBO.calculate(res);
        reservations.add(res);

        try{
            BookingDataOptions.save(reservations);
            IO.println("");
            IO.println("Reservation Saved.");
        } catch(Exception e){
            reservations.remove(res);
            IO.println("Error Saving Reservation: " + e.getMessage());
        }
    }

    /**
     * Retrieves previous bookings.
     *
     * @author Alexander Fendyur
     * @since 27/9/2026
     */
    public static void viewBookings(List<Booking> reservations){
        if(reservations.isEmpty()){
            IO.println("No reservations!");
        }
        else{
            for(Booking booking: reservations){
                IO.println(booking);
            }
        }
    }

    /**
     * Lets user set the reservation room's type.
     *
     * @author Alexander Fendyur
     * @since 27/9/2026
     */
    private static String resRoomType(){
        IO.println("Room Types:");
        for(int i = 0; i < ROOM_TYPES.length; ++i){
            IO.println((i+1) + ": " + ROOM_TYPES[i]);
        }

        int choice = MenuOptions.getInt("Make a selection (ex: 1): ", 1, ROOM_TYPES.length);
        return ROOM_TYPES[choice - 1];
    }

    /**
     * Create the next booking ID
     *
     * @author Alexander Fendyur
     * @since 27/9/2026
     */
    private static int bookingID(List<Booking> reservations){
        int max=0;
        for(Booking booking: reservations){
            max = Math.max(max, booking.getId());
        }
        return max + 1;
    }
}
