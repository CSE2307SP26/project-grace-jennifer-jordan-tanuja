package main;

import java.util.HashMap;
import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_SELECTION = 2;
    private static final int MAX_SELECTION = 5;

    private BankAccount userAccount;
    private HashMap<String, BankAccount> allAccounts;
    private Scanner keyboardInput;

    public MainMenu() {
        this.userAccount = new BankAccount();

        this.allAccounts = new HashMap<>();
        this.allAccounts.put("primary", this.userAccount);

        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");

        System.out.println("1. Make a deposit");
        System.out.println("2. Exit the app");

        System.out.println("4. Create an additional account");
        System.out.println("5. Close an existing account");
    }

    public int getUserSelection(int max) {
        int selection = -1;
        while (selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");
            selection = keyboardInput.nextInt();
        }
        return selection;
    }

    public void processInput(int selection) {
        switch (selection) {
            case 1:
                performDeposit();
                break;
            case 4:
                createAdditionalAccount();
                break;
            case 5:
                closeExistingAccount();
                break;
        }
    }

    // making a deposit
    public void performDeposit() {
        double depositAmount = -1;
        while (depositAmount < 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = keyboardInput.nextInt();
        }
        userAccount.deposit(depositAmount);
    }

    // creating new account
    public void createAdditionalAccount() {
        System.out.print("Enter a unique name for your new account: ");
        String accountName = keyboardInput.next();

        while (this.allAccounts.containsKey(accountName)) {
            System.out.print(accountName + " already exists. Enter a unique name for your new account: ");
            accountName = keyboardInput.next();
        }

        allAccounts.put(accountName, new BankAccount());
        System.out.println("Successfully created new account with name: " + accountName);
    }

    // closing existing account
    public void closeExistingAccount() {
        System.out.print("Please enter the name of the account you wish to close:");
        String accountName = keyboardInput.next();

        // checking if the account exists in the first place
        if (!allAccounts.containsKey(accountName)) {
            System.out.print("That account does not exist.");
            return;
        }
        // can't close primary account
        if (accountName.equals("primary")) {
            System.out.print("This is the primary account, which cannot be closed.");
            return;
        }
        // account must be 0 before closing
        if (allAccounts.get(accountName).getBalance() != 0) {
            System.out.print(
                    "The account balance must be 0 before closing the account. Please transfer the balance to a differnet account before closing.");
            return;
        }
        allAccounts.remove(accountName);
        System.out.println("The account names" + accountName + "has been successfully closed");

    }

    public void run() {
        int selection = -1;
        while (selection != EXIT_SELECTION) {
            displayOptions();
            selection = getUserSelection(MAX_SELECTION);
            processInput(selection);
        }
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }

}
