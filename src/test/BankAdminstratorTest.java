package test;

import main.BankAccount;
import main.BankAdministrator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;
import java.util.HashMap;

public class BankAdminstratorTest {
    String pin = "000000";

    @Test
    public void testCheckPasswordCorrect() {
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        assertTrue(admin.checkPassword("adminpass"));
    }

    @Test
    public void testCheckPasswordIncorrect() {
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        assertFalse(admin.checkPassword("wrongpass"));
    }

    @Test
    public void testCollectFees() {
        BankAccount testAccount = new BankAccount("checking", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        testAccount.deposit(50);
        admin.collectFees(testAccount, 10);
        assertEquals(40, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidCollectFees() {
        BankAccount testAccount = new BankAccount("checking", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
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
        BankAccount testAccount = new BankAccount("checking", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        testAccount.deposit(50);
        admin.collectFees(testAccount, 200);
        assertEquals(-150, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testCollectMultipleFees() {
        BankAccount testAccount = new BankAccount("checking", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        testAccount.deposit(50);
        admin.collectFees(testAccount, 10);
        admin.collectFees(testAccount, 20);
        assertEquals(20, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testCollectFeesWithNegativeBalance() {
        BankAccount testAccount = new BankAccount("checking", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        testAccount.deposit(10);
        admin.collectFees(testAccount, 20);
        admin.collectFees(testAccount, 10);
        assertEquals(-20, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInterestPayment() {
        BankAccount testAccount = new BankAccount("checking", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        testAccount.deposit(50);
        admin.addInterestPayment(testAccount, 10);
        assertEquals(55, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidInterestPayment() {
        BankAccount testAccount = new BankAccount("checking", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        testAccount.deposit(50);
        try {
            admin.addInterestPayment(testAccount, -10);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    @Test
    public void testInterestPaymentOnZeroBalance() {
        BankAccount testAccount = new BankAccount("checking", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        admin.addInterestPayment(testAccount, 10);
        assertEquals(0, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInterestPaymentAllAccountsSavingsOnly() {
        HashMap<String, BankAccount> allAccounts = new HashMap<>();
        BankAccount checkingAccount = new BankAccount("checking", pin);
        BankAccount savingsAccount = new BankAccount("savings", pin);

        checkingAccount.deposit(200);
        savingsAccount.deposit(200);

        allAccounts.put("checkingTest", checkingAccount);
        allAccounts.put("savingsTest", savingsAccount);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        admin.addInterestPaymentAllAccounts(allAccounts, 10);
        assertEquals(200, checkingAccount.getBalance(), 0.01);
        assertEquals(220, savingsAccount.getBalance(), 0.01);
    }

    @Test
    public void testInterestPaymentAllAccountsMultipleSavingsAccounts() {
        HashMap<String, BankAccount> allAccounts = new HashMap<>();
        BankAccount savingsAccount1 = new BankAccount("savings", pin);
        BankAccount savingsAccount2 = new BankAccount("savings", pin);

        savingsAccount1.deposit(200);
        savingsAccount2.deposit(500);

        allAccounts.put("savingsTest1", savingsAccount1);
        allAccounts.put("savingsTest2", savingsAccount2);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        admin.addInterestPaymentAllAccounts(allAccounts, 10);
        assertEquals(220, savingsAccount1.getBalance(), 0.01);
        assertEquals(550, savingsAccount2.getBalance(), 0.01);
    }

    @Test
    public void testInterestPaymentAllAccountsInvalidInterestRate() {
        HashMap<String, BankAccount> allAccounts = new HashMap<>();
        BankAccount savingsAccount = new BankAccount("savings", pin);

        savingsAccount.deposit(200);
        allAccounts.put("savingsTest1", savingsAccount);

        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        try {
            admin.addInterestPaymentAllAccounts(allAccounts, -10);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    @Test
    public void testInterestPaymentAllAccountsNoSavingsAccount() {
        HashMap<String, BankAccount> allAccounts = new HashMap<>();
        BankAccount checkingAccount = new BankAccount("checking", pin);

        checkingAccount.deposit(200);

        allAccounts.put("checking", checkingAccount);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        admin.addInterestPaymentAllAccounts(allAccounts, 10);
        assertEquals(200, checkingAccount.getBalance(), 0.01);
    }

    @Test
    public void testInterestPaymentAllAccountsNullAccount() {
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        try {
            admin.addInterestPaymentAllAccounts(null, 10);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

}
