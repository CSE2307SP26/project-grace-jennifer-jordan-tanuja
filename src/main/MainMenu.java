package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;



public class MainMenu {
    private static final int EXIT_SELECTION = 7;
    private static final int MAX_SELECTION = 7;

    private UserProfile currentUser;
    private HashMap<String, BankAccount> allAccounts;
    private HashMap<String, UserProfile> allUsers;
    private Scanner keyboardInput;

    public MainMenu(UserProfile user, HashMap<String, UserProfile> allUsers) {
        this.currentUser = user;
        this.allUsers = allUsers;
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
        System.out.println("5. Transfer money to another user in the system");
        System.out.println("6. Apply for a loan");
        System.out.println("7. Exit");
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
                transferToAnotherUser();
                break;
            case 6:
                applyForLoan();
                break;
            default:
                break;
        }
    }

    public void selectAccount() {
        displayAccounts();
        System.out.print("Please enter the name of the account you want to select: ");
        String accountName = keyboardInput.next();

        if (!allAccounts.containsKey(accountName)) {
            System.out.println("That account does not exist.");
            return;
        }

        BankAccount selectedAccount = allAccounts.get(accountName);

        System.out.print("Please enter the pin for " + accountName + ": ");
        if (!selectedAccount.verifyPin(keyboardInput.next())) {
            System.out.println("Incorrect PIN. Access denied.");
            return;
        }

        new AccountAdministrationMenu(currentUser, accountName, selectedAccount, keyboardInput).run();
    }

    private void displayAccounts() {
        System.out.println("Your accounts:");
        for (Map.Entry<String, BankAccount> entry : allAccounts.entrySet()) {
            System.out.println("- " + entry.getKey() + " (" + entry.getValue().getAccountType() + ")");
        }
    }

    private boolean promptAndValidateBirthday() {
        System.out.print("Please enter your date of birth (MM/DD/YYYY): ");
        if (!keyboardInput.next().equals(currentUser.getDob())) {
            System.out.print("Date of birth does not match our records. ");
            return false;
        }
        return true;
    }

    public void createAdditionalAccount() {
        String fullName = getFullName();
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

        String accountType = getAccountType();
        String pin = InputValidator.getValidPin(keyboardInput, "Enter a 6 digit pin number for your account: ");
        allAccounts.put(accountName, new BankAccount(accountType, pin));

        System.out.println("Successfully created new " + accountType + " account with name: " + accountName);
    }

    private String getFullName() {
        System.out.print("Please enter your full name: ");
        String fullName = keyboardInput.nextLine();
        while (fullName.trim().isEmpty()) {
            System.out.print("Full name cannot be empty. Please enter your full name: ");
            fullName = keyboardInput.nextLine();
        }
        return fullName;
    }

    private String getAccountType() {
        System.out.print("Enter the account type (savings, checking, or credit): ");
        String type = keyboardInput.next().toLowerCase();
        while (!type.equals("savings") && !type.equals("checking") && !type.equals("credit")) {
            System.out.print("Invalid account type. Please enter savings, checking, or credit: ");
            type = keyboardInput.next().toLowerCase();
        }
        return type;
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

        executeTransfer(sourceAccountName, destinationAccountName);
    }

    private void executeTransfer(String source, String dest) {
        System.out.print("Enter the amount to transfer: ");
        double amount = keyboardInput.nextDouble();

        try {
            allAccounts.get(source).withdraw(amount);
            allAccounts.get(dest).deposit(amount);
            System.out.println("Transfer successful.");
        } catch (IllegalArgumentException e) {
            System.out.println("Transfer failed. Make sure the amount is valid and the source account has enough funds.");
        }
    }

    public void showAllUsersInSystem() {
        System.out.println("Existing users in the system:");
        for (Map.Entry<String, UserProfile> entry : allUsers.entrySet()) {
            if (!entry.getKey().equals(currentUser.getUsername())) {
                System.out.println("- " + entry.getKey());
            }
        }
    }

    public void transferToAnotherUser() {
        showAllUsersInSystem();
        keyboardInput.nextLine(); // consume leftover newline

        System.out.print("Enter the username of the user you would like to transfer to: ");
        String targetUsername = keyboardInput.nextLine();

        if (!allUsers.containsKey(targetUsername)) {
            System.out.println(targetUsername + " does not exist in our system. Transfer failed.");
            return;
        }

        System.out.print("Enter name of the account you are pulling money from: ");
        String sourceAccountName = keyboardInput.next();

        if (!allAccounts.containsKey(sourceAccountName)) {
            System.out.println("Source account does not exist. Transfer failed.");
            return;
        }

        System.out.print("Enter the amount to transfer: ");
        double amount = keyboardInput.nextDouble();

        try {
            allAccounts.get(sourceAccountName).withdraw(amount);
            allUsers.get(targetUsername).getAccounts().get("primary").deposit(amount);
            System.out.println("Transfer successful.");
        } catch (IllegalArgumentException e) {
            System.out.println("Transfer failed. Make sure the amount is valid and the source account has enough funds.");
        }
    }

    public void applyForLoan() {
        keyboardInput.nextLine(); // consume leftover newline
        System.out.println("Loan Application");
        getFullName();

        if (!promptAndValidateBirthday()) {
            System.out.println("Loan application failed.");
            return;
        }

        double income = InputValidator.getValidDouble(keyboardInput, "Please enter your annual income: ", "Income");
        double loanAmt = InputValidator.getValidDouble(keyboardInput, "Please enter the loan amount requested: ", "Loan amount");
        int duration = InputValidator.getValidInt(keyboardInput, "Please enter the duration of the loan in months: ", "Loan duration");

        System.out.println("Loan application submitted successfully.");
        String decision= LoanService.evaluateLoanDecision(currentUser, income, loanAmt, duration, allAccounts.get("primary"));
        System.out.println(decision);
      
        if (decision.equals("Loan Approved!")) {
            chooseAccountToDepositLoan(loanAmt);
        }
    }

    public void chooseAccountToDepositLoan(double loanAmount) {
        System.out.println("Choose an account to deposit your approved loan");
        showAccountOptions();

        String accountSelection = null;
        while (!currentUser.getAccountNames().contains(accountSelection)) {
            if (accountSelection != null) {
                System.out.println("Invalid account selection. Choose from your existing accounts");
            }
            accountSelection = keyboardInput.next();
        }

        currentUser.getAccounts().get(accountSelection).deposit(loanAmount);
        System.out.println("Successfully deposited loan to " + accountSelection);
    }
 
    public Set<String> getAllAccountNames() {
        return allAccounts.keySet();
    }

    public void run() {
        int selection = -1;
        while (selection != EXIT_SELECTION) {
            displayOptions();
            selection = InputValidator.getUserSelection(keyboardInput, MAX_SELECTION);
            processInput(selection);
        }
    }
}