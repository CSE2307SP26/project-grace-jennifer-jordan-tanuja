package test;

import main.BankAccount;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class CheckAccountBalanceTest {

    @Test
    public void testCheckBalance() {
        BankAccount testAccount = new BankAccount("checking", "000000");
        assertEquals(0, testAccount.getBalance(), 0.01);

        testAccount.deposit(125);
        assertEquals(125, testAccount.getBalance(), 0.01);

        testAccount.withdraw(25);
        assertEquals(100, testAccount.getBalance(), 0.01);
    }
}