package ca.hccis.cis2232.a3.data;

/**
 * Running totals shared by the console and GUI threads: how many bookings
 * have been created this run and their combined value. All access is
 * synchronized on the class so both threads see the same, consistent totals.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 12/10/2026
 */
public class BookingAccumulator {

    private static int bookingCount = 0;
    private static double totalValue = 0.0;

    /**
     * Prevent creating instances; the totals are static and shared.
     */
    private BookingAccumulator() {
    }

    /**
     * Add one booking to the running totals.
     *
     * @param bookingTotal the total price of the booking just created
     * @return a summary of the combined totals including this booking
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    public static synchronized String add(double bookingTotal) {
        //Update both totals together so neither thread sees one without the other
        bookingCount++;
        totalValue += bookingTotal;
        return getSummary();
    }

    /**
     * Get the number of bookings created by both threads this run.
     *
     * @return the combined booking count
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    public static synchronized int getBookingCount() {
        return bookingCount;
    }

    /**
     * Get the combined value of the bookings created by both threads this run.
     *
     * @return the combined total value
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    public static synchronized double getTotalValue() {
        return totalValue;
    }

    /**
     * Describe the combined totals for display.
     *
     * @return the booking count and total value as one line
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    public static synchronized String getSummary() {
        return String.format("Bookings this session (both threads): %d, Total value: $%.2f",
                bookingCount, totalValue);
    }

    /**
     * Set the totals back to zero.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    public static synchronized void reset() {
        bookingCount = 0;
        totalValue = 0.0;
    }
}
