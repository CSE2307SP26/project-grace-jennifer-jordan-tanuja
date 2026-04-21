package main;

import java.util.Scanner;

public class InputValidator {

    public static int getUserSelection(Scanner in, int max) {
        int sel = -1;
        while (sel < 1 || sel > max) {
            System.out.print("Please make a selection: ");
            if (in.hasNextInt()) {
                sel = in.nextInt();
                if (sel < 1 || sel > max) System.out.println("This input is invalid. Please select a number from 1-" + max);
            } else {
                in.next();
                System.out.println("This input is invalid. Please select a number from 1-" + max);
            }
        }
        return sel;
    }

    public static double getValidDouble(Scanner in, String prompt, String errorName) {
        double val = -1;
        while (val <= 0) {
            System.out.print(prompt);
            if (in.hasNextDouble()) {
                val = in.nextDouble();
                if (val <= 0) System.out.println(errorName + " must be greater than 0.");
            } else {
                in.next();
                System.out.println("Invalid input. Please enter a valid number for " + errorName.toLowerCase() + ".");
            }
        }
        return val;
    }

    public static int getValidInt(Scanner in, String prompt, String errorName) {
        int val = -1;
        while (val <= 0) {
            System.out.print(prompt);
            if (in.hasNextInt()) {
                val = in.nextInt();
                if (val <= 0) System.out.println(errorName + " must be greater than 0.");
            } else {
                in.next();
                System.out.println("Invalid input. Please enter a valid whole number for " + errorName.toLowerCase() + ".");
            }
        }
        return val;
    }

    public static String getValidPin(Scanner in, String prompt) {
        System.out.print(prompt);
        String pin = in.next();
        while (pin.length() != 6 || !pin.matches("\\d{6}")) {
            System.out.print("Pin number must have 6 numerical digits. Enter a valid pin number: ");
            pin = in.next();
        }
        return pin;
    }
}