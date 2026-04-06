package main;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class MainMenu {

    private static final int EXIT_SELECTION = 10;
    private static final int MAX_SELECTION = 10;

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
        System.out.println("1. Make a deposit");
        System.out.println("2. Withdraw from account");
        System.out.println("3. Check account balance");
        System.out.println("4. View transaction history");
        System.out.println("5. Create an additional account");
        System.out.println("6. Close an existing account");
        System.out.println("7. Transfer money from one account to another");
        System.out.println("8. [Bank Admin] Collect fees");
        System.out.println("9. [Bank Admin] Add an interest payment");
        System.out.println("10. Exit");
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
            case 8:
                adminCollectFees();
                break;
            case 9:
                adminInterestPayment();
                break;
            default:
                break;
        }
    }

    public void performDeposit() {
        System.out.print("Please enter the name of the account: ");
        String accountName = keyboardInput.next();

        if (!allAccounts.containsKey(accountName)) {
            System.out.println("That account does not exist.");
            return;
        }

        double depositAmount = -1;
        while (depositAmount < 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = keyboardInput.nextDouble();
        }

        allAccounts.get(accountName).deposit(depositAmount);
        System.out.println("Deposit successful.");
    }

    public void performWithdraw() {
        System.out.print("Please enter the name of the account: ");
        String accountName = keyboardInput.next();

        if (!allAccounts.containsKey(accountName)) {
            System.out.println("That account does not exist.");
            return;
        }

        double withdrawAmount = -1;
        while (withdrawAmount < 0) {
            System.out.print("How much would you like to withdraw: ");
            withdrawAmount = keyboardInput.nextDouble();
        }

        try {
            allAccounts.get(accountName).withdraw(withdrawAmount);
            System.out.println("Withdrawal successful.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invaild withdrawal amount. Withdrawal failed.");
        }
    }

    public void checkBalance() {
        System.out.print("Please enter the name of the account: ");
        String accountName = keyboardInput.next();

        if (!allAccounts.containsKey(accountName)) {
            System.out.println("That account does not exist.");
            return;
        }

        BankAccount account = allAccounts.get(accountName);
        // should show account type now too
        System.out.println(account.getAccountType() + " account balance: $" + account.getBalance());
    }

    public void displayTransactionHistory() {
        System.out.print("Please enter the name of the account: ");
        String accountName = keyboardInput.next();

        if (!allAccounts.containsKey(accountName)) {
            System.out.println("That account does not exist.");
            return;
        }

        System.out.println("Transaction history: " + allAccounts.get(accountName).getTransactionHistory());
    }

    private String promptToCreatePinNumber() {
        System.out.print("Enter a 6 digit pin number for your new account: ");
        String pin = keyboardInput.next();

        while (pin.length() != 6 || !pin.matches("\\d{6}")) {
            System.out.print("Pin number must have 6 numerical digits. Enter a valid pin number: ");
            pin = keyboardInput.next();
        }
        return pin;
    }

    public void createAdditionalAccount() {
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

    public void adminCollectFees() {
        System.out.print("Please enter the name of the account you want to collect fees from: ");
        String accountName = keyboardInput.next();

        if (!allAccounts.containsKey(accountName)) {
            System.out.println("This account does not exist.");
            return;
        }

        System.out.print("Please enter the fee amount: ");
        try {
            double fee = keyboardInput.nextDouble();
            BankAdministrator admin = new BankAdministrator();
            admin.collectFees(allAccounts.get(accountName), fee);
            System.out.println("Fee collected.");
        } catch (Exception e) {
            System.out.println("Invalid fee amount. Fee collection failed.");
        }
    }

    public void adminInterestPayment() {
        System.out.print("Please enter the name of the account you want to add an interest payment to: ");
        String accountName = keyboardInput.next();

        if (!allAccounts.containsKey(accountName)) {
            System.out.println("This account does not exist.");
            return;
        }

        System.out.print("Please enter the interest rate as a percentage: ");
        try {
            double percentageRate = keyboardInput.nextDouble();
            BankAdministrator admin = new BankAdministrator();
            admin.addInterestPayment(allAccounts.get(accountName), percentageRate);
            System.out.println("Interest payment added.");
        } catch (Exception e) {
            System.out.println("Invalid interest rate. Interest payment failed.");
        }
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