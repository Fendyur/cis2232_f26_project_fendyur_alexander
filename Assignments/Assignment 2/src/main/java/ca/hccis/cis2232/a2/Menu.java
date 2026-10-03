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
 *
 * @modifiedby Claude (AI) 2026-10-02 Room types now come from BookingBO; removed the
 * unused ROOM_COSTS array and Scanner field.
 */
public class Menu {
    //Claude (AI) 2026-10-02 Use the room names defined in BookingBO so prices and names stay in one place
    private static final String[] ROOM_TYPES = {BookingBO.ROOM_PARTY, BookingBO.ROOM_GYM, BookingBO.ROOM_SMALL_REC};

    /**
     * Starts the app: loads saved bookings, then shows the main menu until
     * the user chooses to exit.
     *
     * @param args command line arguments (not used)
     * @author Alexander Fendyur
     * @since 27/9/2026
     */
    public static void main(String[] args) {
        IO.println("Welcome to the Sport and Rec Reservation app!");
        IO.println("");

        List<Booking> reservations;

        /*
         * Searches for the desired directory and creates it in needed.
         * In case of a related error, warns user and prints said error.
         */
        try{
            BookingDataOptions.ensureDir();
            reservations = BookingDataOptions.load();
        } catch(Exception e){
            IO.println("Can't access " + BookingDataOptions.getFilePath());
            IO.println(e.getMessage());
            return;
        }

        //Show the menu and run the chosen option until the user exits
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
     * @param reservations the bookings loaded so far; the new booking is added to it
     * @author Alexander Fendyur
     * @since 27/9/2026
     *
     * @modifiedby Claude (AI) 2026-10-02 Each input is now validated and re-prompted
     * until valid, and the total is calculated with BookingBO.
     */
    public static void addBooking(List<Booking> reservations){
        Booking reservation = new Booking();
        reservation.setId(getNextBookingId(reservations));

        //Claude (AI) 2026-10-02 Prompt for each booking detail, re-prompting until the input is valid
        reservation.setRoomType(promptRoomType());
        reservation.setBookingDate(MenuOptions.getValidString("Date of Booking (yyyy-mm-dd): ",
                input -> BookingValidationBO.isValidBookingDate(input, LocalDate.now()),
                "Enter a real date in yyyy-mm-dd format that is today or later!"));
        reservation.setStartTime(MenuOptions.getValidString("Enter the Start Time (hh:mm): ",
                BookingValidationBO::isValidTime,
                "Enter a time in 24 hour hh:mm format, for example 14:30!"));
        //The end time is checked against the start time entered above
        String startTime = reservation.getStartTime();
        reservation.setEndTime(MenuOptions.getValidString("Enter the End Time (hh:mm): ",
                input -> BookingValidationBO.isEndAfterStart(startTime, input),
                "Enter a time in hh:mm format that is after " + startTime + "!"));
        reservation.setGroupNum(MenuOptions.getInt("Enter the Number of People Attending: "));
        reservation.setBookingName(MenuOptions.getValidString("Enter the Booking's Name: ",
                BookingValidationBO::isNotBlank,
                "A booking name is required!"));
        reservation.setPhone(MenuOptions.getValidString("Enter the Primary Booker's Phone Number: ",
                BookingValidationBO::isValidPhone,
                "Enter a 10 digit phone number, for example 902-555-1234!"));
        reservation.setEmail(MenuOptions.getValidString("Enter the Primary Booker's Email: ",
                BookingValidationBO::isValidEmail,
                "Enter an email address, for example name@example.com!"));
        reservation.setEquipmentNeeded(MenuOptions.getBool("Are you Booking Equipment?"));
        reservation.setBirthday(MenuOptions.getBool("Is this a Birthday party?"));

        //Claude (AI) 2026-10-02 Price the booking with the business object instead of Booking.priceCalc()
        BookingBO.calculate(reservation);
        reservations.add(reservation);

        //Save all bookings; if saving fails, undo the add so memory matches the file
        try{
            BookingDataOptions.save(reservations);
            IO.println("");
            IO.println("Reservation Saved.");
        } catch(Exception e){
            reservations.remove(reservation);
            IO.println("Error Saving Reservation: " + e.getMessage());
        }
    }

    /**
     * Retrieves previous bookings.
     *
     * @param reservations the bookings to display
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
     * @return the name of the room type chosen
     * @author Alexander Fendyur
     * @since 27/9/2026
     *
     * @modifiedby Claude (AI) 2026-10-02 Uses MenuOptions.getInt with a 1 to 3 range so 0
     * and non-numbers are rejected; renamed from resRoomType to a verb.
     */
    private static String promptRoomType(){
        //List the room types, numbered from 1
        IO.println("Room Types:");
        for(int i = 0; i < ROOM_TYPES.length; ++i){
            IO.println((i+1) + ": " + ROOM_TYPES[i]);
        }

        //Claude (AI) 2026-10-02 Only accept a number that matches a listed room
        int choice = MenuOptions.getInt("Make a selection (ex: 1): ", 1, ROOM_TYPES.length);
        return ROOM_TYPES[choice - 1];
    }

    /**
     * Create the next booking ID
     *
     * @param reservations the existing bookings
     * @return one more than the highest booking ID in use
     * @author Alexander Fendyur
     * @since 27/9/2026
     *
     * @modifiedby Claude (AI) 2026-10-03 Renamed from bookingID to a verb.
     */
    private static int getNextBookingId(List<Booking> reservations){
        int max=0;
        for(Booking booking: reservations){
            max = Math.max(max, booking.getId());
        }
        return max + 1;
    }
}
