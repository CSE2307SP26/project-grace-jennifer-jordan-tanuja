package main;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    private double balance;
    private List<Double> transaction_history;

    public BankAccount() {
        this.balance = 0;
        this.transaction_history = new ArrayList<Double>();
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
        if(amount < 0 || amount > this.balance) {
            throw new IllegalArgumentException();
        } else {
            this.balance -= amount;
            this.transaction_history.add(-amount);
        }
    }

     public void adminWithdraw(double amount) {
        if(amount < 0) {
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
}
