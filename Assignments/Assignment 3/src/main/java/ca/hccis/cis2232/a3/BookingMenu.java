package ca.hccis.cis2232.a3;

import ca.hccis.cis2232.a3.bo.BookingBO;
import ca.hccis.cis2232.a3.bo.BookingValidationBO;
import ca.hccis.cis2232.a3.data.Booking;
import ca.hccis.cis2232.a3.data.BookingAccumulator;
import ca.hccis.cis2232.a3.util.BookingStore;
import ca.hccis.cis2232.a3.util.MenuIO;

import java.time.LocalDate;
import java.util.List;

/**
 * The booking menu: add a booking, view all bookings or exit. The same menu
 * is run by the console thread and the GUI thread; only the MenuIO passed in
 * differs, so both threads have exactly the same functionality.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 12/10/2026
 *
 * @modifiedby Claude (AI) 2026-10-12 Moved addBooking, viewBookings and promptRoomType here
 * from Menu so both threads share them, and added the shared accumulator.
 */
public class BookingMenu {

    private static final String[] ROOM_TYPES = {BookingBO.ROOM_PARTY, BookingBO.ROOM_GYM, BookingBO.ROOM_SMALL_REC};

    private final MenuIO menuIO;

    /**
     * Create a menu that talks to the user through the given MenuIO.
     *
     * @param menuIO the console or GUI way of prompting the user
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    public BookingMenu(MenuIO menuIO) {
        this.menuIO = menuIO;
    }

    /**
     * Show the menu and run the chosen option until the user exits.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    public void run() {
        char selection;
        do {
            selection = menuIO.getMenuSelection();
            switch (selection) {
                case MenuIO.ADD:
                    addBooking();
                    break;
                case MenuIO.VIEW:
                    viewBookings();
                    break;
                default:
                    menuIO.showMessage("See you next time!");
                    break;
            }
        } while (selection != MenuIO.EXIT);
    }

    /**
     * Adds a booking to the shared list, then updates and shows the shared
     * accumulator.
     *
     * @author Alexander Fendyur
     * @since 27/9/2026
     *
     * @modifiedby Claude (AI) 2026-10-02 Each input is now validated and re-prompted
     * until valid, and the total is calculated with BookingBO.
     * @modifiedby Claude (AI) 2026-10-12 Moved from Menu; prompts through MenuIO, saves
     * through the shared BookingStore and updates the BookingAccumulator.
     */
    public void addBooking() {
        Booking reservation = new Booking();

        //Prompt for each booking detail, re-prompting until the input is valid
        reservation.setRoomType(promptRoomType());
        reservation.setBookingDate(menuIO.getValidString("Date of Booking (yyyy-mm-dd): ",
                input -> BookingValidationBO.isValidBookingDate(input, LocalDate.now()),
                "Enter a real date in yyyy-mm-dd format that is today or later!"));
        reservation.setStartTime(menuIO.getValidString("Enter the Start Time (hh:mm): ",
                BookingValidationBO::isValidTime,
                "Enter a time in 24 hour hh:mm format, for example 14:30!"));
        //The end time is checked against the start time entered above
        String startTime = reservation.getStartTime();
        reservation.setEndTime(menuIO.getValidString("Enter the End Time (hh:mm): ",
                input -> BookingValidationBO.isEndAfterStart(startTime, input),
                "Enter a time in hh:mm format that is after " + startTime + "!"));
        reservation.setGroupNum(menuIO.getInt("Enter the Number of People Attending: "));
        reservation.setBookingName(menuIO.getValidString("Enter the Booking's Name: ",
                BookingValidationBO::isNotBlank,
                "A booking name is required!"));
        reservation.setPhone(menuIO.getValidString("Enter the Primary Booker's Phone Number: ",
                BookingValidationBO::isValidPhone,
                "Enter a 10 digit phone number, for example 902-555-1234!"));
        reservation.setEmail(menuIO.getValidString("Enter the Primary Booker's Email: ",
                BookingValidationBO::isValidEmail,
                "Enter an email address, for example name@example.com!"));
        reservation.setEquipmentNeeded(menuIO.getBool("Are you Booking Equipment?"));
        reservation.setBirthday(menuIO.getBool("Is this a Birthday party?"));

        //Price the booking with the business object
        double total = BookingBO.calculate(reservation);

        //Claude (AI) 2026-10-12 Save through the shared store, then update the shared accumulator
        try {
            BookingStore.addBooking(reservation);
        } catch (Exception e) {
            menuIO.showMessage("Error Saving Reservation: " + e.getMessage());
            return;
        }
        String summary = BookingAccumulator.add(total);
        menuIO.showMessage(String.format("Reservation Saved. Booking Number: %d, Total: $%.2f%n%s",
                reservation.getId(), total, summary));
    }

    /**
     * Shows all bookings, from the file and from both threads, followed by the
     * shared accumulator.
     *
     * @author Alexander Fendyur
     * @since 27/9/2026
     *
     * @modifiedby Claude (AI) 2026-10-12 Moved from Menu; reads the shared BookingStore,
     * shows the bookings through MenuIO and adds the accumulator summary.
     */
    public void viewBookings() {
        List<Booking> reservations = BookingStore.getBookings();
        if (reservations.isEmpty()) {
            menuIO.showMessage("No reservations!" + System.lineSeparator()
                    + BookingAccumulator.getSummary());
            return;
        }

        //Build one message so the GUI can show it in a single dialog
        StringBuilder message = new StringBuilder();
        for (Booking booking : reservations) {
            message.append(booking).append(System.lineSeparator());
        }
        message.append(BookingAccumulator.getSummary());
        menuIO.showMessage(message.toString());
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
     * @modifiedby Claude (AI) 2026-10-12 Moved from Menu; asks through MenuIO.getChoice.
     */
    private String promptRoomType() {
        return ROOM_TYPES[menuIO.getChoice("Room Types:", ROOM_TYPES)];
    }
}
