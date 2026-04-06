package main;

import java.util.HashMap;
import java.util.Scanner;

public class StartupPage {

    private Scanner keyboardInput;
    private HashMap<String, UserProfile> users;

    public StartupPage() {
        this.keyboardInput = new Scanner(System.in);
        this.users = new HashMap<>();
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        System.out.println("1. Create an account");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }

    public int getUserSelection() {
        int selection = -1;

        while (selection < 1 || selection > 3) {
            System.out.print("Please make a selection: ");

            if (keyboardInput.hasNextInt()) {
                selection = keyboardInput.nextInt();

                if (selection < 1 || selection > 3) {
                    System.out.println("This input is invalid. Please select a number from 1-3");
                }
            } else {
                keyboardInput.next();
                System.out.println("This input is invalid. Please select a number from 1-3");
            }
        }

        return selection;
    }

    public HashMap<String, UserProfile> getUsers() {
        return this.users;
    }

    public void createAccount() {
        System.out.print("Enter a username: ");
        String username = keyboardInput.next();

        while (users.containsKey(username)) {
            System.out.print("That username already exists. Enter a different username: ");
            username = keyboardInput.next();
        }

        System.out.print("Enter a password: ");
        String password = keyboardInput.next();

        System.out.print("Enter a 6 digit pin number for your primary account: ");
        String pin = keyboardInput.next();

        while (pin.length() != 6 || !pin.matches("\\d{6}")) {
            System.out.print("Pin number must have 6 numerical digits. Enter a valid pin number: ");
            pin = keyboardInput.next();
        }

        UserProfile newUser = new UserProfile(username, password, pin);
        users.put(username, newUser);

        System.out.println("Account created successfully.");
    }

    public UserProfile login() {
        System.out.print("Enter your username: ");
        String username = keyboardInput.next();

        System.out.print("Enter your password: ");
        String password = keyboardInput.next();

        if (users.containsKey(username) && users.get(username).checkPassword(password)) {
            System.out.println("Login successful.");
            return users.get(username);
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }

    public void run() {
        int selection = -1;

        while (selection != 3) {
            displayOptions();
            selection = getUserSelection();

            switch (selection) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    UserProfile loggedInUser = login();
                    if (loggedInUser != null) {
                        MainMenu menu = new MainMenu(loggedInUser);
                        menu.run();
                    }
                    break;
                case 3:
                    System.out.println("Exiting app.");
                    break;
                default:
                    break;
            }
        }
    }

    public static void main(String[] args) {
        StartupPage startupPage = new StartupPage();
        startupPage.run();
    }
}