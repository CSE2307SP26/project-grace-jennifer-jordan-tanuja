package test;

import main.BankAccount;
import main.BankAdministrator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

public class BankAdminstratorTest {

    @Test
    public void testCollectFees() {
        BankAccount testAccount = new BankAccount();
        BankAdministrator admin = new BankAdministrator();
        testAccount.deposit(50);
        admin.collectFees(testAccount, 10);
        assertEquals(40, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidCollectFees() {
        BankAccount testAccount = new BankAccount();
        BankAdministrator admin = new BankAdministrator();
        testAccount.deposit(50);
        try {
            admin.collectFees(testAccount, -10);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    @Test
    public void testCollectFeesWithInsufficientAccountBalance() {
        BankAccount testAccount = new BankAccount();
        BankAdministrator admin = new BankAdministrator();
        testAccount.deposit(50);
        admin.collectFees(testAccount, 200);
        assertEquals(-150, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testCollectMultipleFees() {
        BankAccount testAccount = new BankAccount();
        BankAdministrator admin = new BankAdministrator();
        testAccount.deposit(50);
        admin.collectFees(testAccount, 10);
        admin.collectFees(testAccount, 20);
        assertEquals(20, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testCollectFeesWithNegativeBalance() {
        BankAccount testAccount = new BankAccount();
        BankAdministrator admin = new BankAdministrator();
        testAccount.deposit(10);
        admin.collectFees(testAccount, 20);
        admin.collectFees(testAccount, 10);
        assertEquals(-20, testAccount.getBalance(), 0.01);
    }

}
