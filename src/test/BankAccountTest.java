package test;

import main.BankAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void testWithdraw() {
        BankAccount testAccount = new BankAccount("checking", pin);
        testAccount.deposit(100);
        testAccount.withdraw(30);
        assertEquals(70, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidWithdraw() {
        BankAccount testAccount = new BankAccount("checking", pin);
        testAccount.deposit(100);
        try {
            testAccount.withdraw(-50);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    @Test
    public void testOverdraw() {
        BankAccount testAccount = new BankAccount("checking", pin);
        testAccount.deposit(60);
        try {
            testAccount.withdraw(70);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    @Test
    public void testCheckBalance() {
        BankAccount testAccount = new BankAccount("checking", "000000");
        assertEquals(0, testAccount.getBalance(), 0.01);

        testAccount.deposit(125);
        assertEquals(125, testAccount.getBalance(), 0.01);

        testAccount.withdraw(25);
        assertEquals(100, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testVerifyPinCorrectPin() {
        BankAccount testAccount = new BankAccount("checking", "123456");
        assertTrue(testAccount.verifyPin("123456"));
    }

    @Test
    public void testVerifyPinIncorrectPin() {
        BankAccount testAccount = new BankAccount("checking", "123456");
        assertTrue(!testAccount.verifyPin("111111"));
    }

    @Test
    public void testVerifyPinEmptyPinEntered() {
        BankAccount testAccount = new BankAccount("checking", "123456");
        assertTrue(!testAccount.verifyPin(""));
    }

}