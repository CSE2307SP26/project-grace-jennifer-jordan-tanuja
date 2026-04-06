package test;

import main.BankAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.jupiter.api.Test;


public class BankAccountTest {
    String pin = "000000";

    @Test
    public void testDeposit() {
        BankAccount testAccount = new BankAccount("checking", pin);
        testAccount.deposit(50);
        assertEquals(50, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidDeposit() {
        BankAccount testAccount = new BankAccount("checking", pin);
        try {
            testAccount.deposit(-50);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    @Test
    public void testTransactionHistory() {
        BankAccount testAccount = new BankAccount("checking", pin);
        testAccount.deposit(50);
        testAccount.withdraw(10);

        List<Double> transaction_history = testAccount.getTransactionHistory();
        double[] expected_transaction_history = { 50, -10 };

        for (int i = 0; i < transaction_history.size(); i++) {
            assertEquals(expected_transaction_history[i], transaction_history.get(i), 0.001);
        }
    }
}