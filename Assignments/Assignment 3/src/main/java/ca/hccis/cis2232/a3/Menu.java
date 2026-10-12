package ca.hccis.cis2232.a3;

import ca.hccis.cis2232.a3.util.BookingDataOptions;
import ca.hccis.cis2232.a3.util.BookingStore;


/**
 * An app to book rooms at a Sport and Rec facility
 *
 * @author Alexander Fendyur
 * @since 27/9/2026
 *
 * @modifiedby Claude (AI) 2026-10-02 Room types now come from BookingBO; removed the
 * unused ROOM_COSTS array and Scanner field.
 * @modifiedby Claude (AI) 2026-10-12 Now starts two threads, one console and one
 * JOptionPane; the menu itself moved to BookingMenu.
 */
public class Menu {

    /**
     * Starts the app: loads saved bookings, then runs the booking menu in two
     * threads at once, one in the console and one with JOptionPane dialogs.
     * The app ends when the user has exited both.
     *
     * @param args command line arguments (not used)
     * @author Alexander Fendyur
     * @since 27/9/2026
     *
     * @modifiedby Claude (AI) 2026-10-12 Starts the console and GUI threads and waits
     * for both to finish.
     */
    public static void main(String[] args) {
        /*
         * Searches for the desired directory and creates it in needed.
         * In case of a related error, warns user and prints said error.
         */
        try{
            BookingStore.load();
        } catch(Exception e){
            IO.println("Can't access " + BookingDataOptions.getFilePath());
            IO.println(e.getMessage());
            return;
        }

        //Claude (AI) 2026-10-12 Start both threads; each runs the same menu independently
        ConsoleThread consoleThread = new ConsoleThread();
        GuiThread guiThread = new GuiThread();
        consoleThread.start();
        guiThread.start();

        //Wait until the user has exited both menus
        try{
            consoleThread.join();
            guiThread.join();
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }

        //Swing keeps a background thread alive after dialogs, so end the app explicitly
        IO.println("Both menus closed. Goodbye!");
        System.exit(0);
    }
}
