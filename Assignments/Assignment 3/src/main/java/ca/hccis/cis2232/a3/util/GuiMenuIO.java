package ca.hccis.cis2232.a3.util;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.awt.Font;
import java.util.function.Predicate;

/**
 * Runs the booking menu completely with JOptionPane dialogs.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 12/10/2026
 */
public class GuiMenuIO implements MenuIO {

    /** Title shown on every dialog. */
    public static final String TITLE = "Sport and Rec Rentals";

    private static final String[] MENU_BUTTONS = {"Add", "View", "Exit"};
    private static final char[] MENU_SELECTIONS = {ADD, VIEW, EXIT};
    private static final int VIEW_WIDTH = 450;
    private static final int VIEW_HEIGHT = 400;

    /**
     * Show the main menu as a dialog with Add, View and Exit buttons. Closing
     * the dialog counts as Exit.
     *
     * @return ADD, VIEW or EXIT
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Override
    public char getMenuSelection() {
        int button = JOptionPane.showOptionDialog(null, "Welcome to the Sport and Rec Reservation app!",
                TITLE, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, MENU_BUTTONS, MENU_BUTTONS[0]);

        //showOptionDialog returns -1 when the window is closed
        if (button < 0) {
            return EXIT;
        }
        return MENU_SELECTIONS[button];
    }

    /**
     * Show the options in a drop down list and return the one chosen.
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
        while (true) {
            Object selected = JOptionPane.showInputDialog(null, prompt, TITLE,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            //Cancel returns null, so ask again until an option is picked
            for (int i = 0; i < options.length; ++i) {
                if (options[i].equals(selected)) {
                    return i;
                }
            }
            showError("Please choose one of the listed options!");
        }
    }

    /**
     * Ask in a dialog for text until it passes the given check.
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
        while (true) {
            String input = readText(prompt);
            if (isValid.test(input)) {
                return input;
            }
            showError(errorMessage);
        }
    }

    /**
     * Ask in a dialog for a positive whole number.
     *
     * @param prompt the message shown to the user
     * @return the value entered
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Override
    public int getInt(String prompt) {
        while (true) {
            String input = readText(prompt);
            try {
                int value = Integer.parseInt(input);
                if (value >= 1) {
                    return value;
                }
            } catch (NumberFormatException e) {
                //Not a whole number; fall through to the error message
            }
            showError("Please enter a whole number of at least 1!");
        }
    }

    /**
     * Ask a yes or no question in a dialog. Closing the dialog asks again.
     *
     * @param prompt the question shown to the user
     * @return true for yes, false for no
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Override
    public boolean getBool(String prompt) {
        while (true) {
            int answer = JOptionPane.showConfirmDialog(null, prompt, TITLE, JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                return true;
            } else if (answer == JOptionPane.NO_OPTION) {
                return false;
            }
            showError("Please choose Yes or No!");
        }
    }

    /**
     * Show a message in a dialog. Long messages, such as the list of bookings,
     * are shown in a scrolling text area.
     *
     * @param message the message to show
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Override
    public void showMessage(String message) {
        //Short messages fit in a plain dialog; longer ones need scrolling
        if (message.lines().count() <= 3) {
            JOptionPane.showMessageDialog(null, message, TITLE, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, textArea.getFont().getSize()));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
        JOptionPane.showMessageDialog(null, scrollPane, TITLE, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Ask for text in an input dialog.
     *
     * @param prompt the message shown to the user
     * @return the trimmed text, or an empty string if the dialog was cancelled
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    private String readText(String prompt) {
        String input = JOptionPane.showInputDialog(null, prompt, TITLE, JOptionPane.QUESTION_MESSAGE);
        //Cancel returns null; treat it as nothing entered so the caller asks again
        return input == null ? "" : input.trim();
    }

    /**
     * Show an error message in a dialog.
     *
     * @param message the error to show
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, TITLE, JOptionPane.ERROR_MESSAGE);
    }
}
