package ca.hccis.cis2232.a3;

import ca.hccis.cis2232.a3.util.ConsoleMenuIO;

/**
 * Thread that runs the booking menu completely in the console.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 12/10/2026
 */
public class ConsoleThread extends Thread {

    /**
     * Name the thread so it is easy to spot when debugging.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    public ConsoleThread() {
        super("Console booking thread");
    }

    /**
     * Run the booking menu in the console until the user exits.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Override
    public void run() {
        IO.println("Welcome to the Sport and Rec Reservation app! (console)");
        IO.println("");
        new BookingMenu(new ConsoleMenuIO()).run();
    }
}
