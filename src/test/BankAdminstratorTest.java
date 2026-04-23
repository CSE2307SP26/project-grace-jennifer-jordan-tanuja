package test;

import main.BankAccount;
import main.BankAdministrator;
import main.BankAdministratorMenu;
import main.UserProfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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

    @Test
    public void testAdminCanFreezeAccount() {
        BankAccount testAccount = new BankAccount("checking", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");

        admin.freezeAccount(testAccount);
        assertTrue(testAccount.isFrozen());
    }

    @Test
    public void testFreezeAccountNullThrows() {
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        try {
            admin.freezeAccount(null);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    @Test
    public void testFreezeAccountInvalidUsername() {
        HashMap<String, BankAccount> accounts = new HashMap<>();
        HashMap<String, UserProfile> users = new HashMap<>();
        users.put("alice", new UserProfile("alice", "pass", "123456"));

        String input = "5\ncharlie\n7\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BankAdministrator admin = new BankAdministrator("admin", "pass");
        BankAdministratorMenu menu = new BankAdministratorMenu(admin, accounts, users);
        menu.run();

        String printed = output.toString();
        assertTrue(printed.contains("This user does not exist."));
    }

    @Test
    public void testFreezeAccountValidUsernameAndAccountFromMenu() {
        HashMap<String, BankAccount> accounts = new HashMap<>();
        HashMap<String, UserProfile> users = new HashMap<>();
        UserProfile alice = new UserProfile("alice", "pass", "123456");
        users.put("alice", alice);

        String input = "5\nalice\nprimary\n7\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BankAdministrator admin = new BankAdministrator("admin", "pass");
        BankAdministratorMenu menu = new BankAdministratorMenu(admin, accounts, users);
        menu.run();

        assertTrue(alice.getAccounts().get("primary").isFrozen());
        assertTrue(output.toString().contains("is now frozen"));
    }

    @Test
    public void testFreezeAccountInvalidAccountNameForExistingUser() {
        HashMap<String, BankAccount> accounts = new HashMap<>();
        HashMap<String, UserProfile> users = new HashMap<>();
        UserProfile alice = new UserProfile("alice", "pass", "123456");
        users.put("alice", alice);

        String input = "5\nalice\nsavings\n7\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BankAdministrator admin = new BankAdministrator("admin", "pass");
        BankAdministratorMenu menu = new BankAdministratorMenu(admin, accounts, users);
        menu.run();

        assertTrue(output.toString().contains("This account does not exist for that user."));
        assertFalse(alice.getAccounts().get("primary").isFrozen());
    }

    @Test
    public void testFrozenAccountCannotWithdraw() {
        BankAccount testAccount = new BankAccount("checking", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        testAccount.deposit(100);

        admin.freezeAccount(testAccount);

        try {
            testAccount.withdraw(20);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAdminCanUnfreezeAccount() {
        BankAccount testAccount = new BankAccount("checking", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");

        admin.freezeAccount(testAccount);
        assertTrue(testAccount.isFrozen());

        admin.unfreezeAccount(testAccount);
        assertFalse(testAccount.isFrozen());
    }

    @Test
    public void testUnfreezeAccountNullThrows() {
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        try {
            admin.unfreezeAccount(null);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    @Test
    public void testUnfreezeAccountValidUsernameAndAccountFromMenu() {
        HashMap<String, BankAccount> accounts = new HashMap<>();
        HashMap<String, UserProfile> users = new HashMap<>();
        UserProfile alice = new UserProfile("alice", "pass", "123456");
        users.put("alice", alice);
        alice.getAccounts().get("primary").freeze();

        String input = "6\nalice\nprimary\n7\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BankAdministrator admin = new BankAdministrator("admin", "pass");
        BankAdministratorMenu menu = new BankAdministratorMenu(admin, accounts, users);
        menu.run();

        assertFalse(alice.getAccounts().get("primary").isFrozen());
        assertTrue(output.toString().contains("is now unfrozen"));
    }

    @Test
    public void testUnfrozenAccountCanWithdraw() {
        BankAccount testAccount = new BankAccount("checking", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");
        testAccount.deposit(100);

        admin.freezeAccount(testAccount);
        admin.unfreezeAccount(testAccount);

        try {
            testAccount.withdraw(20);
            assertEquals(80, testAccount.getBalance(), 0.01);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void testUnfreezeAccountInvalidUsername() {
        HashMap<String, BankAccount> accounts = new HashMap<>();
        HashMap<String, UserProfile> users = new HashMap<>();
        users.put("alice", new UserProfile("alice", "pass", "123456"));

        String input = "6\ncharlie\n7\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BankAdministrator admin = new BankAdministrator("admin", "pass");
        BankAdministratorMenu menu = new BankAdministratorMenu(admin, accounts, users);
        menu.run();

        String printed = output.toString();
        assertTrue(printed.contains("This user does not exist."));
    }

    @Test
    public void testUnfreezeAccountInvalidAccountNameForExistingUser() {
        HashMap<String, BankAccount> accounts = new HashMap<>();
        HashMap<String, UserProfile> users = new HashMap<>();
        UserProfile alice = new UserProfile("alice", "pass", "123456");
        users.put("alice", alice);
        alice.getAccounts().get("primary").freeze();

        String input = "6\nalice\nsavings\n7\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BankAdministrator admin = new BankAdministrator("admin", "pass");
        BankAdministratorMenu menu = new BankAdministratorMenu(admin, accounts, users);
        menu.run();

        assertTrue(output.toString().contains("This account does not exist for that user."));
        assertTrue(alice.getAccounts().get("primary").isFrozen());
    }

    @Test
    public void testUnfreezeAccountThatIsNotFrozen() {
        BankAccount testAccount = new BankAccount("checking", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");

        admin.unfreezeAccount(testAccount);
        assertFalse(testAccount.isFrozen());
    }

    @Test
    public void testUnfreezeMultipleTimesIsSafe() {
        BankAccount testAccount = new BankAccount("checking", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");

        admin.freezeAccount(testAccount);
        admin.unfreezeAccount(testAccount);
        admin.unfreezeAccount(testAccount);

        assertFalse(testAccount.isFrozen());
    }

    @Test
    public void testUnfrozenAccountCanTransfer() {
        BankAccount sourceAccount = new BankAccount("checking", pin);
        BankAccount destAccount = new BankAccount("savings", pin);
        BankAdministrator admin = new BankAdministrator("admin", "adminpass");

        sourceAccount.deposit(100);
        destAccount.deposit(50);

        admin.freezeAccount(sourceAccount);
        admin.unfreezeAccount(sourceAccount);

        try {
            sourceAccount.withdraw(30);
            destAccount.deposit(30);
            assertEquals(70, sourceAccount.getBalance(), 0.01);
            assertEquals(80, destAccount.getBalance(), 0.01);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

}
