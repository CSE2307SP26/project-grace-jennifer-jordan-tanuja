package main;

import java.util.HashMap;

public class BankAdministrator {

    private String employeeId;
    private String password;

    public BankAdministrator(String employeeId, String password) {
        this.employeeId = employeeId;
        this.password = password;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void collectFees(BankAccount account, double fee) {
        if (fee < 0) {
            throw new IllegalArgumentException();
        } else {
            account.adminWithdraw(fee);
        }
    }

    public void addInterestPayment(BankAccount account, double interestRatePercentage) {
        if (interestRatePercentage < 0 || interestRatePercentage > 100) {
            throw new IllegalArgumentException();
        }
        double interestDecimalRate = interestRatePercentage / 100.0;
        double interest = account.getBalance() * interestDecimalRate;
        if (interest > 0) {
            account.deposit(interest);
        }
    }

    public void addInterestPaymentAllAccounts(HashMap<String, BankAccount> allAccounts, double interestRatePercentage) {
        if (allAccounts == null) {
            throw new IllegalArgumentException();
        }
        if (interestRatePercentage < 0 || interestRatePercentage > 100) {
            throw new IllegalArgumentException();
        }
        for (BankAccount account : allAccounts.values()) {
            if (account != null) {
                String accountType = account.getAccountType();
                if (accountType.equals("savings")) {
                    addInterestPayment(account, interestRatePercentage);
                }
            }
        }
    }

    public void freezeAccount(BankAccount account) {
        if (account == null) {
            throw new IllegalArgumentException();
        }
        account.freeze();
    }

}
