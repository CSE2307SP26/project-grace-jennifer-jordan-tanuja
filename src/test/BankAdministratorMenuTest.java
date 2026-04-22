package test;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import org.junit.After;
import org.junit.Test;

import main.BankAccount;
import main.BankAdministrator;
import main.BankAdministratorMenu;
import main.UserProfile;

public class BankAdministratorMenuTest {

    private final PrintStream originalOut = System.out;
    private final java.io.InputStream originalIn = System.in;

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testViewAllCustomers() {
        HashMap<String, BankAccount> accounts = new HashMap<>();
        HashMap<String, UserProfile> users = new HashMap<>();
        users.put("alice", new UserProfile("alice", "pass", "123456"));
        users.put("bob", new UserProfile("bob", "pass", "123456"));

        String input = "4\n7\n"; // 4 is view all customers, 5 is exit
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BankAdministrator admin = new BankAdministrator("admin", "pass");
        BankAdministratorMenu menu = new BankAdministratorMenu(admin, accounts, users);
        menu.run();

        String printed = output.toString();
        assertTrue(printed.contains("Bank Customers:"));
        assertTrue(printed.contains("- alice"));
        assertTrue(printed.contains("- bob"));
    }
}