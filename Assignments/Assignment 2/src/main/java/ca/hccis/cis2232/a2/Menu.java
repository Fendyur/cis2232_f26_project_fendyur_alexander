package ca.hccis.cis2232.a2;

import ca.hccis.cis2232.a2.bo.BookingBO;
import ca.hccis.cis2232.a2.data.Booking;
import ca.hccis.cis2232.a2.util.BookingDataOptions;
import ca.hccis.cis2232.a2.util.MenuOptions;

import java.util.List;
import java.util.Scanner;


/**
 * An app to book rooms at a Sport and Rec facility
 *
 * @author Alexander Fendyur
 * @since 27/9/2026
 */
public class Menu {
    Scanner input = new Scanner(System.in);
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
        Scanner sc = new Scanner(System.in);
        Booking res = new Booking();
        res.setId(bookingID(reservations));

        res.setRoomType(resRoomType());
        res.setBookingDate(MenuOptions.getString("Date of Booking (yyyy mm dd): "));
        res.setStartTime(MenuOptions.getString("Enter the Start Time (hh:mm): "));
        res.setEndTime(MenuOptions.getString("Enter the End Time (hh:mm): "));
        res.setGroupNum(MenuOptions.getInt("Enter the Number of People Attending: "));
        res.setBookingName(MenuOptions.getString("Enter the Booking's Name: "));
        res.setPhone(MenuOptions.getString("Enter the Primary Booker's Phone Number: "));
        res.setEmail(MenuOptions.getString("Enter the Primary Booker's Email: "));
        res.setEquipmentNeeded(MenuOptions.getBool("Are you Booking Equipment?: "));
        res.setBirthday(MenuOptions.getBool("Is this a Birthday party? (y/n): "));

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
        Scanner sc = new Scanner(System.in);
        boolean valid = false;
        int selectedRoom=0;

        IO.println("Room Types:");
        for(int i = 0; i < ROOM_TYPES.length; ++i){
            IO.println((i+1) + ": " + ROOM_TYPES[i]);
        }

        do {
            IO.println("Make a selection (ex: 1): ");
            int choice = sc.nextInt();

            if(choice < 0 || choice > ROOM_TYPES.length){
                IO.println("Choice must be a previously listed number!");
            } else{
                valid = true;
                selectedRoom = choice - 1;
            }
        } while (!valid);
        return ROOM_TYPES[selectedRoom];
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
