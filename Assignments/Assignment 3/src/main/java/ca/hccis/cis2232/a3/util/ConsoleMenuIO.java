package ca.hccis.cis2232.a3.util;

import java.util.function.Predicate;

/**
 * Runs the booking menu completely in the console, using the prompts from
 * MenuOptions.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 12/10/2026
 */
public class ConsoleMenuIO implements MenuIO {

    /**
     * Show the main menu in the console and read a valid selection.
     *
     * @return ADD, VIEW or EXIT
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Override
    public char getMenuSelection() {
        while (true) {
            IO.println("A) Add");
            IO.println("V) View");
            IO.println("X) Exit");

            //Accept only a, v or x, in either case
            String selection = MenuOptions.getString("Selection: ").toLowerCase();
            if (selection.equals(String.valueOf(ADD)) || selection.equals(String.valueOf(VIEW))
                    || selection.equals(String.valueOf(EXIT))) {
                return selection.charAt(0);
            }
            IO.println("Enter a valid selection.");
        }
    }

    /**
     * List the options numbered from 1 and read the number chosen.
     *
     * @param prompt  the message shown to the user
     * @param options the options to choose from
     * @return the index of the option chosen
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Override
    public int getChoice(String prompt, String[] options) {
        IO.println(prompt);
        for (int i = 0; i < options.length; ++i) {
            IO.println((i + 1) + ": " + options[i]);
        }
        //Only accept a number that matches a listed option
        return MenuOptions.getInt("Make a selection (ex: 1): ", 1, options.length) - 1;
    }

    /**
     * Ask in the console for text until it passes the given check.
     *
     * @param prompt       the message shown to the user
     * @param isValid      the check the trimmed value must pass
     * @param errorMessage the message shown when the check fails
     * @return the trimmed value entered
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Override
    public String getValidString(String prompt, Predicate<String> isValid, String errorMessage) {
        return MenuOptions.getValidString(prompt, isValid, errorMessage);
    }

    /**
     * Ask in the console for a positive whole number.
     *
     * @param prompt the message shown to the user
     * @return the value entered
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Override
    public int getInt(String prompt) {
        return MenuOptions.getInt(prompt);
    }

    /**
     * Ask a y/n question in the console.
     *
     * @param prompt the question shown to the user
     * @return true for y, false for n
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Override
    public boolean getBool(String prompt) {
        return MenuOptions.getBool(prompt);
    }

    /**
     * Print a message to the console.
     *
     * @param message the message to show
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Override
    public void showMessage(String message) {
        IO.println(message);
    }
}
