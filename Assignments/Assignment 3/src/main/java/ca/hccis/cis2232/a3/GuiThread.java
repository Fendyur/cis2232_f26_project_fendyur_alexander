package ca.hccis.cis2232.a3;

import ca.hccis.cis2232.a3.util.GuiMenuIO;

/**
 * Thread that runs the booking menu completely with JOptionPane dialogs.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 12/10/2026
 */
public class GuiThread extends Thread {

    /**
     * Name the thread so it is easy to spot when debugging.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    public GuiThread() {
        super("GUI booking thread");
    }

    /**
     * Run the booking menu with dialogs until the user exits.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Override
    public void run() {
        new BookingMenu(new GuiMenuIO()).run();
    }
}
