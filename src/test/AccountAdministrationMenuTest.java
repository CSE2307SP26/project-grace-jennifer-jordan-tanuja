package test;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import java.util.Scanner;

import org.junit.After;
import org.junit.Test;

import main.AccountAdministrationMenu;

import main.UserProfile;

public class AccountAdministrationMenuTest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testChangingPinSuccess() {
        String input = "01/01/1990\n000000\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        UserProfile user = new UserProfile("testUser", "testPass", "test@email.com", "01/01/1990", "111111");
        Scanner testScanner = new Scanner(System.in);
        AccountAdministrationMenu adminMenu = new AccountAdministrationMenu(user, "primary",
                user.getAccounts().get("primary"), testScanner);
        adminMenu.changePinNumber();

        assertTrue(output.toString().contains("Successfully changed pin number for account primary."));
    }

    @Test
    public void testChangingPinFail() {
        String input = "12/31/1999\n000000\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        UserProfile user = new UserProfile("testUser", "testPass", "test@email.com", "01/01/1990", "111111");
        AccountAdministrationMenu adminMenu = new AccountAdministrationMenu(user, "primary",
                user.getAccounts().get("primary"), new Scanner(System.in));
        adminMenu.changePinNumber();

        assertTrue(
                output.toString().contains("Date of birth does not match our records. Failed to change pin number."));
    }
}