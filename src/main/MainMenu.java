package main;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class MainMenu {

    private static final int EXIT_SELECTION = 10;
    private static final int MAX_SELECTION = 10;

    private BankAccount userAccount;
    private Scanner keyboardInput;
    private HashMap<String, BankAccount> allAccounts;

    public MainMenu() {
        this.userAccount = new BankAccount();

        this.allAccounts = new HashMap<>();
        this.allAccounts.put("primary", this.userAccount);

        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        System.out.println("1. Make a deposit");
        System.out.println("2. Withdraw from account");
        System.out.println("3. Check account balance");
        System.out.println("4. View transaction history");
        System.out.println("5. Create an additional account");
        System.out.println("6. Close an existing account");
        System.out.println("7. Transfer money between accounts");
        System.out.println("10. Exit the app");
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
            case 2:
                performWithdraw();
                break;
            case 3:
                checkBalance();
                break;
            case 4:
                displayTransactionHistory();
                break;
            case 5:
                createAdditionalAccount();
                break;
            case 6:
                closeExistingAccount();
                break;
            case 7:
                transferBetweenAccounts();
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

    // withdrawing from account
    public void performWithdraw() {
        double withdrawAmount = -1;
        while (withdrawAmount < 0) {
            System.out.print("How much would you like to withdraw: ");
            withdrawAmount = keyboardInput.nextInt();
        }
        try {
            userAccount.withdraw(withdrawAmount);
            System.out.println("Withdrawal successful. Current balance: $" + userAccount.getBalance());
        } catch (Exception e) {
            System.out.println("Withdrawal failed.");
        }
    }

    // checking account balance
    public void checkBalance() {
        System.out.println("Account Balance: $" + userAccount.getBalance());
    }

    // viewing transaction history
    public void displayTransactionHistory() {
        System.out.println("Transaction history: " + userAccount.getTransactionHistory());
    }

    // creating additional account
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
                    "The account balance must be 0 before closing the account. Please transfer the balance to a different account before closing.");
            return;
        }
        allAccounts.remove(accountName);
        System.out.println("The account named " + accountName + " has been successfully closed");
    }

    // transferring money between accounts
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

    public Set<String> getAllAccountNames() { // added for testing purposes but can be used in later tasks
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

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }

}
