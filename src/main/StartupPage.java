package main;

import java.util.HashMap;
import java.util.Scanner;

public class StartupPage {

    private Scanner keyboardInput;
    private HashMap<String, UserProfile> users;
    private HashMap<String, BankAdministrator> admins;

    public StartupPage() {
        this.keyboardInput = new Scanner(System.in);
        this.users = new HashMap<>();
        this.admins = new HashMap<>();
        // Add a default admin for login
        this.admins.put("admin", new BankAdministrator("admin", "adminpass"));
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        System.out.println("1. Create an account");
        System.out.println("2. Login");
        System.out.println("3. Bank Administrator Options");
        System.out.println("4. Exit");
    }

    public int getUserSelection() {
        return InputValidator.getUserSelection(keyboardInput, 4);
    }

    public HashMap<String, UserProfile> getUsers() {
        return this.users;
    }

    public void createAccount() {
        String username = getNewUsername();
        System.out.print("Enter a password: ");
        String password = keyboardInput.next();
        String email = getValidEmail();
        String dob = getValidDob();
        String pin = InputValidator.getValidPin(keyboardInput, "Enter a 6 digit pin number for your primary account: ");
        users.put(username, new UserProfile(username, password, email, dob, pin));
        System.out.println("Account created successfully.");
    }

    private String getNewUsername() {
        System.out.print("Enter a username: ");
        String username = keyboardInput.next();
        while (users.containsKey(username)) {
            System.out.print("That username already exists. Enter a different username: ");
            username = keyboardInput.next();
        }
        return username;
    }

    private String getValidEmail() {
        System.out.print("Enter your email address: ");
        String email = keyboardInput.next();
        while (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            System.out.print("Invalid email format. Please enter a valid email address: ");
            email = keyboardInput.next();
        }
        return email;
    }

    private String getValidDob() {
        System.out.print("Enter your date of birth (MM/DD/YYYY): ");
        String dob = keyboardInput.next();
        while (!dob.matches("^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\\d{4}$")) {
            System.out.print("Invalid date format. Please enter your date of birth (MM/DD/YYYY): ");
            dob = keyboardInput.next();
        }
        return dob;
    }

    public UserProfile login() {
        System.out.print("Enter your username: ");
        String username = keyboardInput.next();

        System.out.print("Enter your password: ");
        String password = keyboardInput.next();

        if (users.containsKey(username) && users.get(username).checkPassword(password)) {
            System.out.println("Login successful.");
            return users.get(username);
        }
        System.out.println("Invalid username or password.");
        return null;
    }

    public BankAdministrator loginAdmin() {
        System.out.print("Enter your employee ID: ");
        String employeeId = keyboardInput.next();

        System.out.print("Enter your password: ");
        String password = keyboardInput.next();

        if (admins.containsKey(employeeId) && admins.get(employeeId).checkPassword(password)) {
            System.out.println("Admin login successful.");
            return admins.get(employeeId);
        }
        System.out.println("Invalid employee ID or password.");
        return null;
    }

    public void run() {
        int selection = -1;

        while (selection != 4) {
            displayOptions();
            selection = getUserSelection();

            switch (selection) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    handleLogin();
                    break;
                case 3:
                    handleAdminLogin();
                    break;
                case 4:
                    System.out.println("Exiting app.");
                    break;
                default:
                    break;
            }
        }
    }

    private void handleLogin() {
        UserProfile loggedInUser = login();
        if (loggedInUser != null) {
            new MainMenu(loggedInUser, users).run();
        }
    }

    private void handleAdminLogin() {
        BankAdministrator loggedInAdmin = loginAdmin();
        if (loggedInAdmin != null) {
            HashMap<String, BankAccount> allAccounts = new HashMap<>();
            for (UserProfile user : users.values()) {
                allAccounts.putAll(user.getAccounts());
            }
            new BankAdministratorMenu(loggedInAdmin, allAccounts, users).run();
        }
    }

    public static void main(String[] args) {
        StartupPage startupPage = new StartupPage();
        startupPage.run();
    }
}