package ca.hccis.cis2232.a3.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the accumulator shared by the console and GUI threads.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 12/10/2026
 */
class BookingAccumulatorTest {

    private static final double DELTA = 0.001;
    private static final int ADDS_PER_THREAD = 10_000;

    /**
     * Start every test from zero, since the totals are static.
     *
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @BeforeEach
    void resetTotals() {
        BookingAccumulator.reset();
    }

    /**
     * Adding bookings updates both the count and the total value.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Test
    @DisplayName("Each booking adds one to the count and its total to the value")
    void addUpdatesCountAndValue() {
        BookingAccumulator.add(172.50);
        BookingAccumulator.add(258.75);

        assertEquals(2, BookingAccumulator.getBookingCount());
        assertEquals(431.25, BookingAccumulator.getTotalValue(), DELTA);
    }

    /**
     * The summary returned by add includes the booking just added.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Test
    @DisplayName("The summary returned by add shows the updated totals")
    void addReturnsUpdatedSummary() {
        String summary = BookingAccumulator.add(115.00);

        assertTrue(summary.contains("1"), summary);
        assertTrue(summary.contains("$115.00"), summary);
    }

    /**
     * Two threads adding at the same time lose no bookings, because add is
     * synchronized.
     *
     * @throws InterruptedException if the test is interrupted while waiting
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Test
    @DisplayName("Two threads adding at once lose no bookings")
    void addFromTwoThreadsLosesNothing() throws InterruptedException {
        Runnable addMany = () -> {
            for (int i = 0; i < ADDS_PER_THREAD; i++) {
                BookingAccumulator.add(1.00);
            }
        };
        Thread consoleLike = new Thread(addMany);
        Thread guiLike = new Thread(addMany);

        consoleLike.start();
        guiLike.start();
        consoleLike.join();
        guiLike.join();

        assertEquals(2 * ADDS_PER_THREAD, BookingAccumulator.getBookingCount());
        assertEquals(2.0 * ADDS_PER_THREAD, BookingAccumulator.getTotalValue(), DELTA);
    }

    /**
     * Reset sets both totals back to zero.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Test
    @DisplayName("Reset clears both totals")
    void resetClearsTotals() {
        BookingAccumulator.add(200.00);

        BookingAccumulator.reset();

        assertEquals(0, BookingAccumulator.getBookingCount());
        assertEquals(0.0, BookingAccumulator.getTotalValue(), DELTA);
    }
}
