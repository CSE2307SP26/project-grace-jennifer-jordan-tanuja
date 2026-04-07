package main;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    private double balance;
    private List<Double> transaction_history;
    private String account_type;
    private String pin;

    public BankAccount(String account_type, String pin) {
        this.balance = 0;
        this.transaction_history = new ArrayList<Double>();
        this.account_type = account_type;
        this.pin = pin;
    }

    public void changePin(String newPin) {
        this.pin = newPin;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            this.transaction_history.add(amount);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void withdraw(double amount) {
        if (amount < 0 || amount > this.balance) {
            throw new IllegalArgumentException();
        } else {
            this.balance -= amount;
            this.transaction_history.add(-amount);
        }
    }

    public void adminWithdraw(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        } else {
            this.balance -= amount;
            this.transaction_history.add(-amount);
        }
    }

    public double getBalance() {
        return this.balance;
    }

    public List<Double> getTransactionHistory() {
        return this.transaction_history;
    }

    public String getAccountType() {
        return this.account_type;
    }
}
