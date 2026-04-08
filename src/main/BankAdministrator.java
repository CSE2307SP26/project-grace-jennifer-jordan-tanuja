package main;

import java.util.HashMap;

public class BankAdministrator {

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

}
