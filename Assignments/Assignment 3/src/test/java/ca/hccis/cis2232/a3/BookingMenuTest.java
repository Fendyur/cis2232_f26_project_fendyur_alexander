package ca.hccis.cis2232.a3;

import ca.hccis.cis2232.a3.data.BookingAccumulator;
import ca.hccis.cis2232.a3.util.MenuIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the shared booking menu, using a scripted MenuIO in place of
 * the console or GUI. These tests only view and exit, so no file is written.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 12/10/2026
 */
class BookingMenuTest {

    /**
     * A MenuIO that returns scripted menu selections and records every message.
     *
     * @author Claude (AI)
     * @since 12/10/2026
     */
    private static class ScriptedMenuIO implements MenuIO {
        private final Deque<Character> selections = new ArrayDeque<>();
        private final List<String> messages = new ArrayList<>();

        /**
         * Create a MenuIO that answers the main menu with the given selections.
         *
         * @param menuSelections the selections to return, in order
         * @author Claude (AI)
         * @since 12/10/2026
         */
        ScriptedMenuIO(char... menuSelections) {
            for (char selection : menuSelections) {
                selections.add(selection);
            }
        }

        /**
         * Return the next scripted menu selection. (see MenuIO)
         *
         * @author Claude (AI)
         * @since 12/10/2026
         */
        @Override
        public char getMenuSelection() {
            return selections.remove();
        }

        /**
         * Not used by these tests. (see MenuIO)
         *
         * @author Claude (AI)
         * @since 12/10/2026
         */
        @Override
        public int getChoice(String prompt, String[] options) {
            throw new UnsupportedOperationException("Not used by these tests");
        }

        /**
         * Not used by these tests. (see MenuIO)
         *
         * @author Claude (AI)
         * @since 12/10/2026
         */
        @Override
        public String getValidString(String prompt, Predicate<String> isValid, String errorMessage) {
            throw new UnsupportedOperationException("Not used by these tests");
        }

        /**
         * Not used by these tests. (see MenuIO)
         *
         * @author Claude (AI)
         * @since 12/10/2026
         */
        @Override
        public int getInt(String prompt) {
            throw new UnsupportedOperationException("Not used by these tests");
        }

        /**
         * Not used by these tests. (see MenuIO)
         *
         * @author Claude (AI)
         * @since 12/10/2026
         */
        @Override
        public boolean getBool(String prompt) {
            throw new UnsupportedOperationException("Not used by these tests");
        }

        /**
         * Record the message so the test can check it. (see MenuIO)
         *
         * @author Claude (AI)
         * @since 12/10/2026
         */
        @Override
        public void showMessage(String message) {
            messages.add(message);
        }
    }

    /**
     * Start every test with empty accumulator totals.
     *
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @BeforeEach
    void resetTotals() {
        BookingAccumulator.reset();
    }

    /**
     * Choosing Exit ends the menu with a goodbye message.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Test
    @DisplayName("Exit ends the menu and says goodbye")
    void runExitSaysGoodbye() {
        ScriptedMenuIO menuIO = new ScriptedMenuIO(MenuIO.EXIT);

        new BookingMenu(menuIO).run();

        assertEquals(List.of("See you next time!"), menuIO.messages);
    }

    /**
     * Viewing shows the shared accumulator summary, then Exit ends the menu.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 12/10/2026
     */
    @Test
    @DisplayName("View shows the shared accumulator summary")
    void runViewShowsAccumulatorSummary() {
        ScriptedMenuIO menuIO = new ScriptedMenuIO(MenuIO.VIEW, MenuIO.EXIT);

        new BookingMenu(menuIO).run();

        assertEquals(2, menuIO.messages.size());
        assertTrue(menuIO.messages.get(0).contains(BookingAccumulator.getSummary()),
                menuIO.messages.get(0));
    }
}
