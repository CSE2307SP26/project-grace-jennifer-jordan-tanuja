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

public class TransferMoneyTests {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private MainMenu createMenu() {
        UserProfile user = new UserProfile("testUser", "testPass", "test@email.com", "01/01/1990", "000000");
        return new MainMenu(user);
    }

    @Test
    public void testTransferMoneySuccessfully() {
        String input = "John Doe\n01/01/1990\nsavings\nsavings\n000000\nprimary\n100\nprimary\nsavings\n40\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        MainMenu menu = createMenu();

        menu.createAdditionalAccount();
        menu.performDeposit();
        menu.transferBetweenAccounts();

        assertEquals(new HashSet<>(Arrays.asList("primary", "savings")), menu.getAllAccountNames());
    }

    @Test
    public void testTransferFailsWhenSourceAccountDoesNotExist() {
        String input = "fake\nprimary\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenu menu = createMenu();
        menu.transferBetweenAccounts();

        assertTrue(output.toString().contains("Source account does not exist."));
        assertEquals(new HashSet<>(Arrays.asList("primary")), menu.getAllAccountNames());
    }

    @Test
    public void testTransferFailsWhenDestinationAccountDoesNotExist() {
        String input = "primary\nfake\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenu menu = createMenu();
        menu.transferBetweenAccounts();

        assertTrue(output.toString().contains("Destination account does not exist."));
        assertEquals(new HashSet<>(Arrays.asList("primary")), menu.getAllAccountNames());
    }

    @Test
    public void testTransferFailsWhenInsufficientFunds() {
        String input = "John Doe\n01/01/1990\nsavings\nsavings\n000000\nprimary\nsavings\n50\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenu menu = createMenu();

        menu.createAdditionalAccount();
        menu.transferBetweenAccounts();

        assertTrue(output.toString().contains("Transfer failed."));
        assertEquals(new HashSet<>(Arrays.asList("primary", "savings")), menu.getAllAccountNames());
    }

    @Test
    public void testTransferFailsWhenAmountIsNegative() {
        String input = "John Doe\n01/01/1990\nsavings\nsavings\n000000\nprimary\n100\nprimary\nsavings\n-20\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenu menu = createMenu();

        menu.createAdditionalAccount();
        menu.performDeposit();
        menu.transferBetweenAccounts();

        assertTrue(output.toString().contains("Transfer failed."));
        assertEquals(new HashSet<>(Arrays.asList("primary", "savings")), menu.getAllAccountNames());
    }
}