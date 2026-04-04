package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.After;
import org.junit.Test;

import main.MainMenu;
import main.UserProfile;

public class CloseAccountTest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private MainMenu createMenu() {
        UserProfile user = new UserProfile("testUser", "testPass");
        return new MainMenu(user);
    }

    @Test
    public void testCloseExistingAccountSuccessfully() {
        String input = "savings\nsavings\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        MainMenu menu = createMenu();
        menu.createAdditionalAccount();
        menu.closeExistingAccount();

        assertEquals(new HashSet<>(Arrays.asList("primary")), menu.getAllAccountNames());
    }

    @Test
    public void testCloseNonexistentAccount() {
        String input = "fakeAccount\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenu menu = createMenu();
        menu.closeExistingAccount();

        assertTrue(output.toString().contains("That account does not exist."));
        assertEquals(new HashSet<>(Arrays.asList("primary")), menu.getAllAccountNames());
    }

    @Test
    public void testCannotClosePrimaryAccount() {
        String input = "primary\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenu menu = createMenu();
        menu.closeExistingAccount();

        assertTrue(output.toString().contains("The primary account cannot be closed."));
        assertEquals(new HashSet<>(Arrays.asList("primary")), menu.getAllAccountNames());
    }

    @Test
    public void testCannotCloseAccountWithNonZeroBalance() {
        String input = "savings\nprimary\n50\nprimary\nsavings\n50\nsavings\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenu menu = createMenu();

        menu.createAdditionalAccount();
        menu.performDeposit();
        menu.transferBetweenAccounts();
        menu.closeExistingAccount();

        assertTrue(output.toString().contains(
                "The account balance must be 0 before closing the account."));
        assertEquals(new HashSet<>(Arrays.asList("primary", "savings")), menu.getAllAccountNames());
    }
}