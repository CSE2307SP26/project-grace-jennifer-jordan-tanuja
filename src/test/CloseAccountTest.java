package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import main.MainMenu;

public class CloseAccountTest {

    private final PrintStream originalOut = System.out;

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testCloseExistingAccountSuccessfully() {
        String input = "savings\nsavings\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        MainMenu menu = new MainMenu();
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

        MainMenu menu = new MainMenu();
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

        MainMenu menu = new MainMenu();
        menu.closeExistingAccount();

        assertTrue(output.toString().contains("This is the primary account, which cannot be closed."));
        assertEquals(new HashSet<>(Arrays.asList("primary")), menu.getAllAccountNames());
    }

    @Test
    public void testCannotCloseAccountWithNonZeroBalance() {
        String input = "savings\n50\nprimary\nsavings\n50\nsavings\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenu menu = new MainMenu();

        menu.createAdditionalAccount();
        menu.performDeposit();
        menu.transferBetweenAccounts();
        menu.closeExistingAccount();

        assertTrue(output.toString().contains(
                "The account balance must be 0 before closing the account."));
        assertEquals(new HashSet<>(Arrays.asList("primary", "savings")), menu.getAllAccountNames());
    }
}