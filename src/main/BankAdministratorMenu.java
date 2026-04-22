package main;

import java.util.HashMap;
import java.util.Scanner;

public class BankAdministratorMenu {

    private HashMap<String, BankAccount> allAccounts;
    private HashMap<String, UserProfile> allUsers;
    private Scanner keyboardInput;
    private BankAdministrator admin;

    public BankAdministratorMenu(BankAdministrator admin, HashMap<String, BankAccount> allAccounts, HashMap<String, UserProfile> allUsers) {
        this.admin = admin;
        this.allAccounts = allAccounts;
        this.allUsers = allUsers;
        this.keyboardInput = new Scanner(System.in);
    }

     public void displayOptions() {
        System.out.println("What do you wish to do today?");
        System.out.println("1. [Bank Admin] Collect fees");
        System.out.println("2. [Bank Admin] Add an interest payment");
        System.out.println("3. [Bank Admin] Add an interest payment to all savings accounts");
        System.out.println("4. [Bank Admin] View all customers");
        System.out.println("5. Exit");
    }

     public void processInput(int selection) {
        switch (selection) {
            case 1:
                adminCollectFees();
                break;
            case 2:
                adminInterestPayment();
                break;
            case 3:
                adminInterestPaymentsAllAccounts();
                break;
            case 4:
                viewAllCustomers();
                break;
            default:
                break;
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
            this.admin.collectFees(allAccounts.get(accountName), fee);
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
            this.admin.addInterestPayment(allAccounts.get(accountName), percentageRate);
            System.out.println("Interest payment added.");
        } catch (Exception e) {
            System.out.println("Invalid interest rate. Interest payment failed.");
        }
    }

    public void adminInterestPaymentsAllAccounts(){
        System.out.print("Please enter the interest rate as a percentage: ");
        try {
            double percentageRate = keyboardInput.nextDouble();
            this.admin.addInterestPaymentAllAccounts(allAccounts, percentageRate);
            System.out.println("Interest payment added.");
        } catch (Exception e) {
            System.out.println("Invalid interest rate. Interest payment failed.");
        }

    }

    public void viewAllCustomers() {
        System.out.println("Bank Customers:");
        if (allUsers == null || allUsers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }
        for (String username : allUsers.keySet()) {
            System.out.println("- " + username);
        }
    }

    public void run() {
         int selection = -1;
         while (selection != 5) {
            displayOptions();
            selection = InputValidator.getUserSelection(keyboardInput, 5);
            processInput(selection);
        }
        
    }
    
}
