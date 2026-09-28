package ca.hccis.cis2232.a1.util;

import java.util.Scanner;

/*
* Methods to help the Menu prompt for validated inputs
*
* Alexander Fendyur
* 27/9/2026
*/
public class MenuOptions {
    private static Scanner sc = new Scanner(System.in);

    /*
     * Prompts for a string type input
     *
     * Alexander Fendyur
     * 27/9/2026
     */
    public static String getString(String prompt){
        boolean valid = false;
        String input;
        do{
            IO.println(prompt);
            input = sc.nextLine();
            if(!input.isEmpty()){
                valid = true;
                break;
            }
        } while(valid);
        return input;
    }

    /*
     * Prompts for an int type input
     *
     * Alexander Fendyur
     * 27/9/2026
     */
    public static int getInt(String prompt){
        boolean valid = false;
        int value = 0;
        do{
            IO.println(prompt);
            String input = sc.nextLine();
            try{
                value = Integer.parseInt(input);
                if(value > 0){
                    valid = true;
                    return value;
                }
            } catch(NumberFormatException e){}
        } while(valid);
        return value;
    }

    /*
     * Prompts for a double type input
     *
     * Alexander Fendyur
     * 27/9/2026
     */
    public static double getDouble(String prompt){
        boolean valid = false;
        double input;
        do{
            IO.println(prompt);
            input = sc.nextInt();
            if(input > 0.0){
                valid = true;
                break;
            }
        } while(valid);
        return input;
    }

    /*
     * Prompts for a string type input, returns a boolean type value
     *
     * Alexander Fendyur
     * 27/9/2026
     */
    public static boolean getBool(String prompt){
        boolean valid = false;
        String input;
        boolean choice = false;
        do{
            IO.println(prompt + " (y/n): ");
            input = sc.nextLine().toLowerCase();
            if(!input.isEmpty() && (input.equals("y"))){
                choice = true;
                valid = true;
                break;
            } else if(!input.isEmpty() && (input.equals("n"))){
                choice = false;
                valid = true;
                break;
            }
            else{
                IO.println("Please enter y or n!");
            }
        } while(!valid);
        return choice;
    }
}
