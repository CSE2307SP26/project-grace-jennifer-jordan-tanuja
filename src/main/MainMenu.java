package main;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Map;

public class MainMenu {

    private static final int EXIT_SELECTION = 6;
    private static final int MAX_SELECTION = 6;

    private UserProfile currentUser;
    private HashMap<String, BankAccount> allAccounts;
    private Scanner keyboardInput;

    public MainMenu(UserProfile user) {
        this.currentUser = user;
        this.allAccounts = user.getAccounts();
        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("Logged in as: " + this.currentUser.getUsername());
        System.out.println("Note: Your default account is named 'primary' and is a checking account.");
        System.out.println("What do you wish to do today?");
        System.out.println("1. Select an account");
        System.out.println("2. Create an additional account");
        System.out.println("3. Close an existing account");
        System.out.println("4. Transfer money from one account to another");
        System.out.println("5. Apply for a loan");
        System.out.println("6. Exit");
    }

    public int getUserSelection(int max) {
        int selection = -1;

        while (selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");

            if (keyboardInput.hasNextInt()) {
                selection = keyboardInput.nextInt();

                if (selection < 1 || selection > max) {
                    System.out.println("This input is invalid. Please select a number from 1-" + max);
                }
            } else {

                keyboardInput.next();
                System.out.println("This input is invalid. Please select a number from 1-" + max);
            }
        }

        return selection;
    }

    public HashMap<String, BankAccount> getAccounts() {
        return this.allAccounts;
    }

    public void processInput(int selection) {
        switch (selection) {
            case 1:
                selectAccount();
                break;
            case 2:
                createAdditionalAccount();
                break;
            case 3:
                closeExistingAccount();
                break;
            case 4:
                transferBetweenAccounts();
                break;
            case 5:
                applyForLoan();
                break;
            default:
                break;
        }
    }

    public void selectAccount() {
        System.out.println("Your accounts:");
        for (Map.Entry<String, BankAccount> entry : allAccounts.entrySet()) {
            System.out.println("- " + entry.getKey() + " (" + entry.getValue().getAccountType() + ")");
        }

        System.out.print("Please enter the name of the account you want to select: ");
        String accountName = keyboardInput.next();

        if (!allAccounts.containsKey(accountName)) {
            System.out.println("That account does not exist.");
            return;
        }

        BankAccount selectedAccount = allAccounts.get(accountName);

        System.out.print("Please enter the pin for " + accountName + ": ");
        String enteredPin = keyboardInput.next();
        if (selectedAccount.verifyPin(enteredPin) == false) {
            System.out.println("Incorrect PIN. Access denied.");
            return;
        }

        AccountAdministrationMenu adminMenu = new AccountAdministrationMenu(currentUser, accountName, selectedAccount,
                keyboardInput);
        adminMenu.run();
    }

    private String promptToCreatePinNumber() {
        System.out.print("Enter a 6 digit pin number for your account: ");
        String pin = keyboardInput.next();

        while (pin.length() != 6 || !pin.matches("\\d{6}")) {
            System.out.print("Pin number must have 6 numerical digits. Enter a valid pin number: ");
            pin = keyboardInput.next();
        }
        return pin;
    }

    private boolean promptAndValidateBirthday() {
        System.out.print("Please enter your date of birth (MM/DD/YYYY): ");
        String dob = keyboardInput.next();

        if (!dob.equals(currentUser.getDob())) {
            System.out.print("Date of birth does not match our records. ");
            return false;
        }
        return true;
    }

    public void createAdditionalAccount() {
        System.out.print("Please enter your full name: ");
        String fullName = keyboardInput.nextLine();
        if (fullName.trim().isEmpty()) {
            fullName = keyboardInput.nextLine();
        }

        if (!promptAndValidateBirthday()) {
            System.out.println("Account creation failed.");
            return;
        }

        System.out.print("Enter a unique name for your new account: ");
        String accountName = keyboardInput.next();

        while (allAccounts.containsKey(accountName)) {
            System.out.print(accountName + " already exists. Enter a unique name for your new account: ");
            accountName = keyboardInput.next();
        }

        System.out.print("Enter the account type (savings, checking, or credit): ");
        String accountType = keyboardInput.next().toLowerCase();

        while (!accountType.equals("savings")
                && !accountType.equals("checking")
                && !accountType.equals("credit")) {
            System.out.print("Invalid account type. Please enter savings, checking, or credit: ");
            accountType = keyboardInput.next().toLowerCase();
        }

        String pin = promptToCreatePinNumber();
        allAccounts.put(accountName, new BankAccount(accountType, pin));

        System.out.println("Successfully created new " + accountType + " account with name: " + accountName);
    }

    public void closeExistingAccount() {
        System.out.print("Please enter the name of the account you wish to close: ");
        String accountName = keyboardInput.next();

        if (!allAccounts.containsKey(accountName)) {
            System.out.println("That account does not exist.");
            return;
        }

        if (accountName.equals("primary")) {
            System.out.println("The primary account cannot be closed.");
            return;
        }

        if (allAccounts.get(accountName).getBalance() != 0) {
            System.out.println("The account balance must be 0 before closing the account.");
            return;
        }

        allAccounts.remove(accountName);
        System.out.println("The account named " + accountName + " has been successfully closed");
    }

    public void transferBetweenAccounts() {
        System.out.print("Enter name of the account you are pulling money from: ");
        String sourceAccountName = keyboardInput.next();

        if (!allAccounts.containsKey(sourceAccountName)) {
            System.out.println("Source account does not exist.");
            return;
        }

        System.out.print("Enter the name of the account you are depositing money into: ");
        String destinationAccountName = keyboardInput.next();

        if (!allAccounts.containsKey(destinationAccountName)) {
            System.out.println("Destination account does not exist.");
            return;
        }

        System.out.print("Enter the amount to transfer: ");
        double amount = keyboardInput.nextDouble();

        try {
            allAccounts.get(sourceAccountName).withdraw(amount);
            allAccounts.get(destinationAccountName).deposit(amount);
            System.out.println("Transfer successful.");
        } catch (IllegalArgumentException e) {
            System.out
                    .println("Transfer failed. Make sure the amount is valid and the source account has enough funds.");
        }
    }

    public void applyForLoan() {

        System.out.println("Loan Application");

        System.out.print("Please enter your full name: ");
        String fullName = keyboardInput.nextLine();

        while (fullName.trim().isEmpty()) {
            System.out.print("Full name cannot be empty. Please enter your full name: ");
            fullName = keyboardInput.nextLine();
        }

        // birthday check
        if (!promptAndValidateBirthday()) {
            System.out.println("Loan application failed.");
            return;
        }

        double income = -1;
        while (income <= 0) {
            System.out.print("Please enter your annual income: ");
            if (keyboardInput.hasNextDouble()) {
                income = keyboardInput.nextDouble();
                if (income <= 0) {
                    System.out.println("Income must be greater than 0.");
                }
            } else {
                keyboardInput.next();
                System.out.println("Invalid input. Please enter a valid number for income.");
            }
        }

        double loanAmount = -1;
        while (loanAmount <= 0) {
            System.out.print("Please enter the loan amount requested: ");
            if (keyboardInput.hasNextDouble()) {
                loanAmount = keyboardInput.nextDouble();
                if (loanAmount <= 0) {
                    System.out.println("Loan amount must be greater than 0.");
                }
            } else {
                keyboardInput.next();
                System.out.println("Invalid input. Please enter a valid number for loan amount.");
            }
        }

        int durationMonths = -1;
        while (durationMonths <= 0) {
            System.out.print("Please enter the duration of the loan in months: ");
            if (keyboardInput.hasNextInt()) {
                durationMonths = keyboardInput.nextInt();
                if (durationMonths <= 0) {
                    System.out.println("Loan duration must be greater than 0.");
                }
            } else {
                keyboardInput.next();
                System.out.println("Invalid input. Please enter a valid whole number for loan duration.");
            }
        }

        System.out.println("Loan application submitted successfully.");
    }

    public Set<String> getAllAccountNames() {
        return allAccounts.keySet();
    }

    public void run() {
        int selection = -1;
        while (selection != EXIT_SELECTION) {
            displayOptions();
            selection = getUserSelection(MAX_SELECTION);
            processInput(selection);
        }
    }
}