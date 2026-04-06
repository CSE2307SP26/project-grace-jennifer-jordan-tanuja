package test;

import main.BankAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WithdrawAccountTest {
    private BankAccount testAccount;

    @BeforeEach
    public void setup() {
        this.testAccount = new BankAccount("checking", "000000");
    }

    @Test
    public void testWithdraw() {
        testAccount.deposit(100);
        testAccount.withdraw(30);
        assertEquals(70, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidWithdraw() {
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
        testAccount.deposit(60);
        try {
            testAccount.withdraw(70);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }
}