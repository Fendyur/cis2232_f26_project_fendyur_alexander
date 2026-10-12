package ca.hccis.cis2232.a3.util;

import ca.hccis.cis2232.a3.data.Booking;

import java.util.ArrayList;
import java.util.List;

/**
 * The one list of bookings shared by the console and GUI threads. Adding a
 * booking assigns its ID, adds it and saves the file as one synchronized step,
 * so two threads can never get the same ID or overwrite each other's bookings.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 12/10/2026
 */
public class BookingStore {

    private static List<Booking> bookings = new ArrayList<>();

    /**
     * Prevent creating instances; the list is static and shared.
     */
    private BookingStore() {
    }

    /**
     * Load the saved bookings from the file. Call once before starting the threads.
     *
     * @throws Exception if the file cannot be read
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    public static synchronized void load() throws Exception {
        BookingDataOptions.ensureDir();
        bookings = BookingDataOptions.load();
    }

    /**
     * Give the booking the next ID, add it to the shared list and save all
     * bookings to the file.
     *
     * @param booking the priced booking to add
     * @throws Exception if the file cannot be written; the booking is not kept
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    public static synchronized void addBooking(Booking booking) throws Exception {
        //The ID is chosen inside the lock so both threads cannot pick the same one
        booking.setId(getNextBookingId());
        bookings.add(booking);

        //If saving fails, undo the add so the list still matches the file
        try {
            BookingDataOptions.save(bookings);
        } catch (Exception e) {
            bookings.remove(booking);
            throw e;
        }
    }

    /**
     * Get a copy of all bookings, from the file and from both threads.
     *
     * @return a copy of the shared list, safe to loop over while other threads add
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    public static synchronized List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    /**
     * Find the next booking ID.
     *
     * @return one more than the highest booking ID in use
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    private static int getNextBookingId() {
        int max = 0;
        for (Booking booking : bookings) {
            max = Math.max(max, booking.getId());
        }
        return max + 1;
    }
}
