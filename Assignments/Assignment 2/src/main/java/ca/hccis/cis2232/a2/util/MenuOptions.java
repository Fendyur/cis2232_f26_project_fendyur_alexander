package ca.hccis.cis2232.a2.util;

import java.util.Scanner;
import java.util.function.Predicate;

/**
* Methods to help the Menu prompt for validated inputs. Each method keeps
* asking until the user enters a valid value.
*
* @author Alexander Fendyur
* @author Claude (AI)
* @since 27/9/2026
*/
public class MenuOptions {
    private static Scanner sc = new Scanner(System.in);

    /**
     * Prompts for a string type input that is not blank.
     *
     * @param prompt the message shown to the user
     * @return the trimmed value entered
     * @author Alexander Fendyur
     * @since 27/9/2026
     */
    public static String getString(String prompt){
        return getValidString(prompt, input -> !input.isEmpty(), "A value is required!");
    }

    /**
     * Prompts for a string type input until it passes the given check.
     *
     * @param prompt       the message shown to the user
     * @param isValid      the check the trimmed value must pass
     * @param errorMessage the message shown when the check fails
     * @return the trimmed value entered
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    public static String getValidString(String prompt, Predicate<String> isValid, String errorMessage){
        while(true){
            IO.println(prompt);
            String input = sc.nextLine().trim();
            if(isValid.test(input)){
                return input;
            }
            IO.println(errorMessage);
        }
    }

    /**
     * Prompts for a positive int type input
     *
     * @param prompt the message shown to the user
     * @return the value entered
     * @author Alexander Fendyur
     * @since 27/9/2026
     */
    public static int getInt(String prompt){
        return getInt(prompt, 1, Integer.MAX_VALUE);
    }

    /**
     * Prompts for an int type input between min and max, inclusive.
     *
     * @param prompt the message shown to the user
     * @param min    the smallest value allowed
     * @param max    the largest value allowed
     * @return the value entered
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 2/10/2026
     */
    public static int getInt(String prompt, int min, int max){
        while(true){
            IO.println(prompt);
            String input = sc.nextLine().trim();
            try{
                int value = Integer.parseInt(input);
                if(value >= min && value <= max){
                    return value;
                }
            } catch(NumberFormatException e){
                // Not a whole number; fall through to the error message.
            }
            if(max == Integer.MAX_VALUE){
                IO.println("Please enter a whole number of at least " + min + "!");
            } else{
                IO.println("Please enter a whole number from " + min + " to " + max + "!");
            }
        }
    }

    /**
     * Prompts for a positive double type input
     *
     * @param prompt the message shown to the user
     * @return the value entered
     * @author Alexander Fendyur
     * @since 27/9/2026
     */
    public static double getDouble(String prompt){
        while(true){
            IO.println(prompt);
            String input = sc.nextLine().trim();
            try{
                double value = Double.parseDouble(input);
                if(value > 0.0){
                    return value;
                }
            } catch(NumberFormatException e){
                // Not a number; fall through to the error message.
            }
            IO.println("Please enter a number greater than 0!");
        }
    }

    /**
     * Prompts for a string type input, returns a boolean type value
     *
     * @param prompt the message shown to the user
     * @return true for y, false for n
     * @author Alexander Fendyur
     * @since 27/9/2026
     */
    public static boolean getBool(String prompt){
        while(true){
            IO.println(prompt + " (y/n): ");
            String input = sc.nextLine().trim().toLowerCase();
            if(input.equals("y")){
                return true;
            } else if(input.equals("n")){
                return false;
            }
            IO.println("Please enter y or n!");
        }
    }
}
