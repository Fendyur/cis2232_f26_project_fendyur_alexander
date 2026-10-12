package ca.hccis.cis2232.a3.util;

import java.util.function.Predicate;

/**
 * The ways the booking menu talks to the user. The console thread and the GUI
 * thread each provide their own version, so both run exactly the same menu.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 12/10/2026
 */
public interface MenuIO {

    /** Main menu selection to add a booking. */
    char ADD = 'a';
    /** Main menu selection to view the bookings. */
    char VIEW = 'v';
    /** Main menu selection to exit. */
    char EXIT = 'x';

    /**
     * Ask the user to choose Add, View or Exit.
     *
     * @return ADD, VIEW or EXIT
     */
    char getMenuSelection();

    /**
     * Ask the user to pick one of the listed options.
     *
     * @param prompt  the message shown to the user
     * @param options the options to choose from
     * @return the index of the option chosen
     */
    int getChoice(String prompt, String[] options);

    /**
     * Ask for text until it passes the given check.
     *
     * @param prompt       the message shown to the user
     * @param isValid      the check the trimmed value must pass
     * @param errorMessage the message shown when the check fails
     * @return the trimmed value entered
     */
    String getValidString(String prompt, Predicate<String> isValid, String errorMessage);

    /**
     * Ask for a positive whole number.
     *
     * @param prompt the message shown to the user
     * @return the value entered
     */
    int getInt(String prompt);

    /**
     * Ask a yes or no question.
     *
     * @param prompt the question shown to the user
     * @return true for yes, false for no
     */
    boolean getBool(String prompt);

    /**
     * Show a message to the user.
     *
     * @param message the message to show
     */
    void showMessage(String message);
}
