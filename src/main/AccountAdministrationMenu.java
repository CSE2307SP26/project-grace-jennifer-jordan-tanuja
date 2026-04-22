package main;

import java.util.Scanner;

public class AccountAdministrationMenu {

    private UserProfile currentUser;
    private String accountName;
    private BankAccount account;
    private Scanner keyboardInput;

    public AccountAdministrationMenu(UserProfile user, String accountName, BankAccount account, Scanner keyboardInput) {
        this.currentUser = user;
        this.accountName = accountName;
        this.account = account;
        this.keyboardInput = keyboardInput;
    }
    
    public void displayOptions() {
        System.out.println("Currently in account " + accountName);
        System.out.println("What do you wish to do today?");
        System.out.println("1. Make a deposit");
        System.out.println("2. Withdraw from account");
        System.out.println("3. Check account balance");
        System.out.println("4. View transaction history");
        System.out.println("5. Change pin number for this account");
        System.out.println("6. Exit");
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
                changePinNumber();
                break;
            default:
                break;
        }
    }

    public void performDeposit() {
        double depositAmount = -1;
        while (depositAmount < 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = keyboardInput.nextDouble();
        }

        account.deposit(depositAmount);
        System.out.println("Deposit successful.");
    }

    public void performWithdraw() {
        double withdrawAmount = -1;
        while (withdrawAmount < 0) {
            System.out.print("How much would you like to withdraw: ");
            withdrawAmount = keyboardInput.nextDouble();
        }

        try {
            account.withdraw(withdrawAmount);
            System.out.println("Withdrawal successful.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invaild withdrawal amount. Withdrawal failed.");
        }
    }

    public void checkBalance() {
        System.out.println(account.getAccountType() + " account balance: $" + account.getBalance());
    }

    public void displayTransactionHistory() {
        System.out.println("Transaction history: " + account.getTransactionHistory());
    }

    public void changePinNumber() {
        System.out.print("Please enter your date of birth (MM/DD/YYYY): ");
        String dob = keyboardInput.next();

        if (!dob.equals(currentUser.getDob())) {
            System.out.print("Date of birth does not match our records. ");
            System.out.println("Failed to change pin number.");
            return;
        }

        String pin = InputValidator.getValidPin(keyboardInput, "Enter a 6 digit pin number for your account: ");
        account.changePin(pin);
        
        System.out.println("Successfully changed pin number for account " + accountName + ".");
    }

    public void run() {
        int selection = -1;
        while (selection != 6) {
            displayOptions();
            selection = InputValidator.getUserSelection(keyboardInput, 6);
            processInput(selection);
        }
    }
}
